package Lexico;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Lexer {
    
    public static class ResultadoLexico {
        public List<String> tokens = new ArrayList<>();
        public List<String> errores = new ArrayList<>();
        public List<String> tokensParaParser = new ArrayList<>();
    }

    // Aquí va tu matriz léxica [estado][columna]
    private int[][] matrizLexica;
    String arrReservadas[]= {"if", "else", "switch", "for", "do", "while", "console.log", "forEach",
        "break", "continue", "let", "const", "undefined", "interface", "typeof", "number", "string",
        "any", "interface", "set", "get", "class", "toLowerCase", "toUpperCase", "length", "trim",
        "charAt", "startsWith", "endsWith", "indexOf", "Includes", "slice", "replace", "split", "push",
        "shift", "in", "of", "splice", "concat", "find", "findIndex", "filter", "map", "sort", "reverse",
        "function", "Method", "return", "val", "var"};

    public Lexer() throws IOException {
        cargarMatrizDesdeExcel();
    }

public ResultadoLexico analizar(String codigoFuente) {
    ResultadoLexico resultado = new ResultadoLexico();
    int estado = 0;
    int puntero = 0;
    StringBuilder lexema = new StringBuilder();

    while (puntero < codigoFuente.length()) {
        char actual = codigoFuente.charAt(puntero);
        
        //Ignorar esoacios y tabs en estado 0
        if ((actual == ' ' || actual == '\t' || actual == '\n') && estado == 0) {
            puntero++;
            continue;
        }
        
        int columna = obtenerColumna(actual);

        //Ignorar caracteres invalidos
        if (columna == -1) {
            resultado.errores.add("Carácter inválido: '" + actual + "'");
            puntero++;
            continue;
        }

        int siguienteEstado = matrizLexica[estado][columna];
        
        //Si el estado es error
        if (siguienteEstado >= 500) {
            //System.out.println("-------------------------------------------------");
            //System.out.println("Error en lexico con estado: " + siguienteEstado);
            //System.out.println("El lexema era: " + lexema);
            //System.out.println("-------------------------------------------------");
            resultado.errores.add("Error léxico en: '" + lexema.toString() + actual + "'");
            lexema.setLength(0);
            estado = 0;
            puntero++;
        //Si se reconoce token
        } else if (siguienteEstado < 0) {
            
            String token = lexema.toString();
            String tipoToken = tokenNombre(siguienteEstado);

            if (Arrays.asList(arrReservadas).contains(token)) {
                //System.out.println("-------------------------------------------------");
                //System.out.println("Lexema hasta el momento: " + lexema);
                tipoToken = tokenNombre(-59);
                //System.out.println("Estado: -59");
                //System.out.println("-------------------------------------------------");
                resultado.tokens.add("PALABRA RESERVADA: " + token);
            } else {
                //System.out.println("-------------------------------------------------");
                //System.out.println("Se tokenizo: " + lexema);
                //System.out.println("Estado: " + siguienteEstado);
                //System.out.println("-------------------------------------------------");
                resultado.tokens.add("TOKEN " + tokenNombre(siguienteEstado) + ": " + token);
            }
            
            switch (tipoToken) {
                case "ID" -> resultado.tokensParaParser.add("id");
                case "NUM" -> resultado.tokensParaParser.add("numerica");
                case "REAL" -> resultado.tokensParaParser.add("real");
                case "CADENA" -> resultado.tokensParaParser.add("string");
                case "BOOL" -> resultado.tokensParaParser.add(lexema.toString().toLowerCase()); // true / false
                case "NULL" -> resultado.tokensParaParser.add("null");
                case "EXP" -> resultado.tokensParaParser.add("exp");

                // Operadores y símbolos tal cual se usan
                case "LOG", "LOG_BIN", "CONTROL", "ASIG", "ARIT", "POSTFIX", "REL", "TERNARIO", "AGRUP", "TURNO", "CONV" ->
                    resultado.tokensParaParser.add(lexema.toString());

                case "COMENTARIO_LINEA", "COMENTARIO_BLOQUE" -> {
                    // ignorar completamente
            }

            default -> resultado.tokensParaParser.add("DESCONOCIDO");
                    }

            lexema.append(actual);
            lexema.setLength(0);
            estado = 0;
        //Si el estado es de transicion
        } else {
            lexema.append(actual);
            estado = siguienteEstado;
            //System.out.println("-------------------------------------------------");
            //System.out.println("Lexema hasta el momento: " + lexema);
            //System.out.println("Estado: " + siguienteEstado);
            //System.out.println("-------------------------------------------------");
            puntero++;
        }
    }

    // Capturar cualquier token pendiente al final
    if (lexema.length() > 0 && estado < 0) {
        String token = tokenNombre(estado);
        resultado.tokens.add("TOKEN " + tokenNombre(estado) + ": " + lexema);
        
        switch (token) {
                case "ID" -> resultado.tokensParaParser.add("ID");
                case "NUM" -> resultado.tokensParaParser.add("NUM");
                case "REAL" -> resultado.tokensParaParser.add("REAL");
                case "CADENA" -> resultado.tokensParaParser.add("CADENA");
                case "LOG", "LOG_BIN" -> resultado.tokensParaParser.add(lexema.toString()); // ej: &&, !
                case "CONTROL" -> resultado.tokensParaParser.add(lexema.toString());        // ej: if, while
                case "ASIG" -> resultado.tokensParaParser.add(lexema.toString());           // ej: =, +=
                case "ARIT" -> resultado.tokensParaParser.add(lexema.toString());           // ej: +, -
                case "POSTFIX" -> resultado.tokensParaParser.add(lexema.toString());        // ej: ++, --
                case "REL" -> resultado.tokensParaParser.add(lexema.toString());            // ej: >, ==
                case "EXP" -> resultado.tokensParaParser.add("^");
                case "TERNARIO" -> resultado.tokensParaParser.add(lexema.toString());       // ej: ? :
                case "AGRUP" -> resultado.tokensParaParser.add(lexema.toString());          // ej: (, {, ]
                case "TURNO" -> resultado.tokensParaParser.add(lexema.toString());          // ej: >>
                case "CONV" -> resultado.tokensParaParser.add(lexema.toString());           // ej: !==, ===
                case "COMENTARIO_LINEA", "COMENTARIO_BLOQUE" -> {
                    // Ignoramos comentarios completamente
                }
                default -> resultado.tokensParaParser.add("DESCONOCIDO");
            }
    }
    return resultado;
}

    private void cargarMatrizDesdeExcel() throws IOException {
    File file = new File("src/main/java/resources/Matriz_Lexico.xlsx");

    try (FileInputStream inputStream = new FileInputStream(file);
         Workbook workbook = new XSSFWorkbook(inputStream)) {

        Sheet hoja = workbook.getSheetAt(0);
        int totalFilas = 70;
        int columnas = 34; // ← número fijo de columnas útiles

        matrizLexica = new int[totalFilas - 1][columnas];
        DataFormatter formatter = new DataFormatter();

        for (int i = 1; i < totalFilas; i++) {
            Row fila = hoja.getRow(i);
            for (int j = 1; j <= columnas; j++) {
                Cell celda = fila.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String texto = formatter.formatCellValue(celda).trim();

                int valor = -999;
                try {
                    valor = Integer.parseInt(texto);
                } catch (NumberFormatException e) {
                    //System.err.println("Valor inválido en fila " + i + ", columna " + j + ": '" + texto + "'");
                }

                matrizLexica[i - 1][j - 1] = valor;
            }
        }
    }
    /*for (int[] x : matrizLexica)
    {
       for (int y : x)
       {
            System.out.print(y + " ");
       }
       System.out.println();
    }*/
}


    private int obtenerColumna(char c) {
    if (Character.isUpperCase(c)) return 0;     // A-Z
    if (Character.isLowerCase(c)) return 1;     // a-z
    if (Character.isDigit(c)) return 2;         // 0-9
    if (c == '_') return 3;
    if (c == '@') return 4;
    if (c == '.') return 5;
    if (c == '^') return 6;
    if (c == '+') return 7;
    if (c == '-') return 8;
    if (c == '~') return 9;
    if (c == '|') return 10;
    if (c == '&') return 11;
    if (c == ',') return 12;
    if (c == ';') return 13;
    if (c == ':') return 14;
    if (c == '*') return 15;
    if (c == '/') return 16;
    if (c == '%') return 17;
    if (c == '<') return 18;
    if (c == '>') return 19;
    if (c == '¡') return 20;
    if (c == '!') return 21;
    if (c == '=') return 22;
    if (c == '?') return 23;
    if (c == '{') return 24;
    if (c == '}') return 25;
    if (c == '[') return 26;
    if (c == ']') return 27;
    if (c == '(') return 28;
    if (c == ')') return 29;
    if (c == ' ') return 30;           // OC (espacio)
    if (c == '\n') return 31;          // salto de línea
    if (c == '"') return 32;
    if (c == '\'') return 33;

    return -1; // carácter no reconocido
}

    private String tokenNombre(int estadoFinal) {
    return switch (estadoFinal) {
        case -1 -> "ID";
        case -2 -> "NUM";
        case -3 -> "REAL";
        case -4 -> "EXP";
        case -5 -> "COMENTARIO_LINEA";
        case -6, -13, -16, -22, -24, -31, -33, -35, -40, -42, -47, -48 -> "ASIG";
        case -7 -> "COMENTARIO_BLOQUE";
        case -8, -12, -15, -29, -32 -> "ARIT";
        case -9, -10 -> "CADENA";
        case -11, -14 -> "POSTFIX";
        case -17, -18, -20, -23 -> "LOG_BIN";
        case -19, -21, -51 -> "LOG";
        case -25, -26, -27, -28, -59 -> "CONTROL";
        case -30 -> "EXP";
        case -34, -41 -> "TURNO";
        case -36, -37, -38, -39, -43, -44, -45, -49 -> "REL";
        case -46, -50 -> "CONV";
        case -52 -> "TERNARIO";
        case -53, -54, -55, -56, -57, -58 -> "AGRUP";
        default -> "TOKEN_DESCONOCIDO (" + estadoFinal + ")";
    };
}
}