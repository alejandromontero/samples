import copy, time

# A change in a solution in the form: move taskId from curCPUId to newCPUId.
# This class is used to carry sets of modifications.
# A new solution can be created based on an existing solution and a list of
# changes can be created using the createNeighborSolution(solution, changes) function.
class Change(object):
    def __init__(self, packageId, curTruckId, newTruckId):
        self.packageId = packageId
        self.curTruckId = curTruckId
        self.newTruckId = newTruckId

# Implementation of a local search using two neighborhoods and two different policies.
class LocalSearch(object):
    def __init__(self, config):
        self.enabled = config.localSearch
        self.nhStrategy = config.neighborhoodStrategy
        self.policy = config.policy
        
        self.elapsedTime = 0
        self.iterations = 0

    
    def evaluateNeighbor(self, solution, changes):
        newSolution = copy.deepcopy(solution)

        for change in changes:
        	newSolution.unassign(change.packageId,change.curTruckId)
        for change in changes: 
        	feasible = newSolution.assign(change.packageId,change.newTruckId)
        	if(not feasible): return(None)

        return(newSolution)
    
    def getPackagesAssignmentsSortedByAssignedTruckLoad(self, solution):
        packages = solution.getPacakges()
        trucks = solution.getTrucks()
        
        # create vector of task assignments.
        # Each entry is a tuple (task, cpuAssigned) 
        assignaments = []
        for package in packages:
            packageId = package.getId()
            truckId = solution.getTruckIdAssignedToPackageId(packageId)
            truck = trucks[truckId]
            assignment = (package, truck, truck.getLoad())
            assignaments.append(assignment)

        # For best improvement policy it does not make sense to sort the tasks since all of them must be explored.
        # However, for first improvement, we can start by tasks in most loaded CPUs.
        if(self.policy == 'BestImprovement'): return(assignaments)
        
        # Sort package assignments by the load of the assigned Truck in descending order.
        #sortedAssignments = sorted(assignment, key=lambda assignment:assignment[3].getId(), reverse=True)
        return(assignaments)
    
    def exploreNeighborhood(self, solution):
        trucks = solution.getTrucks()
        
        curOptimization = solution.getMaxLoad()
        bestNeighbor = solution
        
        if(self.nhStrategy == 'Reassignment'):
            sortedAssignments = self.getPackagesAssignmentsSortedByAssignedTruckLoad(solution)
             
            for assignament in sortedAssignments:
                package = assignament[0]
                packageId = package.getId()
                
                curTruck = assignament[1]
                curTruckId = curTruck.getId()
                
                for truck in trucks:
                    newTruckId = truck.getId()
                    if(newTruckId == curTruckId): continue
                    
                    changes = []
                    changes.append(Change(packageId, curTruckId, newTruckId))
                    neighbor = self.evaluateNeighbor(solution, changes)
                    if (neighbor is None): continue
                    if (neighbor.getMaxLoad() < curOptimization):
                    	if(self.policy == 'FirstImprovement'):
                            return(neighbor)
                        else:
                        	bestNeighbor = neighbor
                        	curOptimization = neighbor.getMaxLoad()
                            
        elif(self.nhStrategy == 'Exchange'):
            
            sortedAssignments = self.getPackagesAssignmentsSortedByAssignedTruckLoad(solution)
            numAssignments = len(sortedAssignments)
            
            for i in xrange(0, numAssignments):             # i = 0..(numAssignments-1)
                packageAssignment1 = sortedAssignments[i]
                
                package1 = packageAssignment1[0]
                packageId1 = package1.getId()
                
                curTruck1 = packageAssignment1[1]
                curTruckId1 = curTruck1.getId()
                
                for j in xrange(numAssignments-1, -1, -1):  # j = (numAssignments-1)..0
                    if(i >= j): continue # avoid duplicate explorations and exchange with itself. 
                    
                    packageAssignment2 = sortedAssignments[j]
                    
                    package2 = packageAssignment2[0]
                    packageId2 = package2.getId()
                    
                    curTruck2 = packageAssignment2[1]
                    curTruckId2 = curTruck2.getId()

                    # avoid exploring pairs of tasks assigned to the same CPU
                    if(curTruck1 == curTruck2): continue
                    
                    changes = []
                    changes.append(Change(packageId1, curTruckId1, curTruckId2))
                    changes.append(Change(packageId2, curTruckId2, curTruckId1))
                    #print ("start")
                    #for change in changes:
                    #	print (change.packageId,change.curTruckId,change.newTruckId)
                    #print ("end")
                    neighbor = self.evaluateNeighbor(solution, changes)
                    if(neighbor is None): continue
                    #print(neighbor.getMaxLoad())
                    if(curOptimization > neighbor.getMaxLoad()):
                        if(self.policy == 'FirstImprovement'):
                            return(neighbor)
                        else:
                            bestNeighbor = neighbor
                            curOptimization = neighbor.getMaxLoad()
            
        else:
            raise Exception('Unsupported NeighborhoodStrategy(%s)' % self.nhStrategy)
        
        return(bestNeighbor)
    
    def run(self, solution):
        if(not self.enabled): return(solution)
        if(not solution.isFeasible()): return(solution)

        bestSolution = solution
        bestOptimization = bestSolution.getMaxLoad()
        
        startEvalTime = time.time()
        iterations = 0
        
        # keep iterating while improvements are found
        keepIterating = True
        while(keepIterating):
            keepIterating = False
            iterations += 1
            
            neighbor = self.exploreNeighborhood(bestSolution)
            curOptimization = neighbor.getMaxLoad()
            if(bestOptimization > curOptimization):
                bestSolution = neighbor
                bestOptimization = curOptimization
                keepIterating = True
        
        self.iterations += iterations
        self.elapsedTime += time.time() - startEvalTime
        
        return(bestSolution)
    
    def printPerformance(self):
        if(not self.enabled): return
        
        avg_evalTimePerIteration = 0.0
        if(self.iterations != 0):
            avg_evalTimePerIteration = 1000.0 * self.elapsedTime / float(self.iterations)
        
        print ''
        print 'Local Search Performance:'
        print '  Num. Iterations Eval.', self.iterations
        print '  Total Eval. Time     ', self.elapsedTime, 's'
        print '  Avg. Time / Iteration', avg_evalTimePerIteration, 'ms'
