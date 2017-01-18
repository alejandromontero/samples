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
source("/home/alejandro/Documentos/University/SMDE/Assignament2/Park-Miller.R")

######################################
# Generate Data
######################################

dimnames = list(seq(1:100),
                  c("F1","F2","F3","F4","F5","F6","F7","F8","F9","F10"))

dat <-matrix(nrow=100, ncol=10, dimnames=dimnames)
dat[,1]<-rnorm(100,mean=0,sd=1)
dat[,2]<-rnorm(100, mean=10,sd=5)
dat[,3]<-rexp(100, rate=3)
dat[,4]<-rexp(100, rate=10)
dat[,5]<-runif(100, min=10, max=20)
dat[,6]<-dat[,1] + 5*dat[,3]
dat[,7]<-dat[,5] + 2*dat[,3]
dat[,8]<-dat[,5] + dat[,2]
dat[,9]<-dat[,4] + 3*dat[,2]
dat[,10]<-dat[,1] + dat[,5]

#----------Answer_variable---------------------
answer_var = dat[,1] + 2*dat[,3] - 5*dat[,6] - 4*dat[,4] + dat[,8] - 2*dat[,10] + rnorm(100,5,2)
dat_ans = cbind(dat,answer_var)


######################################
# PCA
######################################

res<-PCA(dat_ans , scale.unit=TRUE, ncp=5, graph = FALSE)

res.hcpc<-HCPC(res ,nb.clust=-1,consol=FALSE,min=3,max=10,graph=TRUE)

plot.PCA(res, axes=c(1, 2), choix="ind", habillage="none", col.ind="black", 
         col.ind.sup="blue", col.quali="magenta", label=c("ind", "ind.sup", "quali"),
         new.plot=TRUE)

plot.PCA(res, axes=c(1, 2), choix="var", new.plot=TRUE, col.var="black", 
         col.quanti.sup="blue", label=c("var", "quanti.sup"), lim.cos2.var=0)

######################################
# Regression model
######################################

dat_frame <- data.frame(dat_ans)
RegModel <- 
  lm(answer_var~F1+F2+F3+F4+F5+F6+F7+F8+F9+F10,
     data=dat_frame)

summary(RegModel)

#-------------------------------------
# SubModel 
#-------------------------------------

RegModel.2 <- 
  lm(answer_var~F1+F2+F3+F5,
     data=dat_frame)

summary(RegModel.2)


#-------------------------------------
# Model assumptions 
#-------------------------------------


#-------------------------------------
# Prediction
#-------------------------------------

prediction <- predict(object=RegModel.2,newdata = dat_frame,interval="prediction")

plotCI(prediction[,1],li=prediction[,2],ui=prediction[,3],pt.bg="green",ylab="prediction"
       ,xlab="observations",pch=21, main="Predicted answer variable")
points(dat_frame$answer_var, col="red")


######################################
# DOE
######################################

par(mfrow=c(2,2))

boxplot(dat[,1], col="red")
boxplot(dat[,2], col="green")
boxplot(dat[,3], col="orange")
boxplot(dat[,5], col="blue")

summary(dat[,1])
summary(dat[,2])
summary(dat[,3])
summary(dat[,5])

#-------------------------------------
# Prediction
#-------------------------------------

dimnames = list(seq(1:16),
                c("F1","F2","F3","F5"))

dat_predict <-matrix(nrow=16, ncol=4, dimnames=dimnames)

dat_predict[,1]<- c(-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,0.67,0.67,0.67,0.67,0.67,0.67,0.67,0.67)
dat_predict[,2]<- c(7.66,7.66,7.66,7.66,13.11,13.11,13.11,13.11,7.66,7.66,7.66,7.66,13.11,13.11,13.11,13.11)
dat_predict[,3]<- c(0.09,0.09,0.6,0.6,0.09,0.09,0.6,0.6,0.09,0.09,0.6,0.6,0.09,0.09,0.6,0.6)
dat_predict[,4]<- c(12.2,16.9,12.2,16.9,12.2,16.9,12.2,16.9,12.2,16.9,12.2,16.9,12.2,16.9,12.2,16.9)
  
prediction <- predict(object=RegModel.2, newdata = data.frame(dat_predict),interval="prediction")
