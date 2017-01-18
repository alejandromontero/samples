package domain;


import exceptions.AlgorithmException;
import java.util.*;

/**
 * Clase GreedyMatch
 * Clase que busca el apareamiento perfecto minimo en un grafo completo G sobre
 * un subconjunto de vertices oddVertex usando la aproximación greedy.
 * @author Alejandro
 */
public class GreedyMatch {
    
    /*
    * Subconjunto de vertices en el que hay que buscar el apareamiento.
    */
    LinkedList<Integer> oddVertex;
    /**
     * Instancia de la clase G. Grafo completo.
     */
    Graph g;
    
    /**
     * Constructora de la clase GreedyMatch.
     * @param oddvl: Subconjunto de vertices en el que hay que buscar el apareamiento.
     * @param g : Instancia de la clase G. Grafo completo.
     */
    public GreedyMatch(LinkedList<Integer> oddvl, Graph g){
       this.oddVertex = oddvl;
       this.g = g;
    }
    
    /**
     * Metodo que busca el apareamiento perfecto minimo.
     * @return: Se devuelve un grafo que contiene el apareamiento perfecto minimo.
     */
    public Graph execute() throws AlgorithmException{
        if(!g.isComplete()) {
            throw new  AlgorithmException("The graph is not complete");
        }
        LinkedList<Relation> matches = new LinkedList<>();
        LinkedList<Relation> adjacentList = new LinkedList<>();
        LinkedList<Integer> controlList = new LinkedList<>();
        int numMatches = oddVertex.size()/2;
        int vertex = 0;
        System.out.println(numMatches);
        System.out.println(oddVertex.size());
        while (matches.size() < numMatches) {
            System.out.println(oddVertex.get(vertex));
            if (!controlList.contains(oddVertex.get(vertex))) { 
                System.out.println("YEah");
                adjacentList = g.getRelationList(oddVertex.get(vertex));
           
                Relation minEdge = new Relation(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
                for (int i = 0; i < adjacentList.size(); ++i) {
                    Relation edge = adjacentList.get(i);
                    if (oddVertex.contains(edge.getId1()) && oddVertex.contains(edge.getId2())){
                        if ((!controlList.contains(edge.getId2())) && (!controlList.contains(edge.getId1()))) {
                            if (edge.getValue() < minEdge.getValue()) minEdge = edge;
                        }
                    }
                }
                if (minEdge.getValue() < Integer.MAX_VALUE) matches.add(minEdge);
                if (!controlList.contains(minEdge.getId1())) controlList.add(minEdge.getId1());
                if (!controlList.contains(minEdge.getId2())) controlList.add(minEdge.getId2());
            }
            ++vertex;
            System.out.println();
            System.out.println(matches.size());
        }
        RelationSetExt R = new RelationSetExt();
        System.out.println(matches.size());
        for (int i = 0; i < matches.size(); ++i) {
            R.addRelation(matches.get(i).getId1(), matches.get(i).getId2(), matches.get(i).getValue());
        }
        return new Graph(R,g.getIdVector());
    }
    
}