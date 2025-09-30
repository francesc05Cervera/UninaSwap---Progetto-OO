package GUI;

import Controller.UtenteController;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField txtUsername, txtEmail, txtNome, txtCognome;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JButton btnSubmit, btnBack;

    private UtenteController utenteController;

    public RegisterFrame() {
        setTitle("Registrazione - Unina Swap");
        setSize(520, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        utenteController = new UtenteController();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblWelcome = new JLabel("Crea il tuo account Unina Swap", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblWelcome, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);
        txtUsername = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Nome:"), gbc);
        txtNome = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtNome, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Cognome:"), gbc);
        txtCognome = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtCognome, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Conferma Password:"), gbc);
        txtConfirmPassword = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(txtConfirmPassword, gbc);

        btnSubmit = new JButton("Registrati");
        btnBack = new JButton("Indietro");

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(btnSubmit, gbc);
        gbc.gridx = 1;
        panel.add(btnBack, gbc);

        add(panel);

        btnBack.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        btnSubmit.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String email = txtEmail.getText().trim();
            String nome = txtNome.getText().trim();
            String cognome = txtCognome.getText().trim();
            String password = new String(txtPassword.getPassword());
            String confirm = new String(txtConfirmPassword.getPassword());

            if (username.isEmpty() || email.isEmpty() || nome.isEmpty() || cognome.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Compila tutti i campi!");
                return;
            }

            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(null, "Le password non coincidono!");
                return;
            }

            boolean esito = utenteController.registrazioneNuovoUtente(username, email, nome, cognome, password);
            JOptionPane.showMessageDialog(null, "Registrazione completata");

            if (esito)
            {
                dispose();
                new LoginFrame();
            }
        });

        setVisible(true);
    }
}
