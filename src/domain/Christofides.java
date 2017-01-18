package domain;
import java.util.*;
import exceptions.AlgorithmException;
/**
 *
 * @author Alejandro
 */
public class Christofides extends FirstSearch{
    
    /*Instancia de la clase Graph. Es nuestro grafo completo. */
    private Graph g;
    
    /**
     * Constructora de la clase Christofides
     * @param positions:Lista de las posiciones(vertices) que solo contienen paquetes.
     * @param relations:Instancia de la clase RelationSetExt que contiene todas las relaciones.
     */
    public Christofides(ArrayList<Integer> positions, RelationSetExt relations) throws AlgorithmException{
        super(positions,relations);
        int[] ID = new int[positions.size()];
        for (int i = 0; i < positions.size(); ++i) {
            ID[i] = positions.get(i);
        }
        g = new Graph(relations,ID);
    }
    /**
     * Método que permite buscar una soluci�n que, como maximo, difiere un 3/2 de la solucion optimia si se cumple la desigualdad triangular. Supondremos que se cumple.
     * @return: Se retorna una vector con la soluci�n.
     */
    public Vector<Integer> Search() throws AlgorithmException {
        Prim prim = new Prim(g);
        LinkedList<Integer> oddList = new LinkedList<>();
        System.out.println("Inicio de PRIM");
        Graph mst = prim.PrimMST();
        System.out.println("Fin de PRIM");
        oddList = mst.getOddVertex();
        GreedyMatch greedyMatch = new GreedyMatch(oddList,g);
        System.out.println("Inicio de Greedy");
        Graph matches = greedyMatch.execute();
        System.out.println("Fin de PRIM");
        MultiGraph multiGraph = new MultiGraph(mst,matches);
        Hierholzer hierholzer = new Hierholzer(multiGraph);
        System.out.println("Inicio de Hierholzer");
        sol = hierholzer.calculateEulerianCircuit();
        System.out.println("Fin de Herholzer");
        System.out.println(sol.size());
        return sol;
    }
}
