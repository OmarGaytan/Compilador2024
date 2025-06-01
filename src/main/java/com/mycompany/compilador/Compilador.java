package com.mycompany.compilador;
import javax.swing.JFrame;

public class Compilador {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame vta= new JFrame("Compilador 2025");
            vta.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            vta.setContentPane(new VentanaCompilador());
            vta.pack();
            vta.setLocationRelativeTo(null);
            vta.setVisible(true);
        });
    } 
    
}