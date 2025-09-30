package GUI;

import Controller.UtenteController;
import entità.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;

    private UtenteController utenteCtrl;

    public LoginFrame() {
        setTitle("Login - Unina Swap");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        utenteCtrl = new UtenteController();

        // Layout principale
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Messaggio di benvenuto
        JLabel lblWelcome = new JLabel("Benvenuto su Unina Swap!", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblWelcome, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);

        txtUsername = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        // Password
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // Bottoni
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Registrati");

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(btnLogin, gbc);

        gbc.gridx = 1;
        panel.add(btnRegister, gbc);

        add(panel);

        // Azione pulsante Registrati
        btnRegister.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        // Azione pulsante Login
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = txtUsername.getText().trim();
                String pass = new String(txtPassword.getPassword());

                if (user.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Compila tutti i campi!");
                } else {
                    // Verifica tramite controller
                    Utente u = utenteCtrl.cercaUtente(user);

                    if (u == null) {
                        JOptionPane.showMessageDialog(null, "Utente non trovato!");
                        return;
                    }

                    if (!u.getPassword().equals(pass)) {
                        JOptionPane.showMessageDialog(null, "Password errata!");
                        return;
                    }

                    JOptionPane.showMessageDialog(null, "Login effettuato con successo!");
                    dispose();
                    new MenuFrame(user);
                }
            }
        });

        setVisible(true);
    }
}
