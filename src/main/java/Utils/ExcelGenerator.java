
package Utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import Ambito.Ambito;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class ExcelGenerator {
    public static void exportarExcel(String ruta) throws FileNotFoundException, IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("AMBITO");

        Row header = sheet.createRow(0);
        String[] columnas = {"Ambito", "number", "string", "boolean", "real", "exp", "null", "#id", 
                             "Error duplicado", "Error no declarado", "Total Tipos", "Total Errores"};
        for (int i = 0; i < columnas.length; i++) {
            header.createCell(i).setCellValue(columnas[i]);
        }

        int filaNum = 1;
        int totalTipos = 0, totalErrores = 0, numbers= 0, strings= 0, booleans= 0, reales= 0, exp= 0, nulls= 0,
                ids= 0, errNoDec= 0;

        for (int ambito : Ambito.obtenerTodosLosAmbitos()) {
            Row fila = sheet.createRow(filaNum++);
            Map<String, Integer> tipos = Ambito.conteoPorAmbito.getOrDefault(ambito, new java.util.HashMap<>());

            fila.createCell(0).setCellValue(ambito);

            int subtotal = 0;

            for (int i = 1; i <= 7; i++) {
                String tipo = columnas[i];
                int count = tipos.getOrDefault(tipo, 0);
                fila.createCell(i).setCellValue(count);
                subtotal += count;

                switch (tipo) {
                    case "number" -> numbers += count;
                    case "string" -> strings += count;
                    case "boolean" -> booleans += count;
                    case "real" -> reales += count;
                    case "exp" -> exp += count;
                    case "null" -> nulls += count;
                    case "#id" -> ids += count;
                }
            }

            int errDup = Ambito.erroresDuplicados.getOrDefault(ambito, 0);
            int errUnd = Ambito.erroresNoDeclarados.getOrDefault(ambito, 0);

            fila.createCell(8).setCellValue(errDup);
            fila.createCell(9).setCellValue(errUnd);
            fila.createCell(10).setCellValue(subtotal);
            fila.createCell(11).setCellValue(errDup + errUnd);
            
            totalTipos += subtotal;
            totalErrores += errDup + errUnd;
        }
        
        Row total = sheet.createRow(filaNum);
        total.createCell(0).setCellValue("Total general:");
        total.createCell(1).setCellValue(numbers);
        total.createCell(2).setCellValue(strings);
        total.createCell(3).setCellValue(booleans);
        total.createCell(4).setCellValue(reales);
        total.createCell(5).setCellValue(exp);
        total.createCell(6).setCellValue(nulls);
        total.createCell(7).setCellValue(ids);
        total.createCell(8).setCellValue(
        Ambito.erroresDuplicados.values().stream().mapToInt(Integer::intValue).sum());
        total.createCell(9).setCellValue(
        Ambito.erroresNoDeclarados.values().stream().mapToInt(Integer::intValue).sum());
        total.createCell(10).setCellValue(totalTipos);
        total.createCell(11).setCellValue(totalErrores);
        

        try (FileOutputStream fileOut = new FileOutputStream(ruta)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }

}