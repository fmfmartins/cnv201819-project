package pt.ulisboa.tecnico.cnv.mss;

import java.util.concurrent.atomic.AtomicInteger;

public class RequestMetrics {


	private static final AtomicInteger counter = new AtomicInteger(0);
	private int sequenceID;
	private long threadID;
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


	public RequestMetrics(long threadID){
		this.threadID = threadID;
		this.sequenceID = counter.incrementAndGet();
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
	

}
