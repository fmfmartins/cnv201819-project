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


	public RequestMetrics(long threadID){
		this.threadID = threadID;
		this.sequenceID = counter.incrementAndGet();
	}

	public int getSequenceID(){
		return this.sequenceID;
	}

	public void setParams(int x, int y, int x0, int x1, int y0, int y1, int xS, int yS, String s, String i){
		this.x = x;
		this.y = y;
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.xS = xS;
		this.yS = yS;
		this.s = s;
		this.i = i;
	}

	public static int getCount(){
		return counter.get();
	}

	public long getThreadID(){
		return this.threadID;
	}
	

}
