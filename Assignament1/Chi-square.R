# Remove previous objects
rm(list=ls(all=TRUE))

#import the data from a csv.
tblExcel <-  read.table("/home/alejandro/Documentos/University/SMDE/Assignament1/2/NormalDistro.csv",   header=TRUE, sep=";",dec=",")
summary(tblExcel)

#generation of a distribution.
tblR=rnorm(200, mean=0, sd=1)
tblR_1=rnorm(200, mean=10, sd=15)
tblR_2=rexp(200, rate=1)
tblR_3=rexp(200, rate=1.5)
tblR_5=rexp(200, rate=15)
tblR_4=rnorm(200,mean=0,sd=2)


#Work with the data as a dataframe.
tblR_v1=data.frame(x1=tblR)
tblR1_v1=data.frame(x1=tblR_1)
tblR2_v1=data.frame(x1=tblR_2)
tblR3_v1=data.frame(x1=tblR_3)
tblR4_v1=data.frame(x1=tblR_4)
tblR5_v1=data.frame(x1=tblR_5)



#Definition of the intervals, categories to be used. R original sample
tblR_v1_cat=transform(tblR_v1, cat = ifelse(x1 < -1,"-1",
                                              ifelse(x1 < -0.5,"-0.5",
                                                     ifelse(x1 < 0,"0",
                                                            ifelse(x1 < 0.5,"0.5",
                                                                   ifelse(x1 <1,"1","Inf"))))))

#R sample 2
tblR1_v1_cat=transform(tblR1_v1, cat = ifelse(x1 < -30,"-1",
                                            ifelse(x1 < -15,"-0.5",
                                                   ifelse(x1 < 0,"0",
                                                          ifelse(x1 < 15,"0.5",
                                                                 ifelse(x1 < 30,"1","Inf"))))))

#R sample 3
tblR2_v1_cat=transform(tblR2_v1, cat = ifelse(x1 < 0.1,"0.2",
                                              ifelse(x1 < 0.6,"0.4",
                                                     ifelse(x1 < 1.1,"0.6",
                                                            ifelse(x1 < 1.6,"0.8",
                                                                   ifelse(x1 < 2.1,"1","Inf"))))))

#R sample 4
tblR3_v1_cat=transform(tblR3_v1, cat = ifelse(x1 < 0.1,"0.2",
                                              ifelse(x1 < 0.4,"0.4",
                                                     ifelse(x1 < 0.7,"0.6",
                                                            ifelse(x1 < 1,"0.8",
                                                                   ifelse(x1 < 1.3,"1","Inf"))))))

#R sample 5
tblR4_v1_cat=transform(tblR4_v1, cat = ifelse(x1 < -1,"-1",
                                            ifelse(x1 < -0.5,"-0.5",
                                                   ifelse(x1 < 0,"0",
                                                          ifelse(x1 < 0.5,"0.5",
                                                                 ifelse(x1 <1,"1","Inf"))))))

#R sample 6
tblR5_v1_cat=transform(tblR5_v1, cat = ifelse(x1 < 0.1,"0.2",
                                              ifelse(x1 < 0.6,"0.4",
                                                     ifelse(x1 < 1.1,"0.6",
                                                            ifelse(x1 < 1.6,"0.8",
                                                                   ifelse(x1 < 2.1,"1","Inf"))))))


tblExcel_cat=transform(tblExcel, cat = ifelse(VALUES < -1,"-1",
                                            ifelse(VALUES < -0.5,"-0.5",
                                                   ifelse(VALUES < 0,"0",
                                                          ifelse(VALUES < 0.5,"0.5",
                                                                 ifelse(VALUES <1,"1","Inf"))))))


#Counting the amount of elements in each category “table” function.
tbl_freq_R=as.data.frame(with(tblR_v1_cat, table(cat)))
tbl_freq_R1=as.data.frame(with(tblR1_v1_cat, table(cat)))
tbl_freq_R2=as.data.frame(with(tblR2_v1_cat, table(cat)))
tbl_freq_R3=as.data.frame(with(tblR3_v1_cat, table(cat)))
tbl_freq_R4=as.data.frame(with(tblR4_v1_cat, table(cat)))
tbl_freq_R5=as.data.frame(with(tblR5_v1_cat, table(cat)))


tbl_freq_Excel=as.data.frame(with(tblExcel_cat, table(cat)))

tbl <- cbind(tbl_freq_R[,"Freq"],tbl_freq_Excel[,"Freq"])
tbl2 <- cbind(tbl_freq_R[,"Freq"],tbl_freq_R1[,"Freq"])
tbl3 <- cbind(tbl_freq_R[,"Freq"],tbl_freq_R2[,"Freq"])
tbl4 <- cbind(tbl_freq_R2[,"Freq"],tbl_freq_R3[,"Freq"])
tbl5 <- cbind(tbl_freq_R[,"Freq"],tbl_freq_R4[,"Freq"])
tbl6 <- cbind(tbl_freq_R2[,"Freq"],tbl_freq_R5[,"Freq"])


chisq.test(tbl, correct=FALSE)
chisq.test(tbl2, correct=FALSE)
chisq.test(tbl3, correct=FALSE)
chisq.test(tbl4, correct=FALSE)
chisq.test(tbl5, correct=FALSE)
chisq.test(tbl6, correct=FALSE)


######################################
# Print histograms for both distributions
######################################

with(tblExcel, Hist(tblExcel))
with(tblR_v1, Hist(tblR_v1))
with(tblR1_v1, Hist(tblR1_v1))
with(tblR2_v1, Hist(tblR2_v1))
with(tblR3_v1, Hist(tblR3_v1))
with(tblR4_v1, Hist(tblR4_v1))
with(tblR5_v1, Hist(tblR5_v1))




