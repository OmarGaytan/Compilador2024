
package Ambito;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Ambito {

    Stack<Integer> pilaAmbitos = new Stack<>();
    int contadorAmbitos = 0;
    boolean enZonaDeclaracion = true;
    public static Map<Integer, Map<String, Integer>> conteoPorAmbito = new HashMap<>();
    public static Map<Integer, Integer> erroresDuplicados = new HashMap<>();
    public static Map<Integer, Integer> erroresNoDeclarados = new HashMap<>();
    private Map<Integer, Set<String>> tablaSimbolos = new HashMap<>();

    public Ambito() {
        pilaAmbitos.push(0);
        conteoPorAmbito.put(0, new HashMap<>());     
        tablaSimbolos.put(0, new HashSet<>());
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
    
        public void registrarId(String id) {
        int ambito = obtenerAmbitoActual();
        tablaSimbolos.putIfAbsent(ambito, new HashSet<>());
        tablaSimbolos.get(ambito).add(id);
        System.out.println("Registrado id: " + id + " en ámbito " + ambito);
    }
    
    public void registrarVariable(String tipo) {
        int actual = obtenerAmbitoActual();    
        conteoPorAmbito.putIfAbsent(actual, new HashMap<>());
        Map<String, Integer> mapa = conteoPorAmbito.get(actual);
        mapa.put(tipo, mapa.getOrDefault(tipo, 0) + 1);
    }
    
    public boolean existeEnAmbito(String id) {
        for (int i = pilaAmbitos.size() - 1; i >= 0; i--) {
            int ambito = pilaAmbitos.get(i);
            if (tablaSimbolos.containsKey(ambito)) {
                Set<String> simbolos = tablaSimbolos.get(ambito);
                if (simbolos.contains(id)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static Set<Integer> obtenerTodosLosAmbitos() {
        Set<Integer> ambitos = new java.util.HashSet<>();
        ambitos.addAll(conteoPorAmbito.keySet());
        ambitos.addAll(erroresDuplicados.keySet());
        ambitos.addAll(erroresNoDeclarados.keySet());
        return ambitos;
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
