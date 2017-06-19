#!/bin/bash

cluster=$1
jobs=$2
destiny_folder=$3

echo "destiny folder: $3"

((i=0))
for job in $jobs ; do


	rsync -avur  -e "ssh -i ./keys/id_rsa -o StrictHostKeyChecking=no -o 'ProxyCommand=ssh -i ./keys/id_rsa npoggi@minerva -p 22 nc %h %p 2> /dev/null'" npoggi@minerva-105:/users/scratch/pristine/share/jobs_$cluster/$job $4
	
	#python /home/alejandro/Documents/aloja_share/users/amontero/utilities/BigBench-times-parser/times-parser.py $4/times-$i.csv
	((i+=1))
 
done
