
package Ambito;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Ambito {

    Stack<Integer> pilaAmbitos = new Stack<>();
    int contadorAmbitos = 0;
    boolean enZonaDeclaracion = false;
    public static Map<Integer, Map<String, Integer>> conteoPorAmbito = new HashMap<>();
    public static Map<Integer, Integer> erroresDuplicados = new HashMap<>();
    public static Map<Integer, Integer> erroresNoDeclarados = new HashMap<>();

    public Ambito() {
        pilaAmbitos.push(0);
        conteoPorAmbito.put(0, new HashMap<>());
    }

    public void crearAmbito() {
        contadorAmbitos++;
        pilaAmbitos.push(contadorAmbitos);
        System.out.println("Se creo nuevo ambito: " + contadorAmbitos);
    }

    public void eliminarAmbito() {
        if (pilaAmbitos.size() > 1) {
            int eliminado = pilaAmbitos.pop();
            System.out.println("Se elimino ambito: " + eliminado);
        } else {
            System.out.println("No se puede eliminar el ambito global");
        }
    }
    
    public void registrarVariable(String tipo) {
        int actual = obtenerAmbitoActual();
        
        conteoPorAmbito.putIfAbsent(actual, new HashMap<>());
        Map<String, Integer> mapa = conteoPorAmbito.get(actual);
        
        mapa.put(tipo, mapa.getOrDefault(tipo, 0) + 1);
    }
    
    public void registrarErrorDuplicado() {
        int ambito = obtenerAmbitoActual();
        erroresDuplicados.put(ambito, erroresDuplicados.getOrDefault(ambito, 0) + 1);
    }
    
    public void registrarErrorNoDeclarado() {
        int ambito = obtenerAmbitoActual();
        erroresNoDeclarados.put(ambito, erroresNoDeclarados.getOrDefault(ambito, 0) + 1);
    }

    public int obtenerAmbitoActual() {
        return pilaAmbitos.peek();
    }

    public void setZonaDeclaracion(boolean estado) {
        this.enZonaDeclaracion = estado;
        System.out.println("[ZONA] Zona de declaracion: " + estado);
    }

    public boolean estaEnZonaDeclaracion() {
        return this.enZonaDeclaracion;
    }
    
    public int numeroAmitos() {
        return this.contadorAmbitos;
    }
}
