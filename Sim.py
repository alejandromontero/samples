#!/usr/bin/env python

import sys
import argparse
import matplotlib.pyplot as plt
import json, random, numpy, math
from scipy.stats import chisquare, describe

def generate_weibull(a, b, n=100000):
    res = []
    for i in range(0, n):
        random_p = random.random()
        value = b*(-math.log(1-random_p))**(1/a)
        res.append(value)

    return res

def generate_theoretical_weibull(a, b, n=100000):
    return numpy.random.weibull(a, size=n)

def simulation():
    print("sim")

def service_time_analysis(N,WSHAPE):
    print ("Testing Weighbull RNG\n")
    weibull_times=generate_weibull(WSHAPE,1,N)
    theoretical_weibull_times=generate_theoretical_weibull(WSHAPE,1,N)

    #### RNG Weibull ####
    obs_n, obs_bins, obs_patches = plt.hist(weibull_times,50,[0,20],color='red')
    # plt.show()
    plt.savefig("obsHist.png")

    #### THEORETICAL Weibull ####
    theor_n, theor_bins, theor_patches = plt.hist(theoretical_weibull_times, 50,[0,20],color='green')
    # plt.show()
    plt.savefig("theorHist.png")

    print("RNG Weibull values: \n   " \
          + str(describe(weibull_times)) + '\n')

    print("Theoretical Weibull values: \n   " \
          + str(describe(theoretical_weibull_times)) + '\n')

    #### Chisquare test ####
    chistat, pvalue = chisquare(obs_n, theor_n)
    print("Resulting Chi-square test:\n"\
            "   Chi-square stat: " + str(chistat) + '\n'\
            "   p-value:         " + str(pvalue) + '\n' )

def main(argc, argv):
    parser = argparse.ArgumentParser(description='Perform a queuing theory analysis')
    parser.add_argument('N',type=int, nargs='?', default=100000, help='number of samples')
    parser.add_argument('WSHAPE', type=int, nargs='?', default=0.55, help='shape of the weibull distribution')
    parser.add_argument('ESHAPE',type=int, nargs='?', default=1, help='shape of the Erlang distribution')
    parser.add_argument('TAU',type=int, nargs='?', default=1, help='shape of the Erlang distribution')
    parser.add_argument('-o','--objective', help='objective of the run(simulation,distribution generator analysis)',required=True,choices=['dist_analysis','sim'])
    args = parser.parse_args()## show values ##

    print ("Simulation Parameters:\n"\
           "    N: " + str(args.N) + '\n'\
           "    Weighbull-shape: " + str(args.WSHAPE) + '\n'\
           "    Erlang-shape: " + str(args.ESHAPE) + '\n'\
           "    Tau-Expectance: " + str(args.TAU) + '\n' )
    if args.objective == 'dist_analysis': service_time_analysis(args.N,args.WSHAPE)
    else: simulation()
    print ("END")
    sys.exit()

if __name__ == "__main__":
    exit(main(len(sys.argv), sys.argv))