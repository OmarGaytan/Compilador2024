package Utils;

import java.sql.*;

public class DBManager {
    private static final String DB_URL = "jdbc:sqlite:tabla_simbolos.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void crearTabla() {
        String eliminar = "DROP TABLE IF EXISTS simbolos;";
        String crear = """
            CREATE TABLE simbolos (
                id TEXT,
                tipo TEXT,
                clase TEXT,
                ambito INTEGER,
                tamanio_arreglo INTEGER,
                dimencion_arr INTEGER,
                num_parametros INTEGER,
                TParr TEXT
            );
        """;

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(eliminar);
            stmt.execute(crear);
            System.out.println("Tabla de símbolos lista.");
        } catch (SQLException e) {
            System.err.println("Error al preparar la tabla: " + e.getMessage());
        }
    }
    
    public static void insertarVar(String id, String tipo, String clase, int ambito) {
        String sql= "INSERT INTO simbolos (id, tipo, clase, ambito) VALUES (?, ?, ?, ?)";
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:tabla_simbolos.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, tipo);
            pstmt.setString(3, clase);
            pstmt.setInt(4, ambito);
            
            pstmt.executeUpdate();
            
        } catch(SQLException e) {
            System.out.println("Error al insertar variable: " + e.getMessage());
        }
    }
    
    public static void insertarArr(String id, String tipo, String clase, int ambito, int tamArr) {
        String sql= "INSERT INTO simbolos (id, tipo, clase, ambito, tamanio_arreglo) VALUES (?, ?, ?, ?, ?)";
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:tabla_simbolos.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, tipo);
            pstmt.setString(3, clase);
            pstmt.setInt(4, ambito);
            pstmt.setInt(5, tamArr);
            
            pstmt.executeUpdate();
            
        } catch(SQLException e) {
            System.out.println("Error al insertar arreglo: " + e.getMessage());
        }
    }
    
    public static void insertarMet(String id, String tipo, String clase, int ambito) {
        String sql= "INSERT INTO simbolos (id, tipo, clase, ambito, tamanio_arreglo) VALUES (?, ?, ?, ?, ?)";
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:tabla_simbolos.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, tipo);
            pstmt.setString(3, clase);
            pstmt.setInt(4, ambito);
            
            pstmt.executeUpdate();
            
        } catch(SQLException e) {
            System.out.println("Error al insertar metodo: " + e.getMessage());
        }
    }
    
    public static void insertarPar(String id, String tipo, String clase, int ambito, String TParr) {
        String sql= "INSERT INTO simbolos (id, tipo, clase, ambito, TParr) VALUES (?, ?, ?, ?, ?)";
        
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:tabla_simbolos.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, tipo);
            pstmt.setString(3, clase);
            pstmt.setInt(4, ambito);
            pstmt.setString(5, TParr);
            
            pstmt.executeUpdate();
            
        } catch(SQLException e) {
            System.out.println("Error al insertar parametros: " + e.getMessage());
        }
    }
   
}
