package db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestConexion {
    
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE CONEXIÓN A LA BASE DE DATOS ===");
        
        try {
            if (ConexionDB.probarConexion()) {
                System.out.println("✓ Conexión establecida exitosamente");
                
                ConexionDB db = ConexionDB.getInstance();
                
                System.out.println("\n--- Probando consulta simple ---");
                ResultSet rs = db.ejecutarConsulta("SELECT 1 as test, NOW() as fecha_hora");
                
                if (rs.next()) {
                    System.out.println("Resultado de prueba: " + rs.getInt("test"));
                    System.out.println("Fecha y hora del servidor: " + rs.getTimestamp("fecha_hora"));
                }
                rs.close();
                
                // Ejemplo de consulta a las tablas del sistema
                System.out.println("\n--- Verificando tablas del sistema ---");
                ResultSet tablas = db.ejecutarConsulta(
                    "SELECT table_name FROM information_schema.tables " +
                    "WHERE table_schema = 'datospersonales' ORDER BY table_name"
                );
                
                System.out.println("Tablas encontradas en la base de datos:");
                while (tablas.next()) {
                    System.out.println("- " + tablas.getString("table_name"));
                }
                tablas.close();
                
                System.out.println("\n--- Insertando datos de prueba ---");
                
                int idPersona = db.ejecutarInsertConId(
                    "INSERT INTO PERSONA (PerNom, PerDir, PerTel, PerEma) VALUES (?, ?, ?, ?)",
                    "Juan Pérez", "Av. Principal 123", "+1234567890", "juan.perez@email.com"
                );
                
                System.out.println("Persona insertada con ID: " + idPersona);
                
                ResultSet persona = db.ejecutarConsultaPreparada(
                    "SELECT * FROM PERSONA WHERE PKPerCod = ?", idPersona
                );
                
                if (persona.next()) {
                    System.out.println("Datos de la persona:");
                    System.out.println("- ID: " + persona.getInt("PKPerCod"));
                    System.out.println("- Nombre: " + persona.getString("PerNom"));
                    System.out.println("- Dirección: " + persona.getString("PerDir"));
                    System.out.println("- Teléfono: " + persona.getString("PerTel"));
                    System.out.println("- Email: " + persona.getString("PerEma"));
                }
                persona.close();
                
                int filasEliminadas = db.ejecutarActualizacionPreparada(
                    "DELETE FROM PERSONA WHERE PKPerCod = ?", idPersona
                );
                System.out.println("Datos de prueba eliminados: " + filasEliminadas + " filas");
                
                db.cerrarConexion();
                System.out.println("\n✓ Prueba completada exitosamente");
                
            } else {
                System.err.println("✗ Error: No se pudo establecer la conexión");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("✗ Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
