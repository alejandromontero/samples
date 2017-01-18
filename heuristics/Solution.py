'''
AMMM Lab Heuristics v1.0
Representation of a solution instance.
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

import copy, time, sys
import numpy as np
from Problem import Problem

# Assignment class stores the load of the highest loaded CPU
# when a task is assigned to a CPU.  
class Assignment(object):
    def __init__(self, packageId, truckId, maxLoad):
        self.packageId = packageId
        self.truckId = truckId
        self.maxLoad = maxLoad

# Solution includes functions to manage the solution, to perform feasibility
# checks and to dump the solution into a string or file.
class Solution(Problem):
    @staticmethod
    def createEmptySolution(config, problem):
        solution = Solution(problem.inputData)
        solution.setVerbose(config.verbose)
        return(solution)

    def __init__(self, inputData):
        super(Solution, self).__init__(inputData)

        self.truckToPackets = {}			# hash table: truckId => list<packagesId
        self.maxLoad = 0
        self.maxLoadTruck = 0
        self.usedTrucks = 0
        self.feasible = True
        self.verbose = False
    
    def setVerbose(self, verbose):
        if(not isinstance(verbose, (bool)) or (verbose not in [True, False])):
            raise Exception('verbose(%s) has to be a boolean value.' % str(verbose))
        self.verbose = verbose
    
    def makeInfeasible(self):
        self.feasible = False
        self.maxLoad = sys.maxsize
        self.usedTrucks = 0
    
    def isFeasible(self):
        return(self.feasible)

    def updateMaxLoad(self):
        self.maxLoadTruck = 0
        self.usedTrucks = 0
        for truck in self.trucks:
    		if (self.truckToPackets.has_key(truck.getId())):
    			if (len(self.truckToPackets[truck.getId()]) >= 1):
    				self.usedTrucks += 1

        for truck in self.trucks:
            truckId = truck.getId()
            load = truck.getLoad()
            self.maxLoadTruck = max(self.maxLoadTruck, load)

        self.maxLoad = self.maxLoadTruck + self.usedTrucks * self.wTruck
        #print (self.maxLoad)
    
    def checkTruckSpace(self, needBoolean,xDim,yDim,layout):
        pbl = [0,0]
    	found = False
    	for x in xrange(0, len(layout) - xDim + 1):
        	for y in xrange(0, len(layout[x]) - yDim + 1):
        		if (layout[x][y] == -1):
        			#Pbl[x][y]
        			pbl[0] = x
        			pbl[1] = y 
        			found = True
        			#pxy[x2][y2]
        			iterations = 1
        			for x2 in xrange(x,x + xDim):
        				for y2 in xrange(y,y + yDim):
        					iterations += 1
        					if (layout[x2][y2] != -1): 
        						found = False

        		if (found): break
        	if (found): break

        if (found):
        	if (needBoolean): return(True)
        	else: return (pbl)
        else:
        	if (needBoolean): return(False) 
        	else: 
        		return (False)        		


    def isFeasibleToAssignPackageToTruck(self, packageId, truckId,layout):

    	package = self.packages[packageId]
        truck = self.trucks[truckId]

        #Package can't be in more than one truck
        for truck in self.trucks:
	        if (self.truckToPackets.has_key(truck.getId())):
	        	if (packageId in self.truckToPackets[truck.getId()]): return (False)

        #A packet cannot be assigned to a truck if it has no available weight
        weight = 0
        if (self.truckToPackets.has_key(truckId)):
            for paquet in self.truckToPackets[truckId]:
                weight += self.packages[paquet].getWeight()
        
        if (weight + package.getWeight() > self.wTruck):
            return (False)

        #If two packages are incompatible they cannot be in the same truck
        if (self.truckToPackets.has_key(truckId)):
	        for paq in self.truckToPackets[truckId]:
	        	if (self.incomp[packageId][paq] == 1): return (False) 
	        
        xDim = package.getxDim()
        yDim = package.getyDim()

        #Check if theere is available space for the packet inside the truck => Constraint
        if (not self.checkTruckSpace(True, xDim, yDim, layout)): 
        	return(False)

        return (True)

    def getTruckIdAssignedToPackageId(self, packageId):
    	for truck in self.truckToPackets:
    		if (packageId in self.truckToPackets[truck]): 
    			return (truck)

    	return(None)


    def assign(self, packageId, truckId):

        package = self.packages[packageId]
        truck = self.trucks[truckId]
    	xDim = package.getxDim()
        yDim = package.getyDim()
        layout = truck.getTruckLayout()

        if(not self.isFeasibleToAssignPackageToTruck(packageId, truckId,layout)):
            if(self.verbose): print('Unable to assign package(%s) to truck(%s)' % (str(packageId), str(truckId)))
            return(False)

        #Allocate resources in the truck

        pbl = self.checkTruckSpace(False,xDim,yDim,layout)
        package.setPbl(pbl)

        if (pbl):
		    for x in xrange(pbl[0],pbl[0] + xDim):
		    	for y in xrange(pbl[1],pbl[1] + yDim):
		    		layout[x][y] = packageId
    	
    	truck.setLayout(layout)
    	truck.setWeight(package.getWeight())

    	#Add package to truckToPackets
        if (not self.truckToPackets.has_key(truckId)): self.truckToPackets[truckId] = []
        self.truckToPackets[truckId].append(packageId)

    	self.updateMaxLoad()
        return(True)

    def isFeasibleToUnassignPackageFromTruck(self, pacakgeId, truckId):        
        if(not self.truckToPackets.has_key(truckId)):
            if(self.verbose): print('Truck(%s) is not used by any Package.' % str(truckId))
            return(False)

        if(pacakgeId not in self.truckToPackets[truckId]):
            if(self.verbose): print('Package(%s) is not assigned to truck(%s).' % (str(truckId), str(packageId)))
            return(False)

        return(True)

    def unassign(self, packageId, truckId):
        if(not self.isFeasibleToUnassignPackageFromTruck(packageId, truckId)):
            if(self.verbose): print('Unable to unassign Pacakge(%s) from Truck(%s)' % (str(packageId), str(truckId)))
            return(False)

        package = self.packages[packageId]
        truck = self.trucks[truckId]

        #Deallocate resources
        #Eliminate assignament

        assignaments = self.truckToPackets[truckId]
        assignaments.remove(packageId)
        self.truckToPackets[truckId] = assignaments

        #Free truck's layout
        layout = truck.getTruckLayout()
        xDim = package.getxDim()
        yDim = package.getyDim()
        pbl = package.getPbl()

        if (pbl):
	        for x in xrange(pbl[0],pbl[0] + xDim):
				for y in xrange(pbl[1],pbl[1] + yDim):
					layout[x][y] = -1

        truck.setLayout(layout)
        truck.setWeight(-package.getWeight())
        self.updateMaxLoad()

        return(True)
    
    def getMaxLoad(self):
        return(self.maxLoad)

    def getUsedTrucks(self):
        return(self.usedTrucks)
    
    def findFeasibleAssignments(self, packageId):
        startEvalTime = time.time()
        evaluatedCandidates = 0
        
        feasibleAssignments = []
        for truck in self.trucks:
            truckId = truck.getId()
            feasible = self.assign(packageId, truckId)
            evaluatedCandidates += 1
            if(not feasible): continue
            
            assignment = Assignment(packageId, truckId, self.getMaxLoad())
            feasibleAssignments.append(assignment)
            
            self.unassign(packageId, truckId)
            
        elapsedEvalTime = time.time() - startEvalTime
        return(feasibleAssignments, elapsedEvalTime, evaluatedCandidates)

    def findBestFeasibleAssignment(self, packageId):
        bestAssignment = Assignment(packageId, None, sys.maxsize)
        for truck in self.trucks:
            truckId = truck.getId()
            feasible = self.assign(packageId, truckId)
            if(not feasible): continue
            
            curMaxLoad = self.getMaxLoad()
            if(bestAssignment.maxLoad > curMaxLoad):
                bestAssignment.truckId = truckId
                bestAssignment.maxLoad = curMaxLoad
            
            self.unassign(packageId, truckId)
            
        return(bestAssignment)
    
    def __str__(self):  # toString equivalent

    	nPackages = self.inputData.nPackages
    	nTrucks = self.inputData.nTrucks
    	xTruck = self.inputData.xTruck
        yTruck = self.inputData.yTruck
        wTruck = self.inputData.wTruck

        optimizationVaulue = self.maxLoad
        trucksWeight = self.usedTrucks * wTruck
        if (optimizationVaulue >= sys.maxsize): strSolution = 'The problem has no solution\n'
        else: strSolution = 'Optimization Value  = %d\n' %optimizationVaulue
        strSolution += 'usedTrucks = %d\n' % self.usedTrucks
        strSolution += 'Total truck weight = %d\n' %trucksWeight
        strSolution += 'Max loaded truck = %d\n' %self.maxLoadTruck

        #Packet assignament

        strSolution += 'Packge assignament:\n'
        for truck in self.truckToPackets:
        	strSolution += 'Truck%d: [' % truck
        	for package in self.truckToPackets[truck]:
        		strSolution += str(package) + ' '
        	strSolution += '],\n'

        #Weight in each truck

        strSolution += 'Truck weight:\n'
        for truck in self.truckToPackets:
            strSolution += 'Truck%d: ' % truck
            weight = 0
            for package in self.truckToPackets[truck]:
                weight += self.packages[package].getWeight()
            strSolution += str(weight) + '\n'

        #Truck layout
        strSolution += 'Trucks Layout:\n'
        for truck in xrange(0,nTrucks):
        	strSolution += 'Truck%d [\n' % truck
        	layout = self.trucks[truck].getTruckLayout()
        	for x in xrange(0,xTruck):
        		strSolution += '     ['
        		for y in xrange(0,yTruck):
        			strSolution += str(layout[x][y]) + ' '
        		strSolution += ']\n'
        	strSolution += ']\n'

        return(strSolution)

    def saveToFile(self, filePath):
        f = open(filePath, 'w')
        f.write(self.__str__())
        f.close()

    def printTrucks(self):
        print (self.truckToPackets)

    def printLayout(self,truck):
        print (self.trucks[truck].getTruckLayout())