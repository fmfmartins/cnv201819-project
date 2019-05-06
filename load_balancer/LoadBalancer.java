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

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import pt.ulisboa.tecnico.cnv.solver.Solver;
import pt.ulisboa.tecnico.cnv.solver.SolverArgumentParser;
import pt.ulisboa.tecnico.cnv.solver.SolverFactory;
import pt.ulisboa.tecnico.cnv.mss.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.elasticloadbalancingv2.*;
import com.amazonaws.services.elasticloadbalancingv2.model.*;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

import javax.imageio.ImageIO;


public class LoadBalancer {

	static AmazonElasticLoadBalancing lb;	

	public static void main(final String[] args) throws Exception{
		
		//final HttpServer load_balancer = HttpServer.create(new InetSocketAddress(8000),0);
		
		//Creation of the Load Balancer 
		init();
        
	}
	
	public static void init() throws Exception{

		//Getting the credentials (inside .aws folder)
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
		

                lb = AmazonElasticLoadBalancingClientBuilder.standard().withRegion("eu-west-3").withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
		
		//Creation of LoadBalancer
                CreateLoadBalancerRequest req = new CreateLoadBalancerRequest();
		//Set name
                req.setName("CNV-PROJECT");
		//Set subnets (two from different zones)
                Collection<String> collection = new ArrayList<String>();
                collection.add("subnet-396e2550"); //eu-west-3a
                collection.add("subnet-fa9126b7"); //eu-west-3c
                req.setSubnets(collection);
		
                CreateLoadBalancerResult result = lb.createLoadBalancer(req);
		
	}	

}

	
