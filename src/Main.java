import gui.GestorTablaReferencial;
import db.ConexionDB;
import javax.swing.*;

/**
 * Clase principal para ejecutar el gestor de tablas referenciales
 */
public class Main {
    
    public static void main(String[] args) {
        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo configurar el Look and Feel: " + e.getMessage());
        }
        
        // Probar conexión a la base de datos
        if (!ConexionDB.probarConexion()) {
            JOptionPane.showMessageDialog(null, 
                "No se pudo conectar a la base de datos. Verifique la configuración.", 
                "Error de Conexión", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Mostrar selector de tabla
        SwingUtilities.invokeLater(() -> {
            mostrarSelectorTabla();
        });
    }
    
    private static void mostrarSelectorTabla() {
        String[] opciones = {
            "TIPO_FORMACION - Tipos de Formación Académica",
            "TIPO_HABILIDAD - Tipos de Habilidades"
        };
        
        String seleccion = (String) JOptionPane.showInputDialog(
            null,
            "Seleccione la tabla referencial a gestionar:",
            "Selector de Tabla",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
        
        if (seleccion != null) {
            if (seleccion.startsWith("TIPO_FORMACION")) {
                new GestorTablaReferencial("TIPO_FORMACION", "PKCodTipoFor", "DescripcionTipoFormacion").setVisible(true);
            } else if (seleccion.startsWith("TIPO_HABILIDAD")) {
                new GestorTablaReferencial("TIPO_HABILIDAD", "PKCodTipoHab", "DescripcionTipoHabilidad").setVisible(true);
            }
        } else {
            System.exit(0);
        }
    }
}
