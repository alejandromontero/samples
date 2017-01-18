/*********************************************
 * OPL 12.6.0.0 Model
 * Author: Alejandro
 * Creation Date: 07/06/2016 at 14:32:36
 *********************************************/
 
//Number of packages, trucks and ranges

 int nTrucks = ...;
 int nPackages = ...;

 range T = 1..nTrucks; 
 range P = 1..nPackages;
 
//Truck Data

 int xTruck = ...;
 int yTruck = ...;
 int wTruck = ...;
 
 range X = 1..xTruck;
 range Y = 1..yTruck;
 
 //Package Data
 
 int xDimp [p in P] = ...;
 int yDimp [p in P] = ...;
 int wp [p in P] = ...; 
 
 //Incompatibility matrix
 
 int incomp[p1 in P][p2 in P] = ...;
 
 //Decision variables
 
 dvar boolean pt[p in P][t in T]; 
 dvar boolean pxy[p in P][x in X][y in Y];
 dvar boolean pbl[p in P][x in X][y in Y];
 dvar boolean used[t in T];
 dvar int z;
 
 /*********************************************
  ***************Constraints*******************	
  ********************************************/ 
 
 /**********Minimization Function*************/
 
  //Minimize max number of used trucks and max used truck
 
 minimize z + sum(t in T)
 	used[t]*wTruck; 
 
 subject to {
  
 //Z is the max charged truck
 
 forall(t in T)
  z >= sum(p in P) pt[p][t] * wp[p];
  
  
 // All packets have to be used
 
 forall (p in P)
   sum (t in T) pt[p][t] == 1;
   
 // A packet cannot be placed in a non-used truck
 
 forall(t in T)
   forall (p in P)
     pt[p][t] <= used[t];
     
 // Two packets cannot be in the same truck if they are incompatible
 
 forall(t in T)
   forall(p1 in P)
     forall(p2 in P)
       pt[p1][t] + pt[p2][t] + incomp[p1][p2] <= 2; 
       
 // The weight of a truck cannot be exceeded
 
 forall (t in T)
   sum (p in P) wp[p] * pt[p][t] <= wTruck;
       
 /**********Packet position******************/
 
  // Each packet can only have one bottom left position placed in a truck.   
 
 //forall (p in P)
   //sum(x in X)
   //sum(y in Y)
     //pbl[p][x][y] == 1;
   
 // The Pbl of a packet cannot be set to 1 in those positions of the truck in which the packet does not fit.
 
 forall(p in P) {
   sum(x in X: x <= xTruck - xDimp[p] + 1)
   sum(y in Y: y <= yTruck - yDimp[p] + 1)
        pbl[p][x][y] == 1;
   
   sum(x in X: x > xTruck - xDimp[p] + 1)
   sum(y in Y: y > yTruck - yDimp[p] + 1)
        pbl[p][x][y] == 0;
   }        
     
 // pxy defines whereas a packet is placed in position x,y. Pxy has to be 1 in those positions occupied by packet
 // Depending on where the pbl of that packet is set. 


 forall (p in P)
   forall (x in X)
     forall (y in Y)
       sum (x2 in X: x - xDimp[p] < x2 <= x) 
       sum (y2 in Y: y - yDimp[p] < y2 <= y)
           pbl[p][x2][y2] == pxy[p][x][y];

   	
  // Two packets cannot be overlaped in the same truck
  
  forall (t in T)
    forall (x in X)
      forall (y in Y)
        forall (p1 in P)
          forall (p2 in P)
            if(p1 < p2)
            	pxy[p1][x][y] + pt[p1][t] + pxy[p2][x][y] + pt[p2][t] <= 3; 
        
}


// Post-processing block
execute {
        writeln("= = = = = = = = = = = = = =        Y     ");
        writeln("= = = Trucks' layouts = = =        ^     ");
        writeln("= = = = = = = = = = = = = =        |     ");
        writeln("                            Frame: 1 -> X")
        writeln("0 = empty cell");
        writeln("1 = occupied cell");
        writeln("2 = problem!");
        
        var s;
        for(var t in T) {
           writeln("Truck " + t);
           for(var y = yTruck; y > 0; y--) {
              for(var x in X) {
                 s = 0;
                 for(var p in P)
                    s += pt[p][t] * pxy[p][x][y];
                 write(s);
              }
              writeln();        
           }
        }
        //NEW TRUCK LAYOUT
        //  ---------
        // |1   4 3 3
        // |2 2 4
        //  ---------
        writeln("= = = = = = = = = = = = = =        Y     ");
        writeln("= = = Trucks' layouts = = =        ^     ");
        writeln("= = = = = = = = = = = = = =        |     ");
        writeln("                            Frame: 1 -> X")
        writeln("void = empty cell");
        writeln("nb q = occupied cell by a package p such as: q = p % 10");
        
        var p_in_txy;           // Boolean: whether package p is in truck t, cell (x,y)
        var counter_txy;        // Integer. Number of packages in truck t, cell (x,y). It should be 0 or 1.
        var weight_t;           // Integer. Weight of truck t
        
        for(var t in T) {
           // truck weight & display
           weight_t = 0;
           for(var p in P)
              weight_t += pt[p][t] * wp[p];
           writeln("Truck " + t + ". w = " + weight_t);
        
          // cosmetic
           for(var x in X)
              write(" -");
           writeln();
        
           for(var y = yTruck; y > 0; y--) {
              write("| ");
              for(var x in X) {
                 s = 0;
                 counter_txy = 0;
                 for(var p in P) {
                    p_in_txy = pt[p][t] * pxy[p][x][y];
                    s += (p % 10) * p_in_txy;
                    counter_txy += p_in_txy;
                         }      
                 if (counter_txy != 0)
                    write(s + " ");
                 else
                    write("  ");
              }
              write("|")
              writeln();        
           }
        
           // cosmetic
           for(var x in X)
              write(" -");
           writeln();   
        }
}