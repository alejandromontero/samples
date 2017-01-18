'''
AMMM Lab Heuristics v1.0
Specific BRKGA Decoder for Task To CPU Assignment.
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

from Solver_BRKGA import BRKGA_Decoder
from Solution import Solution

class PackageToTruckAssignment_Decoder(BRKGA_Decoder):
    def __init__(self, config, problem):
        self.config = config
        self.problem = problem
    
    def getNumGenes(self):
        packages = self.problem.getPacakges()
        return(len(packages))

    def decode(self, individual):
        # get pacakges and genes
        packages = self.problem.getPacakges()     # vector of tasks [t1, t2, t3, ...]
        genes = individual.chromosome       # vector of genes [g1, g2, g3, ...]
        

        # zip vector tasks and vector genes in a vector of tuples with the form:
        # [(t1,g1), (t2,g2), (t3,g3), ...]

        packages_and_genes = zip(packages, genes)
        
        # sort the vector of tuples tasks_and_genes by the genes (index 1 in the tuple)
        packages_and_genes.sort(key=lambda package_and_gene: package_and_gene[0].getxDim() * package_and_gene[0].getyDim() * package_and_gene[1], reverse=True)
        
        # create an empty solution for the individual
        individual.solution = Solution.createEmptySolution(self.config, self.problem)
        
        # assign each task following the given order
        for package_and_gene in packages_and_genes:
            package = package_and_gene[0]
            packageId = package.getId()
            
            # get the feasible assignment for the current task with minimum highest CPU load
            assignment = individual.solution.findBestFeasibleAssignment(packageId)
            bestTruckId = assignment.truckId
            
            if(bestTruckId is None):
                # infeasible solution
                individual.solution.makeInfeasible()
                break
            
            # do assignment
            individual.solution.assign(packageId, assignment.truckId)
        
        individual.fitness = individual.solution.getMaxLoad()
