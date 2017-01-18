###########################################################
# Clear all environment variables
###########################################################

# Remove previous objects
rm(list=ls(all=TRUE))

######################################
# Load libraries 
######################################

library(MASS)
library(plotrix)
library(lmtest)
library(nortest)
library(Rcmdr)
library(FactoMineR)

######################################
# Load data 
######################################

data(decathlon, package="FactoMineR")
decHalf=decathlon[1:20,]
decHalf2=decathlon[20:41,]
names(decHalf) <- make.names(names(decHalf))
names(decHalf2) <- make.names(names(decHalf2))

View(decHalf)

######################################
# Generate Model
######################################

RegModel.2 <- 
  lm(X1500m~Discus+High.jump+Javeline+Long.jump+Points+Pole.vault+Rank+Shot.put+X100m+X110m.hurdle+X400m,
     data=decHalf)

summary(RegModel.2)
mean(decHalf$X1500m)

#-------------------------------------
# SubModel 
#-------------------------------------

RegModel2.2 <- 
  lm(X1500m~Discus+High.jump+Javeline+Long.jump+Points+Pole.vault+Shot.put+X100m+X110m.hurdle+X400m,
     data=decHalf)

summary(RegModel2.2)

######################################
# Test Model Assumptions
######################################

#-------------------------------------
# Normality tests
#-------------------------------------

shapiro.test(residuals(RegModel2.2))

#-------------------------------------
# Independency tests
#-------------------------------------

dwtest(RegModel2.2)

#-------------------------------------
# Homogeneity of variance tests
#-------------------------------------

bptest(RegModel2.2)


######################################
# Model Predictions
######################################

pred <- predict(RegModel2.2,newdata = decHalf2,interval="prediction")
pred

#-------------------------------------
# Plot the results
#-------------------------------------

jpeg('plot.jpg')
plotCI(pred[,1],li=pred[,2],ui=pred[,3],pt.bg="green",ylab="Prediction", xlab="Observations",pch=21, main="Predicted X1500m")
points(decHalf2$X1500m,col="red")
dev.off()
