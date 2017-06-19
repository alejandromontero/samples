#!/bin/bash

cluster_gateway=$1
cluster=$2
jobs=$3
destiny_folder=$4

echo "destiny folder: $4"

((i=0))
for job in $jobs ; do

	ssh -i /home/alejandro/Documents/projects/aloja/secure/keys/id_rsa pristine@${cluster_gateway}-ssh.azurehdinsight.net " 
	
	if [ -d /tmp/executions ] ; then rm -r /tmp/executions ; fi
	mkdir -p /tmp/executions

	ls /home/pristine/share/share-global/jobs_$cluster/$job/BB_100_throughput*	
	tar -C /tmp/executions -xvjf /home/pristine/share/share-global/jobs_$cluster/$job/BB_100_throughput* 

	"
	
	rsync -avur --exclude '.git*' --exclude '*aloja-web*' --exclude '*ganglia*'  --exclude 'blobs' --exclude '.bz2' -e "ssh -i /home/alejandro/Documents/projects/aloja/secure/keys/id_rsa -o StrictHostKeyChecking=no " pristine@${cluster_gateway}-ssh.azurehdinsight.net:/tmp/executions/BB_*_throughput-*/BigBench_logs/bigbench_*/times.csv $4/times-$i.csv

	python /home/alejandro/Documents/aloja_share/users/amontero/utilities/BigBench-times-parser/times-parser.py $4/times-$i.csv
	((i+=1))
 
done
