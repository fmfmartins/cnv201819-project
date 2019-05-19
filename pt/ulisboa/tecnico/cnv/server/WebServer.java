package pt.ulisboa.tecnico.cnv.server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.lang.Thread.*;
import java.util.Arrays;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import pt.ulisboa.tecnico.cnv.solver.Solver;
import pt.ulisboa.tecnico.cnv.solver.SolverArgumentParser;
import pt.ulisboa.tecnico.cnv.solver.SolverFactory;
import pt.ulisboa.tecnico.cnv.mss.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.util.EC2MetadataUtils;

import javax.imageio.ImageIO;

public class WebServer {

	private static DynamoDBMapper dbMapper;
	private static long currentWorkload;

	public static void main(final String[] args) throws Exception {

		// final HttpServer server = HttpServer.create(new
		// InetSocketAddress("127.0.0.1", 8000), 0);

		currentWorkload = 0;

		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
					AmazonDynamoDBHelper.createTable();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

			}
		};

		new Thread(r).start();

		final HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

		server.createContext("/climb", new MyHandler());

		server.createContext("/test", new MyTestHandler());

		// be aware! infinite pool of threads!
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();

		System.out.println(server.getAddress().toString());
	}

	static class MyTestHandler implements HttpHandler {
		@Override
		public void handle(final HttpExchange t) throws IOException {
			final Headers headers = t.getResponseHeaders();

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

	static class MyHandler implements HttpHandler {
		@Override
		public void handle(final HttpExchange t) throws IOException {

			RequestMetricsStorage rms = RequestMetricsStorage.getInstance();

			// Get the query.
			final String query = t.getRequestURI().getQuery();

			System.out.println("> Query:\t" + query);

			System.out.println("> Thread id:\t" + Thread.currentThread().getId());

			// Break it down into String[].
			final String[] params = query.split("&");

			/*
			 * for(String p: params) { System.out.println(p); }
			 */

			// Store as if it was a direct call to SolverMain.
			final ArrayList<String> newArgs = new ArrayList<>();
			for (final String p : params) {
				final String[] splitParam = p.split("=");
				newArgs.add("-" + splitParam[0]);
				newArgs.add(splitParam[1]);

				/*
				 * System.out.println("splitParam[0]: " + splitParam[0]);
				 * System.out.println("splitParam[1]: " + splitParam[1]);
				 */
			}

			newArgs.add("-d");

			// Store from ArrayList into regular String[].
			final String[] args = new String[newArgs.size()];
			int i = 0;
			for (String arg : newArgs) {
				args[i] = arg;
				i++;
			}

			/*
			 * for(String ar : args) { System.out.println("ar: " + ar); }
			 */

			// Store the request parameters

			RequestMetrics metrics = new RequestMetrics(Thread.currentThread().getId(),
					EC2MetadataUtils.getInstanceId());

			metrics.setParams(Integer.parseInt(args[1]), Integer.parseInt(args[3]), Integer.parseInt(args[5]),
					Integer.parseInt(args[7]), Integer.parseInt(args[9]), Integer.parseInt(args[11]),
					Integer.parseInt(args[13]), Integer.parseInt(args[15]), args[17], args[19],
					Long.parseLong(args[21]));

			rms.metricsStorage.put(Thread.currentThread().getId(), metrics);

			System.out.println("EstimatedCost -> " + metrics.getEstimatedCost());

			currentWorkload += metrics.getEstimatedCost();

			// rms.metricsStorage.get(Thread.currentThread().getId()).printInfo();

			final String[] solverArgs = Arrays.copyOfRange(args, 0, 20);

			SolverArgumentParser ap = null;

			try {
				ap = new SolverArgumentParser(solverArgs);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			System.out.println("> Finished parsing args.");

			// Create solver instance from factory.
			Solver s = SolverFactory.getInstance().makeSolver(ap);

			// Write figure file to disk.
			File responseFile = null;
			try {

				final BufferedImage outputImg = s.solveImage();

				final String outPath = ap.getOutputDirectory();

				final String imageName = s.toString();

				if (ap.isDebugging()) {
					System.out.println("> Image name: " + imageName);
				}

				final Path imagePathPNG = Paths.get(outPath, imageName);
				ImageIO.write(outputImg, "png", imagePathPNG.toFile());

				responseFile = imagePathPNG.toFile();

			} catch (final FileNotFoundException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			// Output to file

			RequestMetrics m = rms.metricsStorage.get(Thread.currentThread().getId());
			System.out.println(m);
			// m.outputToFile();

			// Send response to browser.
			final Headers hdrs = t.getResponseHeaders();

			t.sendResponseHeaders(200, responseFile.length());

			hdrs.add("Content-Type", "image/png");

			hdrs.add("Access-Control-Allow-Origin", "*");
			hdrs.add("Access-Control-Allow-Credentials", "true");
			hdrs.add("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
			hdrs.add("Access-Control-Allow-Headers",
					"Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

			final OutputStream os = t.getResponseBody();
			Files.copy(responseFile.toPath(), os);

			os.close();

			System.out.println("> Sent response to " + t.getRemoteAddress().toString());

			/*
			 * URIBuilder builder = new URIBuilder();
			 * builder.setScheme("http").setHost("www.google.com").setPath("/search")
			 * .setParameter("q", "httpclient") .setParameter("btnG", "Google Search")
			 * .setParameter("aq", "f") .setParameter("oq", ""); URI uri = builder.build();
			 * HttpGet httpget = new HttpGet(uri); System.out.println(httpget.getURI());
			 */

			// Upload to amazon DynamoDB
			try {
				long mWeight = MetricsCalculator.computeWeight(m);
				// System.out.println("> mWeight : " + mWeight);
				m.setWeight(mWeight);
				AmazonDynamoDBHelper.uploadItem(m);
				System.out.println("> Metric upload success ");
			} catch (Exception e) {
				System.out.println("> Metric upload failure ");
				e.printStackTrace();
			}
		}
	}
}
