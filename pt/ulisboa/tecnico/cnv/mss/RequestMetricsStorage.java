package pt.ulisboa.tecnico.cnv.mss;

import pt.ulisboa.tecnico.cnv.mss.*;
import java.util.HashMap;

public class RequestMetricsStorage {

    private static HashMap<Integer, RequestMetrics> requests = new HashMap<>();

    private static final RequestMetricsStorage instance;

    private RequestMetricsStorage(){}

    public static synchronized RequestMetricsStorage getInstance(){
        if(instance == null){
            instance = new RequestMetricsStorage();
        }
        return instance;
    }

    public static synchronized int addRequestMetrics(RequestMetrics metrics){	
        requests.put(new Integer(metrics.getSequenceID()), metrics);
        return metrics.getSequenceID();
    }

    public static synchronized RequestMetrics getRequestMetrics(int sequenceID){
	return requests.get(sequenceID);
    }

    public static synchronized RequestMetrics getAndRemoveRequestMetrics(int sequenceID){
	return requests.remove(sequenceID);
    }

}
