
package Ambito;

import Utils.DBManager;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class VtaTablaAmbito extends JFrame {
    public VtaTablaAmbito() {
        setTitle("Tabla de Simbolos");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnas = {"ID", "Tipo", "Clase", "Ambito", "Tamaño Arreglo", "Num. Parametros", "TParr"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);

        try (Connection conn = DBManager.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM simbolos")) {

            while (rs.next()) {
                Object[] fila = {
                    rs.getString("id"),
                    rs.getString("tipo"),
                    rs.getString("clase"),
                    rs.getInt("ambito"),
                    rs.getInt("tamanio_arreglo"),
                    rs.getInt("num_parametros"),
                    rs.getString("TParr")
                };
                modelo.addRow(fila);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la tabla: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);
    }
}
