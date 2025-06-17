package gui;

import dao.TablaReferencialDAO;
import models.RegistroReferencial;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interfaz gráfica para gestionar tablas referenciales
 */
public class GestorTablaReferencial extends JFrame {
    
    private static final Logger LOGGER = Logger.getLogger(GestorTablaReferencial.class.getName());
    
    // Componentes de la interfaz
    private JLabel lblTitulo;
    private JTextField txtCodigo;
    private JTextField txtDescripcion;
    private JTextField txtEstado;
    private JTable tablaRegistros;
    private DefaultTableModel modeloTabla;
    
    // Botones
    private JButton btnAdicionar, btnModificar, btnEliminar, btnCancelar;
    private JButton btnInactivar, btnReactivar, btnActualizar, btnSalir;
    
    // Variables de control
    private TablaReferencialDAO dao;
    private String nombreTabla;
    private int CarFlaAct = 0; // Flag de actualización
    private String operacionActual = "";
    
    public GestorTablaReferencial(String nombreTabla, String campoId, String campoDescripcion) {
        this.nombreTabla = nombreTabla;
        this.dao = new TablaReferencialDAO(nombreTabla, campoId, campoDescripcion);
        
        initComponents();
        configurarEventos();
        cargarDatos();
        configurarEstadoInicial();
    }
    
    private void initComponents() {
        setTitle("Gestor de Tabla Referencial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel superior - Título
        JPanel panelTitulo = new JPanel();
        lblTitulo = new JLabel("Gestión de Tabla: " + nombreTabla.toUpperCase());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelTitulo.add(lblTitulo);
        
        // Panel central - Datos y tabla
        JPanel panelCentral = new JPanel(new BorderLayout());
        
        // Sección 1: Datos del registro
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos del Registro"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Código
        gbc.gridx = 0; gbc.gridy = 0;
        panelDatos.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(10);
        txtCodigo.setEditable(false);
        panelDatos.add(txtCodigo, gbc);
        
        // Descripción
        gbc.gridx = 0; gbc.gridy = 1;
        panelDatos.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        txtDescripcion = new JTextField(30);
        panelDatos.add(txtDescripcion, gbc);
        
        // Estado
        gbc.gridx = 0; gbc.gridy = 2;
        panelDatos.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        txtEstado = new JTextField(5);
        txtEstado.setEditable(false);
        panelDatos.add(txtEstado, gbc);
        
        // Sección 2: Tabla de registros
        String[] columnas = {"Código", "Descripción", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaRegistros = new JTable(modeloTabla);
        tablaRegistros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabla = new JScrollPane(tablaRegistros);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Registros Actuales"));
        scrollTabla.setPreferredSize(new Dimension(500, 200));
        
        panelCentral.add(panelDatos, BorderLayout.NORTH);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        
        // Sección 3: Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBorder(BorderFactory.createTitledBorder("Operaciones"));
        
        btnAdicionar = new JButton("Adicionar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnCancelar = new JButton("Cancelar");
        btnInactivar = new JButton("Inactivar");
        btnReactivar = new JButton("Reactivar");
        btnActualizar = new JButton("Actualizar");
        btnSalir = new JButton("Salir");
        
        panelBotones.add(btnAdicionar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnInactivar);
        panelBotones.add(btnReactivar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnSalir);
        
        // Agregar componentes al frame
        add(panelTitulo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void configurarEventos() {
        // Selección en la tabla
        tablaRegistros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = tablaRegistros.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    cargarRegistroSeleccionado(filaSeleccionada);
                }
            }
        });
        
        // Botones
        btnAdicionar.addActionListener(e -> accionAdicionar());
        btnModificar.addActionListener(e -> accionModificar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnCancelar.addActionListener(e -> accionCancelar());
        btnInactivar.addActionListener(e -> accionInactivar());
        btnReactivar.addActionListener(e -> accionReactivar());
        btnActualizar.addActionListener(e -> accionActualizar());
        btnSalir.addActionListener(e -> accionSalir());
    }
    
    private void cargarDatos() {
        try {
            List<RegistroReferencial> registros = dao.obtenerTodos();
            modeloTabla.setRowCount(0);
            
            for (RegistroReferencial registro : registros) {
                Object[] fila = {
                    registro.getCodigo(),
                    registro.getDescripcion(),
                    registro.getEstadoRegistro()
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar datos", e);
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarRegistroSeleccionado(int fila) {
        txtCodigo.setText(modeloTabla.getValueAt(fila, 0).toString());
        txtDescripcion.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtEstado.setText(modeloTabla.getValueAt(fila, 2).toString());
    }
    
    private void limpiarCampos() {
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtEstado.setText("");
        tablaRegistros.clearSelection();
    }
    
    private void configurarEstadoInicial() {
        txtDescripcion.setEditable(false);
        btnActualizar.setEnabled(false);
        CarFlaAct = 0;
        operacionActual = "";
    }
    
    // Acciones de los botones
    private void accionAdicionar() {
        limpiarCampos();
        txtEstado.setText("A");
        txtDescripcion.setEditable(true);
        txtDescripcion.requestFocus();
        CarFlaAct = 1;
        operacionActual = "ADICIONAR";
        btnActualizar.setEnabled(true);
        habilitarSoloBotonesNecesarios(false);
    }
    
    private void accionModificar() {
        if (tablaRegistros.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un registro para modificar", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        txtDescripcion.setEditable(true);
        txtDescripcion.requestFocus();
        CarFlaAct = 1;
        operacionActual = "MODIFICAR";
        btnActualizar.setEnabled(true);
        habilitarSoloBotonesNecesarios(false);
    }
    
    private void accionEliminar() {
        if (tablaRegistros.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un registro para eliminar", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        txtDescripcion.setEditable(false);
        txtEstado.setText("*");
        CarFlaAct = 1;
        operacionActual = "ELIMINAR";
        btnActualizar.setEnabled(true);
        habilitarSoloBotonesNecesarios(false);
    }
    
    private void accionInactivar() {
        if (tablaRegistros.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un registro para inactivar", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        txtDescripcion.setEditable(false);
        txtEstado.setText("I");
        CarFlaAct = 1;
        operacionActual = "INACTIVAR";
        btnActualizar.setEnabled(true);
        habilitarSoloBotonesNecesarios(false);
    }
    
    private void accionReactivar() {
        if (tablaRegistros.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un registro para reactivar", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        txtDescripcion.setEditable(false);
        txtEstado.setText("A");
        CarFlaAct = 1;
        operacionActual = "REACTIVAR";
        btnActualizar.setEnabled(true);
        habilitarSoloBotonesNecesarios(false);
    }
    
    private void accionActualizar() {
        if (CarFlaAct != 1) {
            JOptionPane.showMessageDialog(this, "No hay operación pendiente para actualizar", 
                                        "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            switch (operacionActual) {
                case "ADICIONAR":
                    realizarInsercion();
                    break;
                case "MODIFICAR":
                    realizarModificacion();
                    break;
                case "ELIMINAR":
                    realizarEliminacion();
                    break;
                case "INACTIVAR":
                    realizarInactivacion();
                    break;
                case "REACTIVAR":
                    realizarReactivacion();
                    break;
            }
            
            cargarDatos();
            accionCancelar();
            JOptionPane.showMessageDialog(this, "Operación realizada exitosamente", 
                                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar", e);
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void accionCancelar() {
        limpiarCampos();
        configurarEstadoInicial();
        habilitarSoloBotonesNecesarios(true);
    }
    
    private void accionSalir() {
        if (CarFlaAct == 1) {
            int opcion = JOptionPane.showConfirmDialog(this, 
                "Hay una operación pendiente. ¿Desea salir sin guardar?", 
                "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (opcion != JOptionPane.YES_OPTION) {
                return;
            }
        }
        System.exit(0);
    }
    
    // Métodos auxiliares
    private void habilitarSoloBotonesNecesarios(boolean estadoNormal) {
        btnAdicionar.setEnabled(estadoNormal);
        btnModificar.setEnabled(estadoNormal);
        btnEliminar.setEnabled(estadoNormal);
        btnInactivar.setEnabled(estadoNormal);
        btnReactivar.setEnabled(estadoNormal);
    }
    
    private void realizarInsercion() throws SQLException {
        if (txtDescripcion.getText().trim().isEmpty()) {
            throw new SQLException("La descripción no puede estar vacía");
        }
        
        RegistroReferencial registro = new RegistroReferencial();
        registro.setDescripcion(txtDescripcion.getText().trim());
        registro.setEstadoRegistro(txtEstado.getText());
        
        dao.insertar(registro);
    }
    
    private void realizarModificacion() throws SQLException {
        if (txtDescripcion.getText().trim().isEmpty()) {
            throw new SQLException("La descripción no puede estar vacía");
        }
        
        RegistroReferencial registro = new RegistroReferencial();
        registro.setCodigo(Integer.parseInt(txtCodigo.getText()));
        registro.setDescripcion(txtDescripcion.getText().trim());
        registro.setEstadoRegistro(txtEstado.getText());
        
        dao.actualizar(registro);
    }
    
    private void realizarEliminacion() throws SQLException {
        int codigo = Integer.parseInt(txtCodigo.getText());
        dao.eliminar(codigo);
    }
    
    private void realizarInactivacion() throws SQLException {
        int codigo = Integer.parseInt(txtCodigo.getText());
        dao.inactivar(codigo);
    }
    
    private void realizarReactivacion() throws SQLException {
        int codigo = Integer.parseInt(txtCodigo.getText());
        dao.reactivar(codigo);
    }
}
