
package domain;

import java.util.Vector;

/**
 * Clase Optimize
 * Esta clase sera la padre de todas las clases que se encarguen de optimizar. 
 * Contiene las estructuras basicas que neceseitara qualquier otra clase que 
 * herede.
 * 
 * @author Gerard Alonso
 * @version 1.1
 * @date 16/11/13
 * 
 */
public abstract class Optimize {
    
    /* Instancia de RelationSet con todas las relaciones. */
    protected final RelationSetExt rs;
     /* Un vector amb con una solción. */
    protected Vector<Integer> solucio;
    
    
    /**
    * Constructora de la classe Optimize con el parametro rs.Inicialitza los datos.
    * @param rese Una instancia de RelationSet con todas las relaciones.
    */
    public Optimize(RelationSetExt rese) {
        this.rs = rese;
    }
    
    /**
    * Método abstracto que redifiniran todos los possibles metodos de optimitzación.
    * @param s Una solución.
    * @return Una solución optimitzada.
    */
    public abstract Vector<Integer> optimize(Vector<Integer> s);
    
    /**
    * Método que calcula el valor de una solución especificada.
    * @param solution Una solución.
    * @return Un entero con el valor de la solución calculado.
    */
   public int calculateSolutionValue(Vector<Integer> solution){
        int valor=0;
        for(int i=0;i<solution.size()-1;i++){
            valor += rs.getRelationValue(solution.get(i), solution.get(i+1));
        }
        valor += rs.getRelationValue(solution.get(solution.size()-1), solution.get(0));
        return valor;
    }
    
}
