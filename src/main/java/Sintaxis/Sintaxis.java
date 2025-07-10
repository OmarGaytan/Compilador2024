
package Sintaxis;

import java.util.*;
import Ambito.Ambito;
import Utils.DBManager;
import java.sql.SQLException;

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

    tokens = new ArrayList<>(tokens);
    lexemas =new ArrayList<>(lexemas);
    lexemas.add("$");
    tokens.add("$");

    int index = 0;
    String lastMet= "";
    boolean isPar = false;

    System.out.println("==== INICIO DE ANALISIS ====");
    while (!pila.isEmpty()) {
        String tope = pila.peek();
        String actual = tokens.get(index);
        String lexema = lexemas.get(index);
        
        //------------------------------------------------------------------------------------------ETAPA: AMBITO
        while(tope.startsWith("@")) {
            switch(tope) {
            case "@crearAmbito" -> {
                ambito.crearAmbito();
                pila.pop();
                tope= pila.peek();
            }
            case "@zonaDec" -> {
                pila.pop();
                tope= pila.peek();
                ambito.setZonaDeclaracion(true);
            }
            case "@zonaEjec" -> {
                pila.pop();
                tope= pila.peek();
                ambito.setZonaDeclaracion(false);
            }
            case "@eliminarAmbito" -> {
                ambito.eliminarAmbito();
                pila.pop();
                tope= pila.peek();
            }
            case "@parOn" -> {
                pila.pop();
                tope= pila.peek();
                isPar= true;
            }
            case "@parOff" -> {
                pila.pop();
                tope= pila.peek();
                isPar= false;
            }   
            case "@decVar" -> {              
                pila.pop();
                tope= pila.peek();  
                if(!ambito.estaEnZonaDeclaracion()) {
                    errores.add("Error: Declarando variable en zona de ejecucion, ID: '" + lexemas.get(index+2));               
                    break;
                }
                switch (lexemas.get(index+2)) {
                    case "[":
                        {
                            int items= 0;
                            String arrDataType= "null";
                            for(int i= 2; !lexemas.get(index+i).equals("]") && (index + i) < lexemas.size(); i++) {
                                String lex= lexemas.get(index+i);
                                if(lex.equals(",") || lex.equals("[") || lex.equals("]")) continue;
                                System.out.println("Elemento: " + lexemas.get(index+i));
                                arrDataType= tokens.get(index+i);
                                items++;
                            }  
                            try {
                                DBManager.insertarArr(lexemas.get(index), arrDataType, "arreglo", ambito.obtenerAmbitoActual(), items);
                                ambito.registrarId(lexemas.get(index));
                                ambito.registrarVariable(arrDataType);
                            }  catch(SQLException e) {
                                errores.add("Error: Variable repetida: " + lexemas.get(index) + " - Linea: Pending");
                                ambito.registrarErrorDuplicado();
                                
                            }
                            break;
                        }
                    case "new":
                        {
                            int it= 0;
                            int items = 0;
                            while(!lexemas.get(index+it).equals("(")) {
                                it++;
                                if (lexemas.get(index+it).equals("(")) {
                                    for(int i= 0; !lexemas.get((index+it)+i).equals(")") && ((index+it) + i) < lexemas.size(); i++) {
                                        String lex= lexemas.get((index+it)+i);
                                        if(lex.equals(",") || lex.equals("(") || lex.equals(")")) continue;
                                        //System.out.println("Elemento: " + lexemas.get(index+i));
                                        items++;
                                    }                                  
                                }
                            }
                            try {
                                DBManager.insertarArr(lexemas.get(index), tokens.get(index+5), "arreglo", ambito.obtenerAmbitoActual(), items); 
                                ambito.registrarId(lexemas.get(index));
                                ambito.registrarVariable(tokens.get(index+5));
                            } catch(SQLException e) {
                                errores.add("Error: Variable repetida: " + lexemas.get(index) + " - Linea: Pending");
                                ambito.registrarErrorDuplicado();
                            }
                            break;
                        }
                    default:
                        try {
                            String clase= isPar ? "par":"var";
                            DBManager.insertarVar(lexemas.get(index), lexemas.get(index+2), clase, ambito.obtenerAmbitoActual());
                            ambito.registrarId(lexemas.get(index));
                            ambito.registrarVariable(lexemas.get(index+2));
                        } catch(SQLException e) {
                            errores.add("Error: Variable repetida: " + lexemas.get(index) + " - Linea: Pending");
                            ambito.registrarErrorDuplicado();
                        }                    
                    }
                }
            case "@decVar2" -> {             
                pila.pop();
                tope= pila.peek();
                if(!ambito.estaEnZonaDeclaracion()) {
                    errores.add("Error: Declarando variable en zona de ejecucion, ID: '" + lexemas.get(index));
                    break;
                } 
                try {
                    String clase= isPar ? "par":"var";
                    DBManager.insertarVar(lexemas.get(index), tokens.get(index+2), clase, ambito.obtenerAmbitoActual());
                    ambito.registrarId(lexemas.get(index));
                    ambito.registrarVariable(lexemas.get(index+2));
                } catch(SQLException e) {
                    errores.add("Error: Variable repetida: " + lexemas.get(index) + " - Linea: Pending");
                    ambito.registrarErrorDuplicado();
                }   
            }
            case "@decMet", "@decFun" -> {
                String aux= pila.pop();
                tope= pila.peek();
                if(!ambito.estaEnZonaDeclaracion()) {
                    errores.add("Error: Declarando variable en zona de ejecucion, ID: #'" + lexemas.get(index+2));
                    break;
                } 
                String tipo= "void";
                for(int i= 0; !lexemas.get(index+i).equals("{");i++) {
                    System.out.println("Elementos: " + lexemas.get(index+i));
                    if(lexemas.get(index+i).equals(":")) {
                        tipo= lexemas.get((index+i)+1);
                        break;
                    }
                }
                lastMet= lexemas.get(index);
                if(aux.equals("@decMet")) {
                    try {
                       DBManager.insertarMet("#"+lexemas.get(index), tipo, "Method", ambito.obtenerAmbitoActual()); 
                       ambito.registrarId("#"+lexemas.get(index)); 
                       ambito.registrarVariable(tipo);           
                    } catch(SQLException e) {
                        errores.add("Error: Variable repetida: " + lexemas.get(index) + " - Linea: Pending");
                        ambito.registrarErrorDuplicado();
                    }
                    
                } else if( aux.equals("@decFun")) {
                    try {
                        DBManager.insertarMet("#"+lexemas.get(index), tipo, "function", ambito.obtenerAmbitoActual());
                        ambito.registrarId("#"+lexemas.get(index));
                        ambito.registrarVariable(tipo);
                    } catch (SQLException e) {
                        errores.add("Error: Variable repetida: " + lexemas.get(index) + " - Linea: Pending");
                        ambito.registrarErrorDuplicado();
                    }
                }
            }
            case "@decInCa" -> {
                pila.pop();
                tope= pila.peek();
                
                try {
                    DBManager.insertarInfaAndClass(lexemas.get(index), tokens.get(index-1), ambito.obtenerAmbitoActual());
                    ambito.registrarId(lexemas.get(index));
                    ambito.registrarVariable(tokens.get(index-1));
                } catch (SQLException e) {
                    errores.add("Error: Variable repetida: " + lexemas.get(index) + " - Linea: Pending");
                    ambito.registrarErrorDuplicado();
                 
                }
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
        
        // Validación de variable no declarada en zona de ejecución
        if (tope.equals("id") && !ambito.estaEnZonaDeclaracion()) {
            String nombreId = lexemas.get(index);
            if (!ambito.existeEnAmbito(nombreId)) {
                errores.add("Error: Variable no declarada: '" + nombreId + "' - Línea: Pending");
                ambito.registrarErrorNoDeclarado();
            }
        }

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