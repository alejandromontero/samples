###########################################################
# Clear all environment variables
###########################################################

# Remove previous objects
rm(list=ls(all=TRUE))

######################################
# Load libraries 
######################################

library(MASS)
library(lmtest)
library(nortest)
library(Rcmdr)
library(FactoMineR)

######################################
# Generate three samples of exponential distribution
######################################

nexp1=data.frame(x1=rexp(200, rate=10),x2="exp1")
nexp2=data.frame(x1=rexp(200, rate=20),x2="exp2")
nexp3=data.frame(x1=rexp(200, rate=30),x2="exp3")
tbl1=(nexp1)
tbl2=(nexp2)
tbl3=(nexp3)

######################################
# Merge Distributions
######################################

tbl=mergeRows(nexp1,nexp2,common.only = FALSE)
tbl=mergeRows(as.data.frame(tbl),nexp3,common.only = FALSE)

######################################
# Distribution summary 
######################################

summary(tbl)

######################################
# Initial analisis
######################################

jpeg('histos.jpg')
with(tbl, Hist(x1,groups=x2,scale="frequency", 
               breaks="Sturges",col="darkgrey"))
dev.off()

jpeg('boxplot.jpg')
Boxplot(x1~x2, data=tbl, id.method="y",col=(c("red","blue","yellow")))
dev.off()

######################################
# One-way Anova
######################################

AnovaModel.1 <- aov(x1~x2, data=tbl)
summary(AnovaModel.1)
with(tbl, numSummary(x1, groups=x2, statistics=c("mean", "sd")))

#-------------------------------------
# Normality tests
#-------------------------------------

shapiro.test(tbl1$x1)
shapiro.test(tbl2$x1)
shapiro.test(tbl3$x1)

#-------------------------------------
# Independency tests
#-------------------------------------

dwtest(AnovaModel.1)

#-------------------------------------
# Homogeneity of variance tests
#-------------------------------------

bptest(AnovaModel.1)

