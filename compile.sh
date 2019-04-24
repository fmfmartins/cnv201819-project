#!/bin/sh

cd /home/ec2-user/cnv-project
javac /home/ec2-user/cnv-project/pt/ulisboa/tecnico/cnv/server/*.java
javac /home/ec2-user/cnv-project/pt/ulisboa/tecnico/cnv/mss/*.java
javac /home/ec2-user/cnv-project/BIT/samples/*.java


cd /home/ec2-user/cnv-project/BIT/samples
java MyTool ~/cnv-project/pt/ulisboa/tecnico/cnv/solver/non_instrumented_classes/ ~/cnv-project/pt/ulisboa/tecnico/cnv/solver/instrumented_classes/
rm /home/ec2-user/cnv-project/pt/ulisboa/tecnico/cnv/solver/*.class
cp /home/ec2-user/cnv-project/pt/ulisboa/tecnico/cnv/solver/instrumented_classes/* /home/ec2-user/cnv-project/pt/ulisboa/tecnico/cnv/solver/

cd ~/cnv-project
