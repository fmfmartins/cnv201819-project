#!/usr/bin/env bash

export CNV_ROOT=$HOME/cnv-project
export CNV_GEN=$CNV_ROOT/pt/ulisboa/tecnico/cnv/generator
export CNV_SERVER=$CNV_ROOT/pt/ulisboa/tecnico/cnv/server
export CNV_SOLVER=$CNV_ROOT/pt/ulisboa/tecnico/cnv/solver
export CNV_UTIL=$CNV_ROOT/pt/ulisboa/tecnico/cnv/util
export CNV_LB=$CNV_ROOT/pt/ulisboa/tecnico/cnv/loadbalancing
export CNV_METRICS=$CNV_ROOT/pt/ulisboa/tecnico/cnv/mss
export CNV_TMP=$HOME/compiled_cnv

# Path to AWS Java SDK
export AWS_SDK=$HOME/aws-java-sdk-1.11.526

export CLASSPATH=$CNV_ROOT:$CNV_METRICS:$AWS_SDK/lib/aws-java-sdk-1.11.526.jar:$AWS_SDK/third-party/lib/*:.

cat ~/cnv-project/mountain.txt

mkdir -p $CNV_TMP

#################################################################
#                          FUNCTIONS                            #
#################################################################

check() {
	
	if [ $? -eq 0 ]; then
		echo -e "$1 SUCCESS"
	else
		echo -e "$1 FAILURE"
	fi
    
}

clean() {
	rm -f $CNV_GEN/*.class
	rm -f $CNV_SERVER/*.class
	rm -f $CNV_SOLVER/*.class
	rm -f $CNV_UTIL/*.class
	rm -f $CNV_METRICS/*.class
	rm -f $CNV_LB/*.class
	check Cleaning	
}

build() {
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
	echo "Compiling CNV_LOADBALANCING ..."
	javac $CNV_LB/*.java
	check Compilation
	cp -r $CNV_ROOT/pt/ $CNV_TMP/
	check Backup
}

metrics() {
	echo "Deleting old solver class files ..."
	rm -r pt/ulisboa/tecnico/cnv/solver/
	check Delete

	echo "Restoring previous compiled version ..."
	cp -r $CNV_TMP/pt/ulisboa/tecnico/cnv/solver $CNV_SOLVER 
	check Copy

	echo "Compiling HillClimbingMetrics ..."
	javac $CNV_METRICS/HillClimbingMetrics.java
	check Compilation

	echo "Instrumenting solver with HillClimbingMetrics ..."
	java HillClimbingMetrics $CNV_SOLVER $CNV_SOLVER
	check Instrumentation
}

run() {
	java pt.ulisboa.tecnico.cnv.server.WebServer	
}

runLB() {
	java pt.ulisboa.tecnico.cnv.loadbalancing.WebServerLB
}

runAS() {
	java pt.ulisboa.tecnico.cnv.loadbalancing.AutoScaler
}



