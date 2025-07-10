package Compilador;

import Ambito.VtaTablaAmbito;
import Lexico.Lexer;
import Sintaxis.Sintaxis;
import Sintaxis.SintaxisLoader;
import Utils.DBManager;
import Utils.TextLineNumber;
import Utils.ExcelGenerator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class VentanaCompilador extends javax.swing.JPanel {
    
    public VentanaCompilador() {
        initComponents();
        TextLineNumber tln= new TextLineNumber(TxtCodigo);
        jScrollPane1.setRowHeaderView(tln);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CompiPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TxtCodigo = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        TxtTokens = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TxtErrores = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        CargarCodigo = new javax.swing.JButton();
        compilar1 = new javax.swing.JButton();
        CargarCodigo1 = new javax.swing.JButton();
        exportarExcel = new javax.swing.JButton();

        TxtCodigo.setColumns(20);
        TxtCodigo.setRows(5);
        jScrollPane1.setViewportView(TxtCodigo);

        TxtTokens.setEditable(false);
        TxtTokens.setColumns(20);
        TxtTokens.setRows(5);
        jScrollPane2.setViewportView(TxtTokens);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Errores");
        jLabel1.setAutoscrolls(true);

        TxtErrores.setEditable(false);
        TxtErrores.setColumns(20);
        TxtErrores.setRows(5);
        jScrollPane3.setViewportView(TxtErrores);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Tokens");
        jLabel2.setAutoscrolls(true);

        CargarCodigo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CargarCodigo.setText("Cargar codigo");
        CargarCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CargarCodigoActionPerformed(evt);
            }
        });

        compilar1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        compilar1.setText("Compilar");
        compilar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compilar1ActionPerformed(evt);
            }
        });

        CargarCodigo1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CargarCodigo1.setText("Tabla de ambito");
        CargarCodigo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarTablaAmbito(evt);
            }
        });

        exportarExcel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        exportarExcel.setText("Generar Excel");
        exportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportarExcel(evt);
            }
        });

        javax.swing.GroupLayout CompiPanelLayout = new javax.swing.GroupLayout(CompiPanel);
        CompiPanel.setLayout(CompiPanelLayout);
        CompiPanelLayout.setHorizontalGroup(
            CompiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CompiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CompiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(CompiPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(CompiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(CompiPanelLayout.createSequentialGroup()
                        .addGroup(CompiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(CargarCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(CompiPanelLayout.createSequentialGroup()
                                .addComponent(exportarExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(compilar1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(CargarCodigo1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(CompiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        CompiPanelLayout.setVerticalGroup(
            CompiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CompiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CompiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(CompiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CompiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(CompiPanelLayout.createSequentialGroup()
                        .addGroup(CompiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(compilar1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(exportarExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(CargarCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(CargarCodigo1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(CompiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(CompiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void CargarCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CargarCodigoActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            
            try {
                String contenido = new String(Files.readAllBytes(archivo.toPath()));
                contenido = contenido.replace("\r", "");
                TxtCodigo.setText(contenido);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al leer el archivo.");
            }
        }
    }//GEN-LAST:event_CargarCodigoActionPerformed

    private void compilar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compilar1ActionPerformed
        //ELIMINA LA TABLA DE AMBITO Y CREA UNA NUEVA
        DBManager.crearTabla();
        
        Lexer lexer = null;
        try {
            lexer = new Lexer();
        } catch (IOException ex) {
            System.getLogger(VentanaCompilador.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        Lexer.ResultadoLexico resultado = lexer.analizar(TxtCodigo.getText()+ "\n");

        TxtTokens.setText(String.join("\n", resultado.tokens));
        TxtErrores.setText(String.join("\n", resultado.errores));
        
        SintaxisLoader.ResultadoSintaxis datos = null;
        try {
            datos = SintaxisLoader.cargarDesdeExcel("src/main/java/resources/Matriz_Sintaxis.xlsx");
        } catch (IOException ex) {
            System.getLogger(VentanaCompilador.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        Sintaxis parser = new Sintaxis(datos.producciones, datos.matrizSintactica);
        //SintaxisLoader.imprimirMatrizSintactica(datos.matrizSintactica);
        parser.trazarAnalisis(resultado.tokensParaParser, resultado.lexemas);
        
        
        if(!parser.errores.isEmpty()) {
            for (String error : parser.errores) {
                TxtErrores.append(error + "\n");
            }
        }

        //Sintaxis.ResultadoSintactico resultadoSintactico = parser.analizar(resultado.tokensParaParser);
    }//GEN-LAST:event_compilar1ActionPerformed

    private void mostrarTablaAmbito(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarTablaAmbito
        VtaTablaAmbito ventana = new VtaTablaAmbito();
        ventana.setVisible(true);
    }//GEN-LAST:event_mostrarTablaAmbito

    private void exportarExcel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportarExcel
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("Ana Luisa Gaytan Estrella-18130217.xlsx"));
        int opcion = fileChooser.showSaveDialog(null);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            if(!archivo.getName().toLowerCase().endsWith("xlsx")) archivo = new File(archivo.getAbsolutePath() + ".xlsx");
            try {
                ExcelGenerator.exportarExcel(archivo.getAbsolutePath());
            } catch (IOException ex) {
                System.getLogger(VentanaCompilador.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }//GEN-LAST:event_exportarExcel


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CargarCodigo;
    private javax.swing.JButton CargarCodigo1;
    private javax.swing.JPanel CompiPanel;
    private javax.swing.JTextArea TxtCodigo;
    private javax.swing.JTextArea TxtErrores;
    private javax.swing.JTextArea TxtTokens;
    private javax.swing.JButton compilar1;
    private javax.swing.JButton exportarExcel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
