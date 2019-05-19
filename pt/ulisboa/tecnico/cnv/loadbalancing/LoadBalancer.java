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

	private static int RANGE_PX_OFFSET = 10;
	private static int MAX_CACHE_SIZE = 20;

	// LoadBalancer instance
	private static final LoadBalancer loadBalancer = new LoadBalancer();
	// Cache where the key is the image name
	private static Map<String, List<RequestMetrics>> cache;
		
	// Port	
	private static final int port = 8000;

    //Hashmap to save the future instances and requests running on them
    private static ConcurrentHashMap<String,ArrayList<Params>> instancesRunning = new ConcurrentHashMap<String,ArrayList<Params>>();
	private static ConcurrentHashMap<String,Long> instancesCost= new ConcurrentHashMap<String,Long>();	

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
			e.printStackTrace();
		}
        
	}

	
	public void init() throws Exception {

		int port = LoadBalancer.getInstance().getPort();

		initCache();

		final HttpServer load_balancer = HttpServer.create(new InetSocketAddress(port),0);
	
		load_balancer.createContext("/climb", new SendQueryHandler());
		load_balancer.createContext("/test", new MyTestHandler());
		load_balancer.setExecutor(Executors.newCachedThreadPool());
		load_balancer.start();
		System.out.println(load_balancer.getAddress().toString());

	}

	//================================================================================
    // REQUEST HANDLERS
	//================================================================================
   
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
			processRequest(params);

			System.out.println("Request Cost = " + params.getCost());
			
			// Get DNSName
			//LoadBalancer loadBalancer = LoadBalancer.getInstance();
			//String DNSName = loadBalancer.chooseInstance()+":8000"; //Verificar se e possivel saber o porto das instancias atraves do autoscaler
			
			// Add request to instance
			//loadBalancer.addRequest(DNSName,params);

			//System.out.println("CONFIG: " + Config.INSTANCE_DNS_TMP);

			loadBalancer.addRequest(Config.INSTANCE_DNS_TMP, params);

			final String newQuery = "/climb?" + query + "&cost=" + params.getCost();
		
			URL url = new URL("http://" + Config.INSTANCE_DNS_TMP + newQuery);
			
			System.out.println("Sending request to -> " + url.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            // Send request
			con.setRequestMethod("GET");

			//Get response        
            int responseCode = con.getResponseCode();
			System.out.println("> Response received from instance: \t" +  String.valueOf(responseCode));

			InputStream response = con.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
		 	//read bytes from the input stream and store them in buffer
		 	while ((len = response.read(buffer)) != -1) {
		 		// write bytes from the buffer into output stream
				bos.write(buffer, 0, len);
			}
			 
			loadBalancer.removeRequest(Config.INSTANCE_DNS_TMP, params);
		
	
			t.sendResponseHeaders(responseCode, bos.toByteArray().length);
			headers.add("Content-Type","image/png");
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Credentials", "true");
            headers.add("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
            OutputStream os = t.getResponseBody();
            os.write(bos.toByteArray());
			os.close();
			
			System.out.println("> Response sent:\t");			
			System.out.println("-------------------------------\t");
        }
    } 
    
	static Params processRequest(Params request){
		List<RequestMetrics> cachedMetrics = Collections.synchronizedList(cache.get(request.getImage()));
		for(RequestMetrics metric : cachedMetrics){
			if(checkCacheHit(metric, request)){
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
	
	//================================================================================
    // INSTANCE MONITORING METHODS
	//================================================================================

	//New instance running
	public static void addInstance(String dnsName){
		instancesRunning.put(dnsName,new ArrayList<Params>());
	   	instancesCost.put(dnsName,new Long(0));
   }

   //Instance off
   public static void removeInstance(String dnsName){
	   instancesRunning.remove(dnsName);
	   instancesCost.remove(dnsName);
   }

   
   // Choose instance to redirect the request
   public static String chooseInstance(){
	   
	   String DNSName="";
	   long actualCost=-1;

	   // Get instance with lower cost
	   for(Map.Entry<String, Long> instance : instancesCost.entrySet()){
		   long instanceCost=instance.getValue();
		   if(instanceCost<actualCost || actualCost==-1){
			   actualCost=instanceCost;
			   DNSName=instance.getKey();
		   }
	   }
	   return DNSName;
							   
   }

   // Add request to instance
   public static void addRequest(String dnsName,Params params){
	   ArrayList<Params> requestsOnInstance = instancesRunning.get(dnsName);
	   requestsOnInstance.add(params);
	   // Update cost of instance
	   Long cost = instancesCost.get(dnsName);
	   instancesCost.put(dnsName,cost+params.getCost());  
   }

   // Remove request from instance
   public static void removeRequest(String dnsName, Params params){
		ArrayList<Params> requestsOnInstance = instancesRunning.get(dnsName);
		requestsOnInstance.remove(params);
	   	// Update cost of instance
	   	Long cost = instancesCost.get(dnsName);
	   	instancesCost.put(dnsName, cost - params.getCost());
   }


	//================================================================================
    // REQUEST COST METHODS
	//================================================================================

	
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
	
	static long computeAverageCost(List<RequestMetrics> dbMetrics){
		long totalWeight = 0;
		if(dbMetrics.size() > 0){
			for(RequestMetrics metric : dbMetrics){
				totalWeight += metric.getWeight();
			}
			return (long) (totalWeight / dbMetrics.size());
		} else {
			// NEED TO DECIDE ON UNKNOWN COST
			return -1;
		}
	}

	
    public static long getEstimatedCost(Params request){
		System.out.println("getEstimatedCost");
		long eCost = -1;
        List<RequestMetrics> dbMetrics = null;
		dbMetrics = getSimilarMetricsFromDB(request);
		if(dbMetrics == null){
			System.out.println("Empty set-> size: " + dbMetrics.size());
		} else {
			for(RequestMetrics metric : dbMetrics){
				addToCache(metric);
			}
			eCost = computeAverageCost(dbMetrics);
		}
        return eCost;
	}


	//================================================================================
    // CACHE METHODS
	//================================================================================
	
	static boolean checkCacheHit(RequestMetrics metric, Params request){
		if(
			metric.getImage().equals(request.getImage()) &&
			metric.getW() == Integer.parseInt(request.getW()) &&
			metric.getH() == Integer.parseInt(request.getH()) &&
			metric.getX0() == Integer.parseInt(request.getX0()) &&
			metric.getX1() == Integer.parseInt(request.getX1()) &&
			metric.getY0() == Integer.parseInt(request.getY0()) &&
			metric.getY1() == Integer.parseInt(request.getY1()) &&
			metric.getXS() == Integer.parseInt(request.getXS()) &&
			metric.getYS() == Integer.parseInt(request.getYS()) &&
			metric.getAlgorithm().equals(request.getAlgorithm()) &&
			metric.getImage().equals(request.getImage())
		) {
			return true;
		}
		return false;
	}

	static void addToCache(RequestMetrics metric){
		List<RequestMetrics> cachedMetrics = Collections.synchronizedList(cache.get(metric.getImage()));
		if(cachedMetrics.size() == MAX_CACHE_SIZE){
			cachedMetrics.remove(0);
		}
		cachedMetrics.add(metric);
	}

	private void initCache(){
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


	//================================================================================
    // GETTERS and SETTERS 
	//================================================================================

	public static int getPort(){
		return port;
	}

}
	
