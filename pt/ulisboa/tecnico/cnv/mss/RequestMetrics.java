package pt.ulisboa.tecnico.cnv.mss;




public class RequestMetrics {

	private long threadID;

	private long executionTime;


	public RequestMetrics(long threadID){
		this.threadID = threadID;
	}

	public void setExecutionTime(long time){
		this.executionTime = time;
	}

	public long getExecutionTime(){
		return this.executionTime;
	}
	

}
