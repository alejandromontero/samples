'''
AMMM Lab Heuristics v1.0
Representation of a Task.
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

class Package(object):
    def __init__(self, packageId,xDim,yDim,w):
        self.packageId = packageId
        self.xDim = xDim
        self.yDim = yDim
        self.w = w      
        self.pbl = [0,0]  
    
    def getId(self):
        return(self.packageId)
    
    def getxDim(self):
        return(self.xDim)
   
    def getyDim(self):
        return(self.yDim)

    def getWeight(self):
        return(self.w)

    def getPbl(self):
        return(self.pbl)

    def setPbl(self,pbl):
        self.pbl = pbl

