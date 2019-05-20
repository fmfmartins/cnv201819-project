package pt.ulisboa.tecnico.cnv.loadbalancing;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Date;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.IOException;

import java.net.URL;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;

/**
 * 
 * Singleton Design Pattern
 * 
 * @author Group CNV Project
 */

public class AutoScaler {

    // ConcurrentHashMap runningInstances <PublicDNS, EC2Instance>
    static ConcurrentHashMap<String, EC2Instance> mapOfInstances = new ConcurrentHashMap<String, EC2Instance>();
    static boolean executingAction;
    static int CHECK_DELAY = 60000;
    static int STARTUP_DELAY = 10000;

    static int MIN_INSTANCES = 1;
    static int MAX_INSTANCES = 15;

    static AutoScaler autoscaler;
    static AmazonEC2 ec2;
    static final long MAX_INSTANCE_WORKLOAD = 30000000000L;

    static AmazonCloudWatch cloudWatch;

    private AutoScaler() {

        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (~/.aws/credentials), and is in valid format.", e);
        }
        // Change the region for the one of your instance
        ec2 = AmazonEC2ClientBuilder.standard().withRegion("eu-west-3")
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

        Timer timer = new Timer();
        timer.schedule(new CheckInstanceWorkloadTask(), STARTUP_DELAY, CHECK_DELAY);

        Timer otherTimer = new Timer();
        otherTimer.schedule(new CheckInstanceStateTask(), STARTUP_DELAY, CHECK_DELAY / 2);
    }

    /*
     * Public method, static performing the first and only access to the construtor
     */
    public synchronized static AutoScaler getInstance() {
        if (autoscaler == null) {
            autoscaler = new AutoScaler();
        }
        return autoscaler;
    }

    /*
     *
     * Replace configuration settings with proper values
     *
     */
    public static class SpawnThread extends Thread {

        public void run() {
            try {
                RunInstancesRequest runInstancesRequest = new RunInstancesRequest();

                runInstancesRequest.withImageId(Config.AMI_ID).withInstanceType(Config.INSTANCE_TYPE).withMinCount(1)
                        .withMaxCount(1).withKeyName(Config.KEY_NAME).withSecurityGroups(Config.SEC_GROUP);

                RunInstancesResult runInstancesResult = ec2.runInstances(runInstancesRequest);

                EC2Instance newEC2Instance = new EC2Instance();

                String newInstanceId = runInstancesResult.getReservation().getInstances().get(0).getInstanceId();

                String newInstanceState = runInstancesResult.getReservation().getInstances().get(0).getState()
                        .getName();

                String newInstanceDNS = "no dns assigned";


                while (!newInstanceState.equals("running")) {
                    DescribeInstancesResult describeInstancesRequest = ec2.describeInstances();
                    List<Reservation> reservations = describeInstancesRequest.getReservations();
                    for (Reservation reservation : reservations) {
                        for (Instance instance : reservation.getInstances()) {
                            if (instance.getInstanceId().equals(newInstanceId)) {
                                newInstanceState = instance.getState().getName();
                                newInstanceDNS = instance.getPublicDnsName();
                                System.out.println("AutoScaler: newInstanceState -> " + newInstanceState);
                                Thread.sleep(5000);
                            }
                        }
                    }
                }

                newEC2Instance.setInstanceID(newInstanceId);
                newEC2Instance.setPublicDNS(newInstanceDNS);

                System.out.println(newEC2Instance);

                synchronized (mapOfInstances) {
                    mapOfInstances.put(newEC2Instance.getPublicDNS(), newEC2Instance);
                    System.out.println("New instance : " + newEC2Instance.getPublicDNS() + " added!");
                }

                LoadBalancer.getInstance().addInstance(newEC2Instance.getPublicDNS());

                executingAction = false;
                System.out.println("AutoScaler: Executing Action -> " + executingAction);

                /*
                 * String publicDNS; String instanceID; synchronized(mapOfInstances){ for
                 * (Reservation reservation : reservations) { for (Instance inst :
                 * reservation.getInstances()) { publicDNS = inst.getPublicDnsName(); instanceID
                 * = inst.getInstanceId(); if(newInstanceId.equals(imageID)){
                 * newEC2Instance.setPublicDNS(publicDNS); mapOfInstances.put(publicDNS,
                 * newEC2Instance); EC2Instance eC2Instance = mapOfInstances.get(publicDNS);
                 * System.out.println(eC2Instance.getInstanceID()); } } } }
                 */

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void spawnEC2Instance() {
        System.out.println("===================");
        System.out.println("Spawn EC2 Instance");
        System.out.println("===================");
        AutoScaler.executingAction = true;
        Thread spawnEC2InstanceThread = new SpawnThread();
        spawnEC2InstanceThread.start();
    }

    // TODO
    public static class DestroyThread extends Thread {
        public void run(String publicDNS) {
            try {
                TerminateInstancesRequest termInstanceReq = new TerminateInstancesRequest();
                termInstanceReq.withInstanceIds(mapOfInstances.get(publicDNS).getInstanceID());
                ec2.terminateInstances(termInstanceReq);
                LoadBalancer.getInstance().removeInstance(publicDNS);
                AutoScaler.executingAction = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void destroyEC2Instance(String publicDNS) {
        System.out.println("=====================");
        System.out.println("Destroy EC2 Instance");
        System.out.println("=====================");
        AutoScaler.executingAction = true;
        DestroyThread destroyEC2Instance = new DestroyThread();
        destroyEC2Instance.run(publicDNS);
        mapOfInstances.remove(publicDNS);
        System.out.println("AutoScaler: Instance: " + publicDNS + " removed.");
    }

    public static void checkInstancesState() throws MalformedURLException {
        System.out.println("======================");
        System.out.println("Check Instances State");
        System.out.println("======================");
        synchronized (mapOfInstances) {
            for (String publicDNSName : mapOfInstances.keySet()) {
                EC2Instance value = mapOfInstances.get(publicDNSName);
                System.out.println("Instance " + publicDNSName);
                URL url = new URL("http://" + publicDNSName + ":8000/test");
                try {
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(4000);
                    int responseCode = con.getResponseCode();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String response = rd.readLine();
                    if (response.equals("test ok")) {
                        System.out.println("AutoScaler: /test endpoint SUCCESS");
                    } else {
                        System.out.println("AutoScaler: /test endpoint FAIL");
                        System.out.println("AutoScaler: Instance " + publicDNSName + " was down! It will be removed");
                        destroyEC2Instance(publicDNSName);
                    }

                } catch (IOException e) {
                    System.out.println("AutoScaler: Instance State: DOWN");
                    System.out.println("AutoScaler: Instance " + publicDNSName + " was down! It will be removed");
                    destroyEC2Instance(publicDNSName);
                    return;
                }
            }
            System.out.println("Done!");
        }
    }

    private static void init() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default] credential profile
         * by reading from the credentials file located at (~/.aws/credentials).
         */

    }

    public static synchronized void checkInstanceWorkload() {
        long totalSystemWorkload = 0;
        long instanceWorkLoad = 0;
        long minWorkLoad = 0;
        boolean joblessInstance = false;

        EC2Instance minWorkLoadInstance = null;

        System.out.println("AutoScaler: Number of Instances Running = " + mapOfInstances.size());
        for (String dnsName : mapOfInstances.keySet()) {
            
            System.out.println("Instance DNS: " + dnsName);

            instanceWorkLoad = LoadBalancer.instancesCost.get(dnsName);

            if (instanceWorkLoad == 0) {
                joblessInstance = true;
            }

            totalSystemWorkload += instanceWorkLoad;
            if (minWorkLoadInstance == null || instanceWorkLoad < minWorkLoad) {
                minWorkLoad = instanceWorkLoad;
                minWorkLoadInstance = mapOfInstances.get(dnsName);
            }

        }

        System.out.println("Total System Workload = " + totalSystemWorkload);

        

        if (minWorkLoadInstance == null) {
            System.out.println("AutoScaler: No instances found. Spawning new instance!");
            spawnEC2Instance();
            return;
        }

        if ((totalSystemWorkload / MAX_INSTANCE_WORKLOAD * mapOfInstances.size()) > 0.8
                && mapOfInstances.size() < AutoScaler.MAX_INSTANCES && !joblessInstance) {
            System.out.println("AutoScaler : Action -> Spawn new instance");
            spawnEC2Instance();
        } else if ((totalSystemWorkload / MAX_INSTANCE_WORKLOAD * mapOfInstances.size()) < 0.4
                && mapOfInstances.size() > AutoScaler.MIN_INSTANCES) {
                    System.out.println("AutoScaler : Action -> Destroy an instance");
            destroyEC2Instance(minWorkLoadInstance.getPublicDNS());
        } else {
            System.out.println("AutoScaler : No scaling action taken during this cycle");
        }
    }

    public class CheckInstanceWorkloadTask extends TimerTask {
        public void run() {
            checkInstanceWorkload();
        }
    }

    public class CheckInstanceStateTask extends TimerTask {
        public void run() {
            try {
                checkInstancesState();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
}