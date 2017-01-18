package domain;

import exceptions.AlgorithmException;
import java.util.*;

/**
 * Clase GreedySearch
 * Clase encargada de buscar una posible solucion inicial aplicando el metodo
 * Greedy. Se construye la solución buscando el nodo mas cercano al actual.
 * @author Alejandro
 */
public class GreedySearch extends FirstSearch {
    
    /**
     * Constructora de GreedySearch
     * @param positions: Array que contiene las ID de las direcciones con paquete.
     * @param relations: Instancia de la clase RelationSet.
     */
    public GreedySearch(ArrayList<Integer> positions, RelationSetExt relations){
        super(positions,relations);
    }
    /**
     * Metodo que busca una solución inicial.
     * @return: Devuelve una solución. 
     */
    public Vector<Integer> Search(){
        int actual;
        int search;
        int savePosition = -1;
        int valor;
        actual = positions.get(0);
        positions.remove(0);
        sol.addElement(actual);
        while(!positions.isEmpty()) {
            int min = Integer.MAX_VALUE; //max distancia
            for (int i = 0; i < positions.size(); ++i) {
                search = positions.get(i);
                valor = relations.getRelationValue(actual,search);
                if (valor < min) {
                    min = valor;
                    savePosition = i;
                }
            }
            sol.addElement(positions.get(savePosition));
            positions.remove(savePosition);
            actual = savePosition;
        }
        return sol;
    }
}