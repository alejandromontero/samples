#!/usr/bin/env python

import sys,os
import argparse
import re
import time
import datetime
import glob
import pprint
import csv
import random

def build_csv(queries,save_path,average):

    path = save_path + "/" + str(len(queries.values()[0])) + "-Stream"
    if (not os.path.exists(path)):
        os.makedirs(path)

    csv_file = open (path + '/times-processed-'+str(len(queries.values()[0]))+'-Streams.csv','wb')

    header = []

    for i in xrange (0,len(queries[random.sample(queries,1)[0]])):
        header.append("Stream " + str(i))

    writer = csv.DictWriter(csv_file,delimiter=',',fieldnames=header)
    writer.writeheader()

    for key in sorted(queries):
        row={}
        # print (key)
        for key2 in sorted(queries[key]):
            # print (key2)
            row["Stream " + key2] = (queries[key][key2]/average)

        writer.writerow(row)


def build_data(paths,save_path):

    queries = {}

    for path in paths:
        file = open (path,'r')
        print ("Parsing log: " + path)
        for line in file:
            if line is not "" and "STARTDATE_EPOCH|" not in line:
                words = line.split('|')
                stream = words[9]
                query = words[6]
                exec_time = int(words[1]) - int(words[0])

                if query not in queries:
                    queries[query] ={}

                if stream not in queries[query]: queries[query][stream] = exec_time
                else: queries[query][stream] = queries[query][stream] + exec_time

    build_csv(queries,save_path,len(paths))

def main(argc, argv):
    parser = argparse.ArgumentParser(description='parse times log file of BigBench and calculate averages (in case that more than one times.csv is provided)')
    parser.add_argument('sources', help='path to the times log files',nargs='*')
    # parser.add_argument('save_path', help='folder in which to save the resulting csv')
    args = parser.parse_args()## show values ##

    save_path_vec = (args.sources[0].split('/')[1:-1])
    save_path="/"
    for path in save_path_vec:
        save_path += path + "/"

    build_data(args.sources,save_path)

    print ("END")
    sys.exit()

if __name__ == "__main__":
    exit(main(len(sys.argv), sys.argv))