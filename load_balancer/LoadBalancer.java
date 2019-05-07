import java.io.OutputStream;
import java.net.InetSocketAddress;
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

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
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
		
		load_balancer.createContext("/climb", new MyHandler());

                //server.createContext("/test", new MyTestHandler());

                // be aware! infinite pool of threads!
                load_balancer.setExecutor(Executors.newCachedThreadPool());
                load_balancer.start();
                
                System.out.println(load_balancer.getAddress().toString());
		
	}
	

	static class MyHandler implements HttpHandler {
                @Override
                public void handle(final HttpExchange t) throws IOException {
                        // Get the query.
			final String query = t.getRequestURI().getQuery();

			System.out.println("> Query:\t" + query);

			System.out.println("> Thread id:\t" + Thread.currentThread().getId());

			// Break it down into String[].
                        final String[] params = query.split("&");
                        
                        //Get all the parameters
                        System.out.println(params);
                        
                        //Try to estimate the wheight of the query, by its params

                        //Get a way of knowing the instances online
			

                }

	}
}
	
