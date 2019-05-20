package pt.ulisboa.tecnico.cnv.loadbalancing;

public class Config {


    // snapshot da imagem das instancias
    public static final String AMI_ID = "ami-054a9047be6beca6b";
    // instancia free! nao mudar
    public static final String INSTANCE_TYPE = "t2.micro";
    // nome da chave
    public static final String KEY_NAME = "CNV-lab-AWS";
    // Nome do security group
    public static final String SEC_GROUP = "CNV-ssh+http";

    // Webserver instance DNS name, while the autoscaler and 
    // loadbalancer dont comm with eachother
    public static final String INSTANCE_DNS_TMP = "ec2-35-180-174-182.eu-west-3.compute.amazonaws.com:8000";

}
