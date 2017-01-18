package domain;

import java.util.Vector;

/**
 * Clase BeamSearch
 * Esta clase hereda de la classe Optimize. Ejecuta el algorismo de BeamSearch
 * con el operador 2-opt. Busca todas las posiblidades aplicando el operador 
 * 2-opt sobre la solución inicial y obtiene la mejor, y luego repite el proceso.
 * El operador 2-Opt se ha integrado dentro de esta misma clase.
 * 
 * @author Gerard Alonso
 * @version 1.3
 * @date 16/12/13
 */
public class BeamSearch extends Optimize {
    
    /* El tamaño maximo del contenedor bestSolutions. */
    private int k;
    
    /* La solución actual*/
    private SolutionExt solActual;
    
    /* El valor de la peor solución de bks*/
    private int worstBKSvalue;
    
    /* La posición del peor elemento de bks*/
    private int pworst;
    
    /* El contenedor de soluciones, para guardar todas les soluciones generades con el 2opt*/
    private Vector<SolutionExt> ss;
    
    /* El contenedor de las K mejores soluciones generadas en todo el algoritmo */
    private final Vector<SolutionExt> bks;
   
    
    /**
    * Constructora de la classe BeamSearch con el parametro rs.
    * Inicialitza los datos. Por defecto como maximo hara 100 iteracions.
    * @param rs Una instancia de RelationSet con todas las relaciones.
    */
    public BeamSearch(RelationSetExt rs) {
        super(rs);
        ss = new Vector<>();
        bks = new Vector<>(k);
    }
    
    /**
    * Método redefinido que inicia el proceso de optimitzacion.
    * @param s Una solución inicial.
    * @return La solución optimitzada.
    */
    @Override
    public Vector<Integer> optimize(Vector<Integer> s) {
        if(s.size()>20) k = s.size() / 2;
        else k = s.size();
        //incialitzem l'estat inicial
        solActual = new SolutionExt(s,calculateSolutionValue(s));
        //inicialitzem el pitjor valor com l'actual
        worstBKSvalue = solActual.getValue();
        pworst=0; 
        
        //solució auxiliar per guardar la millor sol.
        SolutionExt bs; 
        //boolea per controlar la sortida del while
        boolean end = false; 
        
        while(end == false)
        {
            generateAllSolutions();//genera solucions succesores i les guarda a solutionSpace
            if(!updateKBestSolutions()) end = true; //actualitza les K solucions millors. Sino actualitza cap retorna false.
            if(!end) { //si no hem d'acabar
                bs = getBestSolution(); //busquem la millor solucio dintre de bks
                if(bs.getValue() >= solActual.getValue()){
                    end = true;
                } //si son igual de bones acabem(no improvement)
                else{
                    solActual = bs;
                }
            }
        }
        return solActual.getSolution();
    }
   
    /**
    * Método que obtiene la mejor solución del vector de k soluciones y la 
    * guarda como solución actual.
    * @return Devuelve la mejor solución.
    */
    private SolutionExt getBestSolution() {
        SolutionExt bestSolution = solActual;
        for(int i=0;i<bks.size();i++){
            SolutionExt sol = bks.elementAt(i);
            if(sol.getValue() < bestSolution.getValue()){ 
                bestSolution = sol;
            }
        }
        return bestSolution;
    }
    
    /**
    * Método que inicia la generación de soluciones con el operador 2-opt.
    * Guarda el resultado obtenido en una variable privada(ss) que contendrá un 
    * conjunto con todas las possibles soluciones generadas con el 
    * 2-opt.
    */
    private void generateAllSolutions() {
        ss = generateAllSolutions2Opt();
    }

    /**
    * Métode que actualitza las K mejores soluciones comparandolas con el 
    * espacio de soluciones generado.
    * @return Un boolean especificando si se actualitzado o no.
    */
    private boolean updateKBestSolutions() {
        boolean updated = false;
        for(int i=0;i<ss.size();i++){ 
            SolutionExt saux = ss.elementAt(i);
            saux.setValue(calculateSolutionValue(saux.getSolution()));
            if(!bks.contains(saux)){
                //si l'espai de k millors solucions no esta ple
                if(bks.size()<k) {
                    insert(saux);
                }
                else{
                    //Si es una solucio millor que la pitjor de dintre
                    if(saux.getValue()<worstBKSvalue) insert(saux);  
               }
               updated=true;
            }
      }
      return updated;
    }
     
    /**
    * Métode que inserta la solucion al vector de k soluciones.
    * @param s La solución a añadir.
    */
    private void insert(SolutionExt solution){
        if(bks.isEmpty()){
          //afegim solució
          bks.add(solution);
          //actualitzem el pitjor valor de solució
          worstBKSvalue = solution.getValue();
          pworst = 0;
        }else{
            //treiem la pitjor solució si bks es igual a k
            if(bks.size() == k){
                bks.removeElementAt(pworst); //eliminem pitjor sol
            }
            if(!estaRepetit(solution,bks)) bks.add(solution); //afegim la sol a bks
        }
        updatePworst(); //actualitzem la posició del pitjor element
    }

    /**
    * Método que actualiza los valores del peor elemento de 
    * bks(pworst,wortstBKSvalue). Modifica las variables pworst y worstBKSvalue.
    */
    private void updatePworst() {
        pworst = 0;
        worstBKSvalue = bks.elementAt(0).getValue();
        //Actualitzem el pitjor valor de les K solucions
        for(int i=1;i<bks.size(); i++){
            if(worstBKSvalue < bks.elementAt(i).getValue()){
                worstBKSvalue = bks.elementAt(i).getValue();
                pworst = i; 
            }
        }
    }
    
    /**
    * Método que mira si existe la solución al vector especificado. 
    * @param saux La solución a buscar.
    * @param vse El vector de soluciones donde buscar.
    * @return Devuelve un booleano, true si esta repetido, false sino.
    */
     private boolean estaRepetit(SolutionExt saux, Vector<SolutionExt> vse) {
       for(int i=0;i<vse.size();i++)
       {
           if(saux.equals(vse.get(i))) return true;
       }
       return false;
    }
     
    /**
    * Método que genera todas les soluciones posibles aplicando el operador
    * 2-Opt una vez. Solo se queda con las soluciones mejores a la actual.
    * @return Devuelve un vector de soluciones con la mejora aplicada(solo de
    * las soluciones mejores).
    */ 
    private Vector<SolutionExt> generateAllSolutions2Opt(){
        int value = calculateSolutionValue(solActual.getSolution());
        Vector<SolutionExt> vse = new Vector<>();
        int v1,v2,v3,v4;
        for(v1=0;v1<solActual.getSolution().size();v1++){
             for(v2=0;v2<solActual.getSolution().size();v2++){
                  for(v3=0;v3<solActual.getSolution().size();v3++){
                       for(v4=0;v4<solActual.getSolution().size();v4++){
                            if(checkVertex(v1,v2,v3,v4)){
                                //Si v1-v2 + v3-v4 > v1-v3 + v2-v4
                                if(rs.getRelationValue(v1,v2)+rs.getRelationValue(v3,v4) > 
                                        rs.getRelationValue(v1,v3)+rs.getRelationValue(v2,v4)){
                                    Vector<Integer> saux = change2Edges(v2,v3);
                                    int valor = calculateSolutionValue(saux);
                                    SolutionExt auxse = new SolutionExt(saux,valor);
                                    if(valor <= value && !estaRepetit2Opt(auxse,vse)){
                                        vse.addElement(auxse);
                                    }
                                }
                            }
                       }
                  }
             }
        }        
        return vse;
    }
    
    /**
    * Método que intercambia las dos arestas especificadas(2opt).
    * El vertice 1 o 2 forman la aresta 1, y el vertice 3 y 4 formen la aresta 2.
    * @param pv2 La posición del vertice 2(de la aresta1).
    * @param pv3 La posición del vertice 1(de la aresta2).
    * @return Una solución con el intercambio 2opt deseado.*/
    private Vector<Integer> change2Edges(int pv2, int pv3){
            Vector<Integer> saux;
            saux = (Vector<Integer>) solActual.getSolution().clone();
            int a1 = saux.get(pv2);
            int a2 = saux.get(pv3);
            saux.set(pv2, a2);
            saux.set(pv3, a1);
            return saux;
    }
    
    /**
    * Método que mira si todas les posiciones de vertices escojidas son válidas,
    * es decir, diferentes y vecinas.
    * @param v1 La posición del vertice 1.
    * @param v2 La posición del vertice 2.
    * @param v3 La posición del vertice 3.
    * @param v4 La posición del vertice 4.
    * @return Devuelve True si son válidos. False en otro caso.*/
    private boolean checkVertex(int v1, int v2, int v3, int v4) { 
        boolean r = false;
        //diferents
        if(!(v1 == v2) && !(v1 == v3) && !(v1 == v4) && !(v2 == v3) && !(v2 == v4) && !(v3 == v4)){
            //Contiguos        
            if((((v1 == 0) && (v2 == solActual.getSolution().size()-1 || v2 == 1)) || 
                ((v1 == solActual.getSolution().size()-1) && (v2 == 0 || v2 == v1-1))) ||
                ((v2 == v1+1 || v2 == v1-1))        
                            &&        
               (((v3 == 0) && (v4 == solActual.getSolution().size()-1 || v4 == 1)) ||
                ((v3 == solActual.getSolution().size()-1) && (v4 == 0 || v4 == v3-1)) ||
                ((v3 == v4+1 || v3 == v4-1)))
                            ) r = true; 
        }
        return r;
    }
     
    /**
    * Método que devuelve si la solución ya existe dentro de un vector especificado.
    * @param saux La solución a comparar
    * @param vse Un vector de soluciones.
    * @return Un boolean, true si existe, false sino.
    */
    private boolean estaRepetit2Opt(SolutionExt saux, Vector<SolutionExt> vse) {
       if(saux.equals(solActual)) return true;     
       for(int i=0;i<vse.size();i++){
           if(saux.equals(vse.get(i))) return true;
       }
       return false;
    }
}