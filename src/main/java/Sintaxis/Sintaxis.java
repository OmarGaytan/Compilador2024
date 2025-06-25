
package Sintaxis;

import java.util.*;

public class Sintaxis {

    private final Map<String, List<String>> producciones;
    private final Map<String, Map<String, String>> matrizSintactica;

    public Sintaxis(Map<String, List<String>> producciones, Map<String, Map<String, String>> matrizSintactica) {
        this.producciones = producciones;
        this.matrizSintactica = matrizSintactica;
    }
    
    public void trazarAnalisis(List<String> tokens) {
    Stack<String> pila = new Stack<>();
    pila.push("$");
    pila.push("PPAL");

    tokens = new ArrayList<>(tokens); // Copia por seguridad
    tokens.add("$");

    int index = 0;

    System.out.println("==== INICIO DE ANALISIS ====");
    while (!pila.isEmpty()) {
        String tope = pila.peek();
        String actual = tokens.get(index);

        System.out.println("PILA: " + pila);
        System.out.println("TOKEN: " + actual);
        System.out.println("---------------------PILA-------------------------------:");
        List<String> sublista = tokens.subList(index, tokens.size());
        System.out.println(sublista);
        System.out.println("---------------------PILA-------------------------------:");

        if (tope.equals(actual)) {
            System.out.println("Coincidencia terminal: '" + tope + "'\n");
            pila.pop();
            index++;
        } else if (!matrizSintactica.containsKey(tope)) {
            System.out.println("Error: simbolo inesperado '" + actual + "' en tope '" + tope + "'\n");
            return;
        } else {
            String regla = matrizSintactica.get(tope).get(actual);
            if (regla == null) {
                System.out.println("Error sintactico: no hay produccion para [" + tope + ", " + actual + "]\n");
                return;
            }

            List<String> produccion = producciones.get(regla);
            System.out.println("Aplicando regla " + regla + ": " + tope + " → " + String.join(" ", produccion) + "\n");

            pila.pop();
            if (!produccion.get(0).equals("epsilon")) {
                ListIterator<String> it = produccion.listIterator(produccion.size());
                while (it.hasPrevious()) {
                    pila.push(it.previous());
                }
            }
        }
    }

    if (index == tokens.size()) {
        System.out.println("Analisis sintactico exitoso. Entrada valida.");
    } else {
        System.out.println("Analisis termino pero no se consumieron todos los tokens.");
    }
    System.out.println("==== FIN DE ANALISIS ====");
}

    public static class ResultadoSintactico {
        public boolean correcto = false;
        public List<String> pasos = new ArrayList<>();
        public List<String> errores = new ArrayList<>();
    }
}