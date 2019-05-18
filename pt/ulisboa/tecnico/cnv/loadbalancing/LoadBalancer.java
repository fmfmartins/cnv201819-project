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

import javax.imageio.ImageIO;


public class LoadBalancer{

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


	
	//Handler Test        
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
			
			//LoadBalancer loadBalancer = LoadBalancer.getInstance()
			//String DNSName = loadBalancer.chooseInstance()

			//DNS name
			String DNSName="ec2-35-180-192-225.eu-west-3.compute.amazonaws.com:8000";
			URL url = new URL("http://"+DNSName+t.getRequestURI().toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");		


			//Get respons
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
			
}
	
