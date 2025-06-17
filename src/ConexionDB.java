package db;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para manejar las conexiones a la base de datos MySQL
 * Sistema de Gestión de Currículums - Datos Personales
 */
public class ConexionDB {
    
    private static final String URL = "jdbc:mysql://localhost:3306/datospersonales";
    private static final String USUARIO = "appuser";
    private static final String PASSWORD = "labDbAbet";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private static final Logger LOGGER = Logger.getLogger(ConexionDB.class.getName());
    
    // usamos singleton
    private static ConexionDB instancia;
    private Connection conexion;
    
    private ConexionDB() {
        try {
            Class.forName(DRIVER);
            
            Properties props = new Properties();
            props.setProperty("user", USUARIO);
            props.setProperty("password", PASSWORD);
            props.setProperty("useSSL", "false");
            props.setProperty("allowPublicKeyRetrieval", "true");
            props.setProperty("serverTimezone", "UTC");
            props.setProperty("characterEncoding", "UTF-8");
            props.setProperty("useUnicode", "true");
            
            this.conexion = DriverManager.getConnection(URL, props);
            
            LOGGER.info("Conexión establecida exitosamente con la base de datos");
            
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver MySQL no encontrado", e);
            throw new RuntimeException("Error: Driver MySQL no encontrado", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al conectar con la base de datos", e);
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }
    
    /**
     * Obtiene la instancia única de ConexionDB (Singleton)
     * @return instancia de ConexionDB
     */
    public static ConexionDB getInstance() {
        if (instancia == null) {
            synchronized (ConexionDB.class) {
                if (instancia == null) {
                    instancia = new ConexionDB();
                }
            }
        }
        return instancia;
    }
    
    /**
     * Obtiene la conexión a la base de datos
     * @return Connection objeto de conexión
     * @throws SQLException si hay error en la conexión
     */
    public Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            instancia = new ConexionDB();
        }
        return conexion;
    }
    
    /**
     * Cierra la conexión a la base de datos
     */
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                LOGGER.info("Conexión cerrada exitosamente");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error al cerrar la conexión", e);
        }
    }
    
    /**
     * Verifica si la conexión está activa
     * @return true si la conexión está activa, false en caso contrario
     */
    public boolean isConexionActiva() {
        try {
            return conexion != null && !conexion.isClosed() && conexion.isValid(2);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error al verificar el estado de la conexión", e);
            return false;
        }
    }
    
    /**
     * Ejecuta una consulta SELECT y retorna el ResultSet
     * @param consulta SQL SELECT a ejecutar
     * @return ResultSet con los resultados
     * @throws SQLException si hay error en la consulta
     */
    public ResultSet ejecutarConsulta(String consulta) throws SQLException {
        Statement statement = getConexion().createStatement();
        return statement.executeQuery(consulta);
    }
    
    /**
     * Ejecuta una consulta SELECT con parámetros preparados
     * @param consulta SQL con parámetros (?)
     * @param parametros valores para los parámetros
     * @return ResultSet con los resultados
     * @throws SQLException si hay error en la consulta
     */
    public ResultSet ejecutarConsultaPreparada(String consulta, Object... parametros) throws SQLException {
        PreparedStatement statement = getConexion().prepareStatement(consulta);
        
        for (int i = 0; i < parametros.length; i++) {
            statement.setObject(i + 1, parametros[i]);
        }
        
        return statement.executeQuery();
    }
    
    /**
     * Ejecuta una consulta INSERT, UPDATE o DELETE
     * @param consulta SQL a ejecutar
     * @return número de filas afectadas
     * @throws SQLException si hay error en la consulta
     */
    public int ejecutarActualizacion(String consulta) throws SQLException {
        Statement statement = getConexion().createStatement();
        return statement.executeUpdate(consulta);
    }
    
    /**
     * Ejecuta una consulta INSERT, UPDATE o DELETE con parámetros preparados
     * @param consulta SQL con parámetros (?)
     * @param parametros valores para los parámetros
     * @return número de filas afectadas
     * @throws SQLException si hay error en la consulta
     */
    public int ejecutarActualizacionPreparada(String consulta, Object... parametros) throws SQLException {
        PreparedStatement statement = getConexion().prepareStatement(consulta);
        
        for (int i = 0; i < parametros.length; i++) {
            statement.setObject(i + 1, parametros[i]);
        }
        
        return statement.executeUpdate();
    }
    
    /**
     * Ejecuta una consulta INSERT y retorna el ID generado
     * @param consulta SQL INSERT
     * @param parametros valores para los parámetros
     * @return ID generado por la base de datos
     * @throws SQLException si hay error en la consulta
     */
    public int ejecutarInsertConId(String consulta, Object... parametros) throws SQLException {
        PreparedStatement statement = getConexion().prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
        
        for (int i = 0; i < parametros.length; i++) {
            statement.setObject(i + 1, parametros[i]);
        }
        
        int filasAfectadas = statement.executeUpdate();
        
        if (filasAfectadas > 0) {
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }
        
        throw new SQLException("No se pudo obtener el ID generado");
    }
    
    /**
     * Inicia una transacción
     * @throws SQLException si hay error al iniciar la transacción
     */
    public void iniciarTransaccion() throws SQLException {
        getConexion().setAutoCommit(false);
    }
    
    /**
     * Confirma una transacción
     * @throws SQLException si hay error al confirmar la transacción
     */
    public void confirmarTransaccion() throws SQLException {
        getConexion().commit();
        getConexion().setAutoCommit(true);
    }
    
    /**
     * Revierte una transacción
     * @throws SQLException si hay error al revertir la transacción
     */
    public void revertirTransaccion() throws SQLException {
        getConexion().rollback();
        getConexion().setAutoCommit(true);
    }
    
    /**
     * Método para probar la conexión
     * @return true si la conexión es exitosa
     */
    public static boolean probarConexion() {
        try {
            ConexionDB db = ConexionDB.getInstance();
            return db.isConexionActiva();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al probar la conexión", e);
            return false;
        }
    }
}