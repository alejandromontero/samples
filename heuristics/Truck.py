import numpy as np
class Truck(object):
    def __init__(self, truckId, xTruck, yTruck, wTruck):
        self.truckId = truckId
        self.availableSpace = wTruck
        self.load = 0
        self.truckLayout = np.full((xTruck,yTruck), -1, dtype=int) #Store a layout of the truck. Each position stores the ID of the packet occupying that space

    def getId(self):
        return(self.truckId)

    def getLoad(self):
    	return(self.load)

    def getAvailableSpace(self):
    	return(self.availableSpace)

    def getTruckLayout(self):
    	return(self.truckLayout)

    def setWeight(self,packageWeight):
    	self.load = self.load + packageWeight
    	self.availableSpace = self.availableSpace - packageWeight

    def setLayout(self, newLayout):
    	self.truckLayout = newLayout

