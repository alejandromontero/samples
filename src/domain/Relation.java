package domain;

/**
 * Clase Relation.
 * Esta clase define la relación que tienen dos elementos cualquiera. Esta relación se define con un grado.
 * Dicho grado es un valor entre 0 y 100.
 * En esta clase tenemos las creadoras de la clase, y los métodos que definen y obtienen dicho grado entre dos elementos.
 * 
 * @author marc.benito
 */
public class Relation {  
    

    /**
    * Entero que contiene la id del primer elemento.
    */
    protected Integer id1;    
    
    /**
    * Entero que contiene la identificación ID del segundo elemento.
    */
    protected Integer id2;   
    
   /**
    * Entero que define el grado de disimilaridad entre dos elementos.
    */
    protected Integer value;
    
    /**
    * Constructora de la clase Relación.
    * Crea una relación vacía.
    */
    public Relation () {}
    
    /**
    * Constructora de la clase Elemento con parámetros predefinidos.
    * Crea una relación entre dos elementos, que deben existir previamente, y les asigna un grado de disimilaridad.
    * @param i1 El identificador del elemento 1
    * @param i2 El identificador del elemento 2
    * @param v El grado de relación entre el elemento i1 y el elemento i2
    */
    public Relation(Integer i1, Integer i2, Integer v) {
        id1 = i1;
        id2 = i2;
        value = v;
    }

    /**
    * Método que obtiene el nombre del elemento 1.
    * El elemento debe existir previamente.
    * @return un entero con la id del elemento.
    */
    public Integer getId1() {
        return id1;
    }
    
    /**
    * Método que obtiene el nombre del elemento 2.
    * El elemento debe existir previamente.
    * @return un entero con la id del elemento.
    */
    public Integer getId2() {
        return id2;
    }

    /**
    * Método que obtiene el grado de disimilaridad.
    * El grado debe estar definido con anterioridad.
    * @return un entero con el grado de disimilaridad.
    */
    public Integer getValue() {
        return value;
    }
    
    /**
    * Método que define un nuevo grado de disimilaridad entre dos elementos i1, i2.
    * El parámetro tiene que ser un valor entero >= 0 y <=100.
    * Los elementos deben existir previamente.
    
    * @param i1 id del elemento 1.
    * @param i2 id del elemento 2.
    *  @param newValue El grado de relación entre el elemento 1 y el elemento 2.
    * @return true si se ha realizado, false si no se ha realizado a causa de que algún valor introducido es inválido
    */
    public Boolean setRelation(Integer i1, Integer i2, Integer newValue) {
        if (i1 >=0 && i2 >= 0 && newValue >=0 && newValue <= 100) {
           id1 = i1;
           id2 = i2;
           value = newValue;
           return true;
        }
        return false;
    }
    
    /**
    * Método que imprime una relacion por pantalla.
    * Imprime por pantalla la disimilaridad, y a continuacion los id's de los elementos
    */
    public void printRelation() {
        System.out.print(id1);
        System.out.print(" ");
        System.out.println(id2);
        System.out.println(" ");
        System.out.print(value);
        System.out.print(" ");
    }
}
