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

	public long getExecutionTime(){
		return this.executionTime;
	}

	public int getSequenceID(){
		return this.sequenceID;
	}
	

}
