package facturacrudinterface;

import conexion.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FacturaCRUDInterface {

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel sumaLabel;
    private JLabel ivaLabel;
    private JLabel totalLabel;
    private JTextField productoField;
    private JTextField descripcionField;
    private JTextField uniMedField;
    private JTextField cantidadField;
    private JTextField valUniField;
    private JTextField clienteField;

    private JLabel facturaNoLabel;
    private JLabel fechaLabel;
    private JComboBox<String> metodoPagoComboBox;
    private JComboBox<String> ivaComboBox;

    private Connection connection;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FacturaCRUDInterface app = new FacturaCRUDInterface();
            app.createAndShowGUI();
        });
    }

    public FacturaCRUDInterface() {
        connection = Conexion.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Factura - Productos (INTERFAZ CRUD)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("SAMAEL SOFT", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.RED);
        panel.add(title, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.YELLOW);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        facturaNoLabel = new JLabel("Factura No.: FAC-001");
        clienteField = new JTextField(50);
        clienteField.setMaximumSize(clienteField.getPreferredSize());
        fechaLabel = new JLabel("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        infoPanel.add(facturaNoLabel);
        infoPanel.add(new JLabel("Cliente:"));
        infoPanel.add(clienteField);
        infoPanel.add(fechaLabel);
        infoPanel.add(new JLabel("Código SRI: 001-001-009"));

        panel.add(infoPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Producto", "Descripción", "Uni Med", "Cantidad", "Val Uni", "Subtotal"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(Color.LIGHT_GRAY);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel fieldsPanel = new JPanel(new GridLayout(7, 10, 10, 10));
        fieldsPanel.setBackground(Color.LIGHT_GRAY);

        productoField = new JTextField();
        descripcionField = new JTextField();
        uniMedField = new JTextField();
        cantidadField = new JTextField();
        valUniField = new JTextField();
        ivaComboBox = new JComboBox<>(new String[]{"0","8", "12", "15"});

        fieldsPanel.add(new JLabel("Producto:"));
        fieldsPanel.add(productoField);
        fieldsPanel.add(new JLabel("Descripción:"));
        fieldsPanel.add(descripcionField);
        fieldsPanel.add(new JLabel("Uni Med:"));
        fieldsPanel.add(uniMedField);
        fieldsPanel.add(new JLabel("Cantidad:"));
        fieldsPanel.add(cantidadField);
        fieldsPanel.add(new JLabel("Val Uni:"));
        fieldsPanel.add(valUniField);
        fieldsPanel.add(new JLabel("IVA:"));
        fieldsPanel.add(ivaComboBox);

        JLabel metodoPagoLabel = new JLabel("Método de Pago:");
        String[] metodosPago = {"EFECT", "TARCR", "TARDB", "TRANSF", "CHEQ"};
        metodoPagoComboBox = new JComboBox<>(metodosPago);
        fieldsPanel.add(metodoPagoLabel);
        fieldsPanel.add(metodoPagoComboBox);

        inputPanel.add(fieldsPanel);

        JPanel buttonsPanel = new JPanel(new GridLayout(5, 0, 2, 5));
        buttonsPanel.setBackground(Color.LIGHT_GRAY);

        JButton searchButton = new JButton("Buscar Producto");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 10));
        searchButton.setPreferredSize(new Dimension(120, 30));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProducto(productoField);
            }
        });

        JButton addButton = new JButton("Agregar Producto");
        addButton.setFont(new Font("Arial", Font.PLAIN, 10));
        addButton.setPreferredSize(new Dimension(120, 30));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto(productoField, descripcionField, uniMedField, cantidadField, valUniField);
            }
        });

        JButton updateButton = new JButton("Actualizar Producto");
        updateButton.setFont(new Font("Arial", Font.PLAIN, 10));
        updateButton.setPreferredSize(new Dimension(120, 30));
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarProducto(productoField, descripcionField, uniMedField, cantidadField, valUniField);
            }
        });

        JButton deleteButton = new JButton("Eliminar Producto");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 10));
        deleteButton.setPreferredSize(new Dimension(120, 30));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProducto();
            }
        });

        JButton readButton = new JButton("Leer Producto");
        readButton.setFont(new Font("Arial", Font.PLAIN, 11));
        readButton.setPreferredSize(new Dimension(120, 30));
        readButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leerProducto(productoField, descripcionField, uniMedField, cantidadField, valUniField);
            }
        });

        JButton generateButton = new JButton("Generar Factura Final");
        generateButton.setFont(new Font("Arial", Font.PLAIN, 10));
        generateButton.setPreferredSize(new Dimension(120, 30));
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarFacturaFinal();
            }
        });

        JButton detallesButton = new JButton("Detalles de Factura");
        detallesButton.setFont(new Font("Arial", Font.PLAIN, 10));
        detallesButton.setPreferredSize(new Dimension(120, 30));
        detallesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDetallesFactura();
            }
        });

        buttonsPanel.add(searchButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(readButton);
        buttonsPanel.add(generateButton);
        buttonsPanel.add(detallesButton);

        inputPanel.add(buttonsPanel);

        mainPanel.add(inputPanel, BorderLayout.EAST);
        panel.add(mainPanel, BorderLayout.CENTER);

        JPanel totalPanel = new JPanel(new GridLayout(3, 2));
        totalPanel.setBackground(Color.YELLOW);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        totalPanel.add(new JLabel("SUMA"));
        sumaLabel = new JLabel("$0.00");
        totalPanel.add(sumaLabel);
        totalPanel.add(new JLabel("IVA"));
        ivaLabel = new JLabel("$0.00");
        totalPanel.add(ivaLabel);
        totalPanel.add(new JLabel("TOTAL"));
        totalLabel = new JLabel("$0.00");
        totalPanel.add(totalLabel);

        panel.add(totalPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);

        actualizarFacturaInfo();
    }

    private void buscarProducto(JTextField productoField) {
        String productoCodigo = productoField.getText();
        String query = "SELECT * FROM PRODUCTOS WHERE PROCODIGO = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, productoCodigo);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    descripcionField.setText(resultSet.getString("PRODESCRIPCION"));
                    uniMedField.setText(resultSet.getString("PROUNIDADMEDIDA"));
                    valUniField.setText(resultSet.getString("PROPRECIOUM"));
                } else {
                    JOptionPane.showMessageDialog(null, "Producto no encontrado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void agregarProducto(JTextField productoField, JTextField descripcionField, JTextField uniMedField, JTextField cantidadField, JTextField valUniField) {
        if (productoField.getText().isEmpty() || descripcionField.getText().isEmpty() || uniMedField.getText().isEmpty() ||
            cantidadField.getText().isEmpty() || valUniField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos para agregar un producto.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadField.getText());
            if (cantidad < 0) {
                JOptionPane.showMessageDialog(null, "La cantidad no puede ser negativa.");
                return;
            }
        
            String producto = productoField.getText();
            String descripcion = descripcionField.getText();
            String uniMed = uniMedField.getText();
            double valUni = Double.parseDouble(valUniField.getText());
            double subtotal = cantidad * valUni;

            tableModel.addRow(new Object[]{producto, descripcion, uniMed, cantidad, "$" + valUni, "$" + subtotal});

            productoField.setText("");
            descripcionField.setText("");
            uniMedField.setText("");
            cantidadField.setText("");
            valUniField.setText("");

            actualizarTotales();
        } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Cantidad y Valor Unitario deben ser números válidos.");
    }
}

    private void actualizarProducto(JTextField productoField, JTextField descripcionField, JTextField uniMedField, JTextField cantidadField, JTextField valUniField) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String producto = productoField.getText();
            String descripcion = descripcionField.getText();
            String uniMed = uniMedField.getText();
            int cantidad = Integer.parseInt(cantidadField.getText());
            double valUni = Double.parseDouble(valUniField.getText());
            double subtotal = cantidad * valUni;

            tableModel.setValueAt(producto, selectedRow, 0);
            tableModel.setValueAt(descripcion, selectedRow, 1);
            tableModel.setValueAt(uniMed, selectedRow, 2);
            tableModel.setValueAt(cantidad, selectedRow, 3);
            tableModel.setValueAt("$" + valUni, selectedRow, 4);
            tableModel.setValueAt("$" + subtotal, selectedRow, 5);

            productoField.setText("");
            descripcionField.setText("");
            uniMedField.setText("");
            cantidadField.setText("");
            valUniField.setText("");

            actualizarTotales();
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un producto para actualizar");
        }
    }

    private void eliminarProducto() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar el producto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
                actualizarTotales();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un producto para eliminar");
        }
    }

    private void leerProducto(JTextField productoField, JTextField descripcionField, JTextField uniMedField, JTextField cantidadField, JTextField valUniField) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            productoField.setText((String) tableModel.getValueAt(selectedRow, 0));
            descripcionField.setText((String) tableModel.getValueAt(selectedRow, 1));
            uniMedField.setText((String) tableModel.getValueAt(selectedRow, 2));
            cantidadField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
            valUniField.setText(((String) tableModel.getValueAt(selectedRow, 4)).replace("$", ""));
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un producto para leer");
        }
    }

    private void actualizarTotales() {
        double suma = 0.0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String subtotalStr = (String) tableModel.getValueAt(i, 5);
            double subtotal = Double.parseDouble(subtotalStr.replace("$", ""));
            suma += subtotal;
        }

        double iva = suma * (Integer.parseInt((String) ivaComboBox.getSelectedItem()) / 100.0);
        double total = suma + iva;

        sumaLabel.setText(String.format("$%.2f", suma));
        ivaLabel.setText(String.format("$%.2f", iva));
        totalLabel.setText(String.format("$%.2f", total));
    }

    private void generarFacturaFinal() {
        String facturaNo = generarNuevoNumeroFactura();
        String clienteNombre = clienteField.getText();
        String clienteCodigo = getClienteCodigo(clienteNombre);
        if (clienteCodigo == null) {
            int response = JOptionPane.showConfirmDialog(null, "El cliente no existe. ¿Desea agregar un nuevo cliente?", "Agregar nuevo cliente", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                mostrarVentanaAgregarCliente();
            }
            return;
        }
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        double suma = Double.parseDouble(sumaLabel.getText().replace("$", ""));
        double iva = Double.parseDouble(ivaLabel.getText().replace("$", ""));
        String metodoPago = (String) metodoPagoComboBox.getSelectedItem();

        try {
            String insertFacturaSQL = "INSERT INTO FACTURAS (FACNUMERO, CLICODIGO, FACFECHA, FACSUBTOTAL, FACIVA, FACSTATUS, FACFORMAPAGO, FACACTIVO) VALUES (?, ?, ?, ?, ?, 'PAG', ?, 'ACT')";
            try (PreparedStatement insertFacturaStmt = connection.prepareStatement(insertFacturaSQL)) {
                insertFacturaStmt.setString(1, facturaNo);
                insertFacturaStmt.setString(2, clienteCodigo);
                insertFacturaStmt.setString(3, fecha);
                insertFacturaStmt.setDouble(4, suma);
                insertFacturaStmt.setDouble(5, iva);
                insertFacturaStmt.setString(6, metodoPago);
                insertFacturaStmt.executeUpdate();
            }

            String insertPXFSQL = "INSERT INTO PXF (FACNUMERO, PROCODIGO, PXFCANTIDAD, PXFVALOR, PXFSUBTOTAL, PXFSTATUS) VALUES (?, ?, ?, ?, ?, 'ACT')";
            try (PreparedStatement insertPXFStmt = connection.prepareStatement(insertPXFSQL)) {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String procodigo = (String) tableModel.getValueAt(i, 0);
                    int cantidad = Integer.parseInt(tableModel.getValueAt(i, 3).toString());
                    double valor = Double.parseDouble(((String) tableModel.getValueAt(i, 4)).replace("$", ""));
                    double subtotal = Double.parseDouble(((String) tableModel.getValueAt(i, 5)).replace("$", ""));

                    insertPXFStmt.setString(1, facturaNo);
                    insertPXFStmt.setString(2, procodigo);
                    insertPXFStmt.setDouble(3, cantidad);
                    insertPXFStmt.setDouble(4, valor);
                    insertPXFStmt.setDouble(5, subtotal);
                    insertPXFStmt.addBatch();
                }
                insertPXFStmt.executeBatch();
            }

            connection.commit();

            JFrame facturaFrame = new JFrame("Factura Final");
            facturaFrame.setSize(600, 400);
            facturaFrame.setLayout(new BorderLayout());

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(Color.YELLOW);
            infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            infoPanel.add(new JLabel("Factura No.: " + facturaNo));
            infoPanel.add(new JLabel("Cliente: " + clienteNombre));
            infoPanel.add(new JLabel("Fecha: " + fecha));
            infoPanel.add(new JLabel("Código SRI: 001-001-009"));

            facturaFrame.add(infoPanel, BorderLayout.NORTH);

            JTable facturaTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(facturaTable);
            facturaFrame.add(scrollPane, BorderLayout.CENTER);

            JPanel totalPanel = new JPanel(new GridLayout(3, 2));
            totalPanel.setBackground(Color.YELLOW);
            totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            totalPanel.add(new JLabel("SUMA"));
            totalPanel.add(new JLabel("$" + suma));
            totalPanel.add(new JLabel("IVA"));
            totalPanel.add(new JLabel("$" + iva));
            totalPanel.add(new JLabel("TOTAL"));
            totalPanel.add(new JLabel("$" + (suma + iva)));

            facturaFrame.add(totalPanel, BorderLayout.SOUTH);

            facturaFrame.setVisible(true);

            actualizarFacturaInfo();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private String generarNuevoNumeroFactura() {
        String nuevoNumero = "FAC-001";
        String query = "SELECT MAX(FACNUMERO) FROM FACTURAS";
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                String ultimoNumero = resultSet.getString(1);
                if (ultimoNumero != null) {
                    int numero = Integer.parseInt(ultimoNumero.split("-")[1]);
                    nuevoNumero = String.format("FAC-%03d", numero + 1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nuevoNumero;
    }
    
    private String getClienteNombre(String clienteCodigo) {
        String query = "SELECT CLINOMBRE FROM CLIENTES WHERE CLICODIGO = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, clienteCodigo);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("CLINOMBRE");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getClienteCodigo(String clienteNombre) {
        String query = "SELECT CLICODIGO FROM CLIENTES WHERE CLINOMBRE = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, clienteNombre);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("CLICODIGO");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void actualizarFacturaInfo() {
        facturaNoLabel.setText("Factura No.: " + generarNuevoNumeroFactura());
        clienteField.setText("");
        fechaLabel.setText("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    private void mostrarVentanaAgregarCliente() {
        AgregarCliente agregarCliente = new AgregarCliente();
        agregarCliente.mostrarVentanaAgregarCliente(new ClienteAgregadoListener() {
            @Override
            public void clienteAgregado(String codigo, String nombreCompleto) {
                clienteField.setText(nombreCompleto);
            }
        });
    }

    private void mostrarDetallesFactura() {
        JFrame detallesFrame = new JFrame("Detalles de Factura");
        detallesFrame.setSize(800, 400);

        String[] columnNames = {"Factura No.", "Cliente", "Fecha", "Subtotal", "IVA", "Estado", "Método de Pago", "Activo"};
        DefaultTableModel detallesTableModel = new DefaultTableModel(columnNames, 0);
        JTable detallesTable = new JTable(detallesTableModel);
        JScrollPane scrollPane = new JScrollPane(detallesTable);
        detallesFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout());
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"PEN", "PAG", "CANC"});
        JButton updateStatusButton = new JButton("Actualizar Estado");
        updateStatusButton.addActionListener(e -> {
            int selectedRow = detallesTable.getSelectedRow();
            if (selectedRow >= 0) {
                String facturaNo = (String) detallesTableModel.getValueAt(selectedRow, 0);
                String nuevoStatus = (String) statusComboBox.getSelectedItem();
                actualizarEstadoFactura(facturaNo, nuevoStatus);
                cargarDetallesFactura(detallesTableModel);
            } else {
                JOptionPane.showMessageDialog(detallesFrame, "Seleccione una factura para actualizar el estado.");
            }
        });

        JButton eliminarFacturaButton = new JButton("Eliminar Factura");
        eliminarFacturaButton.addActionListener(e -> {
            int selectedRow = detallesTable.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(detallesFrame, "¿Está seguro de que desea eliminar la factura?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String facturaNo = (String) detallesTableModel.getValueAt(selectedRow, 0);
                    actualizarEstadoActivoFactura(facturaNo, "ANU");
                    cargarDetallesFactura(detallesTableModel);
                }
            } else {
                JOptionPane.showMessageDialog(detallesFrame, "Seleccione una factura para eliminar.");
            }
        });

        JButton previsualizarButton = new JButton("Previsualizar Factura");
        previsualizarButton.addActionListener(e -> {
            int selectedRow = detallesTable.getSelectedRow();
            if (selectedRow >= 0) {
                String facturaNo = (String) detallesTableModel.getValueAt(selectedRow, 0);
                previsualizarFactura(facturaNo);
            } else {
                JOptionPane.showMessageDialog(detallesFrame, "Seleccione una factura para previsualizar.");
            }
        });

        JButton editarButton = new JButton("Editar Factura");
        editarButton.addActionListener(e -> {
            int selectedRow = detallesTable.getSelectedRow();
            if (selectedRow >= 0) {
                String facturaNo = (String) detallesTableModel.getValueAt(selectedRow, 0);
                editarFactura(facturaNo);
                detallesFrame.dispose(); // Close the detalles frame
            } else {
                JOptionPane.showMessageDialog(detallesFrame, "Seleccione una factura para editar.");
            }
        });

        statusPanel.add(new JLabel("Actualizar Estado:"), BorderLayout.WEST);
        statusPanel.add(statusComboBox, BorderLayout.CENTER);
        statusPanel.add(updateStatusButton, BorderLayout.EAST);
        statusPanel.add(previsualizarButton, BorderLayout.NORTH);
        statusPanel.add(eliminarFacturaButton, BorderLayout.SOUTH);
        statusPanel.add(editarButton, BorderLayout.WEST);
        detallesFrame.add(statusPanel, BorderLayout.SOUTH);

        cargarDetallesFactura(detallesTableModel);

        detallesFrame.setVisible(true);
    }

    private void cargarDetallesFactura(DefaultTableModel detallesTableModel) {
        detallesTableModel.setRowCount(0); // Clear existing data
        String query = "SELECT * FROM FACTURAS";
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String facturaNo = resultSet.getString("FACNUMERO");
                String cliente = resultSet.getString("CLICODIGO");
                String fecha = resultSet.getString("FACFECHA");
                double subtotal = resultSet.getDouble("FACSUBTOTAL");
                double iva = resultSet.getDouble("FACIVA");
                String estado = resultSet.getString("FACSTATUS");
                String metodoPago = resultSet.getString("FACFORMAPAGO");
                String activo = resultSet.getString("FACACTIVO");

                detallesTableModel.addRow(new Object[]{facturaNo, cliente, fecha, subtotal, iva, estado, metodoPago, activo});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void actualizarEstadoActivoFactura(String facturaNo, String nuevoActivo) {
        String updateSQL = "UPDATE FACTURAS SET FACACTIVO = ? WHERE FACNUMERO = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, nuevoActivo);
            statement.setString(2, facturaNo);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("El estado activo de la factura se ha actualizado correctamente a " + nuevoActivo);
                connection.commit(); // Confirmar la transacción
            } else {
                System.out.println("No se encontró la factura con el número: " + facturaNo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al actualizar el estado activo de la factura: " + e.getMessage());
            try {
                connection.rollback(); // Revertir la transacción en caso de error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    private void actualizarEstadoFactura(String facturaNo, String nuevoEstado) {
        String updateSQL = "UPDATE FACTURAS SET FACSTATUS = ? WHERE FACNUMERO = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, nuevoEstado);
            statement.setString(2, facturaNo);
            statement.executeUpdate();
            connection.commit(); // Asegúrate de que los cambios se confirmen en la base de datos
        } catch (SQLException e) {
            try {
                connection.rollback(); // Revierte los cambios en caso de error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void previsualizarFactura(String facturaNo) {
        try {
            String query = "SELECT * FROM FACTURAS WHERE FACNUMERO = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, facturaNo);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String clienteCodigo = resultSet.getString("CLICODIGO");
                        String clienteNombre = getClienteNombre(clienteCodigo);
                        String fecha = resultSet.getString("FACFECHA");
                        double subtotal = resultSet.getDouble("FACSUBTOTAL");
                        double iva = resultSet.getDouble("FACIVA");
                        String metodoPago = resultSet.getString("FACFORMAPAGO");

                        JFrame facturaFrame = new JFrame("Previsualización de Factura");
                        facturaFrame.setSize(600, 400);
                        facturaFrame.setLayout(new BorderLayout());

                        JPanel infoPanel = new JPanel();
                        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                        infoPanel.setBackground(Color.YELLOW);
                        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                        infoPanel.add(new JLabel("Factura No.: " + facturaNo));
                        infoPanel.add(new JLabel("Cliente: " + clienteNombre));
                        infoPanel.add(new JLabel("Fecha: " + fecha));
                        infoPanel.add(new JLabel("Método de Pago: " + metodoPago));

                        facturaFrame.add(infoPanel, BorderLayout.NORTH);

                        String[] columnNames = {"Producto", "Descripción", "Uni Med", "Cantidad", "Val Uni", "Subtotal"};
                        DefaultTableModel facturaTableModel = new DefaultTableModel(columnNames, 0);

                        query = "SELECT * FROM PXF WHERE FACNUMERO = ?";
                        try (PreparedStatement pxfStatement = connection.prepareStatement(query)) {
                            pxfStatement.setString(1, facturaNo);
                            try (ResultSet pxfResultSet = pxfStatement.executeQuery()) {
                                while (pxfResultSet.next()) {
                                    String producto = pxfResultSet.getString("PROCODIGO");
                                    int cantidad = pxfResultSet.getInt("PXFCANTIDAD");
                                    double valUni = pxfResultSet.getDouble("PXFVALOR");
                                    double subtotalProd = pxfResultSet.getDouble("PXFSUBTOTAL");
                                    facturaTableModel.addRow(new Object[]{producto, "Descripción no disponible", "Unidad no disponible", cantidad, "$" + valUni, "$" + subtotalProd});
                                }
                            }
                        }

                        JTable facturaTable = new JTable(facturaTableModel);
                        JScrollPane scrollPane = new JScrollPane(facturaTable);
                        facturaFrame.add(scrollPane, BorderLayout.CENTER);

                        JPanel totalPanel = new JPanel(new GridLayout(3, 2));
                        totalPanel.setBackground(Color.YELLOW);
                        totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                        totalPanel.add(new JLabel("SUBTOTAL"));
                        totalPanel.add(new JLabel("$" + subtotal));
                        totalPanel.add(new JLabel("IVA"));
                        totalPanel.add(new JLabel("$" + iva));
                        totalPanel.add(new JLabel("TOTAL"));
                        totalPanel.add(new JLabel("$" + (subtotal + iva)));

                        facturaFrame.add(totalPanel, BorderLayout.SOUTH);

                        facturaFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Factura no encontrada.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void editarFactura(String facturaNo) {
        try {
            String query = "SELECT * FROM FACTURAS WHERE FACNUMERO = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, facturaNo);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String clienteCodigo = resultSet.getString("CLICODIGO");
                        String clienteNombre = getClienteNombre(clienteCodigo);
                        String fecha = resultSet.getString("FACFECHA");
                        double subtotal = resultSet.getDouble("FACSUBTOTAL");
                        double iva = resultSet.getDouble("FACIVA");
                        String metodoPago = resultSet.getString("FACFORMAPAGO");

                        facturaNoLabel.setText("Factura No.: " + facturaNo);
                        clienteField.setText(clienteNombre);
                        fechaLabel.setText("Fecha: " + fecha);
                        metodoPagoComboBox.setSelectedItem(metodoPago);

                        // Clear existing products in the table
                        tableModel.setRowCount(0);

                        query = "SELECT * FROM PXF WHERE FACNUMERO = ?";
                        try (PreparedStatement pxfStatement = connection.prepareStatement(query)) {
                            pxfStatement.setString(1, facturaNo);
                            try (ResultSet pxfResultSet = pxfStatement.executeQuery()) {
                                while (pxfResultSet.next()) {
                                    String producto = pxfResultSet.getString("PROCODIGO");
                                    int cantidad = pxfResultSet.getInt("PXFCANTIDAD");
                                    double valUni = pxfResultSet.getDouble("PXFVALOR");
                                    double subtotalProd = pxfResultSet.getDouble("PXFSUBTOTAL");
                                    tableModel.addRow(new Object[]{producto, "Descripción no disponible", "Unidad no disponible", cantidad, "$" + valUni, "$" + subtotalProd});
                                }
                            }
                        }

                        actualizarTotales();
                    } else {
                        JOptionPane.showMessageDialog(null, "Factura no encontrada.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public interface ClienteAgregadoListener {
        void clienteAgregado(String codigo, String nombreCompleto);
    }
}
