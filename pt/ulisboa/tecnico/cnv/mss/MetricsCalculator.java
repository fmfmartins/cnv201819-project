package pt.ulisboa.tecnico.cnv.mss;

import pt.ulisboa.tecnico.cnv.mss.RequestMetrics;

public class MetricsCalculator{



    public static int computeWeight(RequestMetrics metrics){

        // fastest request order of magnitude
        long tempmax = Long.parseLong("1000000");

        double weight = (metrics.getBbCount()*0.7 + metrics.getLoadcount()*0.1 + metrics.getStorecount()*0.2) / tempmax;
        return (int) weight;

    }





}