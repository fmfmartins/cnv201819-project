package pt.ulisboa.tecnico.cnv.mss;

import java.util.concurrent.atomic.AtomicInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class RequestMetrics {


	private static final AtomicInteger counter = new AtomicInteger(0);

	private int sequenceID;
	private long threadID;
	private Timestamp timestamp;

	private int w;
	private int h;
	private int x0;
	private int x1;
	private int y0;
	private int y1;
	private int xS;
	private int yS;
	private String s;
	private String i;
	
	private int bbCount;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");


	public RequestMetrics(long threadID){
		this.threadID = threadID;
		this.sequenceID = counter.incrementAndGet();
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}

	public int getSequenceID(){
		return this.sequenceID;
	}

	public void setParams(int w, int h, int x0, int x1, int y0, int y1, int xS, int yS, String s, String i){
		this.w = w;
		this.h = h;
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.xS = xS;
		this.yS = yS;
		this.s = s;
		this.i = i;
	}

	public void setBBCount(int bb){
		this.bbCount = bb;
	}

	public void incrBBCount(int incr){
		this.bbCount += incr;
	}

	public static int getCount(){
		return counter.get();
	}

	public long getThreadID(){
		return this.threadID;
	}

	public void printInfo(){
		System.out.println("\t//Request Parameters//\t");
		System.out.println("Width: " + this.w);
		System.out.println("Height: " + this.h);
		System.out.println("Upper-Left Corner X: " + this.x0);
		System.out.println("Upper-left Corner Y: " + this.y0);
		System.out.println("Lower-Right Corner X: " + this.x1);
		System.out.println("Lower-Right Corner Y: " + this.y1);
		System.out.println("Starting Point X: " + this.xS);
		System.out.println("Starting Point Y: " + this.yS);
		System.out.println("Solver Algorithm: " + this.s);
		System.out.println("Image Path: " + this.i);
		System.out.println("\t//Metrics//\t");
		System.out.println("Number of basic blocks: " + this.bbCount);
	}

	public void outputToFile(){
		try{
			String fileName = "/tmp/stats__" + Thread.currentThread().getId() + "__" + sdf.format(this.timestamp) + ".txt";
			File file = new File(fileName);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(this.toString());
			writer.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public String toString(){
		String s;
		s = "\t//Request Parameters//\t\n";
		s += String.format("Width: %d\n" , this.w);
		s += String.format("Height: %d\n", this.h);
		s += String.format("Upper-Left Corner X: %d\n", this.x0);
		s += String.format("Upper-left Corner Y: %d\n" , this.y0);
		s += String.format("Lower-Right Corner X: %d\n" , this.x1);
		s += String.format("Lower-Right Corner Y: %d\n" , this.y1);
		s += String.format("Starting Point X: %d\n" , this.xS);
		s += String.format("Starting Point Y: %d\n" , this.yS);
		s += String.format("Solver Algorithm: %s\n" , this.s);
		s += String.format("Image Path: %s\n" , this.i);
		s += String.format("\t//Metrics//\t\n");
		s += String.format("Number of basic blocks: %d\n" , this.bbCount);
		return s;
	}
	

}
