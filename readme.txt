Architecture

The system will be run within the Amazon Web Services ecosystem and its architecture is organized in four main components: 

Web Servers
Load Balancer
Auto-Scaler
Metrics Storage System (pt.ulisboa.tecnico.cnv.mss.RequestMetrics class stores the metrics for each thread. And the instrumentation tool used is named MyTool and it is placed inside the BIT/samples directory)

Currently both the Web Servers and the Metrics Storage System components are in practice implemented in the same instances.

The extracted metrics are also placed in a timestamped file inside the /tmp/ directory.


Configuration settings:

AUTO-SCALER
** Scaling Policies
The set of defined rules at this point in time are the following :
Scale Up
    When AverageCPUUtilization >= 80 for 2 consecutive periods of 300 seconds add one instance
Scale Down
    When AverageCPUUtilization <= 40 for 300 seconds remove one instance

** Other settings
Default Cooldown : 300 seconds
Health Check Grace Period: 300 seconds
Minimum Group Size: 1
Maximum Group Size: 3

LOAD BALANCER
    Idle Timeout : 1020 seconds
    Health Check Settings :
	Ping Protocol: HTTP
	Ping Port: 8000
	Ping Path: /test
	Response Timeout: 5 seconds
	Interval: 30 seconds
	Unhealthy threshold: 2 
	Healthy threshold: 4





