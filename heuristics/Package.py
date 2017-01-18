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

