package domain;
import exceptions.AlgorithmException;
import java.util.*;

/**
 * Clase Algorithm.
 * Clase encargada de inicializar la busqueda inicial y la optimizacion 
 * @author Alejandro
 */
public class Algorithm {
    
    /*Vector que contiene las IDs de las posiciones a recorrer ordenadamente.*/
    private Vector<Integer> sol;
    
    /*Instancia de la clase FirstSearch.*/
    private FirstSearch firstS;
   
    /*Instancia de la clase Optimize. */
    private Optimize opt;
    
    /*constructora clase Algorithm. */
    public Algorithm(){
        sol = new Vector<>();
    }
    
    /**
     * Constructora de la clase Algorithm.
     * @param firstS Instancia de la clase FirstSearch.
     * @param opt  Instancia de la clase Optimize.
     */
    public Algorithm(FirstSearch firstS, Optimize opt) throws AlgorithmException {
        this.firstS = firstS;
        this.opt = opt;
        sol = new Vector<>();
    }
    
    /**
     * Metodo que busca una solución óptima al problema del viajante.
     * @return: Se retorna un vector con la solución optimizada.
     */
    public Vector<Integer> searchSolution() throws AlgorithmException {
            sol = firstS.Search();
            sol = opt.optimize(sol);
            return sol;
    }
}