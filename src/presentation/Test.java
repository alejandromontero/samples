
package presentation;

import domain.*;
import exceptions.FileException;
import static java.lang.System.exit;
import java.util.*;
import persistence.FileManagerController;
/**
 *
 * @author Pau
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileException {
        System.out.println("Bienvenido al driver para testear. Primero crearemos el mapa.");
        System.out.println("Introduzca el numero de direcciones que desea crear:");
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        if (N > 27) {System.out.println("Error! El sistema no puede soportar tantas direcciones!"); exit(0);}
        AddressSet adreces = new AddressSet();
        RelationSetExt distancies = new RelationSetExt();
        char c = 'A';
        for (int i = 0; i < N; i++) {
            adreces.addAddress(Character.toString(c));
            c += 1;
        }
        System.out.println("AddressSet creado con el siguiente contenido: " + adreces.getAllString().toString());
        System.out.println("Ahora crearemos el RelationSetExt");
        Vector<String> aux = adreces.getAllString();
        Random rand = new Random();
        for(int i = 0; i < N; ++i) {
            int id1 = adreces.getAddress(aux.get(i)).getId();
            for (int j = i+1; j < N; j++) {
                int id2 = adreces.getAddress(aux.get(j)).getId();
                int distancia = rand.nextInt(100-1)+1;
                distancies.addRelation(id1,id2,distancia);
            }
        }
        
        System.out.println("RelationSet creado con el siguiente tamaño y contenido:" + distancies.size());
        Vector<ArrayList<String>> v = distancies.getAll();
        for (int i = 0; i < v.size(); i++) {
                String d1 = adreces.getAddress(Integer.parseInt(v.elementAt(i).get(0))).getAddress();
                String d2 = adreces.getAddress(Integer.parseInt(v.elementAt(i).get(1))).getAddress();
                String dist = v.elementAt(i).get(2);
                System.out.println(d1+" "+d2+ " " +dist); 
        }
        System.out.println("Introduzca el numero de paquetes que desea añadir:");
        int npaquets = sc.nextInt();
        System.out.println("Se añadiran "+npaquets+" paquetes al azar");
        PacketSet paquets = new PacketSet();
        for (int i = 0; i < npaquets; i++) {
            int aux2 = rand.nextInt(N);
            paquets.addPacket(adreces.getAddress(aux2));
            
        }
        System.out.println("Conjunto de paquetes creado en las siguienetes direcciones:");
        v = paquets.getAllPackets();
        for (int i = 0; i < npaquets; i++) {
           int id = paquets.getPacket(Integer.parseInt(v.elementAt(i).get(0))).getId();
           String ad = paquets.getPacket(id).getAddress().getAddress();
           System.out.println("id paquete: "+id+" direccion: "+ad);
        }
        sc.nextLine();
        DomainController dc = new DomainController();
        FileManagerController fm = new FileManagerController();
        System.out.println("Introduzca ahora la ruta absoluta donde quiere crear el fichero mapa");
        String map = sc.nextLine();
        fm.saveMaps(map,adreces.getAllString(),dc.getAllRelationsAddress());
        //sc.nextLine();
        System.out.println("Introduzca ahora la ruta absoluta donde quiere crear el fichero paquets");
        String pak = sc.nextLine();
        fm.savePackets(pak, paquets.getAllAddressString());
        System.out.println("Ficheros creados!");
    }
}
