import BIT.highBIT.*;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import pt.ulisboa.tecnico.cnv.mss.*;
import pt.ulisboa.tecnico.cnv.server.WebServer;


public class MyTool 
{
	private static int dyn_method_count = 0;
	private static int dyn_bb_count = 0;
	private static int dyn_instr_count = 0;
	
	private static int newcount = 0;
	private static int newarraycount = 0;
	private static int anewarraycount = 0;
	private static int multianewarraycount = 0;

	private static int loadcount = 0;
	private static int storecount = 0;
	private static int fieldloadcount = 0;
	private static int fieldstorecount = 0;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

	//private static final RequestMetricsStorage rms = RequestMetricsStorage.getInstance();
		
	public static void printUsage() {
		System.out.println("Syntax: java MyTool in_path [out_path]");
		System.out.println("        in_path:  directory from which the class files are read");
		System.out.println("        out_path: directory to which the class files are written");
		System.exit(-1);
	}

	public static void doDynamic(File in_dir, File out_dir) {
		String filelist[] = in_dir.list();

		for (int i = 0; i < filelist.length; i++) {
			String filename = filelist[i];
			if (filename.endsWith(".class")) {
				String in_filename = in_dir.getAbsolutePath() + System.getProperty("file.separator") + filename;
				String out_filename = out_dir.getAbsolutePath() + System.getProperty("file.separator") + filename;
				ClassInfo ci = new ClassInfo(in_filename);
				for (Enumeration e = ci.getRoutines().elements(); e.hasMoreElements(); ) {
					Routine routine = (Routine) e.nextElement();
                   
					for (Enumeration b = routine.getBasicBlocks().elements(); b.hasMoreElements(); ) {
						BasicBlock bb = (BasicBlock) b.nextElement();
						bb.addBefore("MyTool", "dynBBCount", new Integer(1));
					}
					if(routine.getMethodName().equals("solveImage")){
						routine.addAfter("MyTool", "printDynamic", "null");
					}
				}
				ci.write(out_filename);
			}
		}
	}

	public static void doLoadStore(File in_dir, File out_dir) {
		String filelist[] = in_dir.list();
		
		for (int i = 0; i < filelist.length; i++) {
			String filename = filelist[i];
			if (filename.endsWith(".class")) {
				String in_filename = in_dir.getAbsolutePath() + System.getProperty("file.separator") + filename;
				String out_filename = out_dir.getAbsolutePath() + System.getProperty("file.separator") + filename;
				ClassInfo ci = new ClassInfo(in_filename);

				for (Enumeration e = ci.getRoutines().elements(); e.hasMoreElements(); ) {
					Routine routine = (Routine) e.nextElement();
					
					for (Enumeration instrs = (routine.getInstructionArray()).elements(); instrs.hasMoreElements(); ) {
						Instruction instr = (Instruction) instrs.nextElement();
						int opcode=instr.getOpcode();
						if (opcode == InstructionTable.getfield)
							instr.addBefore("MyTool", "LSFieldCount", new Integer(0));
						else if (opcode == InstructionTable.putfield)
							instr.addBefore("MyTool", "LSFieldCount", new Integer(1));
						else {
							short instr_type = InstructionTable.InstructionTypeTable[opcode];
							if (instr_type == InstructionTable.LOAD_INSTRUCTION) {
								instr.addBefore("MyTool", "LSCount", new Integer(0));
							}
							else if (instr_type == InstructionTable.STORE_INSTRUCTION) {
								instr.addBefore("MyTool", "LSCount", new Integer(1));
							}
						}
					}
				}
				ci.write(out_filename);
			}
		}	
	}


	public static synchronized void LSFieldCount(int type) {
		if (type == 0)
			WebServer.metricsStorage.get(Thread.currentThread().getId()).incrFieldLoadCount(1);
		else
			WebServer.metricsStorage.get(Thread.currentThread().getId()).incrFieldStoreCount(1);
	}

	public static synchronized void LSCount(int type) {
		if (type == 0)
			WebServer.metricsStorage.get(Thread.currentThread().getId()).incrLoadCount(1);
		else
			WebServer.metricsStorage.get(Thread.currentThread().getId()).incrStoreCount(1);
	}



	public static synchronized void printSequenceID(String foo){
		System.out.println("MyTool:\t" + Thread.currentThread().getId() + "\t" + RequestMetrics.getCount());
	}
	

	public static synchronized void printDynamic(String foo) {
				
		/*try {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String fileName = "/tmp/stats__" + Thread.currentThread().getId() + "__" + sdf.format(timestamp) + ".txt";
			File file = new File(fileName);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("Dynamic information summary:\n");
			writer.append("Number of methods:\t" + dyn_method_count+ "\n");
			writer.append("Number of basic blocks:\t" + dyn_bb_count + "\n");
			writer.append("Number of instructions:\t" + dyn_instr_count+ "\n");
			writer.append("Average number of instructions per basic block:\t" + instr_per_bb + "\n");
			writer.append("Average number of instructions per method:\t" + instr_per_method + "\n");
			writer.append("Average number of basic blocks per method:\t" + bb_per_method + "\n");
			writer.close();
		}catch (IOException e){
			e.printStackTrace();
		}*/

		RequestMetrics m = WebServer.metricsStorage.get(Thread.currentThread().getId());
		m.printInfo();
		//WebServer.metricsStorage.put(Thread.currentThread().getId(), m);

		// RESET STATS
		dyn_method_count = 0;
		dyn_bb_count = 0;
		dyn_instr_count = 0;
	}

	public static void dynBBCount(int incr) {
		WebServer.metricsStorage.get(Thread.currentThread().getId()).incrBBCount(incr);
	}

	public static void main(String argv[]) {
		if (argv.length != 2) {
			printUsage();
		}else {
			try {
				File in_dir = new File(argv[0]);
				File out_dir = new File(argv[1]);

				if (in_dir.isDirectory() && out_dir.isDirectory()) {
					//ADICIONAR FUNCS DE INSTRUMENTACAO AQUI
					doDynamic(in_dir, out_dir);
					doLoadStore(in_dir, out_dir);
				}
				else {
					printUsage();
				}
			}
			catch (NullPointerException e) {
				printUsage();
			}
		}
	}
}
