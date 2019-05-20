package pt.ulisboa.tecnico.cnv.loadbalancing;

import java.nio.channels.FileChannel.MapMode;

public class WebServerLB {

    private static LoadBalancer loadBalancer;
    private static AutoScaler autoScaler;

    public static void main(String args[]) throws Exception {
        loadBalancer = LoadBalancer.getInstance();
        autoScaler = AutoScaler.getInstance();

        System.out.println("===========================================");
        System.out.println("Welcome to the Hill Climbing AutoScaler");
        System.out.println("===========================================");

        /*if (autoScaler.mapOfInstances.size() < autoScaler.MIN_INSTANCES) {
            if (!AutoScaler.executingAction) {
                autoScaler.spawnEC2Instance();
                while (autoScaler.executingAction) {
                    System.out.println("Execution Action (SPAWN) MIN INSTANCES -> " + autoScaler.executingAction);
                    Thread.sleep(5000);
                }
            }
        }*/

        boolean wait = true;
        while (wait) {
            System.in.read();
            wait = false;
        }
        
        for(EC2Instance inst : autoScaler.mapOfInstances.values()){
            autoScaler.destroyEC2Instance(inst.getPublicDNS());
        }
        System.out.println("Shutting down..");
        Thread.sleep(2000);
        System.exit(0);
    }
}
