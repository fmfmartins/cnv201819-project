

public class EC2Instance {
    private String publicDNS;
    private String instanceID;
    
    public void setPublicDNC(String publicDNS){
        this.publicDNS = publicDNS;
    }

    public String getPublicDNC(){
        return this.publicDNS;
    }

    public void setInstanceID(String instanceID){
        this.instanceID = instanceID;
    } 

    public String getInstanceID(){
        return this.instanceID;
    }

}