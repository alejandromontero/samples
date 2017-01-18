package domain;

import exceptions.AlgorithmException;
import java.util.*;

/**
 * Clase FirstSearch
 * Clase Abstracta para la busqueda inicial del TSP.
 * @author Alejandro
 */
public abstract class FirstSearch {
    
    /**
     * Mapa de hash que contiene las IDs de las posiciones con paquetes.
     */
    
    protected ArrayList<Integer> positions;
    protected RelationSetExt relations;
    protected Vector<Integer> sol;
   
    /**
     * Constructora de la clase FirstSearch.
     * @param positions: Array que contiene las ID de las direcciones con paquete.
     * @param relations: Instancia de RelationSet.
     */
    public FirstSearch(ArrayList<Integer> positions, RelationSetExt relations){
        this.positions = positions;
        this.relations = relations;
        sol = new Vector<>();
    }

    
    /**
     * Metodo que busca una soluci�n inicial.
     * @return: Devuelve una soluci�n. 
     */
    public abstract Vector<Integer> Search() throws AlgorithmException;
    
}
