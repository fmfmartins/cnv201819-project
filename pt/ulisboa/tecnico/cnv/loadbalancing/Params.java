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
                    w=params[0].split("=")[1];
                    h=params[1].split("=")[1];
                    x0=params[2].split("=")[1];
                    x1=params[3].split("=")[1];
                    y0=params[4].split("=")[1];
                    y1=params[5].split("=")[1];
                    xs=params[6].split("=")[1];
                    ys=params[7].split("=")[1];
                    algorithm=params[8].split("=")[1];
                    image=params[9].split("=")[1];
            }

            else{
                    System.out.println("Something wrong with params:length incorrect.");
            }

            //default cost
            this.cost=-1;

        }

        public String getW(){
            return w;
        }

        public String getH(){
             return h;
        }

        public String getX0(){
            return x0;
        }

        public String getX1(){
            return x1;
        }

        public String getY0(){
            return y0;
        }

        public String getY1(){
            return y1;
        }

        public String getXS(){
            return xs;
        }

        public String getYS(){
            return ys;
        }

        public String getAlgorithm(){
            return algorithm;
        }

        public String getImage(){
            return image;
        }

        public long getCost(){
            return this.cost;
        }

        public void setCost(long eCost){
            this.cost = eCost;
        }

}
