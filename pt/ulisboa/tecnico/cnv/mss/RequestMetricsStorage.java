package pt.ulisboa.tecnico.cnv.mss;

import pt.ulisboa.tecnico.cnv.mss.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class RequestMetricsStorage {

    public static ConcurrentHashMap<Long, RequestMetrics> metricsStorage = new ConcurrentHashMap<>();

    private static RequestMetricsStorage instance;

    private RequestMetricsStorage() {
    }

    public static synchronized RequestMetricsStorage getInstance() {
        if (instance == null) {
            instance = new RequestMetricsStorage();
        }
        return instance;
    }

    public static String getRequestsProgress() {
        String s = "";
        int i = 1;
        long totalInstanceWorkload = 0L;
        for (RequestMetrics metrics : metricsStorage.values()) {
            totalInstanceWorkload += metrics.getEstimatedCost();
            int progress = (int) (((MetricsCalculator.computeWeight(metrics) * 100L) / metrics.getEstimatedCost()));
            if (progress > 100) {
                progress = 99;
            }
            int numHash = (int) ((double) progress / 10);
            int numDash = 10 - numHash;
            int j = 0;
            s += "Request " + i + ": |";
            StringBuilder builder = new StringBuilder(s);
            for (j = 0; j < numHash; j++) {
                builder.append('#');
            }
            for (j = 0; j < numDash; j++) {
                builder.append('-');
            }
            builder.append('|');
            builder.append(" " + progress + "%");
            builder.append('\n');
            s = builder.toString();
            i++;
        }
        s += "Total Instance Workload = " + totalInstanceWorkload + "\n";
        return s;
    }

}
