package domain;

import java.util.Vector;

/**
 * Clase SolutionExt
 Esta clase permite guardar uan solución con su coste. Automaticamente se
 * ordenan las soluciones dejando el menor valor(de id) al principio. Tambien
 * tiene implementado un metodo para comparar este tipo de objetos.
 * 
 * @author Gerard Alonso
 * @version 1.1
 * @date 25/11/13
 * 
 */
public class SolutionExt {
        
         /* Un vector con el orden de ids de la solución. */
        private Vector<Integer> s;
        
         /* El valor calculado de la solución. */
        private Integer v;

        /**
        * Constructora de la clase SolutionExtended por defecto. Inicialitza 
        * el vector de soluciones.
        */
        public SolutionExt() {
            s = new Vector<>();
        }
        
        
        /**
        * Constructora de la clase SolutionExtended con parametros. Inicialitza 
        * los datos.
        * @param so Una vector con la solución inicial.
        * @param va El coste de la solución inicial.
        */
        public SolutionExt(Vector<Integer> so, Integer va) {
                s = so;
                v = va;
                orderSolution();
        }
        
        /**
        * Constructora de la clase SolutionExtended con un parametro. Inicialitza 
        * el vector de soluciones.
        * @param so Una vector con la solución inicial.
        */
        public SolutionExt(Vector<Integer> so) {
                s = so;
                v = -1;
                orderSolution();
        }
        
        /**
        * Método que reordena la solución poniendo el elemento mas pequeño al 
        * principio. Se utiliza para dejar las soluciones iguales i poder compararlas 
        * mas comodamente.
        */
         private void orderSolution() {
            //trobar la posició del node mes petit
            int minPos = 0;
            
            for(int i=1;i<s.size();i++){
                Integer element = s.get(i);
                if(element<s.get(minPos)) minPos = i;
            }
            
            //reOrdenar si el primer element no esta la posició 0
            if(minPos>0){
                
                int cont=0;
                Vector<Integer> aux = new Vector<>();
                for(int i=minPos;cont<s.size();){
                    aux.add(cont,s.get(i));
                    if(i == (s.size()-1)) i=0;
                    else i++;
                    cont++;
                }
                s = aux;
            }
        }
        
        /**
        * Método que modifica el vector de la solución.
        * @param sol Un vector con la nueva solución.
        */
        public void setSolution(Vector<Integer> sol) {
            s = sol;
            orderSolution();
        }
        
        /**
        * Método que modifica el valor de la solución.
        * @param val Nuevo valor de la solución.
        */
        public void setValue(Integer val) {
            v = val;
        }
        
        /**
        * Método que deuvelve el vector con las solución.
        * @return Un vector con la solución.
        */
        public Vector<Integer> getSolution() {
            return s;
        }
        
        /**
        * Método que devuelve el valor de la solución.
        * @return El valor de la solución.
        */
        public Integer getValue() {
            return v;
        }
        
        /**
        * Método que muestra el valor de la solución completa(con el valor).
        */
        public void print() {
            System.out.println(s.toString() + "->" + v);
        }
        
        /**
        * Método que compara la solción actual con la pasada por parametro.
        * @param solution La solucion a comparar.
        * @return Devuelve true si son iguales, false sino.
        */
        public boolean equals(SolutionExt solution) {
            if(solution.getSolution().size() != s.size()) return false;
            Vector<Integer> aux = solution.getSolution();
            for(int i=0;i<aux.size();i++){
                if(s.get(i) != aux.get(i)) return false;
            }
            return true;
        }
    }
