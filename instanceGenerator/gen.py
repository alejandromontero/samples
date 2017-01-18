
import sys
import os
import numpy
from random import randint
import random

def saveInstance(problem):

	dataInstance = len(os.listdir("/home/alejandro/Documentos/University/AMMM/project/heuristics/data"))	
	dataFile = open ("/home/alejandro/Documentos/University/AMMM/project/heuristics/data/instance" + str(dataInstance), 'w')
    
	dataFile.write("nTrucks=" + str(problem["nTrucks"]) + ";" + "\n")
        dataFile.write("nPackages=" + str(problem["nPackages"]) + ";" + "\n")
        dataFile.write("xTruck=" + str(problem["xTruck"]) + ";" + "\n")
        dataFile.write("yTruck=" + str(problem["yTruck"]) + ";" + "\n")
        dataFile.write("wTruck=" + str(problem["wTruck"]) + ";" + "\n")

        dataFile.write("xDimp=[")
        for x in xrange(0,len(problem["xDimp"])):
        	dataFile.write(str(problem["xDimp"][x]) + " ")
        dataFile.write("];\n")

        dataFile.write("yDimp=[")
        for x in xrange(0,len(problem["yDimp"])):
        	dataFile.write(str(problem["yDimp"][x]) + " ")
        dataFile.write("];\n")

        dataFile.write("wp=[")
        for x in xrange(len(problem["wp"])):
        	dataFile.write(str(problem["wp"][x]) + " ")
        dataFile.write("];\n")

        dataFile.write("incomp=" + str(problem["incomp"]) + ";" + "\n")

	dataFile.flush()
        dataFile.close()
	
	print ("Done")

def check(problem,difficulty):
	
	#check space
	
	totalSpaceX = problem["xTruck"] * problem["nTrucks"]
	totalSpaceY = problem["yTruck"] * problem["nTrucks"]
	areaTrucks = problem["xTruck"] * problem["yTruck"] * problem["nTrucks"]
	totalWeight = problem["wTruck"] *  problem["nTrucks"]
	areaPackages = 0
	packageSpaceX = 0
	packageSpaceY = 0


	for p in range (0, problem["nPackages"]):
		packageSpaceX += problem["xDimp"][p]
		packageSpaceY += problem["yDimp"][p]
		areaPackages += problem["xDimp"][p] * problem["yDimp"][p]

		#spaceOccupiedX = (float(packageSpaceX)/float(totalSpaceX))
		#spaceOccupiedY = (float(packageSpaceY)/float(totalSpaceY))
	areaOccupied = float(areaPackages)/float(areaTrucks)
	print (areaOccupied)

	if (areaOccupied >= difficulty and areaOccupied <= 1.0):
		print ("Area Occcupied: " + str(areaOccupied))

		print ("space: ", str(packageSpaceX), str(packageSpaceY))
		print ("truck: ", str(totalSpaceX*2.0),str(totalSpaceY*2.0) )
		# print (spaceOccupiedX, spaceOccupiedY)

		packageWeight = 0

		for p in range (0, problem["nPackages"]):
               		packageWeight = packageWeight + problem["wp"][p]

		if (packageWeight <= totalWeight):
			saveInstance(problem)
			print (areaOccupied * areaTrucks * areaPackages)
			return True
	return False


def random(difficulty):
	
	numpy.set_printoptions(threshold=numpy.nan)
	problem = {}
	problem["nTrucks"] = 22
	problem["nPackages"] = randint(problem["nTrucks"]*3, problem["nTrucks"]*5)
	#problem["nPackages"] = 50
	problem["xTruck"] = 10	
	problem["yTruck"] = 10
	problem["wTruck"] = 30
	
	problem["xDimp"] = []
	problem["yDimp"] = []
	problem["wp"] = []
	#print ("nPackages: ",str(problem["nPackages"]))
	#print ("nTrucks: ",str(problem["nTrucks"]))		
	for p in range(0,problem["nPackages"]):
		#problem["xDimp"].append(7)
		#problem["yDimp"].append(3)
		problem["xDimp"].append(randint(3,6))
		problem["yDimp"].append(randint(3,6))
		problem["wp"].append(randint(2,4))	

	problem["incomp"] = numpy.zeros((problem["nPackages"],problem["nPackages"]),dtype=numpy.int)
	for x in range (0,3):
		problem["incomp"][randint(0,problem["nPackages"]) - 1][randint(0,problem["nPackages"]) - 1] = 1

	if (check(problem,difficulty) == True): return True
	else: return False	 


if (len(sys.argv) > 1):
	difficulty = float(sys.argv[1])
else: 
	difficulty = 0.5

boolean = False
while not boolean: 
	boolean = random(difficulty)


