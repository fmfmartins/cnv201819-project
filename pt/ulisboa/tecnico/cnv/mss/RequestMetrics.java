package pt.ulisboa.tecnico.cnv.mss;

import java.util.concurrent.atomic.AtomicInteger;

public class RequestMetrics {


	private static final AtomicInteger counter = new AtomicInteger(0);
	private int sequenceID;
	private long threadID;


	public RequestMetrics(long threadID){
		this.threadID = threadID;
		this.sequenceID = counter.incrementAndGet();
	}

	public int getSequenceID(){
		return this.sequenceID;
	}

	public static int getCount(){
		return counter.get();
	}
	

}
