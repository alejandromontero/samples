
import sys,os,signal
import argparse
import random
import re
import time
import datetime
import glob
import pprint
import csv
import StringIO
import time
from shutil import copyfile

import pycurl
import stem
from stem import OperationFailed, InvalidRequest, InvalidArguments
from stem.control import Controller, EventType
from stem.connection import connect_port
from stem.version import Version

TOR_PORT=9151
SOCKS_PORT=9150
CONNECTION_TIMEOUT=10
QUERY_TIMEOUT=3600

def export_to_csv(queries,__location__,test,type):

    path = __location__ + "/results"
    if (not os.path.exists(path)):
        os.makedirs(path)

    csv_file = open(path + "/" + test + "-" + type + "-test.csv" ,'wb')

    if (type=="TOR"): header = ["web-page", "circuit_average_creation_times", "query_average_times", "total_query_average_times","average_circuit_fails","average_query_fails","bandwith"]
    else: header = ["web-page", "query_average_times", "total_query_average_times","average_query_fails","bandwith"]
    writer = csv.DictWriter(csv_file,delimiter=',',fieldnames=header)
    writer.writeheader()
    for key,value in queries.iteritems():
        row={}
        average_circuit_times=0
        average_circuit_fails=0
        average_query_times=0
        average_query_fails=0
        total_query_average_times=0
        average_bandwith=0
        for j in xrange(0, len(value["query_times"])):
            if (type=="TOR"): average_circuit_times += value["circuit_creation_times"][j]
            if (type=="TOR"): average_circuit_fails += value["circuit_fails"][j]
            average_query_times += value["query_times"][j]
            average_query_fails += value["query_fails"][j]
            total_query_average_times += value["query_total_times"][j]
            average_bandwith += value["bandwith"][j]

            if (test == "bandwith"):
                row["web-page"]=key
                if (type=="TOR"): row["circuit_average_creation_times"] = value["circuit_creation_times"][j]
                if (type=="TOR"): row["average_circuit_fails"] = value["circuit_fails"][j]
                row["query_average_times"] = value["query_times"][j]
                row["average_query_fails"] = value["query_fails"][j]
                row["total_query_average_times"] = value["query_total_times"][j]
                row["bandwith"] = value["bandwith"][j]
                writer.writerow(row)

        if (test == "latency"):
            row["web-page"]=key
            if (type=="TOR"): row["circuit_average_creation_times"] = average_circuit_times/len(value["query_times"])
            if (type=="TOR"): row["average_circuit_fails"] = average_circuit_fails/len(value["query_times"])
            row["query_average_times"] = average_query_times/len(value["query_times"])
            row["average_query_fails"] = average_query_fails/len(value["query_times"])
            row["total_query_average_times"] = total_query_average_times/len(value["query_times"])
            row["bandwith"] = average_bandwith/len(value["query_times"])
            writer.writerow(row)

def query(url,type):
    """
    Uses pycurl to fetch a site using the proxy on the SOCKS_PORT.
    """
    def devnull(body):
        """ Drop Curl output. """
        return

    output = StringIO.StringIO()

    if (type == "TOR"):

        query = pycurl.Curl()
        query.setopt(pycurl.URL, url)
        query.setopt(pycurl.PROXY, 'localhost')
        query.setopt(pycurl.PROXYPORT, SOCKS_PORT)
        query.setopt(pycurl.PROXYTYPE, pycurl.PROXYTYPE_SOCKS5_HOSTNAME)
        query.setopt(pycurl.CONNECTTIMEOUT, CONNECTION_TIMEOUT)
        query.setopt(pycurl.TIMEOUT, QUERY_TIMEOUT)
        query.setopt(pycurl.WRITEFUNCTION, devnull)
        query.setopt(pycurl.USERAGENT, "")
        query.setopt(pycurl.ENCODING, "identity")

    else:
        query = pycurl.Curl()
        query.setopt(pycurl.URL, url)
        query.setopt(pycurl.CONNECTTIMEOUT, CONNECTION_TIMEOUT)
        query.setopt(pycurl.TIMEOUT, QUERY_TIMEOUT)
        query.setopt(pycurl.WRITEFUNCTION, devnull)

    try:
        query.perform()
    except pycurl.error as exc:
        sys.stderr.write ("Unable to perform query: ")
        print (exc)
        return {"query_sucess":"FAIL","query_size":query.getinfo(pycurl.SIZE_DOWNLOAD)}

    return {"query_sucess":"SUCCESS","query_size":query.getinfo(pycurl.SIZE_DOWNLOAD)}


def make_experiments(controller,test,iterations,type):

    print ("Starting experiment-",test,"of network: ",type)
    print
    #Close all current TOR circuits (if any)
    print "stopping circuits"
    for circuit in controller.get_circuits():
        print ("Stopping: ", circuit.id)
        controller.close_circuit(circuit.id)

    print "stopping streams"
    for stream in controller.get_streams():
        print ("Stopping: ", stream.id)
        controller.close_stream(stream.id)

    # Configure tor client
    controller.set_conf("__DisablePredictedCircuits", "1")

    #Open file with URL to analyze
    __location__ = os.path.realpath(
        os.path.join(os.getcwd(), os.path.dirname(__file__)))

    file="sites-"+test+".txt"
    print (file)
    file = open(os.path.join(__location__,file),'r')

    #Create data structures
    queries={}

    for line in file:
        line = line.strip()
        print("Testing web-page: ", line )

        queries[line]={}
        queries[line]["circuit_creation_times"]=[]
        queries[line]["circuit_fails"]=[]
        queries[line]["query_times"]=[]
        queries[line]["query_fails"]=[]
        queries[line]["query_total_times"]=[]
        queries[line]["bandwith"]=[]

        for i in xrange (0, int(iterations)):
            print ("Iteration ", i+1, "of web-page ", line)
            query_sucess=""
            circuit_fails=0
            query_fails=0
            init_query=time.time()
            while query_sucess != "SUCCESS":

                controller.set_conf("__LeaveStreamsUnattached", "1")

                if (type=="TOR"):
                    #create a new circuit
                    init_circuit=time.time()

                    circuit_id=""
                    while not circuit_id:
                        try:
                            circuit_id = controller.new_circuit(await_build = True)
                        except stem.ControllerError as exc:
                            circuit_fails+=1
                            sys.stderr.write("Couldn't create a new circuit, retrying \n")
                            print ("Number of circuit fails: ", circuit_fails)

                    def attach_stream(stream):
                        if stream.status == 'NEW':
                            try:
                                controller.attach_stream(stream.id, circuit_id)
                            except Exception as exc:
                                print (exc)

                    controller.add_event_listener(attach_stream, stem.control.EventType.STREAM)

                    circuit_finish_time=time.time() - init_circuit
                    print ("time to create circuit: ", circuit_finish_time)

                #Do a query
                query_start_time=time.time()
                query_res = query(line,type)
                query_sucess = query_res["query_sucess"]
                if (query_res["query_sucess"] != "SUCCESS"):
                    query_fails+=1
                    print ("Number of query fails: ", query_fails)
                query_time=(time.time() - query_start_time)

                bandwith=query_res["query_size"]/query_time #In bytes per second
                #close circuit. Sometimes the circuits are already closed so do nothing if that happens
                try:
                    controller.close_circuit(circuit)
                    controller.close_stream(stream.id)
                    controller.remove_event_listener(attach_stream)
                    controller.reset_conf('__LeaveStreamsUnattached')

                except Exception as exc: pass

            total_query_time=(time.time() - init_query)
            if (type=="TOR"): queries[line]["circuit_creation_times"].append(circuit_finish_time)
            if (type=="TOR"): queries[line]["circuit_fails"].append(circuit_fails)
            queries[line]["query_times"].append(query_time)
            queries[line]["query_fails"].append(query_fails)
            queries[line]["query_total_times"].append(total_query_time)
            queries[line]["bandwith"].append(bandwith)

            print ("time to query: ", query_time)
            print ("total query time: ", total_query_time)
            print
            controller.reset_conf('__LeaveStreamsUnattached')

        export_to_csv(queries,__location__,test,type)

def connect_controller():
    controller = connect_port(port=TOR_PORT)

    if not controller:
        sys.stderr.write("ERROR: Couldn't connect to tor. \n")
        sys.exit(1)
    else:
        sys.stdout.write("Successfully connected to TOR. \n")
    return controller

def main(argc, argv):
    parser = argparse.ArgumentParser(description='Perform a TOR analysis')
    parser.add_argument('iterations', help='Number of iterations to test every web-page')
    # parser.add_argument('save_path', help='folder in which to save the resulting csv')
    args = parser.parse_args()## show values ##

    #connect to TOR

    # controller = connect_controller()
    # make_experiments(controller,"latency",args.iterations,"TOR")
    controller = connect_controller()
    make_experiments(controller,"bandwith",args.iterations,"TOR")
    controller = connect_controller()
    make_experiments(controller,"latency",args.iterations,"DEFAULT")
    controller = connect_controller()
    make_experiments(controller,"bandwith",args.iterations,"DEFAULT")

    controller.close()
    # os.kill(controller.get_pid(),signal.SIGTERM)

    print ("END")
    sys.exit()

if __name__ == "__main__":
    exit(main(len(sys.argv), sys.argv))