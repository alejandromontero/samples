
package domain;
import java.util.*;

/**
 *
 * @author Alejandro
 */
public class Hierholzer {
    /**
     * Instancia de la clase MultiGraph. Es un grafo que combina la solución del algoritmo de Prim y el greedy que busca el apareamiento perfecto maximo.
     */
    private MultiGraph g;
    
    /**
     * Constructora de la clase Hierholzer
     * @param g: Instancia de la clase MultiGraph. 
     */
    public Hierholzer(MultiGraph g) {
        this.g = g;
    }
    
    /**
     * Metodo que comprueba si el cyclo contiene todos vertices.
     * @param cycle: ciclo.
     * @param g: Instancia de la clase MultiGraph.
     * @return: true si el ciclo contiene todos los vertices del grafo. False si no.
     */
    
    private boolean containsAllVertex(LinkedList<Integer> cycle, MultiGraph g) {
        Vector<Integer> nVertex = g.getIdVector();
        for (int i = 0; i < nVertex.size(); ++i) {
            int vertex = nVertex.get(i);
            if (!cycle.contains(vertex)) return false;
        }
        return true;
    }
     /**
     * Metodo  que une dos ciclos en un único ciclo.
     * @param list1: Lista que contiene el primer ciclo.
     * @param list2: Lista que contiene el segundo ciclo.
     * @return: Se retorna una lista con los dos ciclos unidos.
     */
    public LinkedList<Integer> mergeTours(LinkedList<Integer> list1, LinkedList<Integer> list2) {
        System.out.println("Merge Tours");
       LinkedList<Integer> result = list1;
       //Buscamos un vertice de la lista que estamos aÃ±adiendo que tambiÃ©n estÃ© en la primera lista.
       int insert = list2.get(0);
       list2.remove(0);
       int index = -1;
 
       for (int i = 0; i < list1.size(); ++i) {
           int vertex = list1.get(i);
           if (vertex == insert) {
               index = list1.indexOf(vertex);

           }
       }
       if (index < 0) {
           return null;
       }
       else {
           result.addAll(index + 1, list2);
           return result;
       }
    }
    
    /**
     * Metodo que busca un vertice que esta relacionado con vertices no incluidos en el ciclo. 
     * @param g: Multigrafo.
     * @param cycle: Ciclo.
     * @return: Se retorna la ID del vertice que tiene un vecino no incluido en el ciclo.
     */
    public Integer findCycleNeighbor(MultiGraph g, LinkedList<Integer> cycle) {
        Vector<Integer> neighbors; 
        int cycleSize = cycle.size();
        for (int i = 0; i < cycleSize; ++i) {
            neighbors = g.getVectorVertex(cycle.get(i));
            
            for (int j = 0; j < neighbors.size(); ++j) {
                if (!(cycle.contains(neighbors.get(j)))) {
                    return cycle.get(i);
                }
            }
            
        }
        return -1;
    }
    /**
     * Metodo que busca un ciclo en un grafo desde un vertice de partida.
     * @param g: Multigrafo en el que se busca el ciclo.
     * @param sourceVector: Vertice desde el que se comienza a crear el ciclo.
     * @return: Se retorna una lista con el ciclo encontrado.
     */
    public LinkedList<Integer> makeCycle(MultiGraph g, Integer sourceVector){
        if (g.getIdVector().size() < 2) {
          
        }
        //Lista de Vertices del ciclo
        LinkedList<Integer> cycle = new LinkedList<>();
        //Vertices ya descubiertos
        Set<Integer> discovered = new HashSet<>();
        
        //DFS
        
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(sourceVector);
        
        discovered.add(sourceVector);
        cycle.add(sourceVector);
        
        while(!stack.empty()) {
            int v = stack.pop();
            discovered.add(v);
            
            if(v == sourceVector && cycle.size() > 2) {
                return cycle;
            }
            Vector<Integer> adjacent = g.getVectorVertex(v);
            boolean found = false;
            for (int i = 0; i < adjacent.size(); ++i) {
                if (!found){
                    if (adjacent.get(i) != v) {
                        int w = adjacent.get(i);

                        if (cycle.size() > 2 && w == sourceVector){
                            return cycle;
                        }
                        if(!discovered.contains(w)) {
                            discovered.add(w);
                            stack.push(w);
                            cycle.add(w);
                            found = true;
                        }

                    }
                }
            }
        }
        if(cycle.size() < 2) {
            cycle.add(cycle.getFirst());
        }
        return cycle;
    }
    /**
     * Metodo que busca un circuito euleriano
     * @return: Se retorna un vector con la solucion.
     */
    public Vector<Integer> calculateEulerianCircuit() /*throws noEulerianException */ {
        if (!g.isEulerian()) {
              System.out.println("Error Error Error. El grafo no es Euleriano");
              return null;
        }
        Integer vertex = g.getRandomVertex();
        LinkedList<Integer> cycle = new LinkedList<>();
        LinkedList<Integer> nextCycle = new LinkedList<>();
        cycle = makeCycle(g,vertex);
        nextCycle = cycle;
        
        Vector<Integer> allVertex;
        allVertex = new Vector<>();
        allVertex = g.getIdVector();
        boolean complete = containsAllVertex(cycle,g);

        while (!complete) {
            Integer v1 = findCycleNeighbor(g,cycle);
            System.out.println(v1);
            if(cycle != null) {
                g.delEdges(cycle);
            }
            nextCycle = makeCycle(g,v1);
            cycle = mergeTours(cycle,nextCycle);
            complete = containsAllVertex(cycle,g);
        }
        Vector<Integer> shortcut = new Vector<>();
        for (int i = 0; i < cycle.size(); ++i) {
            if (!shortcut.contains(cycle.get(i))) shortcut.add(cycle.get(i));
        }
        return shortcut;
    }
}

 