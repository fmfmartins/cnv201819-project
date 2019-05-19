package pt.ulisboa.tecnico.cnv.loadbalancing;


public class Params{

        private String w;
        private String h;
        private String x0;
        private String x1;
        private String y0;
        private String y1;
        private String xs;
        private String ys;
        private String algorithm;
        private String image;

        //estimated cost
        private long cost;


        public Params(String[] params){
            if(params.length==10){
                    this.w=params[0].split("=")[1];
                    this.h=params[1].split("=")[1];
                    this.x0=params[2].split("=")[1];
                    this.x1=params[3].split("=")[1];
                    this.y0=params[4].split("=")[1];
                    this.y1=params[5].split("=")[1];
                    this.xs=params[6].split("=")[1];
                    this.ys=params[7].split("=")[1];
                    this.algorithm=params[8].split("=")[1];
                    this.image=params[9].split("=")[1];
            }

            else{
                    System.out.println("Something wrong with params:length incorrect.");
            }

            //default cost
            this.cost=-1;

        }

        public String getW(){
            return this.w;
        }

        public String getH(){
             return this.h;
        }

        public String getX0(){
            return this.x0;
        }

        public String getX1(){
            return this.x1;
        }

        public String getY0(){
            return this.y0;
        }

        public String getY1(){
            return this.y1;
        }

        public String getXS(){
            return this.xs;
        }

        public String getYS(){
            return this.ys;
        }

        public String getAlgorithm(){
            return this.algorithm;
        }

        public String getImage(){
            return this.image;
        }

        public long getCost(){
            return this.cost;
        }

        public void setCost(long eCost){
            this.cost = eCost;
        }

}
