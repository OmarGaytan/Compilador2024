package Compilador.Lexico;

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
    }

    // Aquí va tu matriz léxica [estado][columna]
    private int[][] matrizLexica;
    String arrReservadas[]= {"if", "else", "switch", "for", "do", "while", "console.log", "forEach",
        "break", "continue", "let", "const", "undefined", "interface", "typeof", "Number", "String",
        "any", "interface", "set", "get", "class", "toLowerCase", "toUpperCase", "length", "trim",
        "charAt", "startsWith", "endsWith", "indexOf", "Includes", "slice", "replace", "split", "push",
        "shift", "in", "of", "splice", "concat", "find", "findIndex", "filter", "map", "sort", "reverse"};

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
            System.out.println("-------------------------------------------------");
            System.out.println("Error en lexico con estado: " + siguienteEstado);
            System.out.println("El lexema era: " + lexema);
            System.out.println("-------------------------------------------------");
            resultado.errores.add("Error léxico en: '" + lexema.toString() + actual + "'");
            lexema.setLength(0);
            estado = 0;
            puntero++;
        //Si se reconoce token
        } else if (siguienteEstado < 0) {
            
            String token = lexema.toString();

            if (Arrays.asList(arrReservadas).contains(token)) {
                System.out.println("-------------------------------------------------");
                System.out.println("Lexema hasta el momento: " + lexema);
                System.out.println("Estado: Manualmente");
                System.out.println("-------------------------------------------------");
                resultado.tokens.add("PALABRA RESERVADA: " + token);
            } else {
                System.out.println("-------------------------------------------------");
                System.out.println("Se tokenizo: " + lexema);
                System.out.println("Estado: " + siguienteEstado);
                System.out.println("-------------------------------------------------");
                resultado.tokens.add("TOKEN " + tokenNombre(siguienteEstado) + ": " + token);
            }
            lexema.append(actual);
            lexema.setLength(0);
            estado = 0;
        //Si el estado es de transicion
        } else {
            lexema.append(actual);
            estado = siguienteEstado;
            System.out.println("-------------------------------------------------");
            System.out.println("Lexema hasta el momento: " + lexema);
            System.out.println("Estado: " + siguienteEstado);
            System.out.println("-------------------------------------------------");
            puntero++;
        }
    }

    // Capturar cualquier token pendiente al final
    if (lexema.length() > 0 && estado < 0) {
        resultado.tokens.add("TOKEN " + tokenNombre(estado) + ": " + lexema);
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
                    System.err.println("Valor inválido en fila " + i + ", columna " + j + ": '" + texto + "'");
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
        // Devuelve el nombre del token según el estado final (e.g., -57 → TK_ID)
        return switch (estadoFinal) {
        case -1, -2 -> "TK_ARIT";
        case -3, -4, -5, -6 -> "TK_LOG_BIN";
        case -7, -8, -9, -10 -> "TK_CONTROL";
        case -11, -12, -13 -> "TK_ARIT";
        case -14, -15, -44, -49, -50, -53 -> "TK_REL";
        case -16, -29, -31, -34, -35, -37, -39, -41, -42, -45, -47, -52 -> "TK_ASIG";
        case -17, -32, -33 -> "TK_LOG";
        case -18 -> "TK_TERNARIO";
        case -19, -20, -21, -22, -23, -24 -> "TK_AGRUP";
        case -25, -26 -> "TK_CADENA";
        case -27 -> "TK_NUM";
        case -28, -30 -> "TK_POSTFIX";
        case -36 -> "TK_EXP";
        case -38 -> "TK_COMENTARIO_BLOQUE";
        case -40 -> "TK_COMENTARIO_LINEA";
        case -43, -46, -48 -> "TK_TURNO";
        case -51, -54 -> "TK_CONV";
        case -55, -56 -> "TK_REAL";
        case -57 -> "TK_ID";
        case -58, -59 -> "TK_BOOL";
        case -60 -> "TK_NULL";
        case -61 -> "TK_RESERVADA";
        default -> "TOKEN_DESCONOCIDO (" + estadoFinal + ")";
    };
    }
}