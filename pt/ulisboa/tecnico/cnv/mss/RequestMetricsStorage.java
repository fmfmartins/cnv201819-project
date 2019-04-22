package pt.ulisboa.tecnico.cnv.mss;

import pt.ulisboa.tecnico.cnv.mss.*;
import java.util.HashMap;

public class RequestMetricsStorage {


    private static int sequenceID;
    private static HashMap<Integer, RequestMetrics> requests = new HashMap<>();

    private static RequestMetricsStorage instance;

    private RequestMetricsStorage(){}

    public static synchronized RequestMetricsStorage getInstance(){
        if(instance == null){
            instance = new RequestMetricsStorage();
            sequenceID = 1;
        }
        return instance;
    }

    public static synchronized int addMetric(RequestMetricsStorage metrics){
        this.requests.put(sequenceID, metrics);
        this.incSequenceID();
        return this.sequenceID;
    }

    public static synchronized RequestMetrics getMetric(){

    }

    private void incSequenceID(){
        this.sequenceID++;
    }

}