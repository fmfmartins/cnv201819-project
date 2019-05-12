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

import com.sun.jndi.toolkit.url.UrlUtil;
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

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.AmazonClientException;
import com.amazonaws.services.elasticloadbalancingv2.*;
import com.amazonaws.services.elasticloadbalancingv2.model.*;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

import javax.imageio.ImageIO;


public class LoadBalancer{

        //Hashset to save the future instances
        Set<Instance> instances = new HashSet<Instance>();
	
	public static void main(final String[] args) throws Exception{
		
		//Creation of the Load Balancer 
		init();
        
	}
	
	public static void init() throws Exception{

                final HttpServer load_balancer = HttpServer.create(new InetSocketAddress(8000),0);
		
                load_balancer.createContext("/climb", new SendQueryHandler());

                load_balancer.createContext("/test", new MyTestHandler());

                // be aware! infinite pool of threads!
                load_balancer.setExecutor(Executors.newCachedThreadPool());
                load_balancer.start();
                
                System.out.println(load_balancer.getAddress().toString());
		
	}
	        
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
			
			//DNS name
                	String DNSName="ec2-35-180-190-107.eu-west-3.compute.amazonaws.com:8000";		
                	URL url = new URL("http://"+DNSName+t.getRequestURI().toString());
                	HttpURLConnection con = (HttpURLConnection) url.openConnection();
               
                	// Send request
                	con.setRequestMethod("GET");

			//Get respons
                      
                	/*int responseCode = con.getResponseCode();
			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    			String inputLine;
    			StringBuffer response = new StringBuffer();

    			while ((inputLine = in.readLine()) != null) {
        			response.append(inputLine);
    			}
    			in.close();

			String result=response.toString();
    			System.out.println(result);*/

			System.out.println("-------------------------------\t");
                }
	} 

}
	
