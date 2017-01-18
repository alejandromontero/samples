from Package import Package
from Truck import Truck

class Problem(object):
    def __init__(self, inputData):
        self.inputData = inputData
        
        nPackages = self.inputData.nPackages
        nTrucks = self.inputData.nTrucks

        xTruck = self.inputData.xTruck
        yTruck = self.inputData.yTruck
        self.wTruck = self.inputData.wTruck

        xDimp = self.inputData.xDimp
        yDimp = self.inputData.yDimp
        wp = self.inputData.wp

        self.incomp = self.inputData.incomp

        self.packages = []                             # vector with nPackages
        for pId in xrange(0, nPackages):               # pId = 0..(nPackages-1)
            pacakge = Package(pId,xDimp[pId],yDimp[pId],wp[pId])
            self.packages.append(pacakge)

        self.trucks = []
        for tId in xrange(0, nTrucks):
        	truck = Truck(tId, xTruck, yTruck, self.wTruck)
        	self.trucks.append(truck)

    def getPacakges(self):
        return(self.packages)

    def getTrucks(self):
        return(self.trucks)
    #Be sure to modify this packet before delivery!!!!!!!!!!!
