package dao;

import db.ConexionDB;
import models.RegistroReferencial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para manejar tablas referenciales
 */
public class TablaReferencialDAO {
    
    private static final Logger LOGGER = Logger.getLogger(TablaReferencialDAO.class.getName());
    private ConexionDB conexion;
    private String nombreTabla;
    private String campoId;
    private String campoDescripcion;
    
    public TablaReferencialDAO(String nombreTabla, String campoId, String campoDescripcion) {
        this.conexion = ConexionDB.getInstance();
        this.nombreTabla = nombreTabla;
        this.campoId = campoId;
        this.campoDescripcion = campoDescripcion;
    }
    
    /**
     * Obtiene todos los registros de la tabla
     */
    public List<RegistroReferencial> obtenerTodos() throws SQLException {
        List<RegistroReferencial> registros = new ArrayList<>();
        String sql = String.format("SELECT %s, %s, EstadoRegistro FROM %s ORDER BY %s", 
                                 campoId, campoDescripcion, nombreTabla, campoId);
        
        try (ResultSet rs = conexion.ejecutarConsulta(sql)) {
            while (rs.next()) {
                RegistroReferencial registro = new RegistroReferencial();
                registro.setCodigo(rs.getInt(campoId));
                registro.setDescripcion(rs.getString(campoDescripcion));
                registro.setEstadoRegistro(rs.getString("EstadoRegistro"));
                registros.add(registro);
            }
        }
        
        return registros;
    }
    
    /**
     * Inserta un nuevo registro
     */
    public int insertar(RegistroReferencial registro) throws SQLException {
        String sql = String.format("INSERT INTO %s (%s, EstadoRegistro) VALUES (?, ?)", 
                                 nombreTabla, campoDescripcion);
        
        return conexion.ejecutarInsertConId(sql, registro.getDescripcion(), registro.getEstadoRegistro());
    }
    
    /**
     * Actualiza un registro existente
     */
    public int actualizar(RegistroReferencial registro) throws SQLException {
        String sql = String.format("UPDATE %s SET %s = ?, EstadoRegistro = ? WHERE %s = ?", 
                                 nombreTabla, campoDescripcion, campoId);
        
        return conexion.ejecutarActualizacionPreparada(sql, 
                                                      registro.getDescripcion(), 
                                                      registro.getEstadoRegistro(), 
                                                      registro.getCodigo());
    }
    
    /**
     * Elimina lógicamente un registro (marca con *)
     */
    public int eliminar(int codigo) throws SQLException {
        String sql = String.format("UPDATE %s SET EstadoRegistro = '*' WHERE %s = ?", 
                                 nombreTabla, campoId);
        
        return conexion.ejecutarActualizacionPreparada(sql, codigo);
    }
    
    /**
     * Inactiva un registro (marca con I)
     */
    public int inactivar(int codigo) throws SQLException {
        String sql = String.format("UPDATE %s SET EstadoRegistro = 'I' WHERE %s = ?", 
                                 nombreTabla, campoId);
        
        return conexion.ejecutarActualizacionPreparada(sql, codigo);
    }
    
    /**
     * Reactiva un registro (marca con A)
     */
    public int reactivar(int codigo) throws SQLException {
        String sql = String.format("UPDATE %s SET EstadoRegistro = 'A' WHERE %s = ?", 
                                 nombreTabla, campoId);
        
        return conexion.ejecutarActualizacionPreparada(sql, codigo);
    }
    
    /**
     * Busca un registro por código
     */
    public RegistroReferencial buscarPorCodigo(int codigo) throws SQLException {
        String sql = String.format("SELECT %s, %s, EstadoRegistro FROM %s WHERE %s = ?", 
                                 campoId, campoDescripcion, nombreTabla, campoId);
        
        try (ResultSet rs = conexion.ejecutarConsultaPreparada(sql, codigo)) {
            if (rs.next()) {
                RegistroReferencial registro = new RegistroReferencial();
                registro.setCodigo(rs.getInt(campoId));
                registro.setDescripcion(rs.getString(campoDescripcion));
                registro.setEstadoRegistro(rs.getString("EstadoRegistro"));
                return registro;
            }
        }
        
        return null;
    }
}
