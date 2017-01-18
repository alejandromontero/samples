'''
AMMM Lab Heuristics v1.0
Representation of a CPU.
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

