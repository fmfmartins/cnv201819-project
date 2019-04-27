#!/bin/sh

cd /home/ec2-user/cnv-project/

rm /home/ec2-user/cnv-project/pt/ulisboa/tecnico/cnv/server/*.class
rm /home/ec2-user/cnv-project/pt/ulisboa/tecnico/cnv/mss/*.class
rm /home/ec2-user/cnv-project/BIT/samples/*.class

javac /home/ec2-user/cnv-project/pt/ulisboa/tecnico/cnv/server/*.java
javac /home/ec2-user/cnv-project/pt/ulisboa/tecnico/cnv/mss/*.java
javac /home/ec2-user/cnv-project/BIT/samples/*.java

echo "COMPILING SUCCESS"

java HillClimbingMetrics ~/cnv-project/pt/ulisboa/tecnico/cnv/solver/non_instrumented_classes/ ~/cnv-project/pt/ulisboa/tecnico/cnv/solver/instrumented_classes/
rm /home/ec2-user/cnv-project/pt/ulisboa/tecnico/cnv/solver/*.class
cp /home/ec2-user/cnv-project/pt/ulisboa/tecnico/cnv/solver/instrumented_classes/* /home/ec2-user/cnv-project/pt/ulisboa/tecnico/cnv/solver/

echo "INSTRUMENTED CLASSES UPDATE SUCCESS"
