package domain;
import exceptions.AlgorithmException;
/**
 *
 * @author Alejandro
 */
public class Prim {
    
    private Graph g;

   
    public Prim (Graph g) {
        this.g = g;
    }
    
    public Graph PrimMST () throws  AlgorithmException  {
        
        if(!g.isComplete()) {
            throw new  AlgorithmException("The graph is not complete");
        }
        
        int[] vertex = g.getIdVector();
        int numVertices = g.getnVertex();
        int[] dist = new int[numVertices];
        int[] prev = new int[numVertices];
        boolean[] visited = new boolean[numVertices];
        
        for (int i = 0; i < dist.length; ++i) {
            dist[i] = Integer.MAX_VALUE;
        }
        
        dist[0] = 0;
        
        for (int i = 0; i < dist.length; ++i) {
            final int actualVertex = getActualVertex(dist,visited);
            visited[actualVertex] = true;
            
            for (int j = 0; j < dist.length; ++j){
                if (j != actualVertex && !visited[j]){
                    final int valor = g.getEdgeValue(vertex[actualVertex],vertex[j]);
                   // System.out.println(valor);
                    if (valor < dist[j]) {
                        dist[j] = valor;
                        prev[j] = vertex[actualVertex];
                    }
                }
            }
        }
        RelationSetExt prims = new RelationSetExt();
        for (int i = 1; i < prev.length; ++i){
            prims.addRelation(vertex[i],prev[i],g.getEdgeValue(vertex[i],prev[i]));
        }
               
        return new Graph(prims,vertex);
    }
    
    public int getActualVertex (int[] dist, boolean[] visited) {
        int minDist = Integer.MAX_VALUE;
        int vertex_aux = -1;
        for (int i = 0; i < dist.length; ++i) {
            if (dist[i] < minDist && !visited[i]){
                minDist = dist[i];
                vertex_aux = i;
            }
        }
        return vertex_aux;
    }
}
