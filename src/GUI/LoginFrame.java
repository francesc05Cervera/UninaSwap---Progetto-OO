package GUI; //Pacchetto in cui si trova questa classe
import Controller.UtenteController; //Importa il controller per interagire con il database degli utenti
import entità.Utente; //Importa la classe Utente
import javax.swing.*; //Importa tutte le componenti Swing
import javax.swing.border.*; //Importa supporto per i bordi
import java.awt.*; //Importa i componenti grafici di AWT
import java.awt.event.*; //Importa gli eventi di AWT

public class LoginFrame extends JFrame //Classe che rappresenta la finestra di login
{
    private JTextField txtUsername; //Campo di testo per inserire l'username
    private JPasswordField txtPassword; //Campo di testo per inserire la password (oscura)
    private JButton btnLogin, btnRegister; //Bottoni per login e registrazione
    private UtenteController utenteCtrl; //Controller per le operazioni sul database utenti
    
    public LoginFrame() //Costruttore della classe
    {
        setTitle("Login - Unina Swap"); //Titolo della finestra
        setSize(550, 450); //Dimensione della finestra
        setMinimumSize(new Dimension(500, 400)); //Dimensione minima consentita
        setLocationRelativeTo(null); //Posizionamento centrato
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Chiude l'applicazione quando si chiude la finestra
        
        utenteCtrl = new UtenteController(); //Istanzia il controller degli utenti
        
        // Panel principale con sfondo colorato
        JPanel mainPanel = new JPanel(new BorderLayout()); //Panel principale con BorderLayout
        
        mainPanel.setBackground(new Color(240, 242, 245)); //Colore di sfondo grigio chiaro
        
        // Panel centrale con il form di login
        
        JPanel panel = new JPanel(new GridBagLayout()); //Panel con layout a griglia complessa
        
        panel.setBackground(Color.WHITE); //Sfondo bianco
        
        // Bordo arrotondato con ombra
        panel.setBorder(BorderFactory.createCompoundBorder( 
        		
            BorderFactory.createEmptyBorder(30, 30, 30, 30), //Margine interno
            
            BorderFactory.createCompoundBorder(new LineBorder(new Color(220, 220, 220), 1, true), BorderFactory.createEmptyBorder(30, 40, 30, 40)))
        		);
        
        GridBagConstraints gbc = new GridBagConstraints(); //Constraints per il layout
        
        gbc.insets = new Insets(10, 10, 10, 10); //Margini tra componenti
        
        gbc.fill = GridBagConstraints.HORIZONTAL; //Componenti si estendono in larghezza
        
        // Titolo principale
        JLabel lblWelcome = new JLabel("Benvenuto su Unina Swap!", SwingConstants.CENTER); //Etichetta di benvenuto centrata
        
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 24)); //Carattere e stile
        
        lblWelcome.setForeground(new Color(0, 102, 204)); //Colore blu Unina
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; //Occupare due colonne
        
        panel.add(lblWelcome, gbc); //Aggiunta al pannello
        
        // Sottotitolo
        JLabel lblSubtitle = new JLabel("Accedi al tuo account", SwingConstants.CENTER); //Sottotitolo centrato
        
        lblSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 14)); //Carattere
        
        lblSubtitle.setForeground(new Color(100, 100, 100)); //Colore grigio scuro
        
        gbc.gridy++;
        
        panel.add(lblSubtitle, gbc); //Aggiunta sotto il titolo
        
        // Spazio vuoto
        gbc.gridy++;
        
        panel.add(Box.createVerticalStrut(10), gbc); //Spazio verticale di 10 px
        
        
        // Label per username
        gbc.gridwidth = 1; //Una sola colonna
        
        gbc.gridy++;
        
        gbc.gridx = 0;
        
        JLabel lblUser = new JLabel("Username:"); //Etichetta per username
        
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 13)); //Stile carattere
        
        lblUser.setForeground(new Color(60, 60, 60)); //Colore scuro
        
        panel.add(lblUser, gbc); //Aggiunta al pannello
        
        // Campo di input username stilizzato
        txtUsername = new JTextField(20); //Campo di testo di 20 colonne
        
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14)); //Carattere
        
        txtUsername.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200), 1, true), BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        
        gbc.gridx = 1; //Collocato accanto alla label
        
        panel.add(txtUsername, gbc); //Aggiunta al pannello
        
        // Label per password
        gbc.gridy++;
        
        gbc.gridx = 0;
        
        JLabel lblPass = new JLabel("Password:"); //Etichetta password
        
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        lblPass.setForeground(new Color(60, 60, 60));
        
        panel.add(lblPass, gbc);
        
        // Campo di input password stilizzato
        txtPassword = new JPasswordField(20); //Campo password
        
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        txtPassword.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200), 1, true), BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        
        gbc.gridx = 1; //Accanto alla label
        
        panel.add(txtPassword, gbc);
        
        // Spazio prima dei bottoni
        gbc.gridy++;
        
        gbc.gridwidth = 2; //Occupare tutta la larghezza
        
        gbc.gridx = 0;
        
        panel.add(Box.createVerticalStrut(5), gbc); //Spazio di 5 px
        
        // Panel per i bottoni affiancati
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0)); //Due bottoni affiancati
        
        buttonPanel.setBackground(Color.WHITE); //Sfondo bianco
        
        buttonPanel.setMaximumSize(new Dimension(280, 45)); //Dimensione massima
        
        // Bottone login principale
        btnLogin = new JButton("Accedi"); //Bottone accedi
        
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        btnLogin.setBackground(new Color(0, 102, 204)); //Blu
        
        btnLogin.setForeground(Color.WHITE); //Testo bianco
        
        btnLogin.setFocusPainted(false); //Nessun effetto focus
        
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20)); //Padding
        
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR)); //Cursore a mano
        
        buttonPanel.add(btnLogin); //Aggiungi al pannello
        
        // Bottone registrati, secondario
        btnRegister = new JButton("Registrati");
        
        btnRegister.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        btnRegister.setBackground(Color.WHITE);
        
        btnRegister.setForeground(new Color(0, 102, 204));
        
        btnRegister.setFocusPainted(false);
        
        btnRegister.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 102, 204), 2, true), BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        buttonPanel.add(btnRegister); //Aggiunta al pannello
        
        // Posizionamento del pannello dei bottoni
        gbc.gridy++;
        
        gbc.gridwidth = 2;
        
        gbc.fill = GridBagConstraints.NONE;
        
        gbc.anchor = GridBagConstraints.CENTER; //Al centro
        
        panel.add(buttonPanel, gbc);
        
        // Aggiungi il pannello principale al frame
        mainPanel.add(panel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Azione del pulsante Registrati
        btnRegister.addActionListener(e -> {
            dispose(); //Chiude la finestra attuale
            new RegisterFrame(); //Apri la finestra di registrazione
        });
        
        // Azione del pulsante Login
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                String user = txtUsername.getText().trim(); //Ottieni username
                String pass = new String(txtPassword.getPassword()); //Ottieni password
                
                if (user.isEmpty() || pass.isEmpty()) { //Se uno dei due campi è vuoto
                    showStyledMessage(
                        "Inserisci username e password per continuare", //Messaggio di errore
                        "Campi mancanti",
                        JOptionPane.WARNING_MESSAGE
                    );
                } else 
                	{
                    Utente u = utenteCtrl.cercaUtente(user); //Cerca utente nel db
                    
                    if (u == null) 
                    { //Se non trovato
                        showStyledMessage(
                            "L'username inserito non esiste nel sistema",
                            "Utente non trovato",
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    if (!u.getPassword().equals(pass)) { //Se la password non corrisponde
                        showStyledMessage(
                            "La password inserita non è corretta",
                            "Password errata",
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    
                    showStyledMessage( //Se login corretto, messaggio di benvenuto
                        "Benvenuto " + user + "!",
                        "Accesso effettuato",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    dispose(); //Chiude la finestra di login
                    new MenuFrame(user); //Apri la schermata del menu principale
                }
            }
        });
        
        setVisible(true); //Rende la finestra visibile
    }
    
    // Metodo per mostrare messaggi personalizzati e stilizzati
    private void showStyledMessage(String message, String title, int messageType) 
    {
        // Crea un pannello per contenere il messaggio
        JPanel panel = new JPanel();
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); //Layout verticale
        
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //Margini interni
        
        // Icona e colore in base al tipo di messaggio
        JLabel iconLabel = new JLabel(); //Etichetta per icona
        
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 40)); //Carattere grande
        
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT); //Centrare
        
        Color titleColor; //Colore del titolo e icona
        
        if (messageType == JOptionPane.ERROR_MESSAGE) 
        { //Errore
            iconLabel.setText("✖");
            titleColor = new Color(220, 53, 69); //Rosso
        } else if (messageType == JOptionPane.WARNING_MESSAGE) 
        	{ //Warnning
            iconLabel.setText("⚠");
            titleColor = new Color(255, 193, 7); //Oro/Arancio
        	} else 
        	{ //Informazione
            iconLabel.setText("✓");
            titleColor = new Color(40, 167, 69); //Verde
        	}
        
        iconLabel.setForeground(titleColor); //Colore icona
        
        panel.add(iconLabel); //Aggiungi icona
        
        panel.add(Box.createVerticalStrut(10)); //Spazio verticale di 10 px
        
        // Titolo del messaggio
        JLabel titleLabel = new JLabel(title);
        
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        titleLabel.setForeground(titleColor);
        
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titleLabel); //Aggiungi titolo
        
        panel.add(Box.createVerticalStrut(5)); //Spazio di 5 px
        
        // Testo del messaggio
        JLabel messageLabel = new JLabel(message);
        
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        messageLabel.setForeground(new Color(60, 60, 60));
        
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(messageLabel); //Aggiungi messaggio
        
        // Mostra il dialogo personalizzato
        JOptionPane.showMessageDialog(
            this, 
            panel, 
            title, 
            JOptionPane.PLAIN_MESSAGE
        );
    }
}
