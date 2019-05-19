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

import pt.ulisboa.tecnico.cnv.mss.RequestMetrics;
import pt.ulisboa.tecnico.cnv.mss.RequestMetricsStorage;



public class HillClimbingMetrics 
{
		
	public static void printUsage() {
		System.out.println("Syntax: java HillClimbingMetrics in_path [out_path]");
		System.out.println("        in_path:  directory from which the class files are read");
		System.out.println("        out_path: directory to which the class files are written");
		//System.exit(-1);
	}

	public static void doInstrumentation(File in_dir, File out_dir) {
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
						bb.addBefore("HillClimbingMetrics", "dynBBCount", new Integer(1));
					}
					
					for (Enumeration instrs = (routine.getInstructionArray()).elements(); instrs.hasMoreElements(); ) {
						Instruction instr = (Instruction) instrs.nextElement();
						int opcode=instr.getOpcode();
						short instr_type = InstructionTable.InstructionTypeTable[opcode];
						if (instr_type == InstructionTable.LOAD_INSTRUCTION) {
							instr.addBefore("HillClimbingMetrics", "LSCount", new Integer(0));
						}
						else if (instr_type == InstructionTable.STORE_INSTRUCTION) {
							instr.addBefore("HillClimbingMetrics", "LSCount", new Integer(1));
						}
					}
				}
				ci.write(out_filename);
			}
		}	
	}


	public static synchronized void LSCount(int type) {
		if (type == 0)
			RequestMetricsStorage.metricsStorage.get(Thread.currentThread().getId()).incrLoadCount(1);
		else
			RequestMetricsStorage.metricsStorage.get(Thread.currentThread().getId()).incrStoreCount(1);
	}


	public static synchronized void printSequenceID(String foo){
		System.out.println("HillClimbingMetrics:\t" + Thread.currentThread().getId());
	}
	

	public static void dynBBCount(int incr) {
		RequestMetricsStorage.metricsStorage.get(Thread.currentThread().getId()).incrBBCount(incr);
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
					doInstrumentation(in_dir, out_dir);
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
