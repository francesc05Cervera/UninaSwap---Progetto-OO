package GUI;

import javax.swing.*;
import java.awt.*;
import Controller.UtenteController;
import entità.Utente;

public class AccountFrame extends JFrame {

    public AccountFrame(String usernameUtente) {
        // Titolo della finestra
        setTitle("Il mio Account - Unina Swap");

        // Dimensioni
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chiude solo questa finestra

        // Layout principale
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Titolo
        JLabel lblTitle = new JLabel("Informazioni Account");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        // Reset gridwidth
        gbc.gridwidth = 1;
        UtenteController ctrlU = new UtenteController();
        Utente ut = ctrlU.cercaUtente(usernameUtente);
        // Etichetta Username
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(usernameUtente), gbc);

        // Etichetta Nome (placeholder)
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(ut.getNome()), gbc); // Da sostituire con dato reale

        // Etichetta Cognome (placeholder)
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Cognome:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(ut.getCognome()), gbc); // Da sostituire con dato reale

        // Etichetta Email (placeholder)
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(ut.getEmail()), gbc); // Da sostituire con dato reale

        // Aggiunge il pannello alla finestra
        add(panel);

        // Rende visibile la finestra
        setVisible(true);
    }
}
