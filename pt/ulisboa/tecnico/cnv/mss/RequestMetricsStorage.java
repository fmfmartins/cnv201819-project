package pt.ulisboa.tecnico.cnv.mss;

import pt.ulisboa.tecnico.cnv.mss.*;
import java.util.HashMap;

public class RequestMetricsStorage {

    private static HashMap<Long, RequestMetrics> requests = new HashMap<>();

    private static RequestMetricsStorage instance;

    private RequestMetricsStorage(){}

    public static synchronized RequestMetricsStorage getInstance(){
        if(instance == null){
            instance = new RequestMetricsStorage();
        }
        return instance;
    }

    public static synchronized void addRequestMetrics(RequestMetrics metrics){	
        requests.put(metrics.getThreadID(), metrics);
    }

    public static synchronized RequestMetrics getRequestMetrics(int threadID){
	    return requests.get(threadID);
    }

    public static synchronized RequestMetrics getAndRemoveRequestMetrics(int threadID){
	    return requests.remove(threadID);
    }

}
