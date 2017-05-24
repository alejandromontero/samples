
import sys,os,signal
import argparse
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
CONNECTION_TIMEOUT=30

def export_to_csv(queries,__location__):

    csv_file = open(__location__ + '/results_of_experiment.csv','wb')

    header = ["web-page", "circuit_average_creation_times", "query_average_times"]
    writer = csv.DictWriter(csv_file,delimiter=',',fieldnames=header)
    writer.writeheader()

    for key,value in queries.iteritems():
        row={}
        average_circuit_times=0
        average_query_times=0
        for j in xrange(0, len(value["query_times"])):
            average_circuit_times += value["circuit_creation_times"][j]
            average_query_times += value["query_times"][j]

        row["web-page"]=key
        row["circuit_average_creation_times"] = average_circuit_times/len(value["query_times"])
        row["query_average_times"] = average_query_times/len(value["query_times"])
        writer.writerow(row)

def query(url,queries,web):
    """
    Uses pycurl to fetch a site using the proxy on the SOCKS_PORT.
    """

    output = StringIO.StringIO()

    query = pycurl.Curl()
    query.setopt(pycurl.URL, url)
    query.setopt(pycurl.PROXY, 'localhost')
    query.setopt(pycurl.PROXYPORT, SOCKS_PORT)
    query.setopt(pycurl.PROXYTYPE, pycurl.PROXYTYPE_SOCKS5_HOSTNAME)
    query.setopt(pycurl.CONNECTTIMEOUT, CONNECTION_TIMEOUT)
    query.setopt(pycurl.WRITEFUNCTION, output.write)

    try:
        query.perform()
    except pycurl.error as exc:
        sys.stderr.write ("Unable to perform query")
        return "FAIL"

    return "SUCCESS"

def create_circuit(controller):
    #attach stream to circuit
    def attach_stream(stream):
        if stream.status == 'NEW':
            controller.attach_stream(stream.id, circuit)

    try:
        circuit = controller.new_circuit(await_build = True)
        controller.add_event_listener(attach_stream, stem.control.EventType.STREAM)
        return circuit,attach_stream
    except Exception as exc:
        sys.stderr.write("Couldn't create a new circuit")
        return

def make_experiments(controller,iterations):

    #Close all current TOR circuits (if any)
    for circuit in controller.get_circuits():
        controller.close_circuit(circuit.id)

    # Configure tor client
    controller.set_conf("__DisablePredictedCircuits", "1")
    controller.set_conf("__LeaveStreamsUnattached", "1")
    controller.set_conf("MaxClientCircuitsPending", "1024")

    #Open file with URL to analyze
    __location__ = os.path.realpath(
        os.path.join(os.getcwd(), os.path.dirname(__file__)))

    file = open(os.path.join(__location__,'sites.txt'),'r')

    #Create data structures
    queries={}

    for line in file:
        line = line.strip()
        print("Testing web-page: ", line )

        queries[line]={}
        queries[line]["circuit_creation_times"]=[]
        queries[line]["query_times"]=[]

        for i in xrange (0, int(iterations)):
            print ("Iteration ", i, "of web-page ", line)
            #create a new circuit

            init_circuit=time.time()
            circuit=""
            while not circuit:
                circuit,attach_stream = create_circuit(controller)
            circuit_finish_time=(time.time() - init_circuit)
            print ("time to create circuit: ", circuit_finish_time)

            queries[line]["circuit_creation_times"].append(circuit_finish_time)

            query_start_time=time.time()
            success_or_fail=""
            while (success_or_fail != "SUCCESS"):
                success_or_fail = query(line,queries,line)

            query_time=(time.time() - query_start_time)
            queries[line]["query_times"].append(query_time)
            print ("time to query: ", query_time)
            print ("total query time: ", float(query_time) + float(queries[line]["circuit_creation_times"][-1]))
            print

            #close circuit
            controller.close_circuit(circuit)
            controller.remove_event_listener(attach_stream)
            controller.reset_conf('__LeaveStreamsUnattached')

    export_to_csv(queries,__location__)

def main(argc, argv):
    parser = argparse.ArgumentParser(description='Perform a TOR analysis')
    parser.add_argument('iterations', help='Number of iterations to test every web-page')
    # parser.add_argument('save_path', help='folder in which to save the resulting csv')
    args = parser.parse_args()## show values ##

    #connect to TOR

    controller = connect_port(port=TOR_PORT)

    if not controller:
        sys.stderr.write("ERROR: Couldn't connect to tor. \n")
        sys.exit(1)
    else:
        sys.stdout.write("Successfully connected to TOR. \n")

    make_experiments(controller,args.iterations)
    controller.close()
    # os.kill(controller.get_pid(),signal.SIGTERM)

    print ("END")
    sys.exit()

if __name__ == "__main__":
    exit(main(len(sys.argv), sys.argv))