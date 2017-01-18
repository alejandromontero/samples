'''
AMMM Lab Heuristics v1.0
Main function.
Copyright 2016 Luis Velasco and Lluis Gifre.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
'''

import argparse
import sys
import numpy as np

from DATParser import DATParser
from ValidateInputData import ValidateInputData
from ValidateConfig import ValidateConfig
from Solver_Greedy import Solver_Greedy
from Solver_GRASP import Solver_GRASP
from Solver_BRKGA import Solver_BRKGA
from Problem import Problem
from Solution import Solution
from BRKGA_Decoder_PackageToTruckAssignment import PackageToTruckAssignment_Decoder

def run():
    argp = argparse.ArgumentParser(description='PacketManager')
    argp.add_argument('configFile', help='configuration file path')
    args = argp.parse_args()
    
    print '---PacketManager---'
    print '-------------------'
    
    print 'Reading Config file %s...' % args.configFile
    config = DATParser.parse(args.configFile)
    ValidateConfig.validate(config)
    
    print 'Reading Input Data file %s...' % config.inputDataFile
    inputData = DATParser.parse(config.inputDataFile)
    ValidateInputData.validate(inputData)
    
    print 'Creating Problem...'
    problem = Problem(inputData)

    print 'Solving Problem...'
    solver = None
    solution = None
    if(config.solver == 'Greedy'):
        solver = Solver_Greedy()
        solution = solver.solve(config, problem)
    elif(config.solver == 'GRASP'):
        solver = Solver_GRASP()
        solution = solver.solve(config, problem)
    elif(config.solver == 'BRKGA'):
        solver = Solver_BRKGA()
        decoder = PackageToTruckAssignment_Decoder(config, problem)
        solution = solver.solve(config, problem, decoder)
        
    solution.saveToFile(config.solutionFile)
        
    return(0)

if __name__ == '__main__':
    sys.exit(run())
