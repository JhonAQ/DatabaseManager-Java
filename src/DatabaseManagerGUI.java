package db;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Interfaz gráfica para gestionar tablas referenciales
 */
public class DatabaseManagerGUI extends JFrame {
    
    private String nombreTabla;
    private JTextField txtCodigo;
    private JTextField txtDescripcion;
    private JComboBox<String> cmbEstado;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    
    // Botones
    private JButton btnAdicionar, btnModificar, btnEliminar, btnCancelar;
    private JButton btnInactivar, btnReactivar, btnActualizar, btnSalir;
    
    public DatabaseManagerGUI(String nombreTabla) {
        this.nombreTabla = nombreTabla;
        initComponents();
        setupLayout();
        setupEventListeners();
    }
    
    private void initComponents() {
        setTitle("Gestor de " + nombreTabla);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Campos del formulario
        txtCodigo = new JTextField(10);
        txtDescripcion = new JTextField(30);
        cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        
        // Modelo de la tabla
        String[] columnas = {"Código", "Descripción", "Estado de Registro"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa en la tabla
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
        
        // Panel principal con las 3 secciones
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
        scrollPane.setPreferredSize(new Dimension(750, 200));
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
        
        // Agregar las secciones al panel principal
        panelPrincipal.add(panelRegistro, BorderLayout.NORTH);
        panelPrincipal.add(panelTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    private void setupEventListeners() {
        // Evento para seleccionar fila de la tabla
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
            }
        });
        
        // Eventos de botones (por ahora solo mensajes)
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
        if (filaSeleccionada >= 0) {
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
    
    // Métodos de acciones (por implementar la lógica específica)
    private void accionAdicionar() {
        JOptionPane.showMessageDialog(this, "Función Adicionar - Por implementar");
    }
    
    private void accionModificar() {
        JOptionPane.showMessageDialog(this, "Función Modificar - Por implementar");
    }
    
    private void accionEliminar() {
        JOptionPane.showMessageDialog(this, "Función Eliminar - Por implementar");
    }
    
    private void accionCancelar() {
        limpiarCampos();
    }
    
    private void accionInactivar() {
        JOptionPane.showMessageDialog(this, "Función Inactivar - Por implementar");
    }
    
    private void accionReactivar() {
        JOptionPane.showMessageDialog(this, "Función Reactivar - Por implementar");
    }
    
    private void accionActualizar() {
        JOptionPane.showMessageDialog(this, "Función Actualizar - Por implementar");
    }
    
    private void accionSalir() {
        int opcion = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro que desea salir?", 
            "Confirmar Salida", 
            JOptionPane.YES_NO_OPTION
        );
        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    // Método para cargar datos de ejemplo en la tabla
    public void cargarDatosEjemplo() {
        modeloTabla.addRow(new Object[]{"001", "Ejemplo 1", "Activo"});
        modeloTabla.addRow(new Object[]{"002", "Ejemplo 2", "Activo"});
        modeloTabla.addRow(new Object[]{"003", "Ejemplo 3", "Inactivo"});
    }
    
    // Método main para probar la interfaz
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            DatabaseManagerGUI gui = new DatabaseManagerGUI("TIPO_FORMACION");
            gui.cargarDatosEjemplo();
            gui.setVisible(true);
        });
    }
}
