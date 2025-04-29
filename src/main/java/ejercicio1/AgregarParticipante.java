package ejercicio1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AgregarParticipante extends JFrame {
    private Connection dbConn;
    private JTextField nombre;
    private JTextField telefono;
    private JTextField region;

    public AgregarParticipante() throws SQLException {
        setupBaseDeDatos();
        setupUIComponents();
    }

    private void setupBaseDeDatos() throws SQLException {
        String url = "jdbc:derby:memory:participantes;create=true";
        String user = "app";
        String password = "app";
        this.dbConn = DriverManager.getConnection(url, user, password);
    }

    private void setupUIComponents() {
        setTitle("Add Participant");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.nombre = new JTextField(10);
        this.telefono = new JTextField(10);
        this.region = new JTextField(10);
        this.nombre.setText("");
        this.telefono.setText("");
        this.region.setText("China");
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new FlowLayout());
        contentPane.add(new JLabel("Nombre: "));
        contentPane.add(nombre);
        contentPane.add(new JLabel("Telefono: "));
        contentPane.add(telefono);
        contentPane.add(new JLabel("Region: "));
        contentPane.add(region);
        JButton botonCargar = new JButton("Cargar");
        botonCargar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onBotonCargar();
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
            }
        });
        contentPane.add(botonCargar);
        setContentPane(contentPane);
        contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        pack();
        setVisible(true);
    }

    private void onBotonCargar() throws SQLException {
        if (nombre.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Debe cargar un nombre");

            return;
        }
        if (telefono.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Debe cargar un telefono");
            return;
        }
        if (!validarTelefono(telefono.getText())) {
            JOptionPane.showMessageDialog(this, "El teléfono debe ingresarse de la siguiente  forma: NNNN-NNNNNN");
            return;
        }
        if (!region.getText().equals("China") && !region.getText().equals("US") && !
                region.getText().equals("Europa")) {
            JOptionPane.showMessageDialog(this, "Region desconocida. Las conocidas son:  China, US, Europa");
            return;
        }
        PreparedStatement st = dbConn
                .prepareStatement("insert into participantes(nombre, telefono, region) values(?,?,?)");
        try {
            st.setString(1, nombre.getText());
            st.setString(2, telefono.getText());
            st.setString(3, region.getText());
            st.executeUpdate();
        } finally {
            st.close();
        }
        dispose();
    }

    private boolean validarTelefono(String telefono) {
        String regex = "\\d{4}-\\d{6}";
        return telefono.matches(regex);
    }
}

