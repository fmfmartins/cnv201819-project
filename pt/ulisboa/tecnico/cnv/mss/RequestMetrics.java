package pt.ulisboa.tecnico.cnv.mss;

import java.util.concurrent.atomic.AtomicInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class RequestMetrics {


	private static final AtomicInteger counter = new AtomicInteger(0);

	private int sequenceID;
	private long threadID;
	private Timestamp timestamp;

	private int w;
	private int h;
	private int x0;
	private int x1;
	private int y0;
	private int y1;
	private int xS;
	private int yS;
	private String s;
	private String i;
	
	private int bbCount;
	private int loadcount;
	private int storecount;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");


	public RequestMetrics(long threadID){
		this.threadID = threadID;
		this.sequenceID = counter.incrementAndGet();
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}

	public String getAlgorithm(){
		return this.s;
	}

	public String getImage(){
		return this.i;
	}

	public int getSequenceID(){
		return this.sequenceID;
	}

	public void setParams(int w, int h, int x0, int x1, int y0, int y1, int xS, int yS, String s, String i){
		this.w = w;
		this.h = h;
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.xS = xS;
		this.yS = yS;
		this.s = s;
		this.i = i;
	}

	public void incrLoadCount(int incr){
		this.loadcount += incr;
	}

	public void incrStoreCount(int incr){
		this.storecount += incr;
	}

	public void setBBCount(int bb){
		this.bbCount = bb;
	}

	public void incrBBCount(int incr){
		this.bbCount += incr;
	}

	public static int getCount(){
		return counter.get();
	}

	public long getThreadID(){
		return this.threadID;
	}

	public void printInfo(){
		System.out.println("\t//Request Parameters//\t");
		System.out.println("Width: " + this.w);
		System.out.println("Height: " + this.h);
		System.out.println("Upper-Left Corner X: " + this.x0);
		System.out.println("Upper-left Corner Y: " + this.y0);
		System.out.println("Lower-Right Corner X: " + this.x1);
		System.out.println("Lower-Right Corner Y: " + this.y1);
		System.out.println("Starting Point X: " + this.xS);
		System.out.println("Starting Point Y: " + this.yS);
		System.out.println("Solver Algorithm: " + this.s);
		System.out.println("Image Path: " + this.i);
		System.out.println("\t//Metrics//\t");
		System.out.println("Number of basic blocks: " + this.bbCount);
		System.out.println("Load Instructions: " + this.loadcount);
		System.out.println("Store Instructions: " + this.storecount);
	}

	public void outputToFile(){
		try{
			String fileName = "/tmp/" + sdf.format(this.timestamp) + "__stats__" + Thread.currentThread().getId() + ".txt";
			File file = new File(fileName);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(this.toString());
			writer.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public String toString(){
		String s;
		s = "\t//Request Parameters//\t\n";
		s += String.format("Width: %d\n" , this.w);
		s += String.format("Height: %d\n", this.h);
		s += String.format("Upper-Left Corner X: %d\n", this.x0);
		s += String.format("Upper-left Corner Y: %d\n" , this.y0);
		s += String.format("Lower-Right Corner X: %d\n" , this.x1);
		s += String.format("Lower-Right Corner Y: %d\n" , this.y1);
		s += String.format("Starting Point X: %d\n" , this.xS);
		s += String.format("Starting Point Y: %d\n" , this.yS);
		s += String.format("Solver Algorithm: %s\n" , this.s);
		s += String.format("Image Path: %s\n" , this.i);
		s += String.format("\t//Metrics//\t\n");
		s += String.format("Number of basic blocks: %d\n" , this.bbCount);
		s += String.format("Load Instructions: %d\n" , this.loadcount);
		s += String.format("Store Instructions: %d\n" , this.storecount);
		return s;
	}
	


    /**
     * @param sequenceID the sequenceID to set
     */
    public void setSequenceID(int sequenceID) {
        this.sequenceID = sequenceID;
    }

    /**
     * @param threadID the threadID to set
     */
    public void setThreadID(long threadID) {
        this.threadID = threadID;
    }

    /**
     * @return Timestamp return the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return int return the w
     */
    public int getW() {
        return w;
    }

    /**
     * @param w the w to set
     */
    public void setW(int w) {
        this.w = w;
    }

    /**
     * @return int return the h
     */
    public int getH() {
        return h;
    }

    /**
     * @param h the h to set
     */
    public void setH(int h) {
        this.h = h;
    }

    /**
     * @return int return the x0
     */
    public int getX0() {
        return x0;
    }

    /**
     * @param x0 the x0 to set
     */
    public void setX0(int x0) {
        this.x0 = x0;
    }

    /**
     * @return int return the x1
     */
    public int getX1() {
        return x1;
    }

    /**
     * @param x1 the x1 to set
     */
    public void setX1(int x1) {
        this.x1 = x1;
    }

    /**
     * @return int return the y0
     */
    public int getY0() {
        return y0;
    }

    /**
     * @param y0 the y0 to set
     */
    public void setY0(int y0) {
        this.y0 = y0;
    }

    /**
     * @return int return the y1
     */
    public int getY1() {
        return y1;
    }

    /**
     * @param y1 the y1 to set
     */
    public void setY1(int y1) {
        this.y1 = y1;
    }

    /**
     * @return int return the xS
     */
    public int getXS() {
        return xS;
    }

    /**
     * @param xS the xS to set
     */
    public void setXS(int xS) {
        this.xS = xS;
    }

    /**
     * @return int return the yS
     */
    public int getYS() {
        return yS;
    }

    /**
     * @param yS the yS to set
     */
    public void setYS(int yS) {
        this.yS = yS;
    }

    /**
     * @param s the s to set
     */
    public void setS(String s) {
        this.s = s;
    }

    /**
     * @param i the i to set
     */
    public void setI(String i) {
        this.i = i;
    }

    /**
     * @return int return the bbCount
     */
    public int getBbCount() {
        return bbCount;
    }

    /**
     * @param bbCount the bbCount to set
     */
    public void setBbCount(int bbCount) {
        this.bbCount = bbCount;
    }

    /**
     * @return int return the loadcount
     */
    public int getLoadcount() {
        return loadcount;
    }

    /**
     * @param loadcount the loadcount to set
     */
    public void setLoadcount(int loadcount) {
        this.loadcount = loadcount;
    }

    /**
     * @return int return the storecount
     */
    public int getStorecount() {
        return storecount;
    }

    /**
     * @param storecount the storecount to set
     */
    public void setStorecount(int storecount) {
        this.storecount = storecount;
    }

}
