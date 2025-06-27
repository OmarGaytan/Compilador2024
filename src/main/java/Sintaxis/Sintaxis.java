
package Sintaxis;

import java.util.*;
import Ambito.Ambito;
import Utils.DBManager;

public class Sintaxis {

    private final Map<String, List<String>> producciones;
    private final Map<String, Map<String, String>> matrizSintactica;
    public List<String> errores = new ArrayList<>();


    public Sintaxis(Map<String, List<String>> producciones, Map<String, Map<String, String>> matrizSintactica) {
        this.producciones = producciones;
        this.matrizSintactica = matrizSintactica;
    }
    
    public void trazarAnalisis(List<String> tokens, List<String> lexemas) {
    Stack<String> pila = new Stack<>();
    Ambito ambito = new Ambito();
    pila.push("$");
    pila.push("PPAL");

    tokens = new ArrayList<>(tokens); // Copia por seguridad
    lexemas =new ArrayList<>(lexemas);
    lexemas.add("$");
    tokens.add("$");

    int index = 0;
    String lastMet= "";

    System.out.println("==== INICIO DE ANALISIS ====");
    while (!pila.isEmpty()) {
        String tope = pila.peek();
        String actual = tokens.get(index);
        String lexema = lexemas.get(index);
        
        //------------------------------------------------------------------------------------------ETAPA: AMBITO
        switch(tope) {
            case "@crearAmbito" -> {
                ambito.crearAmbito();
                pila.pop();
                tope= pila.peek();
            }
            case "@eliminarAmbito" -> {
                ambito.eliminarAmbito();
                pila.pop();
                tope= pila.peek();
            }
            case "@decVar" -> {
                pila.pop();
                tope= pila.peek();
                DBManager.insertarVar(lexemas.get(index+1), lexemas.get(index+3), "var", ambito.obtenerAmbitoActual());
                ambito.registrarVariable(lexemas.get(index+3));
                
            }
            case "@decArr" -> {
                pila.pop();
                tope= pila.peek();
                int items = 0;
                for(int i= 0; !lexemas.get(index+i).equals("]") && (index + i) < lexemas.size(); i++) {
                   String lex= lexemas.get(index+i);
                   if(lex.equals(",") || lex.equals("[") || lex.equals("]")) continue;
                       //System.out.println("Elemento: " + lexemas.get(index+i));
                       items++;
                }
                DBManager.insertarArr(lexemas.get(index-2), tokens.get(index+1), "arreglo", ambito.obtenerAmbitoActual(), items);
                ambito.registrarVariable(tokens.get(index+1));
            }
            case "@decNewArr" -> {
                pila.pop();
                tope= pila.peek();
                int it= 0;
                int items = 0;
                while(!lexemas.get(index+it).equals("(")) {
                    it++;
                    if (lexemas.get(index+it).equals("(")) {
                        for(int i= 0; !lexemas.get((index+it)+i).equals(")") && ((index+it) + i) < lexemas.size(); i++) {
                            String lex= lexemas.get((index+it)+i);
                            if(lex.equals(",") || lex.equals("(") || lex.equals(")")) continue;
                                System.out.println("Elemento: " + lexemas.get(index+i));
                                items++;
                        }
                        break;
                    }
                }
                DBManager.insertarArr(lexemas.get(index-2), tokens.get(index+3), "arreglo", ambito.obtenerAmbitoActual(), items);
                ambito.registrarVariable(tokens.get(index+3));
            }
            case "@decMet" -> {
                pila.pop();
                tope= pila.peek();
                String tipo= "void";
                for(int i= 0; !lexemas.get(index+i).equals("{");i++) {
                    if(lexemas.get(index+i).equals(":")) {
                        tipo= lexemas.get((index+i)+1);
                        break;
                    }
                }
                lastMet= lexemas.get(index);
                DBManager.insertarMet(lexemas.get(index), tipo, "Method", ambito.obtenerAmbitoActual());
                ambito.registrarVariable(tipo);
            }
            
            case "@decPar" -> {
                pila.pop();
                tope= pila.peek();
                for(int i= 0; !lexemas.get(index+i).equals(")");i++) {                
                    String lex= lexemas.get(index+i);
                    if(!tokens.get(index+i).equals("id")) continue;
                    
                    DBManager.insertarPar(lex, lexemas.get(index+2), "Par", ambito.obtenerAmbitoActual(), lastMet);
                    ambito.registrarVariable(lexemas.get(index+2));
                }
                
            }
        }

        //------------------------------------------------------------------------------------------ETAPA: SINTAXIS
        System.out.println("PILA: " + pila);
        System.out.println("TOKEN: " + actual);
        System.out.println("---------------------PILA-------------------------------:");
        List<String> sublista1 = lexemas.subList(index, lexemas.size());
        List<String> sublista = tokens.subList(index, tokens.size());
        System.out.println(sublista1);
        System.out.println(sublista);
        System.out.println("---------------------PILA-------------------------------:");

        if (tope.equals(actual)) {
            System.out.println("Coincidencia terminal: '" + tope + "'\n");
            pila.pop();
            index++;
        } else if (!matrizSintactica.containsKey(tope)) {
            System.out.println("Error: simbolo inesperado '" + actual + "' en tope '" + tope + "'\n");
            errores.add("Error: símbolo inesperado '" + actual + "' en tope '" + tope + "'");
            return;
        } else {
            String regla = matrizSintactica.get(tope).get(actual);
            if (regla == null) {
                System.out.println("Error sintactico: no hay produccion para [" + tope + ", " + actual + "]\n");
                errores.add("Error fatal");
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
    System.out.println("NUMERO DE AMBITOS: " + ambito.numeroAmitos());
}

    public static class ResultadoSintactico {
        public boolean correcto = false;
        public List<String> pasos = new ArrayList<>();
        public List<String> errores = new ArrayList<>();
    }
}