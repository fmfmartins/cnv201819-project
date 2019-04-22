//
// StatisticsTool.java
//
// This program measures and instruments to obtain different statistics
// about Java programs.
//
// Copyright (c) 1998 by Han B. Lee (hanlee@cs.colorado.edu).
// ALL RIGHTS RESERVED.
//
// Permission to use, copy, modify, and distribute this software and its
// documentation for non-commercial purposes is hereby granted provided 
// that this copyright notice appears in all copies.
// 
// This software is provided "as is".  The licensor makes no warrenties, either
// expressed or implied, about its correctness or performance.  The licensor
// shall not be liable for any damages suffered as a result of using
// and modifying this software.

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

	private static StatisticsBranch[] branch_info;
	private static int branch_number;
	private static int branch_pc;
	private static String branch_class_name;
	private static String branch_method_name;

	public static HashMap<Long, RequestMetrics> requests = new HashMap<>();


	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		
	public static void printUsage() {
		System.out.println("Syntax: java StatisticsTool -stat_type in_path [out_path]");
		System.out.println("        where stat_type can be:");
		System.out.println("        static:     static properties");
		System.out.println("        dynamic:    dynamic properties");
		System.out.println("        alloc:      memory allocation instructions");
		System.out.println("        load_store: loads and stores (both field and regular)");
		System.out.println("        branch:     gathers branch outcome statistics");
		System.out.println();
		System.out.println("        in_path:  directory from which the class files are read");
		System.out.println("        out_path: directory to which the class files are written");
		System.out.println("        Both in_path and out_path are required unless stat_type is static");
		System.out.println("        in which case only in_path is required");
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
					routine.addBefore("MyTool", "dynMethodCount", new Integer(1));
                   
					for (Enumeration b = routine.getBasicBlocks().elements(); b.hasMoreElements(); ) {
						BasicBlock bb = (BasicBlock) b.nextElement();
						bb.addBefore("MyTool", "dynInstrCount", new Integer(bb.size()));
					}
					if(routine.getMethodName().equals("solveImage")){
						routine.addBefore("MyTool", "startTime", 0);
						routine.addAfter("MyTool", "stopTime", 0);
						//routine.addAfter("MyTool", "printDynamic", "null");
					}
				}
				ci.write(out_filename);
			}
		}
	}
	

	public static synchronized void printDynamic(String foo) {
			
		System.out.println("Dynamic information summary:");
		System.out.println("Number of methods:      " + dyn_method_count);
		System.out.println("Number of basic blocks: " + dyn_bb_count);
		System.out.println("Number of instructions: " + dyn_instr_count);
	
		if (dyn_method_count == 0) {
			return;
		}
	
		float instr_per_bb = (float) dyn_instr_count / (float) dyn_bb_count;
		float instr_per_method = (float) dyn_instr_count / (float) dyn_method_count;
		float bb_per_method = (float) dyn_bb_count / (float) dyn_method_count;
	
		System.out.println("Average number of instructions per basic block: " + instr_per_bb);
		System.out.println("Average number of instructions per method:      " + instr_per_method);
		System.out.println("Average number of basic blocks per method:      " + bb_per_method);
			
		try {
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

			// RESET STATS
			dyn_method_count = 0;
			dyn_bb_count = 0;
			dyn_instr_count = 0;

		}catch (IOException e){
			e.printStackTrace();
		}
	}


	public static synchronized void startTime(int threadID){
		long thread = Thread.currentThread().getId();
		System.out.println("StartTime ThreadID: " + thread);
		RequestMetrics metrics = new RequestMetrics(thread);
		long time = System.currentTimeMillis()/1000;
		metrics.setExecutionTime(time);
		requests.put(thread, metrics);
    	}

	public static synchronized void stopTime(int threadID){
		long thread = Thread.currentThread().getId();
		System.out.println("StopTime ThreadID: " + thread);
		RequestMetrics metrics = requests.get(thread);
		metrics.setExecutionTime((System.currentTimeMillis()/1000) - metrics.getExecutionTime());
        	System.out.println("Execution Time : " + metrics.getExecutionTime());
    	} 

	public static synchronized void dynInstrCount(int incr) {
		dyn_instr_count += incr;
		dyn_bb_count++;
	}

	public static synchronized void dynMethodCount(int incr) {
		dyn_method_count++;
	}

	public static void main(String argv[]) {
		if (argv.length < 2 || !argv[0].startsWith("-")) {
			printUsage();
		}
		if (argv[0].equals("-dynamic")) {
			if (argv.length != 3) {
				printUsage();
			}
			
			try {
				File in_dir = new File(argv[1]);
				File out_dir = new File(argv[2]);

				if (in_dir.isDirectory() && out_dir.isDirectory()) {
					doDynamic(in_dir, out_dir);
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
