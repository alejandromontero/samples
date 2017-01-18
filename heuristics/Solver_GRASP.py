import random, time
from Solver import Solver
from Solution import Solution
from LocalSearch import LocalSearch

# Inherits from a parent abstract solver.
class Solver_GRASP(Solver):
    def selectCandidate(self, config, candidateList):
        if(len(candidateList) == 0): return(None)
        
        # sort candidate assignments by highestLoad in ascending order
        sortedCL = sorted(candidateList, key=lambda candidate: candidate.maxLoad, reverse=False)
        
        # compute boundary highest load as a function of the minimum and maximum highest loads and the alpha parameter
        alpha = config.alpha
        minMaxLoad = sortedCL[0].maxLoad
        maxMaxLoad = sortedCL[len(sortedCL)-1].maxLoad
        boundaryMaxLoad = minMaxLoad + (maxMaxLoad - minMaxLoad) * alpha
        
        # find elements that fall into the RCL (those fulfilling: highestLoad < boundaryHighestLoad)
        maxIndex = 0
        for x in sortedCL:
            if(x.maxLoad > boundaryMaxLoad): break
            maxIndex += 1

        # create RCL and pick an element randomly
        rcl = sortedCL[0:maxIndex]          # pick first maxIndex elements starting from element 0
        if(len(rcl) == 0): return(None)
        return(random.choice(rcl))          # pick an element from rcl at random
    
    def greedyRandomizedConstruction(self, config, problem):
        # get an empty solution for the problem
        solution = Solution.createEmptySolution(config, problem)

        iteration_elapsedEvalTime = 0
        iteration_evaluatedCandidates = 0
        
        # get packages and sort them by their total occupied space in ascending order
        packages = problem.getPacakges()
        sortedPackages = sorted(packages, key=lambda package: package.getxDim()*package.getyDim(), reverse=True)
        
        # for each packet taken in sorted order
        for package in sortedPackages:
            packageId = package.getId()
            
            # compute feasible assignments
            candidateList, elapsedEvalTime, evaluatedCandidates = solution.findFeasibleAssignments(packageId)
            iteration_elapsedEvalTime += elapsedEvalTime
            iteration_evaluatedCandidates += evaluatedCandidates
            
            # no candidate assignments => no feasible assignment found
            if(len(candidateList) == 0):
                solution.makeInfeasible()
                break

            # select an assignment
            candidate = self.selectCandidate(config, candidateList)
            if(candidate is None): break

            # assign the current task to the CPU that resulted in a minimum highest load
            solution.assign(candidate.packageId, candidate.truckId)
            #print(candidate.packageId,candidate.truckId,solution.getMaxLoad())
          
        #print ("done")
        return(solution, iteration_elapsedEvalTime, iteration_evaluatedCandidates)
    
    def solve(self, config, problem):
        bestSolution = Solution.createEmptySolution(config, problem)
        bestSolution.makeInfeasible() 
        bestOptimization = bestSolution.getMaxLoad()
        self.startTimeMeasure()
        self.writeLogLine(bestOptimization, 0)
        
        total_elapsedEvalTime = 0
        total_evaluatedCandidates = 0
        
        localSearch = LocalSearch(config)
        
        iteration = 0
        while(time.time() - self.startTime < config.maxExecTime): #Or set number of iterations
            iteration += 1
            
            # force first iteration as a Greedy execution (alpha == 0)
            originalAlpha = config.alpha 
            #if(iteration == 1): config.alpha = 1
            
            solution, it_elapsedEvalTime, it_evaluatedCandidates = self.greedyRandomizedConstruction(config, problem)
            total_elapsedEvalTime += it_elapsedEvalTime
            total_evaluatedCandidates += it_evaluatedCandidates
           
            # recover original alpha
            if(iteration == 1): config.alpha = originalAlpha
            
            if(not solution.isFeasible()): 
            	continue
            
            if (config.localSearch): solution = localSearch.run(solution)
            solutionOptimization = solution.getMaxLoad()
            #print (solutionOptimization)
            if(solutionOptimization < bestOptimization):  # Need to add the used trucks
                bestSolution = solution
                bestOptimization = solutionOptimization
                self.writeLogLine(bestOptimization, iteration)
            
        self.writeLogLine(bestOptimization, iteration)
        
        avg_evalTimePerCandidate = 0.0
        if(total_evaluatedCandidates != 0):
            avg_evalTimePerCandidate = 1000.0 * total_elapsedEvalTime / float(total_evaluatedCandidates)
        
        print ''
        print 'GRASP Candidate Evaluation Performance:'
        print '  Num. Candidates Eval.', total_evaluatedCandidates
        print '  Total Eval. Time     ', total_elapsedEvalTime, 's'
        print '  Avg. Time / Candidate', avg_evalTimePerCandidate, 'ms'

        
        localSearch.printPerformance()
        
        return(bestSolution)
