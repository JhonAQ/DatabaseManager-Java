package db;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interfaz gráfica para gestionar tablas referenciales
 */
public class DatabaseManagerGUI extends JFrame {
    
    private String nombreTabla;
    private String campoId;
    private String campoDescripcion;
    private String campoEstado;
    
    private JTextField txtCodigo;
    private JTextField txtDescripcion;
    private JComboBox<String> cmbEstado;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    
    // Botones
    private JButton btnAdicionar, btnModificar, btnEliminar, btnCancelar;
    private JButton btnInactivar, btnReactivar, btnActualizar, btnSalir;
    
    // Flag de actualización
    private int CarFlaAct = 0; // 0 = no actualizar, 1 = actualizar
    private String operacionActual = ""; // Para saber qué operación se está realizando
    
    // Base de datos
    private ConexionDB db;
    
    public DatabaseManagerGUI(String nombreTabla, String campoId, String campoDescripcion, String campoEstado) {
        this.nombreTabla = nombreTabla;
        this.campoId = campoId;
        this.campoDescripcion = campoDescripcion;
        this.campoEstado = campoEstado;
        this.db = ConexionDB.getInstance();
        
        initComponents();
        setupLayout();
        setupEventListeners();
        configurarEstadoInicial();
        cargarDatosTabla(); // Mover después de configurar estado inicial
    }
    
    private void initComponents() {
        setTitle("Gestor de " + nombreTabla);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        // Campos del formulario
        txtCodigo = new JTextField(10);
        txtDescripcion = new JTextField(30);
        cmbEstado = new JComboBox<>(new String[]{"A", "I", "*"});
        
        // Modelo de la tabla
        String[] columnas = {"Código", "Descripción", "Estado de Registro"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Botones
        btnAdicionar = new JButton("Adicionar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnCancelar = new JButton("Cancelar");
        btnInactivar = new JButton("Inactivar");
        btnReactivar = new JButton("Reactivar");
        btnActualizar = new JButton("Actualizar");
        btnSalir = new JButton("Salir");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // PRIMERA SECCIÓN: Registro
        JPanel panelRegistro = new JPanel();
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de " + nombreTabla));
        panelRegistro.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Código
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panelRegistro.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        panelRegistro.add(txtCodigo, gbc);
        
        // Descripción
        gbc.gridx = 0; gbc.gridy = 1;
        panelRegistro.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panelRegistro.add(txtDescripcion, gbc);
        
        // Estado
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Estado de Registro:"), gbc);
        gbc.gridx = 1;
        panelRegistro.add(cmbEstado, gbc);
        
        // SEGUNDA SECCIÓN: Tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Tabla " + nombreTabla));
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(850, 250));
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        // TERCERA SECCIÓN: Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBorder(BorderFactory.createTitledBorder("Operaciones"));
        panelBotones.add(btnAdicionar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnInactivar);
        panelBotones.add(btnReactivar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnSalir);
        
        panelPrincipal.add(panelRegistro, BorderLayout.NORTH);
        panelPrincipal.add(panelTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    private void setupEventListeners() {
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
            }
        });
        
        btnAdicionar.addActionListener(e -> accionAdicionar());
        btnModificar.addActionListener(e -> accionModificar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnCancelar.addActionListener(e -> accionCancelar());
        btnInactivar.addActionListener(e -> accionInactivar());
        btnReactivar.addActionListener(e -> accionReactivar());
        btnActualizar.addActionListener(e -> accionActualizar());
        btnSalir.addActionListener(e -> accionSalir());
    }
    
    private void cargarDatosSeleccionados() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0 && CarFlaAct == 0) {
            txtCodigo.setText(tabla.getValueAt(filaSeleccionada, 0).toString());
            txtDescripcion.setText(tabla.getValueAt(filaSeleccionada, 1).toString());
            cmbEstado.setSelectedItem(tabla.getValueAt(filaSeleccionada, 2).toString());
        }
    }
    
    private void limpiarCampos() {
        txtCodigo.setText("");
        txtDescripcion.setText("");
        cmbEstado.setSelectedIndex(0);
        tabla.clearSelection();
    }
    
    private void configurarEstadoInicial() {
        txtCodigo.setEditable(false);
        txtDescripcion.setEditable(false);
        cmbEstado.setEnabled(false);
        btnActualizar.setEnabled(false);
        CarFlaAct = 0;
        operacionActual = "";
        
        // Limpiar campos al iniciar
        limpiarCampos();
    }
    
    private void configurarEstadoOperacion(String operacion) {
        operacionActual = operacion;
        CarFlaAct = 1;
        btnActualizar.setEnabled(true);
        
        switch (operacion) {
            case "ADICIONAR":
                txtCodigo.setEditable(true);
                txtDescripcion.setEditable(true);
                cmbEstado.setEnabled(false);
                cmbEstado.setSelectedItem("A");
                break;
            case "MODIFICAR":
                txtCodigo.setEditable(false);
                txtDescripcion.setEditable(true);
                cmbEstado.setEnabled(false);
                break;
            case "ELIMINAR":
            case "INACTIVAR":
            case "REACTIVAR":
                txtCodigo.setEditable(false);
                txtDescripcion.setEditable(false);
                cmbEstado.setEnabled(false);
                break;
        }
    }
    
    private void accionAdicionar() {
        // Blanquear las cajas de texto del área de Registro
        limpiarCampos();
        
        // Configurar para operación ADICIONAR
        configurarEstadoOperacion("ADICIONAR");
        
        // El estado se muestra por defecto como "A" (Activo) y no se puede modificar
        cmbEstado.setSelectedItem("A");
        cmbEstado.setEnabled(false);
        
        // Establecer el flag de actualización en 1
        CarFlaAct = 1;
        
        // Dar foco al campo código para que el usuario pueda empezar a escribir
        txtCodigo.requestFocus();
        
        System.out.println("Modo ADICIONAR activado. CarFlaAct = " + CarFlaAct);
    }
    
    private void accionModificar() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un registro para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        configurarEstadoOperacion("MODIFICAR");
        txtDescripcion.requestFocus();
    }
    
    private void accionEliminar() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un registro para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        configurarEstadoOperacion("ELIMINAR");
        cmbEstado.setSelectedItem("*");
    }
    
    private void accionInactivar() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un registro para inactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        configurarEstadoOperacion("INACTIVAR");
        cmbEstado.setSelectedItem("I");
    }
    
    private void accionReactivar() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un registro para reactivar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        configurarEstadoOperacion("REACTIVAR");
        cmbEstado.setSelectedItem("A");
    }
    
    private void accionCancelar() {
        // Limpiar campos del área de Registro
        limpiarCampos();
        
        // Desactivar la función que se estuvo trabajando
        configurarEstadoInicial();
        
        // Colocar el flag de actualización en valor 0
        CarFlaAct = 0;
        
        System.out.println("Operación cancelada. CarFlaAct = " + CarFlaAct);
    }
    
    private void accionActualizar() {
        if (CarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay operación pendiente para actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String codigo = txtCodigo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String estado = cmbEstado.getSelectedItem().toString();
        
        // Validar campos obligatorios
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El código es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            txtCodigo.requestFocus();
            return;
        }
        
        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
            txtDescripcion.requestFocus();
            return;
        }
        
        try {
            switch (operacionActual) {
                case "ADICIONAR":
                    // Verificar si el código ya existe
                    if (existeCodigo(codigo)) {
                        JOptionPane.showMessageDialog(this, "El código '" + codigo + "' ya existe. Ingrese un código diferente.", "Error", JOptionPane.ERROR_MESSAGE);
                        txtCodigo.selectAll();
                        txtCodigo.requestFocus();
                        return;
                    }
                    ejecutarAdicionar(codigo, descripcion, estado);
                    break;
                case "MODIFICAR":
                    ejecutarModificar(codigo, descripcion, estado);
                    break;
                case "ELIMINAR":
                case "INACTIVAR":
                case "REACTIVAR":
                    ejecutarCambioEstado(codigo, descripcion, estado);
                    break;
            }
            
            // Recargar los datos en la tabla (grilla)
            cargarDatosTabla();
            
            // Limpiar campos del área de Registro
            limpiarCampos();
            
            // Desactivar el modo de actualización
            configurarEstadoInicial();
            
            JOptionPane.showMessageDialog(this, "Operación '" + operacionActual + "' realizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ejecutarAdicionar(String codigo, String descripcion, String estado) throws SQLException {
        String consulta = "INSERT INTO " + nombreTabla + " (" + campoId + ", " + campoDescripcion + ", " + campoEstado + ") VALUES (?, ?, ?)";
        db.ejecutarActualizacionPreparada(consulta, codigo, descripcion, estado);
    }
    
    private void ejecutarModificar(String codigo, String descripcion, String estado) throws SQLException {
        String consulta = "UPDATE " + nombreTabla + " SET " + campoDescripcion + " = ? WHERE " + campoId + " = ?";
        db.ejecutarActualizacionPreparada(consulta, descripcion, codigo);
    }
    
    private void ejecutarCambioEstado(String codigo, String descripcion, String estado) throws SQLException {
        String consulta = "UPDATE " + nombreTabla + " SET " + campoEstado + " = ? WHERE " + campoId + " = ?";
        db.ejecutarActualizacionPreparada(consulta, estado, codigo);
    }
    
    private boolean existeCodigo(String codigo) throws SQLException {
        String consulta = "SELECT COUNT(*) FROM " + nombreTabla + " WHERE " + campoId + " = ?";
        ResultSet rs = db.ejecutarConsultaPreparada(consulta, codigo);
        
        if (rs.next()) {
            int count = rs.getInt(1);
            rs.close();
            return count > 0;
        }
        rs.close();
        return false;
    }
    
    private void cargarDatosTabla() {
        try {
            // Limpiar tabla
            modeloTabla.setRowCount(0);
            
            // Verificar si la columna EstadoRegistro existe
            if (!verificarColumnaExiste(campoEstado)) {
                // Si no existe la columna, agregarla
                agregarColumnaEstadoRegistro();
            }
            
            String consulta = "SELECT " + campoId + ", " + campoDescripcion + ", " + campoEstado + 
                             " FROM " + nombreTabla + " ORDER BY " + campoId;
            ResultSet rs = db.ejecutarConsulta(consulta);
            
            boolean hayDatos = false;
            while (rs.next()) {
                hayDatos = true;
                Object[] fila = {
                    rs.getString(campoId),
                    rs.getString(campoDescripcion),
                    rs.getString(campoEstado) != null ? rs.getString(campoEstado) : "A"
                };
                modeloTabla.addRow(fila);
            }
            rs.close();
            
            if (!hayDatos) {
                System.out.println("No se encontraron registros en la tabla " + nombreTabla);
            } else {
                System.out.println("Cargados " + modeloTabla.getRowCount() + " registros de la tabla " + nombreTabla);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos de la tabla " + nombreTabla + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private boolean verificarColumnaExiste(String nombreColumna) {
        try {
            String consulta = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS " +
                             "WHERE TABLE_SCHEMA = 'datospersonales' AND TABLE_NAME = ? AND COLUMN_NAME = ?";
            ResultSet rs = db.ejecutarConsultaPreparada(consulta, nombreTabla, nombreColumna);
            boolean existe = rs.next();
            rs.close();
            return existe;
        } catch (SQLException e) {
            System.err.println("Error verificando columna: " + e.getMessage());
            return false;
        }
    }
    
    private void agregarColumnaEstadoRegistro() {
        try {
            String alterTable = "ALTER TABLE " + nombreTabla + " ADD COLUMN " + campoEstado + " CHAR(1) DEFAULT 'A'";
            db.ejecutarActualizacion(alterTable);
            
            // Actualizar registros existentes
            String updateExistentes = "UPDATE " + nombreTabla + " SET " + campoEstado + " = 'A' WHERE " + campoEstado + " IS NULL";
            db.ejecutarActualizacion(updateExistentes);
            
            System.out.println("Columna " + campoEstado + " agregada exitosamente a la tabla " + nombreTabla);
            
        } catch (SQLException e) {
            System.err.println("Error agregando columna EstadoRegistro: " + e.getMessage());
        }
    }
    
    private void accionSalir() {
        if (CarFlaAct == 1) {
            int opcion = JOptionPane.showConfirmDialog(
                this, 
                "Hay una operación pendiente. ¿Desea cancelarla y salir?", 
                "Confirmar Salida", 
                JOptionPane.YES_NO_OPTION
            );
            if (opcion == JOptionPane.NO_OPTION) {
                return;
            }
        }
        
        int opcion = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro que desea salir?", 
            "Confirmar Salida", 
            JOptionPane.YES_NO_OPTION
        );
        if (opcion == JOptionPane.YES_OPTION) {
            db.cerrarConexion();
            System.exit(0);
        }
    }
    
    // Método main para probar la interfaz
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Verificar conexión
            if (!ConexionDB.probarConexion()) {
                JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Crear ventana para tabla TIPO_FORMACION
            DatabaseManagerGUI gui = new DatabaseManagerGUI(
                "TIPO_FORMACION", 
                "PKCodTipoFor", 
                "DescripcionTipoFormacion",
                "EstadoRegistro"
            );
            gui.setVisible(true);
        });
    }
}
