
package Utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class POI {
    public static List<List<String>> crearTabla(String rutaArchivo) {
        List<List<String>> lista = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(new File(rutaArchivo));
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            int filas = sheet.getPhysicalNumberOfRows();

            for (int i = 0; i < filas; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                List<String> fila = new ArrayList<>();
                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cell.getCellType()) {
                        case STRING -> fila.add(cell.getStringCellValue());
                        case NUMERIC -> fila.add(String.valueOf((int) cell.getNumericCellValue()));
                        case BOOLEAN -> fila.add(String.valueOf(cell.getBooleanCellValue()));
                        case BLANK -> fila.add("");
                        default -> fila.add("?");
                    }
                }
                lista.add(fila);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    
    public static int[][] matrizEnteros(List<List<String>> lista) {
        int filas = lista.size();
        int columnas = lista.get(0).size();
        int[][] matriz = new int[filas][columnas];

        for (int i = 0; i < filas; i++) {
            List<String> fila = lista.get(i);
            for (int j = 0; j < columnas; j++) {
                try {
                    matriz[i][j] = Integer.parseInt(fila.get(j));
                } catch (NumberFormatException e) {
                    matriz[i][j] = -1;
                }
            }
        }

        return matriz;
    }
    
    public static void mostrarMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
