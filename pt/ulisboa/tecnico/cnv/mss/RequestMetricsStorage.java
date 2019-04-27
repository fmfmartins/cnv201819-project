package pt.ulisboa.tecnico.cnv.mss;

import pt.ulisboa.tecnico.cnv.mss.*;
import java.util.HashMap;

public class RequestMetricsStorage {

    public static HashMap<Long, RequestMetrics> metricsStorage = new HashMap<>();

    private static RequestMetricsStorage instance;

    private RequestMetricsStorage(){}

    public static synchronized RequestMetricsStorage getInstance(){
        if(instance == null){
            instance = new RequestMetricsStorage();
        }
        return instance;
    }

    /*public static synchronized void addRequestMetrics(RequestMetrics metrics){	
        metricsStorage.put(metrics.getThreadID(), metrics);
    }

    public static synchronized RequestMetrics getRequestMetrics(long threadID){
	    return metricsStorage.get(threadID);
    }

    public static synchronized RequestMetrics getAndRemoveRequestMetrics(long threadID){
	    return metricsStorage.remove(threadID);
    }*/

}
