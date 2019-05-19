package pt.ulisboa.tecnico.cnv.loadbalancing;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Set;
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
 *  @author Group 
 *          CNV Project
 */

public final class AutoScaler {

    //ConcurrentHashMap runningInstances <PublicDNS, EC2Instance>
    private static ConcurrentHashMap<String, EC2Instance> mapOfInstances = new ConcurrentHashMap<String, EC2Instance>();
    private static boolean executingAction;

    int MIN_INSTANCES = 1;
    int MAX_INSTANCES = 3;

    private static final AutoScaler INSTANCE = new AutoScaler();
    private static AmazonEC2 ec2;
    private static final long MAX_INSTANCE_WORKLOAD = 10000000000L;
    private static AmazonCloudWatch cloudWatch;
    

    private AutoScaler(){
        //TODO
    }

    /*
    * Public method, static performing the first 
    * and only access to the construtor
    */
    public static AutoScaler getInstance(){
        return INSTANCE;
    }

    /* 
    *
    * Replace configuration settings with proper values
    *
    */
    public static class SpawnThread extends Thread{

        public void run(){
            try {
                System.out.println("Starting a new instance.");
                RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
                
                runInstancesRequest.withImageId(Config.AMI_ID)
                               .withInstanceType(Config.INSTANCE_TYPE)
                               .withMinCount(1)
                               .withMaxCount(1)
                               .withKeyName(Config.KEY_NAME)
                               .withSecurityGroups(Config.SEC_GROUP);
                
                RunInstancesResult runInstancesResult = ec2.runInstances(runInstancesRequest);
                System.out.println("Instance started");

                EC2Instance newEC2Instance = new EC2Instance();

                
                
                String newInstanceId = runInstancesResult.getReservation().getInstances()
                                        .get(0).getInstanceId();

                String newInstanceState = runInstancesResult.getReservation().getInstances()
                                        .get(0).getState().getName();

                String newInstanceDNS = "no dns assigned";

                while(!newInstanceState.equals("running")){
                    DescribeInstancesResult describeInstancesRequest = ec2.describeInstances();
                    List<Reservation> reservations = describeInstancesRequest.getReservations();
                    for(Reservation reservation : reservations){
                        for(Instance instance : reservation.getInstances()){
                            if(instance.getInstanceId().equals(newInstanceId)){
                                newInstanceState = instance.getState().getName();
                                newInstanceDNS = instance.getPublicDnsName();
                                System.out.println("newInstanceState -> " + newInstanceState);
                                Thread.sleep(5000);
                            }
                        }
                    }
                }


                newEC2Instance.setInstanceID(newInstanceId);
                newEC2Instance.setPublicDNS(newInstanceDNS);

                System.out.println(newEC2Instance);

                synchronized(mapOfInstances){
                    mapOfInstances.put(newEC2Instance.getPublicDNS(), newEC2Instance);
                    System.out.println("New instance : " + newEC2Instance.getPublicDNS() + " added!");
                }

                AutoScaler.executingAction = false;
                
                /*String publicDNS;
                String instanceID;
                synchronized(mapOfInstances){
                    for (Reservation reservation : reservations) {
                        for (Instance inst : reservation.getInstances()) {
                            publicDNS = inst.getPublicDnsName();
                            instanceID = inst.getInstanceId();
                            if(newInstanceId.equals(imageID)){
                                newEC2Instance.setPublicDNS(publicDNS);
                                mapOfInstances.put(publicDNS, newEC2Instance);
                                EC2Instance eC2Instance = mapOfInstances.get(publicDNS);
                                System.out.println(eC2Instance.getInstanceID());
                            }    
                        }
                    }
                }*/
                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void spawnEC2Instance(){
        AutoScaler.executingAction = true;
        Thread spawnEC2InstanceThread = new SpawnThread();
        spawnEC2InstanceThread.start();
    }

    //TODO
    public static class DestroyThread extends Thread{
        public void run(String publicDNS){
            try {
                System.out.println("Terminating the instance.");
                TerminateInstancesRequest termInstanceReq = new TerminateInstancesRequest();
                termInstanceReq.withInstanceIds(mapOfInstances.get(publicDNS).getInstanceID());
                ec2.terminateInstances(termInstanceReq);

                AutoScaler.executingAction = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void destroyEC2Instance(String publicDNS){
        AutoScaler.executingAction = true;

        DestroyThread destroyEC2Instance = new DestroyThread();
        System.out.println("destroyEC2 -> " + publicDNS);
        destroyEC2Instance.run(publicDNS);
        //mapOfInstances.remove(publicDNS);
        //System.out.println("Instance: "+ publicDNS +" removed");
    }

    public static void checkInstancesState() throws MalformedURLException{
        System.out.println("Check Instances State");
        synchronized(mapOfInstances){
            for (String publicDNSName : mapOfInstances.keySet()) {
                EC2Instance value = mapOfInstances.get(publicDNSName);	
                System.out.println("Instance "+publicDNSName);
                URL url = new URL("http://"+publicDNSName+":8000/test");
                try{
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(4000);
                    int responseCode = con.getResponseCode();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String response = rd.readLine();
                    if(response.equals("test ok")){
                        System.out.println("Instance State: OK");
                    } else {
                        System.out.println("Something wrong");
                    }
                    
                } catch(IOException e){
                    System.out.println("Instance State: DOWN");
                    System.out.println("Instance was down! It has been removed");
                    mapOfInstances.remove(publicDNSName);
                    return;
                }
            }
            System.out.println("Done!");
        }   
    }
    
    private static void init() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (~/.aws/credentials).
         */
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        //Change the region for the one of your instance
        ec2 = AmazonEC2ClientBuilder.standard().withRegion("eu-west-3")
        .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

    public static void main(String[] args) throws Exception {

        System.out.println("===========================================");
        System.out.println("Welcome to the Hill Climbing AutoScaler");
        System.out.println("===========================================");

        init();

        if(!AutoScaler.executingAction){
            spawnEC2Instance();
        }

        while(AutoScaler.executingAction){
            System.out.println("Execution Action (SPAWN) -> " + AutoScaler.executingAction);
            Thread.sleep(5000);
        }
        
        

        //checkInstancesState();

        for(String publicDNS : mapOfInstances.keySet()){
            System.out.println(mapOfInstances.get(publicDNS));
            while(AutoScaler.executingAction){
                System.out.println("Execution Action (DESTROY) -> " + AutoScaler.executingAction);
                Thread.sleep(5000);
            }
            destroyEC2Instance(publicDNS);
            System.out.println("Instance " + publicDNS + " Destroyed!");   
        }
        
        
        //checkInstancesState();
        boolean wait = true;
        while(wait){
            System.in.read();
            wait = false;
        }   
    }
    
    public void checkInstanceMetrics(){
        
    }
}