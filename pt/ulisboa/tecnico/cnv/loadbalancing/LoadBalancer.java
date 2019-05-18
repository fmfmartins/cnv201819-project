package pt.ulisboa.tecnico.cnv.loadbalancing;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.lang.Thread.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
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

    private static int RANGE_PX_OFFSET = 50;

	// LoadBalancer instance
	private static final LoadBalancer loadBalancer = new LoadBalancer();	
	
	// Port	
	private static final int port = 8000;

    //Hashset to save the future instances
    private ArrayList<String> instancesRunning = new ArrayList<String>();
	
	//Hashmap that stores the requests that instances are doing
	//Map<String,Map<String,int>> requestsOnInstances = new Map<String,Map<String,int>>();

	//Test main class
	public static void main(String[] args){
    		LoadBalancer loadBalancer = LoadBalancer.getInstance();
	}


	//Get instance of LoadBalancer
	public static LoadBalancer getInstance() {
		return loadBalancer;
	}

	//Contruct
	private LoadBalancer(){
		try {
			//Creation of the Load Balancer 
			init();
		}
		catch(Exception e){
			System.out.println("Exception on init:"+e.getMessage());
		}
        
	}

	public static int getPort(){
		return port;
	}
	
	public void init() throws Exception{

		int port = LoadBalancer.getInstance().getPort();		

        final HttpServer load_balancer = HttpServer.create(new InetSocketAddress(port),0);
		
        load_balancer.createContext("/climb", new SendQueryHandler());
        load_balancer.createContext("/test", new MyTestHandler());
        // be aware! infinite pool of threads!
        load_balancer.setExecutor(Executors.newCachedThreadPool());
        load_balancer.start();
		System.out.println(load_balancer.getAddress().toString());

	}

	//New instance running
	public void AddInstance(String dnsName){
 		synchronized(instancesRunning){
			instancesRunning.add(dnsName);
		}
	
	}

	//Instance off
	public void RemoveInstance(String dnsName){
		synchronized(instancesRunning){
			instancesRunning.remove(dnsName);
		}
	}

	/*
	//Choose instance to redirect the request
	public static String ChoosesInstance(){
		
		
			
	}

	public static String getInstancePublicDnsName(String instanceId){
	
		for (Reservation reservation : reservations) {
      			for (Instance instance : reservation.getInstances()) {
        			if (instance.getInstanceId().equals(instanceId)){
          				return instance.getPublicDnsName();
      				}
    		}
    		return null;
	
	}*/

	/*public static void AddRequestToInstance(String instanceId,String requestWeight){

		Map<String,Map<String,Map<String,int>> requests = this.requetsOnInstance.get(instanceId);
		
		int value = requests.get(requestWeight)+1;
		
		requests.put(requestWeight,value);
	
	}

	public static void RemoveRequestFromInstance(String intanceId, String requestWeight){

		Map<String,Map<String,Map<String,int>> requests = this.requetsOnInstance.get(instanceId);

                int value = requests.get(requestWeight)-1;

                requests.put(requestWeight,value);

	}*/


	
	        
    static class MyTestHandler implements HttpHandler {
        @Override
		public void handle(final HttpExchange t) throws IOException {
            final Headers headers = t.getResponseHeaders();
                        
            final String query = t.getRequestURI().getQuery();

            System.out.println("> Query:\t" + query);
                        
			String response = "test ok";
				
			t.sendResponseHeaders(200, response.getBytes().length);

			headers.add("Access-Control-Allow-Origin", "*");
			headers.add("Access-Control-Allow-Credentials", "true");
			headers.add("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
			headers.add("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

	
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

	}

	// Handler that deal with the requests
	static class SendQueryHandler implements HttpHandler{
		@Override
		public void handle(final HttpExchange t) throws IOException{
            final Headers headers = t.getResponseHeaders();
                       
            System.out.println("----------NEW REQUEST----------\t");
            
            final String query = t.getRequestURI().getQuery();
			
            System.out.println("> Headers:\t" + query);
            System.out.println("> Query:\t" + query);
			System.out.println("> Request:\t" + t.getRequestURI().toString());
			
			
			//Create a params object
			String[] listParams = query.split("&");
			Params params = new Params(listParams);	

			getEstimatedCost(params);
			
			//LoadBalancer loadBalancer = LoadBalancer.getInstance()
			//String DNSName = loadBalancer.chooseInstance()

			//DNS name

            //String DNSName="ec2-35-180-31-140.eu-west-3.compute.amazonaws.com:8000";
            String DNSName="ec2-35-180-98-85.eu-west-3.compute.amazonaws.com:8000";
		
            URL url = new URL("http://"+DNSName+t.getRequestURI().toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            // Send request
            con.setRequestMethod("GET");


			//Get response
                      
            int responseCode = con.getResponseCode();

						
			InputStream response = con.getInputStream();
    			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int len;

		 	//read bytes from the input stream and store them in buffer
		 	while ((len = response.read(buffer)) != -1) {
		 		// write bytes from the buffer into output stream
				bos.write(buffer, 0, len);
		 	}
		
		
			t.sendResponseHeaders(responseCode, bos.toByteArray().length);

			headers.add("Content-Type","image/png");
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Credentials", "true");
            headers.add("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

            OutputStream os = t.getResponseBody();
            os.write(bos.toByteArray());
            os.close();

			System.out.println("> Response \t:" +  String.valueOf(responseCode));			

			System.out.println("-------------------------------\t");
        }
    } 
    


    public static long getEstimatedCost(Params request){
		System.out.println("getEstimatedCost");
        
        List<RequestMetrics> dbMetrics = null;
		dbMetrics = getSimilarMetricsFromDB(request);
		if(dbMetrics == null){
			System.out.println("Hello" + dbMetrics.size());
		} else {
			for(RequestMetrics metric : dbMetrics){
				System.out.println(metric);
			}
		}
        return 69420;
    }

    private static List<RequestMetrics> getSimilarMetricsFromDB(Params request){
		System.out.println("getSimilarMetricsFromDB");
		List<RequestMetrics> queryResult = new ArrayList<>();
		try{
			Map<String, AttributeValue> queryParams = new HashMap<>();
			queryParams.put(":min_upper_left_x", new AttributeValue().withN(Integer.toString(computeLowerBound(Integer.parseInt(request.getX0())))));
			queryParams.put(":max_upper_left_x", new AttributeValue().withN(Integer.toString(computeUpperBound(Integer.parseInt(request.getX0())))));
			queryParams.put(":min_upper_left_y", new AttributeValue().withN(Integer.toString(computeLowerBound(Integer.parseInt(request.getY0())))));
			queryParams.put(":max_upper_left_y", new AttributeValue().withN(Integer.toString(computeUpperBound(Integer.parseInt(request.getY0())))));
			queryParams.put(":min_lower_right_x", new AttributeValue().withN(Integer.toString(computeLowerBound(Integer.parseInt(request.getX1())))));
			queryParams.put(":max_lower_right_x", new AttributeValue().withN(Integer.toString(computeUpperBound(Integer.parseInt(request.getX1())))));
			queryParams.put(":min_lower_right_y", new AttributeValue().withN(Integer.toString(computeLowerBound(Integer.parseInt(request.getY1())))));
			queryParams.put(":max_lower_right_y", new AttributeValue().withN(Integer.toString(computeUpperBound(Integer.parseInt(request.getY1())))));
			queryParams.put(":min_start_x", new AttributeValue().withN(Integer.toString(computeLowerBound(Integer.parseInt(request.getXS())))));
			queryParams.put(":max_start_x", new AttributeValue().withN(Integer.toString(computeUpperBound(Integer.parseInt(request.getXS())))));
			queryParams.put(":min_start_y", new AttributeValue().withN(Integer.toString(computeLowerBound(Integer.parseInt(request.getYS())))));
			queryParams.put(":max_start_y", new AttributeValue().withN(Integer.toString(computeUpperBound(Integer.parseInt(request.getYS())))));
			queryParams.put(":solver_algorithm", new AttributeValue().withS(request.getAlgorithm()));
			queryParams.put(":image_name", new AttributeValue().withS(request.getImage()));

			DynamoDBQueryExpression<RequestMetrics> queryExpression = new DynamoDBQueryExpression()
				.withKeyConditionExpression("image_name = :image_name")
				.withFilterExpression("solver_algorithm = :solver_algorithm"
					+ " and image_name = :image_name"
					+ " and upper_left_x between :min_upper_left_x and :max_upper_left_x"
					+ " and upper_left_y between :min_upper_left_y and :max_upper_left_y"
					+ " and lower_right_x between :min_lower_right_x and :max_lower_right_x"
					+ " and lower_right_y between :min_lower_right_y and :max_lower_right_y"
					+ " and start_x between :min_start_x and :max_start_x"
					+ " and start_y between :min_start_y and :max_start_y")
				.withExpressionAttributeValues(queryParams);
			
			queryResult = AmazonDynamoDBHelper.query(RequestMetrics.class, queryExpression);
		} catch (Exception e){
			e.printStackTrace();
		}
        
        return queryResult;
    }

    static int computeLowerBound(int param){
        int result = param - RANGE_PX_OFFSET;
        return result > 0 ? result : 0;
    }

    static int computeUpperBound(int param){
        int result = param + RANGE_PX_OFFSET;
        return result;
    }

}
	
