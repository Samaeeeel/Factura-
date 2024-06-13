package facturacrudinterface;

import conexion.Conexion;
import javax.swing.*;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class AgregarCliente {
    private Connection connection;

    public AgregarCliente() {
        connection = Conexion.getConnection();
    }

    public void mostrarVentanaAgregarCliente(FacturaCRUDInterface.ClienteAgregadoListener listener) {
        JFrame frame = new JFrame("Agregar Cliente");
        frame.setSize(400, 600);
        frame.setLayout(new GridLayout(15, 2));

        JTextField codigoField = new JTextField();
        JTextField nombre1Field = new JTextField();
        JTextField nombre2Field = new JTextField();
        JTextField apellido1Field = new JTextField();
        JTextField apellido2Field = new JTextField();
        JTextField nombreCompletoField = new JTextField();
        JTextField identificacionField = new JTextField();
        JTextField direccionField = new JTextField();
        JTextField telefonoField = new JTextField();
        JTextField celularField = new JTextField();
        JTextField emailField = new JTextField();

        JComboBox<String> tipoComboBox = new JComboBox<>(new String[]{"JUR", "NOR"});
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"ACT", "INA"});

        frame.add(new JLabel("Código:"));
        frame.add(codigoField);
        frame.add(new JLabel("Primer Nombre:"));
        frame.add(nombre1Field);
        frame.add(new JLabel("Segundo Nombre:"));
        frame.add(nombre2Field);
        frame.add(new JLabel("Primer Apellido:"));
        frame.add(apellido1Field);
        frame.add(new JLabel("Segundo Apellido:"));
        frame.add(apellido2Field);
        frame.add(new JLabel("Nombre Completo:"));
        frame.add(nombreCompletoField);
        frame.add(new JLabel("Identificación:"));
        frame.add(identificacionField);
        frame.add(new JLabel("Dirección:"));
        frame.add(direccionField);
        frame.add(new JLabel("Teléfono:"));
        frame.add(telefonoField);
        frame.add(new JLabel("Celular:"));
        frame.add(celularField);
        frame.add(new JLabel("Email:"));
        frame.add(emailField);
        frame.add(new JLabel("Tipo:"));
        frame.add(tipoComboBox);
        frame.add(new JLabel("Estado:"));
        frame.add(statusComboBox);

        JButton guardarButton = new JButton("Guardar");
        guardarButton.addActionListener(e -> {
            String codigo = codigoField.getText();
            String nombre1 = nombre1Field.getText();
            String nombre2 = nombre2Field.getText().isEmpty() ? null : nombre2Field.getText();
            String apellido1 = apellido1Field.getText();
            String apellido2 = apellido2Field.getText().isEmpty() ? null : apellido2Field.getText();
            String nombreCompleto = nombre1 + " " + (nombre2 != null ? nombre2 + " " : "") + apellido1 + (apellido2 != null ? " " + apellido2 : "");
            String identificacion = identificacionField.getText();
            String direccion = direccionField.getText();
            String telefono = telefonoField.getText();
            String celular = celularField.getText();
            String email = emailField.getText();
            String tipo = (String) tipoComboBox.getSelectedItem();
            String status = (String) statusComboBox.getSelectedItem();

            String insertClienteSQL = "INSERT INTO CLIENTES (CLICODIGO, CLINOMBRE1, CLINOMBRE2, CLIAPELLIDO1, CLIAPELLIDO2, CLINOMBRE, CLIIDENTIFICACION, CLIDIRECCION, CLITELEFONO, CLICELULAR, CLIEMAIL, CLITIPO, CLISTATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertClienteStmt = connection.prepareStatement(insertClienteSQL)) {
                insertClienteStmt.setString(1, codigo);
                insertClienteStmt.setString(2, nombre1);
                insertClienteStmt.setString(3, nombre2);
                insertClienteStmt.setString(4, apellido1);
                insertClienteStmt.setString(5, apellido2);
                insertClienteStmt.setString(6, nombreCompleto);
                insertClienteStmt.setString(7, identificacion);
                insertClienteStmt.setString(8, direccion);
                insertClienteStmt.setString(9, telefono);
                insertClienteStmt.setString(10, celular);
                insertClienteStmt.setString(11, email);
                insertClienteStmt.setString(12, tipo);
                insertClienteStmt.setString(13, status);
                insertClienteStmt.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Cliente agregado exitosamente.");
                frame.dispose();
                listener.clienteAgregado(codigo, nombreCompleto);
            } catch (SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(frame, "Error: La identificación ya está registrada.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error al agregar cliente: " + ex.getMessage());
            }
        });

        frame.add(new JLabel(""));
        frame.add(guardarButton);

        frame.setVisible(true);
    }
}
