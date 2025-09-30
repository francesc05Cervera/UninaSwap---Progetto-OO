package GUI;
import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JFrame 
{
    private JButton btnAnnuncio, btnOfferte, btnConsegne, btnLogout, btnAccount, btnCategorie;
    
    public MenuFrame(String usernameUtente) {
        // Titolo della finestra
        setTitle("Menu Principale - Unina Swap");
        // Dimensioni
        setSize(420, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Layout principale
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Titolo
        JLabel lblTitle = new JLabel("Seleziona una sezione", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);
        
        // Sottotitolo con nome utente
        JLabel lblUser = new JLabel("Utente loggato: " + usernameUtente, SwingConstants.CENTER);
        lblUser.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridy++;
        panel.add(lblUser, gbc);
        
        // Reset gridwidth per i bottoni
        gbc.gridwidth = 1;
        
        // Pulsante Annuncio
        btnAnnuncio = new JButton("Gestisci Annunci");
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(btnAnnuncio, gbc);
        
        // Pulsante Offerte
        btnOfferte = new JButton("Gestisci Offerte");
        gbc.gridx = 1;
        panel.add(btnOfferte, gbc);
        
        // Pulsante Consegne
        btnConsegne = new JButton("Gestisci Consegne");
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(btnConsegne, gbc);
        
        // Pulsante Categorie
        btnCategorie = new JButton("Gestisci Categorie");
        gbc.gridx = 1;
        panel.add(btnCategorie, gbc);
        
        // Pulsante Il mio Account
        btnAccount = new JButton("Il mio account");
        gbc.gridy++; 
        gbc.gridx = 0; 
        panel.add(btnAccount, gbc); 
        
        // Pulsante Logout
        btnLogout = new JButton("Logout");
        gbc.gridx = 1;
        panel.add(btnLogout, gbc);
        
        // Aggiunge il pannello alla finestra
        add(panel);
        
        // Azioni dei pulsanti
        btnAnnuncio.addActionListener(e -> {
            dispose();
            new AnnuncioFrame(usernameUtente);
        });
        
        btnOfferte.addActionListener(e -> {
        	dispose();
            new OffertaFrame(usernameUtente);
        });
        
        btnConsegne.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Sezione Consegne non ancora realizzata!");
            
            new ConsegnaFrame(usernameUtente);
        });
        
        btnCategorie.addActionListener(e -> {
        	dispose();
            new CategoriaFrame(usernameUtente);
        });
        
        btnAccount.addActionListener(e -> {
            new AccountFrame(usernameUtente);
        });
        
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        
        // Rende visibile la fineùstra
        setVisible(true);
    }
}