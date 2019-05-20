package pt.ulisboa.tecnico.cnv.loadbalancing;

import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.lang.Thread.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import pt.ulisboa.tecnico.cnv.solver.Solver;
import pt.ulisboa.tecnico.cnv.solver.SolverArgumentParser;
import pt.ulisboa.tecnico.cnv.solver.SolverFactory;
import pt.ulisboa.tecnico.cnv.mss.*;
import pt.ulisboa.tecnico.cnv.rainbow.Menu;

import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.AmazonClientException;
import com.amazonaws.services.elasticloadbalancingv2.*;
import com.amazonaws.services.elasticloadbalancingv2.model.*;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.util.EC2MetadataUtils;

import javax.imageio.ImageIO;


public class LoadBalancer {

    private static int RANGE_PX_OFFSET = 20;
    private static int MAX_CACHE_SIZE = 20;

    // LoadBalancer instance
    private static LoadBalancer loadBalancer;
    // Cache where the key is the image name
    private static Map<String, List<RequestMetrics>> cache;

    // Port
    private static final int port = 8000;

    // Hashmap to save the future instances and requests running on them
    static ConcurrentHashMap<String, ArrayList<Params>> instancesRunning = new ConcurrentHashMap<String, ArrayList<Params>>();
    static ConcurrentHashMap<String, ArrayList<HttpExchange>> instancesHttpRequests = new ConcurrentHashMap<String, ArrayList<HttpExchange>>();
    static ConcurrentHashMap<String, Long> instancesCost = new ConcurrentHashMap<String, Long>();

    // Test main class
    public static void main(String[] args) throws Exception {

    }

    // Get instance of LoadBalancer
    public static synchronized LoadBalancer getInstance() {
        if (loadBalancer == null) {
            loadBalancer = new LoadBalancer();
        }
        return loadBalancer;
    }

    // Contruct
    private LoadBalancer() {
        initCache();
        try {
            final HttpServer load_balancer = HttpServer.create(new InetSocketAddress(port), 0);
            load_balancer.createContext("/climb", new SendQueryHandler());
            load_balancer.createContext("/test", new MyTestHandler());
            load_balancer.createContext("/progress", new MyProgressHandler());
            //load_balancer.setExecutor(Executors.newCachedThreadPool());
            load_balancer.setExecutor(Executors.newFixedThreadPool(20));
            load_balancer.start();
            System.out.println(load_balancer.getAddress().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // ================================================================================
    // REQUEST HANDLERS
    // ================================================================================

    static class MyProgressHandler implements HttpHandler {
        @Override
        public void handle(final HttpExchange t) throws IOException {

            try {
                final Headers headers = t.getResponseHeaders();

                System.out.println(Menu.ANSI_CYAN + "----------PROGRESS REQUEST----------\t" + Menu.ANSI_RESET);
    
                final String query = t.getRequestURI().getQuery();
    
                /*System.out.println("> Headers:\t" + headers);
                System.out.println("> Query:\t" + query);
                System.out.println("> Request:\t" + t.getRequestURI().toString());*/

                System.out.println("LoadBalancer: Checking requests progress..");
                System.out.println("");

                String reply = "";
                reply += String.format("*=================================================*\n");
                reply += String.format("* HillClimbing@Cloud - Group 2 - 2018/19 - MEIC-A *\n");
                reply += String.format("*=================================================*\n");


                for (String dns : instancesCost.keySet()) {


                    reply += String.format("--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>\n"); 
                    reply += dns + "\n";
                    reply += String.format("======================= REQUESTS =======================\n"); 
                    String line;
                    StringBuffer response = new StringBuffer();
                    BufferedReader rd;
                    int responseCode;
                    try{
                        URL url = new URL("http://" + dns + ":8000/requestprogress");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        con.setConnectTimeout(2000);
                        responseCode = con.getResponseCode();
                        rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        response = new StringBuffer();
                    } catch (Exception e){
                        System.out.println(Menu.ANSI_RED + "Progress handler: Could not check instance requests." + Menu.ANSI_RESET);
                        continue;
                    }
                    
                    while( (line = rd.readLine() ) != null){
                        response.append(line + "\n");
                    }
                    reply += response.toString();
                    reply += String.format("--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>\n\n");
                }

                //System.out.println("reply length : " + reply.getBytes().length);

                t.sendResponseHeaders(200, reply.getBytes().length);
                headers.add("Access-Control-Allow-Origin", "*");
                headers.add("Access-Control-Allow-Credentials", "true");
                headers.add("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
                headers.add("Access-Control-Allow-Headers",
                        "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
                OutputStream os = t.getResponseBody();
                os.write(reply.getBytes());
                os.close();
            } catch (Exception e) {
                System.out.println(Menu.ANSI_RED + "Progress handler: Outer exception!" + Menu.ANSI_RESET);
                e.printStackTrace();
            }

        }
    }

    static class MyTestHandler implements HttpHandler {
        @Override
        public void handle(final HttpExchange t) throws IOException {
            final Headers headers = t.getResponseHeaders();

            final String query = t.getRequestURI().getQuery();

            System.out.println(Menu.ANSI_CYAN + "----------TEST REQUEST----------\t" + Menu.ANSI_RESET);

            //System.out.println("> Query:\t" + query);

            String response = "test ok";

            t.sendResponseHeaders(200, response.getBytes().length);

            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Credentials", "true");
            headers.add("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
            headers.add("Access-Control-Allow-Headers",
                    "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class SendQueryHandler implements HttpHandler {
        @Override
        public void handle(final HttpExchange t) throws IOException {
            final Headers headers = t.getResponseHeaders();

            System.out.println(Menu.ANSI_CYAN + "----------CLIMB REQUEST----------\t" + Menu.ANSI_RESET);

            final String query = t.getRequestURI().getQuery();

            /*System.out.println("> Headers:\t" + headers);
            System.out.println("> Query:\t" + query);
            System.out.println("> Request:\t" + t.getRequestURI().toString());*/

            // Create a params object
            String[] listParams = query.split("&");
            Params params = new Params(listParams);
            processRequest(params);

            System.out.println(Menu.ANSI_PURPLE + "Request Cost = " + Menu.ANSI_RESET + params.getCost());

            // Get DNSName
            // LoadBalancer loadBalancer = LoadBalancer.getInstance();
            // String DNSName = loadBalancer.chooseInstance()+":8000"; //Verificar se e
            // possivel saber o porto das instancias atraves do autoscaler

            // System.out.println("CONFIG: " + Config.INSTANCE_DNS_TMP);

            // DEFAULT INSTANCE VALUE
            String instanceDNS = null;

            try {
                instanceDNS = chooseInstance();
                loadBalancer.addRequest(instanceDNS, params, t);

                final String newQuery = "/climb?" + query + "&cost=" + params.getCost();

                URL url = new URL("http://" + instanceDNS + ":8000" + newQuery);

                // System.out.println("Sending request to -> " + url.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
 
                con.setRequestMethod("GET");

                // Get response
                int responseCode = con.getResponseCode();
                // System.out.println("> Response received from instance: \t" + String.valueOf(responseCode));

                InputStream response = con.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                // read bytes from the input stream and store them in buffer
                while ((len = response.read(buffer)) != -1) {
                    // write bytes from the buffer into output stream
                    bos.write(buffer, 0, len);
                }

                loadBalancer.removeRequest(instanceDNS, params, t);
            
                /*System.out.println();
                System.out.println("Instance after remove total cost : " + instancesCost.get(instanceDNS));
                System.out.println();*/

                t.sendResponseHeaders(responseCode, bos.toByteArray().length);
                headers.add("Content-Type", "image/png");
                headers.add("Access-Control-Allow-Origin", "*");
                headers.add("Access-Control-Allow-Credentials", "true");
                headers.add("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
                headers.add("Access-Control-Allow-Headers",
                        "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
                OutputStream os = t.getResponseBody();
                os.write(bos.toByteArray());
                os.close();

                System.out.println("> Response sent:\t");
                System.out.println("-------------------------------\t");

            } catch ( Exception e ) {
                e.printStackTrace();

                loadBalancer.removeRequest(instanceDNS, params, t);

                System.out.println("SLEEPING");
                try{
                    Thread.sleep(3000);
                } catch(InterruptedException ie){
                    System.out.println(ie.getMessage());
                }

                System.out.println("LoadBalancer : Retrying request...");

                SendQueryHandler retry = new LoadBalancer.SendQueryHandler();
                retry.handle(t);

            }
        }
    }

    static Params processRequest(Params request) {
        List<RequestMetrics> cachedMetrics = Collections.synchronizedList(cache.get(request.getImage()));
        for (RequestMetrics metric : cachedMetrics) {
            if (checkCacheHit(metric, request)) {
                // Refresh Cache
                int cachedMetricIndex = cachedMetrics.indexOf(metric);
                RequestMetrics cachedMetric = cachedMetrics.remove(cachedMetricIndex);
                cachedMetrics.add(cachedMetric);
                request.setCost(metric.getWeight());
                return request;
            }
        }
        request.setCost(getEstimatedCost(request));
        return request;
    }

    // ================================================================================
    // INSTANCE MONITORING METHODS
    // ================================================================================

    // New instance running
    public static void addInstance(String dnsName) {
        instancesRunning.put(dnsName, new ArrayList<Params>());
        instancesCost.put(dnsName, new Long(0));
        instancesHttpRequests.put(dnsName, new ArrayList<HttpExchange>());
        System.out.println(Menu.ANSI_CYAN + "LoadBalancer: Instance " + dnsName + " added to loadbalancer" + Menu.ANSI_RESET);
    }

    // Instance off
    public static void removeInstance(String dnsName) {
        instancesRunning.remove(dnsName);
        instancesCost.remove(dnsName);
        instancesHttpRequests.remove(dnsName);
        System.out.println(Menu.ANSI_RED + "LoadBalancer: Instance " + dnsName + " removed from loadbalancer" + Menu.ANSI_RESET);
    }

    // Choose instance to redirect the request
    public static String chooseInstance() {
        String DNSName = "";
        Long actualCost = new Long(-1);
        Long instanceCost = new Long(0);

        for (String dns : instancesCost.keySet()) {
            //System.out.println("ChooseInstance : dns -> " + dns + " Workload -> " + instancesCost.get(dns)
            //        + " ActualCost -> " + actualCost);

            int cond1 = instancesCost.get(dns).compareTo(actualCost);
            int cond2 = actualCost.compareTo(new Long(-1));

            if (cond1 < 0 || cond2 == 0) {
                actualCost = instancesCost.get(dns);
                DNSName = dns;
            }
        }
        System.out.println(Menu.ANSI_CYAN + "LoadBalancer: Chosen Instance -> " + DNSName + Menu.ANSI_RESET);
        return DNSName;
    }

    // Add request to instance
    public static void addRequest(String dnsName, Params params, HttpExchange t) {
        synchronized (instancesRunning) {
            ArrayList<Params> requestsOnInstance = instancesRunning.get(dnsName);
            requestsOnInstance.add(params);
            ArrayList<HttpExchange> httpOnInstance = instancesHttpRequests.get(dnsName);
            httpOnInstance.add(t);
        }
        // Update cost of instance
        synchronized (instancesCost) {
            Long cost = instancesCost.get(dnsName);

            instancesCost.put(dnsName, cost + params.getCost());
            //System.out.println();
            //System.out.println("instancecost add for " + dnsName + " -> " + instancesCost.get(dnsName));
            System.out.println();
        }
    }

    // Remove request from instance
    public static void removeRequest(String dnsName, Params params, HttpExchange t) {
        synchronized (instancesRunning) {
            ArrayList<Params> requestsOnInstance = instancesRunning.get(dnsName);
            requestsOnInstance.remove(params);
            ArrayList<HttpExchange> httpOnInstance = instancesHttpRequests.get(dnsName);
            httpOnInstance.remove(t);
        }
        // Update cost of instance
        synchronized (instancesCost) {
            Long cost = instancesCost.get(dnsName);
            /*System.out.println();
            System.out.println("instancecost remove for " + dnsName + " -> " + instancesCost.get(dnsName));
            System.out.println();*/
            instancesCost.put(dnsName, cost - params.getCost());
        }
    }

    // ================================================================================
    // REQUEST COST METHODS
    // ================================================================================

    private static List<RequestMetrics> getSimilarMetricsFromDB(Params request) {
        //System.out.println("getSimilarMetricsFromDB");
        List<RequestMetrics> queryResult = new ArrayList<>();
        try {
            Map<String, AttributeValue> queryParams = new HashMap<>();
            queryParams.put(":min_upper_left_x",
                    new AttributeValue().withN(Integer.toString(computeLowerBound(Integer.parseInt(request.getX0())))));
            queryParams.put(":max_upper_left_x",
                    new AttributeValue().withN(Integer.toString(computeUpperBound(Integer.parseInt(request.getX0())))));
            queryParams.put(":min_upper_left_y",
                    new AttributeValue().withN(Integer.toString(computeLowerBound(Integer.parseInt(request.getY0())))));
            queryParams.put(":max_upper_left_y",
                    new AttributeValue().withN(Integer.toString(computeUpperBound(Integer.parseInt(request.getY0())))));
            queryParams.put(":min_lower_right_x",
                    new AttributeValue().withN(Integer.toString(computeLowerBound(Integer.parseInt(request.getX1())))));
            queryParams.put(":max_lower_right_x",
                    new AttributeValue().withN(Integer.toString(computeUpperBound(Integer.parseInt(request.getX1())))));
            queryParams.put(":min_lower_right_y",
                    new AttributeValue().withN(Integer.toString(computeLowerBound(Integer.parseInt(request.getY1())))));
            queryParams.put(":max_lower_right_y",
                    new AttributeValue().withN(Integer.toString(computeUpperBound(Integer.parseInt(request.getY1())))));
            queryParams.put(":min_start_x",
                    new AttributeValue().withN(Integer.toString(computeLowerBound(Integer.parseInt(request.getXS())))));
            queryParams.put(":max_start_x",
                    new AttributeValue().withN(Integer.toString(computeUpperBound(Integer.parseInt(request.getXS())))));
            queryParams.put(":min_start_y",
                    new AttributeValue().withN(Integer.toString(computeLowerBound(Integer.parseInt(request.getYS())))));
            queryParams.put(":max_start_y",
                    new AttributeValue().withN(Integer.toString(computeUpperBound(Integer.parseInt(request.getYS())))));
            queryParams.put(":solver_algorithm", new AttributeValue().withS(request.getAlgorithm()));
            queryParams.put(":image_name", new AttributeValue().withS(request.getImage()));

            DynamoDBQueryExpression<RequestMetrics> queryExpression = new DynamoDBQueryExpression()
                    .withKeyConditionExpression("image_name = :image_name")
                    .withFilterExpression("solver_algorithm = :solver_algorithm"
                            + " and upper_left_x between :min_upper_left_x and :max_upper_left_x"
                            + " and upper_left_y between :min_upper_left_y and :max_upper_left_y"
                            + " and lower_right_x between :min_lower_right_x and :max_lower_right_x"
                            + " and lower_right_y between :min_lower_right_y and :max_lower_right_y"
                            + " and start_x between :min_start_x and :max_start_x"
                            + " and start_y between :min_start_y and :max_start_y")
                    .withExpressionAttributeValues(queryParams);
            queryResult = AmazonDynamoDBHelper.query(RequestMetrics.class, queryExpression);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryResult;
    }

    private static List<RequestMetrics> getSameImageMetricsFromDB(Params request) {
        //System.out.println("getSimilarMetricsFromDB");
        List<RequestMetrics> queryResult = new ArrayList<>();
        try {
            Map<String, AttributeValue> queryParams = new HashMap<>();
            queryParams.put(":solver_algorithm", new AttributeValue().withS(request.getAlgorithm()));
            queryParams.put(":image_name", new AttributeValue().withS(request.getImage()));
            queryParams.put(":upper_left_x",
                    new AttributeValue().withN(Integer.toString(Integer.parseInt(request.getX0()))));
            queryParams.put(":upper_left_y",
                    new AttributeValue().withN(Integer.toString(Integer.parseInt(request.getY0()))));
            queryParams.put(":lower_right_x",
                    new AttributeValue().withN(Integer.toString(Integer.parseInt(request.getX1()))));
            queryParams.put(":lower_right_y",
                    new AttributeValue().withN(Integer.toString(Integer.parseInt(request.getY1()))));
            queryParams.put(":start_x",
                    new AttributeValue().withN(Integer.toString(Integer.parseInt(request.getXS()))));
            queryParams.put(":start_y",
                    new AttributeValue().withN(Integer.toString(Integer.parseInt(request.getYS()))));

            DynamoDBQueryExpression<RequestMetrics> queryExpression = new DynamoDBQueryExpression()
                    .withKeyConditionExpression("image_name = :image_name")
                    .withFilterExpression("solver_algorithm = :solver_algorithm" + " and upper_left_x = :upper_left_x"
                            + " and upper_left_y = :upper_left_y" + " and lower_right_x = :lower_right_x"
                            + " and lower_right_y = :lower_right_y" + " and start_x = :start_x"
                            + " and start_y = :start_y")
                    .withExpressionAttributeValues(queryParams);
            queryResult = AmazonDynamoDBHelper.query(RequestMetrics.class, queryExpression);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryResult;
    }

    private static List<RequestMetrics> getMatchingMetricFromDB(Params request) {
        //System.out.println("getMatchingMetricFromDB");
        List<RequestMetrics> queryResult = new ArrayList<>();
        try {
            Map<String, AttributeValue> queryParams = new HashMap<>();
            queryParams.put(":solver_algorithm", new AttributeValue().withS(request.getAlgorithm()));
            queryParams.put(":image_name", new AttributeValue().withS(request.getImage()));

            DynamoDBQueryExpression<RequestMetrics> queryExpression = new DynamoDBQueryExpression()
                    .withKeyConditionExpression("image_name = :image_name")
                    .withFilterExpression("solver_algorithm = :solver_algorithm")
                    .withExpressionAttributeValues(queryParams);
            queryResult = AmazonDynamoDBHelper.query(RequestMetrics.class, queryExpression);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryResult;
    }

    static int computeLowerBound(int param) {
        int result = param - RANGE_PX_OFFSET;
        return result > 0 ? result : 0;
    }

    static int computeUpperBound(int param) {
        int result = param + RANGE_PX_OFFSET;
        return result;
    }

    static long computeAverageCost(List<RequestMetrics> dbMetrics) {
        long totalWeight = 0;
        if (dbMetrics.size() > 0) {
            for (RequestMetrics metric : dbMetrics) {
                totalWeight += metric.getWeight();
            }
            return (long) (totalWeight / dbMetrics.size());
        } else {
            System.out.println(Menu.ANSI_RED + "LoadBalancer: No recorded requests for requested heightmap!" +  Menu.ANSI_RESET);
            // NEED TO DECIDE ON UNKNOWN COST
            return (long) AutoScaler.MAX_INSTANCE_WORKLOAD / 2;
        }
    }

    public static long getEstimatedCost(Params request) {
        //System.out.println("getEstimatedCost");
        long eCost = -1;
        List<RequestMetrics> dbMetrics = null;
        dbMetrics = getMatchingMetricFromDB(request);
        if (dbMetrics.size() == 0) {
            System.out.println(Menu.ANSI_RED + "LoadBalancer: No matching requests on DynamoDB!" + Menu.ANSI_RESET);
            dbMetrics = getSimilarMetricsFromDB(request);
            if (dbMetrics.size() == 0) {
                System.out.println(Menu.ANSI_RED + "LoadBalancer: No similar requests on DynamoDB!" + Menu.ANSI_RESET);
                dbMetrics = getSimilarMetricsFromDB(request);
            } else {
                return (long) AutoScaler.MAX_INSTANCE_WORKLOAD / 2;
            }
        }
        for (RequestMetrics metric : dbMetrics) {
            addToCache(metric);
        }
        eCost = computeAverageCost(dbMetrics);
        return eCost;
    }

    // ================================================================================
    // CACHE METHODS
    // ================================================================================

    static boolean checkCacheHit(RequestMetrics metric, Params request) {
        if (metric.getImage().equals(request.getImage()) && metric.getW() == Integer.parseInt(request.getW())
                && metric.getH() == Integer.parseInt(request.getH())
                && metric.getX0() == Integer.parseInt(request.getX0())
                && metric.getX1() == Integer.parseInt(request.getX1())
                && metric.getY0() == Integer.parseInt(request.getY0())
                && metric.getY1() == Integer.parseInt(request.getY1())
                && metric.getXS() == Integer.parseInt(request.getXS())
                && metric.getYS() == Integer.parseInt(request.getYS())
                && metric.getAlgorithm().equals(request.getAlgorithm())
                && metric.getImage().equals(request.getImage())) {
            return true;
        }
        return false;
    }

    static void addToCache(RequestMetrics metric) {
        List<RequestMetrics> cachedMetrics = Collections.synchronizedList(cache.get(metric.getImage()));
        if (cachedMetrics.size() == MAX_CACHE_SIZE) {
            cachedMetrics.remove(0);
        }
        cachedMetrics.add(metric);
    }

    private static void initCache() {
        cache = new ConcurrentHashMap();
        cache.put(HeightMaps.IMAGE_1, new LinkedList<RequestMetrics>());
        cache.put(HeightMaps.IMAGE_2, new LinkedList<RequestMetrics>());
        cache.put(HeightMaps.IMAGE_3, new LinkedList<RequestMetrics>());
        cache.put(HeightMaps.IMAGE_4, new LinkedList<RequestMetrics>());
        cache.put(HeightMaps.IMAGE_5, new LinkedList<RequestMetrics>());
        cache.put(HeightMaps.IMAGE_6, new LinkedList<RequestMetrics>());
        cache.put(HeightMaps.IMAGE_7, new LinkedList<RequestMetrics>());
        cache.put(HeightMaps.IMAGE_8, new LinkedList<RequestMetrics>());
        cache.put(HeightMaps.IMAGE_9, new LinkedList<RequestMetrics>());
        cache.put(HeightMaps.IMAGE_10, new LinkedList<RequestMetrics>());
        cache.put(HeightMaps.IMAGE_11, new LinkedList<RequestMetrics>());
        cache.put(HeightMaps.IMAGE_12, new LinkedList<RequestMetrics>());
        cache.put(HeightMaps.IMAGE_13, new LinkedList<RequestMetrics>());
    }

    // ================================================================================
    // GETTERS and SETTERS
    // ================================================================================

    public static int getPort() {
        return port;
    }

}
