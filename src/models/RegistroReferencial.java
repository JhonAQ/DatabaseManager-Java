package models;

/**
 * Modelo para representar registros de tablas referenciales
 */
public class RegistroReferencial {
    private int codigo;
    private String descripcion;
    private String estadoRegistro;
    
    public RegistroReferencial() {
        this.estadoRegistro = "A"; // Por defecto activo
    }
    
    public RegistroReferencial(int codigo, String descripcion, String estadoRegistro) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.estadoRegistro = estadoRegistro;
    }
    
    // Getters y Setters
    public int getCodigo() {
        return codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getEstadoRegistro() {
        return estadoRegistro;
    }
    
    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }
    
    @Override
    public String toString() {
        return String.format("Código: %d, Descripción: %s, Estado: %s", 
                           codigo, descripcion, estadoRegistro);
    }
}
