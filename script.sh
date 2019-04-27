#!/usr/bin/env bash
# Hill@Climb config files for Linux based Environments
# Group 9 - Alameda

export CNV_ROOT=$HOME/cnv-project
export CNV_GEN=$CNV_ROOT/pt/ulisboa/tecnico/cnv/generator
export CNV_SERVER=$CNV_ROOT/pt/ulisboa/tecnico/cnv/server
export CNV_SOLVER=$CNV_ROOT/pt/ulisboa/tecnico/cnv/solver
export CNV_UTIL=$CNV_ROOT/pt/ulisboa/tecnico/cnv/util

export CNV_METRICS=$CNV_ROOT/pt/ulisboa/tecnico/cnv/mss
export CNV_TMP=$HOME/compiled_cnv

export CNV_LOGS=$CNV_ROOT/Logs

# Path to AWS Java SDK
export AWS_SDK=$HOME/aws-java-sdk-1.11.526

export CLASSPATH=$CNV_ROOT:$CNV_METRICS:$AWS_SDK/lib/aws-java-sdk-1.11.526.jar:$AWS_SDK/third-party/lib/*:.

# Color codes
export BLINK_ENABLE="\e[5m"
export BLINK_DISABLE="\e[25m"
export BASH_RED="\e[91m"
export BASH_DEFAULT="\e[39m"
export BASH_GREEN="\e[32m"

cat mountain.txt

mkdir -p $CNV_TMP
mkdir -p $CNV_LOGS

#################################################################
#                          FUNCTIONS                            #
#################################################################

# Clean all *.class from all directories
check() {
	
	if [ $? -eq 0 ]; then
		echo -e "$1 ${BASH_GREEN} OK! ${BASH_DEFAULT}"
	else
		echo -e "$1 ${BASH_RED} ${BLINK_ENABLE} FAILED! ${BLINK_DISABLE} ${BASH_DEFAULT}"
	fi
    
}

clean() {
	rm -f $CNV_GEN/*.class
	rm -f $CNV_SERVER/*.class
	rm -f $CNV_SOLVER/*.class
	rm -f $CNV_UTIL/*.class
	rm -f $CNV_METRICS/*.class
	check Cleaning	
}

compile() {
	echo "Compiling CNV_GEN ..."
	javac $CNV_GEN/*.java
	check Compilation
	echo "Compiling CNV_SERVER..."
	javac $CNV_SERVER/*.java
	check Compilation
	echo "Compiling CNV_SOLVER ..."
	javac $CNV_SOLVER/*.java
	check Compilation
	echo "Compiling CNV_UTIL ..."
	javac $CNV_UTIL/*.java
	check Compilation
	echo "Compiling CNV_METRICS ..."
	javac $CNV_METRICS/*.java
	check Compilation
	cp -r pt/ $CNV_TMP/
	check Backup
}

inst() {
	echo "Deleting old solver class files ..."
	rm -r pt/ulisboa/tecnico/cnv/solver/
	check Delete

	echo "Restoring previous compiled version ..."
	cp -r $CNV_TMP/pt/ulisboa/tecnico/cnv/solver $CNV_SOLVER 
	check Copy

	echo "Compiling HillClimbingMetrics ..."
	javac $CNV_METRICS/HillClimbingMetrics.java
	check Compilation

	echo "Instrumenting solver folder with HillClimbingMetrics ..."
	java HillClimbingMetrics $CNV_SOLVER $CNV_SOLVER
	check Instrumentation
}

wsvc() {
	java pt.ulisboa.tecnico.cnv.server.WebServer	
}



