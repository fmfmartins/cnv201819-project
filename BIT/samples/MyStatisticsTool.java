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
import pt.ulisboa.tecnico.cnv.mss.*;


public class StatisticsTool 
{
	private static int dyn_bb_count = 0;
	
	private static int newcount = 0;
	private static int newarraycount = 0;
	private static int anewarraycount = 0;
	private static int multianewarraycount = 0;

	private static int loadcount = 0;
	private static int storecount = 0;
	private static int fieldloadcount = 0;
	private static int fieldstorecount = 0;

	public RequestMetrics xD = new RequestMetrics(1);

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		
	public static void printUsage() 
		{
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

	public static void doStatic(File in_dir) 
		{
			String filelist[] = in_dir.list();
			int method_count = 0;
			int bb_count = 0;
			int instr_count = 0;
			int class_count = 0;
			
			for (int i = 0; i < filelist.length; i++) {
				String filename = filelist[i];
				if (filename.endsWith(".class")) {
					class_count++;
					String in_filename = in_dir.getAbsolutePath() + System.getProperty("file.separator") + filename;
					ClassInfo ci = new ClassInfo(in_filename);
					Vector routines = ci.getRoutines();
					method_count += routines.size();

					for (Enumeration e = routines.elements(); e.hasMoreElements(); ) {
						Routine routine = (Routine) e.nextElement();
						BasicBlockArray bba = routine.getBasicBlocks();
						bb_count += bba.size();
						InstructionArray ia = routine.getInstructionArray();
						instr_count += ia.size();
					}
				}
			}

			System.out.println("Static information summary:");
			System.out.println("Number of class files:  " + class_count);
			System.out.println("Number of methods:      " + method_count);
			System.out.println("Number of basic blocks: " + bb_count);
			System.out.println("Number of instructions: " + instr_count);
			
			if (class_count == 0 || method_count == 0) {
				return;
			}
			
			float instr_per_bb = (float) instr_count / (float) bb_count;
			float instr_per_method = (float) instr_count / (float) method_count;
			float instr_per_class = (float) instr_count / (float) class_count;
			float bb_per_method = (float) bb_count / (float) method_count;
			float bb_per_class = (float) bb_count / (float) class_count;
			float method_per_class = (float) method_count / (float) class_count;
			
			System.out.println("Average number of instructions per basic block: " + instr_per_bb);
			System.out.println("Average number of instructions per method:      " + instr_per_method);
			System.out.println("Average number of instructions per class:       " + instr_per_class);
			System.out.println("Average number of basic blocks per method:      " + bb_per_method);
			System.out.println("Average number of basic blocks per class:       " + bb_per_class);
			System.out.println("Average number of methods per class:            " + method_per_class);
		}

	public static void doDynamic(File in_dir, File out_dir) 
		{
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
							bb.addBefore("StatisticsTool", "dynBbCount", new Integer(bb.size()));
						}
						if(routine.getMethodName().equals("solveImage")){
							routine.addAfter("StatisticsTool", "printDynamic", "null");
						}
					}
					ci.write(out_filename);
				}
			}
		}
	public static synchronized void dynBbCount(int incr)
        {
                dyn_bb_count++;
        }
    	public static synchronized void printDynamic(String foo) 
		{
			System.out.println("Dynamic information summary:");
			System.out.println("Number of basic blocks: " + dyn_bb_count);
		
			try {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				String fileName = "/tmp/stats__" + Thread.currentThread().getId() + "__" + sdf.format(timestamp) + ".txt";
				File file = new File(fileName);
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write("Dynamic information summary:\n");
				writer.append("Number of basic blocks:\t" + dyn_bb_count + "\n");
				writer.close();i

				// RESET STATS
				dyn_bb_count = 0;

			}catch (IOException e){
				e.printStackTrace();
			}

	}
	
	public static void doAlloc(File in_dir, File out_dir) 
		{
			String filelist[] = in_dir.list();
			
			for (int i = 0; i < filelist.length; i++) {
				String filename = filelist[i];
				if (filename.endsWith(".class")) {
					String in_filename = in_dir.getAbsolutePath() + System.getProperty("file.separator") + filename;
					String out_filename = out_dir.getAbsolutePath() + System.getProperty("file.separator") + filename;
					ClassInfo ci = new ClassInfo(in_filename);

					for (Enumeration e = ci.getRoutines().elements(); e.hasMoreElements(); ) {
						Routine routine = (Routine) e.nextElement();
						InstructionArray instructions = routine.getInstructionArray();
		  
						for (Enumeration instrs = instructions.elements(); instrs.hasMoreElements(); ) {
							Instruction instr = (Instruction) instrs.nextElement();
							int opcode=instr.getOpcode();
							if ((opcode==InstructionTable.NEW) ||
								(opcode==InstructionTable.newarray) ||
								(opcode==InstructionTable.anewarray) ||
								(opcode==InstructionTable.multianewarray)) {
								instr.addBefore("StatisticsTool", "allocCount", new Integer(opcode));
							}
						}
						if(routine.getMethodName().equals("solveImage")){
                                                        routine.addAfter("StatisticsTool", "printAlloc", "null");
                                                }
					}
					ci.write(out_filename);
				}
			}
		}
	
	public static synchronized void printAlloc(String s) 
		{
			System.out.println("Allocations summary:");
			System.out.println("new:            " + newcount);
			System.out.println("newarray:       " + newarraycount);
			System.out.println("anewarray:      " + anewarraycount);
			System.out.println("multianewarray: " + multianewarraycount);
		}

	public static synchronized void allocCount(int type)
		{
			switch(type) {
			case InstructionTable.NEW:
				newcount++;
				break;
			case InstructionTable.newarray:
				newarraycount++;
				break;
			case InstructionTable.anewarray:
				anewarraycount++;
				break;
			case InstructionTable.multianewarray:
				multianewarraycount++;
				break;
			}
		}
	
	public static void doLoadStore(File in_dir, File out_dir) 
		{
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
								instr.addBefore("StatisticsTool", "LSFieldCount", new Integer(0));
							else if (opcode == InstructionTable.putfield)
								instr.addBefore("StatisticsTool", "LSFieldCount", new Integer(1));
							else {
								short instr_type = InstructionTable.InstructionTypeTable[opcode];
								if (instr_type == InstructionTable.LOAD_INSTRUCTION) {
									instr.addBefore("StatisticsTool", "LSCount", new Integer(0));
								}
								else if (instr_type == InstructionTable.STORE_INSTRUCTION) {
									instr.addBefore("StatisticsTool", "LSCount", new Integer(1));
								}
							}
						}
						if(routine.getMethodName().equals("solveImage")){
							routine.addAfter("StatisticsTool", "printLoadStore", "null");
						}
					}
					ci.write(out_filename);
				}
			}	
		}

	public static synchronized void printLoadStore(String s) 
		{
			System.out.println("Load Store Summary:");
			System.out.println("Field load:    " + fieldloadcount);
			System.out.println("Field store:   " + fieldstorecount);
			System.out.println("Regular load:  " + loadcount);
			System.out.println("Regular store: " + storecount);
		}

	public static synchronized void LSFieldCount(int type) 
		{
			if (type == 0)
				fieldloadcount++;
			else
				fieldstorecount++;
		}

	public static synchronized void LSCount(int type) 
		{
			if (type == 0)
				loadcount++;
			else
				storecount++;
		}
			
	public static void main(String argv[]) 
		{
			if (argv.length < 2 || !argv[0].startsWith("-")) {
				printUsage();
			}

			if (argv[0].equals("-static")) {
				if (argv.length != 2) {
					printUsage();
				}
				
				try {
					File in_dir = new File(argv[1]);
					
					if (in_dir.isDirectory()) {
						doStatic(in_dir);
					}
					else {
						printUsage();
					}
				}
				catch (NullPointerException e) {
					printUsage();
				}
			}

			else if (argv[0].equals("-dynamic")) {
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
			
			else if (argv[0].equals("-alloc")) {
				if (argv.length != 3) {
					printUsage();
				}
				
				try {
					File in_dir = new File(argv[1]);
					File out_dir = new File(argv[2]);

					if (in_dir.isDirectory() && out_dir.isDirectory()) {
						doAlloc(in_dir, out_dir);
					}
					else {
						printUsage();
					}
				}
				catch (NullPointerException e) {
					printUsage();
				}
			}
			
			else if (argv[0].equals("-load_store")) {
				if (argv.length != 3) {
					printUsage();
				}
				
				try {
					File in_dir = new File(argv[1]);
					File out_dir = new File(argv[2]);

					if (in_dir.isDirectory() && out_dir.isDirectory()) {
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

			else if (argv[0].equals("-branch")) {
				if (argv.length != 3) {
					printUsage();
				}
				
				try {
					File in_dir = new File(argv[1]);
					File out_dir = new File(argv[2]);

					if (in_dir.isDirectory() && out_dir.isDirectory()) {
						doBranch(in_dir, out_dir);
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
