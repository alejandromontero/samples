'''
AMMM Lab Heuristics v1.0
Instance file validator.
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

# Validate instance attributes read from a DAT file.
# It validates the structure of the parameters read from the DAT file.
# It does not validate that the instance is feasible or not.
# Use Problem.checkInstance() function to validate the feasibility of the instance.
import json 

class ValidateInputData(object):
    @staticmethod
    def validate(data):
    	print (data)
        # Validate that all input parameters were found
        
        #for paramName in ['nPackages', 'nTrucks', 'xTruck', 'yTruck', 'wTruck', 'xDimp', 'yDimp', 'wp','incomp']:
            #if(not data.__dict__.has_key(paramName)):
                #raise Exception('Parameter/Set(%s) not contained in Input Data' % str(paramName))

        # Validate nPackages
        nPackages = data.nPackages
        if(not isinstance(nPackages, (int, long)) or (nPackages <= 0)):
            raise Exception('nPackages(%s) has to be a positive integer value.' % str(nPackages))
        
        # Validate nTrucks
        nTrucks = data.nTrucks
        if(not isinstance(nTrucks, (int, long)) or (nTrucks <= 0)):
            raise Exception('nTrucks(%s) has to be a positive integer value.' % str(nTrucks))
        
        # Validate xTruck
        xTruck = data.xTruck
        if(not isinstance(xTruck, (int, long)) or (xTruck <= 0)):
            raise Exception('nCPUs(%s) has to be a positive integer value.' % str(xTruck))
        
        # Validate yTruck
        yTruck = data.yTruck
        if(not isinstance(yTruck, (int, long)) or (yTruck <= 0)):
            raise Exception('nCores(%s) has to be a positive integer value.' % str(yTruck))

        # Validate wTruck
        wTruck = data.wTruck
        if(not isinstance(yTruck, (int, long)) or (wTruck <= 0)):
            raise Exception('wTruck(%s) has to be a positive integer value.' % str(wTruck))
        
        # Validate xDimp
        xDimp = data.xDimp
        if(len(xDimp) != nPackages):
            raise Exception('Size of xDimp(%d) does not match with value of nPackages(%d).' % (len(xDimp), nPackages))

        # Validate yDimp
        yDimp = data.yDimp
        if(len(yDimp) != nPackages):
            raise Exception('Size of yDimp(%d) does not match with value of nPackages(%d).' % (len(yDimp), nPackages))

        # Validate yDimp
        wp = data.wp
        if(len(wp) != nPackages):
            raise Exception('Size of wp(%d) does not match with value of nPackages(%d).' % (len(wp), nPackages))   

        # Validate incomp first dimension
        incomp = data.incomp
        if(len(incomp) != nPackages):
            raise Exception('Size of first dimension of incomp(%d) does not match with value of nPackages(%d).' % (len(incomp), nPackages))

        # Validate incomp second dimension
        for entry in incomp:
        	if (len(entry) != nPackages):
        		raise Exception('Size of second dimension of incomp(%d) does not match with value of nPackages(%d).' % (len(entry), nPackages))