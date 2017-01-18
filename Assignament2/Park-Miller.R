######################################
# Random Number Generator 
######################################

parkMiller <- function(k,mul,increm,seed){
  X2=0
  X1=seed
  
  out = 0
  sortOut = c()
  
  for (i in 1:k) {
    X2 = (mul*X1 + increm) %% (2147483647)
    out = X2 / (2147483647)
    sortOut <- c(sortOut,out)
    X1 = X2
  }
  return(sortOut)
}