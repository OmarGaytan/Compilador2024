
package Sintaxis;

import java.io.*;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SintaxisLoader {

    public static class ResultadoSintaxis {
        public Map<String, List<String>> producciones = new HashMap<>();
        public Map<String, Map<String, String>> matrizSintactica = new HashMap<>();
    }

    public static ResultadoSintaxis cargarDesdeExcel(String ruta) throws IOException {
        ResultadoSintaxis resultado = new ResultadoSintaxis();

        try (FileInputStream fis = new FileInputStream(new File(ruta));
             Workbook wb = new XSSFWorkbook(fis)) {

            //Producciones
            Sheet hojaProducciones = wb.getSheetAt(1);
            for (int i = 0; i <= hojaProducciones.getLastRowNum(); i++) {
                Row fila = hojaProducciones.getRow(i);
                if (fila == null || fila.getCell(0) == null) continue;

                String texto = fila.getCell(0).getStringCellValue().trim();
                if (!texto.contains("⟶")) {
                    resultado.producciones.put("P" + 169, List.of("epsilon"));
                } else {

                String[] partes = texto.split("⟶");
                String ladoDerecho = partes[1].trim();
                List<String> simbolos = Arrays.asList(ladoDerecho.split("\\s+"));
                resultado.producciones.put("P" + i, simbolos);
                }
            }


            //Matriz sintáctica
            Sheet hojaMatriz = wb.getSheetAt(0);
            Row encabezados = hojaMatriz.getRow(0);
            List<String> terminales = new ArrayList<>();
            for (int j = 2; j < encabezados.getLastCellNum(); j++) {
                terminales.add(encabezados.getCell(j).getStringCellValue().trim());
            }

            for (int i = 1; i <= hojaMatriz.getLastRowNum(); i++) {
                Row fila = hojaMatriz.getRow(i);
                if (fila == null || fila.getCell(1) == null) continue;

                String noTerminal = fila.getCell(1).getStringCellValue().trim();
                Map<String, String> filaMapa = new HashMap<>();

                for (int j = 2; j < fila.getLastCellNum(); j++) {
                    Cell celda = fila.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (celda.getCellType() == CellType.NUMERIC && celda.getNumericCellValue() < 500) {
                        String terminal = terminales.get(j - 2);
                        filaMapa.put(terminal, "P" + (int) celda.getNumericCellValue());
                    }
                }
                resultado.matrizSintactica.put(noTerminal, filaMapa);
            }
        }
        return resultado;
    }
    
    public static void imprimirMatrizSintactica(Map<String, Map<String, String>> matriz) {
    for (Map.Entry<String, Map<String, String>> fila : matriz.entrySet()) {
        String noTerminal = fila.getKey();
        System.out.println("No Terminal: " + noTerminal);
        
        Map<String, String> columnas = fila.getValue();
        for (Map.Entry<String, String> entrada : columnas.entrySet()) {
            System.out.println("   Terminal: " + entrada.getKey() + " → Produccion: " + entrada.getValue());
        }
    }
}
}