#!/usr/bin/env python

import sys,os
import argparse
import re
import time
import datetime
import glob
import pprint
import csv

def get_id(line,type):
    for word in line.split():
        if (type == "application" and "application" in word):
            id = word.split('_')
            return id[1]+'_'+id[2]
        elif (type == "container" and "container_" in word):
            id = word.split('_')
            return id[1]+'_'+id[2]+'_'+id[3]+'_'+id[4]

def get_times(dict,division=1):

    start = sys.maxint
    stop = 0
    for key,value in dict.iteritems():
        if (value["start_time"] < start): start = value["start_time"]
        if (value["stop_time"] > stop) : stop = value["stop_time"]

    duration = float(stop) - float(start)
    steps = duration/division
    times = []
    for i in xrange (0, int(steps) + 2):
        times.append(start + (i*division))

    return times,start,stop,duration

def get_states(dict):

    states = ["timestamp","RUNNING"]
    for key,value in dict.iteritems():
        for key2 in value.keys():
            if (key2 not in states and key2 != "stop_time" and key2 != "start_time"): states.append(key2)

    return states

def check_timestamp(stop_time,start_time,timestamp,division):

    if (stop_time >= timestamp and stop_time <= (timestamp + division)): return True
    elif (start_time >= timestamp and start_time <= (timestamp + division)): return True
    elif (start_time <= timestamp and stop_time >= (timestamp + division)): return True
    else: return False

def get_app_resources(app_id,containers,time,division):

    total_mem = 0
    total_cores = 0

    for key,value in containers.iteritems():
        if (app_id in key):
            for key2,value2 in value.iteritems():
                if (key2 == "RUNNING"):
                    for k in xrange(0,len(value2)):

                        if (check_timestamp(value2[k]["stop_state"],value2[k]["start_state"],time,division)):

                            total_cores += value["cores"]
                            total_mem += value["memory"]

    return total_cores,total_mem


def update_dict(dict,id,previous_state,new_state,timestamp):

    if (id not in dict):
        dict[id] = {}
        dict[id]["start_time"] = timestamp
        dict[id][previous_state] = []
        state = {"start_state" : timestamp}
        dict[id][previous_state].append(state)

    dict[id][previous_state][-1]["stop_state"] = timestamp

    if (new_state not in dict[id]): dict[id][new_state]=[]
    state = {"start_state" : timestamp,"stop_state": timestamp}
    dict[id][new_state].append(state)

    dict[id]["stop_time"] = timestamp

def build_csv(dict,containers,name,save_path,division=1):

    times,start_time,stop_time,duration=get_times(dict)

    if (not os.path.exists(save_path)):
        os.makedirs(save_path)

    file = open (save_path + '/' + name+'.csv','wb')

    states = get_states(dict)
    states.append("ended")

    if (dict != containers):
        apps = dict.keys()
        states.extend(apps)
    else:
        states.extend(("memory_usage","cpu_usage"))

    writer = csv.DictWriter(file,delimiter=',',fieldnames=states)
    writer.writeheader()

    terminated = []
    for t in xrange(0, len(times)):
        if (len(times)/4 == t) : print ("25% completed....")
        elif (len(times)/2 == t) : print ("50% completed....")
        elif (len(times)-len(times)/4 == t) : print ("75% completed....")
        row={}
        total_mem = 0
        total_cores = 0

        for i in xrange (0,len(states)):
            row[states[i]] = 0

        for key,value in dict.iteritems():
            if (times[t] >= value["stop_time"] and key not in terminated) : terminated.append(key)
            winner=""
            max_time=0
            for key2,value2 in value.iteritems():

                if (key2 != "start_time" and key2 != "stop_time" and key2 != "memory" and key2 != "cores"):

                    for k in xrange(0,len(value2)):

                        if (check_timestamp(value2[k]["stop_state"],value2[k]["start_state"],times[t],division)):

                            if (times[t] > max_time):
                                winner = key2
                                max_time = times[t]

                            if (key2 == "RUNNING"):
                                if ("cores" in value):
                                    total_cores += value["cores"]
                                    total_mem += value["memory"]
                                else:
                                    cor,mem = get_app_resources(key,containers,times[t],division)
                                    row[key] = row[key] + cor

            if (winner): row[winner] = row[winner] + 1

        row["timestamp"] = times[t]
        row["ended"] = len(terminated)
        if (total_mem): row["memory_usage"] = total_mem
        if (total_cores):row["cpu_usage"] = total_cores

        writer.writerow(row)

def build_data(path,save_path):

    file = open (path,'r')
    containers = {}
    applications = {}

    print ("Parsing log: " + path)

    for line in file:
        if re.match('\d{4}-\d{2}-\d{2}', line):

            date = line[0:19]
            milis = line[20:23]
            timestamp = time.mktime(datetime.datetime.strptime(date, "%Y-%m-%d %H:%M:%S").timetuple())
            timestamp = float(timestamp) + (float(milis)/1000)
            if ("application" in line and "State change from" in line):

                new_state =  line.split()[-1]
                previous_sate = line.split()[-3]
                update_dict(applications,get_id(line,"application"),previous_sate,new_state,timestamp)

            elif ("container_" in line and "Container Transitioned" in line):

                new_state =  line.split()[-1]
                previous_sate = line.split()[-3]
                update_dict(containers,get_id(line,"container"),previous_sate,new_state,timestamp)

            elif ("Assigned container" in line and "of capacity" in line and "memory" not in containers[get_id(line,"container")]):

                data = re.split('<|>',line)[1]
                memory = re.split(', ',data)[0]
                cores = re.split(', ',data)[1]
                memory = memory.split(':')[1]
                cores = cores.split(':')[1]
                containers[get_id(line,"container")]["memory"] = int(memory)
                containers[get_id(line,"container")]["cores"] = int(cores)

    print("Finished parsing log....")
    print("Processing applications....")
    build_csv(applications,containers,"applications",save_path)
    print ("Done, data sotored in: " + save_path + "/applications.csv")

    print("Processing containers....")
    build_csv(containers,containers,"containers",save_path)
    print ("Done, data sotored in: " + save_path + "/containers.csv")

def main(argc, argv):
    parser = argparse.ArgumentParser(description='parse yarn log')
    parser.add_argument('source', help='path to the directory containing the logs')
    parser.add_argument('save_path', help='folder in which to save the resulting csv')
    args = parser.parse_args()## show values ##

    save_path = args.source.split('/')
    if (save_path[-1] == ''):
        save_path = args.save_path + save_path[-4] + '/' + save_path[-3]
        path = args.source + '*resourcemanager*.log'
    else:
        save_path = args.save_path + '/' + save_path[-3] + '/' + save_path[-2]
        path = args.source + '/*resourcemanager*.log'

    file = glob.glob(path)
    build_data(file[0],save_path)

    print ("END")
    sys.exit()

if __name__ == "__main__":
    exit(main(len(sys.argv), sys.argv))