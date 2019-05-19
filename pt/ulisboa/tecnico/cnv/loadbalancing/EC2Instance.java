package pt.ulisboa.tecnico.cnv.loadbalancing;

public class EC2Instance {

    private String publicDNS;
    private String instanceID;

    public void setPublicDNS(String publicDNS) {
        this.publicDNS = publicDNS;
    }

    public String getPublicDNS() {
        return this.publicDNS;
    }

    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }

    public String getInstanceID() {
        return this.instanceID;
    }

    @Override
    public String toString() {
        String s = "";
        s += "\n=========== EC2 INSTANCE ===========\n";
        s += "ID: " + this.instanceID + "\n";
        s += "Public DNS: " + this.publicDNS + "\n";
        return s;
    }

}
