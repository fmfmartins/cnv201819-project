package pt.ulisboa.tecnico.cnv.mss;

import pt.ulisboa.tecnico.cnv.mss.RequestMetrics;

public class MetricsCalculator{



    public static int computeWeight(RequestMetrics metrics){

        int weight = (metrics.getBbCount()*0.5 + metrics.getLoadcount()*0.25 + metrics.getStorecount()*0.25) / Integer.MAX_VALUE;
        return weight;
    }

    

}