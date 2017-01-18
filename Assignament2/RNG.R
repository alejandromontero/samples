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
source("/home/alejandro/Documentos/University/SMDE/Assignament2/Park-Miller.R") #Change the path to suit your environment

######################################
# Generate Stream
######################################

strA <- parkMiller(5000,16807,0,48271)

#-------------------------------------
# Generate Substreams
#-------------------------------------

strA_B = strA[1:1500]
strA_C = strA[1500:3000]
strA_D = strA[3000:5000]

#-------------------------------------
# Sort Substreams
#-------------------------------------

#----------strA_B---------------------
srtA_B_tmp = strA_B[1000:1500]
srtA_B_tmp2 = strA_B[1:1000]
strA_B = c(srtA_B_tmp, srtA_B_tmp2)

#----------strA_C---------------------
srtA_C_tmp = strA_C[1000:1500]
srtA_C_tmp2 = strA_C[1:1000]
strA_C = c(srtA_C_tmp, srtA_C_tmp2)

#----------strA_D---------------------
srtA_D_tmp = strA_D[1000:2000]
srtA_D_tmp2 = strA_D[1:1000]
strA_D = c(srtA_D_tmp, srtA_D_tmp2)

######################################
# Chi-Square Test
######################################

#----------X2 formula--------------------
# X2= k/n* sum(f-n/k)2

intervals <- seq(from = 0, to = 1, by = 0.01)
strA_int <- cut(x = strA, breaks = intervals, right = TRUE, labels = FALSE)
hist(strA_int)

theor_val = length(strA)/(length(intervals) - 1)
theor_val_int = rep(theor_val,length(strA_int))
tbl <- table(strA_int, theor_val_int)
chisq.test(tbl)

#-------------------------------------
# Chi-Square Test Substreams
#-------------------------------------

#----------strA_B---------------------
jpeg('home/alejandro/Documentos/Shared/plot.jpg')

intervals <- seq(from = 0, to = 1, by = 0.01)
strA_B_int <- cut(x = strA_B, breaks = intervals, right = TRUE, labels = FALSE)
hist(strA_B_int)

dev.off()

theor_val = length(strA_B)/(length(intervals) - 1)
theor_val_int = rep(theor_val,length(strA_B_int))
tbl <- table(strA_B_int, theor_val_int)
chisq.test(tbl)

#----------strA_C---------------------

intervals <- seq(from = 0, to = 1, by = 0.01)
strA_C_int <- cut(x = strA_C, breaks = intervals, right = TRUE, labels = FALSE)
hist(strA_C_int)

theor_val = length(strA_C)/(length(intervals) - 1)
theor_val_int = rep(theor_val,length(strA_C_int))
tbl <- table(strA_C_int, theor_val_int)
chisq.test(tbl)

#----------strA_D---------------------

intervals <- seq(from = 0, to = 1, by = 0.01)
strA_D_int <- cut(x = strA_D, breaks = intervals, right = TRUE, labels = FALSE)
hist(strA_D_int)

theor_val = length(strA_D)/(length(intervals) - 1)
theor_val_int = rep(theor_val,length(strA_D_int))
tbl <- table(strA_D_int, theor_val_int)
chisq.test(tbl)

