package pt.ulisboa.tecnico.cnv.mss;

import pt.ulisboa.tecnico.cnv.mss.RequestMetrics;

public class MetricsCalculator{



    public static long computeWeight(RequestMetrics metrics){
        long weight = (long) (metrics.getBbCount()*0.7 + metrics.getLoadcount()*0.1 + metrics.getStorecount()*0.2);
        return weight;
    }





}