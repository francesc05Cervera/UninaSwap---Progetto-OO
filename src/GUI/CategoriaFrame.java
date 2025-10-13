package GUI; // Pacchetto per le classi GUI

import Controller.CategoriaController; // Importa il controller per gestire le categorie
import entità.Categoria; // Importa l'entità Categoria
import javax.swing.*; // Importa componenti Swing
import javax.swing.border.*; // Importa supporti per bordi
import javax.swing.table.DefaultTableCellRenderer; // Importa renderer di celle per tabelle
import javax.swing.table.DefaultTableModel; // Importa modello di tabella predefinito
import javax.swing.table.JTableHeader; // Importa header delle tabelle
import java.awt.*; // Importa componenti AWT grafici
import java.util.List; // Importa lista

public class CategoriaFrame extends JFrame 
{ // Classe che rappresenta la finestra di gestione categorie

    private CategoriaController controller; // Controller per interazione con i dati delle categorie
    private JTable table; // Tabella per mostrare le categorie
    private DefaultTableModel tableModel; // Modello di dati per la tabella
    private JButton btnAggiungi, btnElimina, btnModifica, btnAggiorna, btnIndietro; // Pulsanti funzionali
    private String usernameUtente; // Username utente per mantenere contesto

    public CategoriaFrame(String usernameUtente) 
    { // Costruttore accetta username utente
    	
        this.usernameUtente = usernameUtente; // Inizializza username
        this.controller = new CategoriaController(); // Istanzia controller categorie

        setTitle("Gestione Categorie - Unina Swap"); // Imposta titolo finestra
        
        setSize(800, 600); // Imposta dimensione finestra
        
        setMinimumSize(new Dimension(750, 550)); // Dimensione minima della finestra
        
        setLocationRelativeTo(null); // Centra la finestra
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chiude solo questa finestra

        // Panel principale con layout BorderLayout e sfondo personalizzato
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        
        mainPanel.setBackground(new Color(240, 242, 245));

        // Header della finestra con titolo e sottotitolo
        JPanel headerPanel = new JPanel();
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS)); // Layout verticale
        
        headerPanel.setBackground(Color.WHITE);
        
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)), // Bordo inferiore
            BorderFactory.createEmptyBorder(20, 30, 20, 30) // Margini interni
        ));

        JLabel lblTitle = new JLabel("Gestione Categorie"); // Titolo principale
        
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26)); // Font grosso e grassetto
        
        lblTitle.setForeground(new Color(111, 66, 193)); // Colore viola
        
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra orizzontalmente
        
        headerPanel.add(lblTitle); // Aggiunge titolo al pannello header

        headerPanel.add(Box.createVerticalStrut(5)); // Spazio verticale di 5px

        JLabel lblSubtitle = new JLabel("Visualizza e gestisci le categorie degli annunci"); // Sottotitolo
        
        lblSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Font normale
        
        lblSubtitle.setForeground(new Color(100, 100, 100)); // Colore grigio scuro
        
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra
        
        headerPanel.add(lblSubtitle); // Aggiungi sottotitolo

        mainPanel.add(headerPanel, BorderLayout.NORTH); // Posiziona header in alto

        // Panel centrale per contenere la tabella con categorie
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10)); // Spaziatura 10 px
        
        centerPanel.setBackground(new Color(240, 242, 245)); // Sfondo grigio chiaro
        
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Margini interni

        // Definizione colonne tabella
        String[] colonne = {"ID", "Nome Categoria"};
        // Modello tabella disabilita modifica diretta delle celle
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Celle non modificate direttamente
            }
        };
        
        table = new JTable(tableModel); // Crea tabella con il modello dati
        
        table.setFont(new Font("SansSerif", Font.PLAIN, 13)); // Font contenuti tabella
        
        table.setRowHeight(35); // Altezza riga
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Selezione singola riga
        
        table.setSelectionBackground(new Color(111, 66, 193, 50)); // Sfondo selezione trasparente viola
        
        table.setSelectionForeground(Color.BLACK); // Colore testo selezionato
        
        table.setGridColor(new Color(230, 230, 230)); // Colore griglia
        
        table.setShowGrid(true); // Mostra griglia
        
        // Larghezze preferite colonne
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(400);

        // Stile header tabella
        JTableHeader header = table.getTableHeader();
        
        header.setFont(new Font("SansSerif", Font.BOLD, 14)); // Font grassetto
        
        header.setBackground(new Color(111, 66, 193)); // Sfondo viola
        
        header.setForeground(Color.WHITE); // Testo bianco
        
        header.setPreferredSize(new Dimension(header.getWidth(), 40)); // Altezza header
        
        header.setBorder(BorderFactory.createEmptyBorder()); // Nessun bordo

        // Centra contenuto prima colonna (ID)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        // Scroll pane per abilitare scroll sulla tabella
        JScrollPane scrollPane = new JScrollPane(table);
        
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1)); // Bordo grigio
        
        scrollPane.getViewport().setBackground(Color.WHITE); // Sfondo bianco viewport
        
        centerPanel.add(scrollPane, BorderLayout.CENTER); // Aggiunge scroll pane al centro panel

        mainPanel.add(centerPanel, BorderLayout.CENTER); // Aggiunge centro panel al main panel

        // Panel per i pulsanti con layout flow e margini
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        
        buttonPanel.setBackground(Color.WHITE);
        
        buttonPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(220, 220, 220))); // Bordo superiore

        // Creazione pulsanti con stili e colori diversi
        btnAggiungi = createStyledButton("+ Aggiungi", new Color(40, 167, 69)); // Verde
        btnModifica = createStyledButton("✏ Modifica", new Color(0, 102, 204)); // Blu
        btnElimina = createStyledButton("🗑 Elimina", new Color(220, 53, 69)); // Rosso
        btnAggiorna = createStyledButton("↻ Aggiorna", new Color(108, 117, 125)); // Grigio scuro
        btnIndietro = createStyledButton("← Indietro", new Color(108, 117, 125)); // Grigio scuro

        // Aggiunta pulsanti al pannello
        buttonPanel.add(btnAggiungi);
        buttonPanel.add(btnModifica);
        buttonPanel.add(btnElimina);
        buttonPanel.add(btnAggiorna);
        buttonPanel.add(btnIndietro);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Aggiunge il pannello pulsanti in basso

        add(mainPanel); // Aggiunge il pannello principale al frame

        caricaCategorie(); // Carica e visualizza le categorie dalla fonte dati

        // Action listeners per ogni pulsante con relativa azione
        btnAggiungi.addActionListener(e -> aggiungiCategoria());
        btnModifica.addActionListener(e -> modificaCategoria());
        btnElimina.addActionListener(e -> eliminaCategoria());
        btnAggiorna.addActionListener(e -> caricaCategorie());
        btnIndietro.addActionListener(e -> {
            dispose(); // Chiude la finestra corrente
            new MenuFrame(usernameUtente); // Apre il menu principale con l'username
        });

        setVisible(true); // Rende la finestra visibile
    }

    private JButton createStyledButton(String text, Color color) 
    {
        JButton button = new JButton(text); // Crea un JButton con testo
        
        button.setFont(new Font("SansSerif", Font.BOLD, 13)); // Font e stile
        
        button.setBackground(color); // Colore background
        
        button.setForeground(Color.WHITE); // Colore testo bianco
        
        button.setFocusPainted(false); // Disabilita bordo focus
        
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding interno
        
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursore a mano al passaggio

        // Cambia colore bottone al passaggio mouse
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) 
            {
                button.setBackground(color.darker()); // Scurisce il colore
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) 
            {
                button.setBackground(color); // Ritorna colore originale
            }
        });

        return button; // Ritorna il bottone stilizzato
    }

    private void caricaCategorie() 
    {
        tableModel.setRowCount(0); // Svuota la tabella
        
        List<Categoria> categorie = controller.listCategorie(); // Prende categorie dal controller

        if (categorie != null && !categorie.isEmpty()) // Se ci sono categorie
        { 
            for (Categoria cat : categorie) 
            { // Per ogni categoria
                tableModel.addRow(new Object[]{
                    cat.getIdCategoria(), // Aggiunge ID categoria
                    cat.getNomeCategoria() // Aggiunge nome categoria
                });
            }
        } else { // Nessuna categoria trovata
            showStyledMessage(
                "Non ci sono categorie nel database",
                "Nessuna categoria",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void aggiungiCategoria() 
    {
        JPanel inputPanel = new JPanel();
        
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS)); // Layout verticale
        
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Nuova Categoria"); // Titolo finestra input
        
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        titleLabel.setForeground(new Color(111, 66, 193)); // Viola
        
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        inputPanel.add(titleLabel);

        inputPanel.add(Box.createVerticalStrut(10)); // Spazio verticale

        JLabel descLabel = new JLabel("Inserisci il nome della categoria:"); // Descrizione input
        
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        descLabel.setForeground(new Color(60, 60, 60));
        
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        inputPanel.add(descLabel);

        inputPanel.add(Box.createVerticalStrut(8)); // Spazio verticale

        JTextField textField = new JTextField(20); // Campo testo per nome categoria
        
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        textField.setBorder(BorderFactory.createCompoundBorder( // Bordo e padding
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        textField.setMaximumSize(new Dimension(400, 40)); // Dimensione massima
        
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        inputPanel.add(textField);
        

        // Mostra dialogo con OK e Cancel
        int result = JOptionPane.showConfirmDialog(this, inputPanel,
            "Aggiungi Categoria", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) 
        { // Se OK premuto
            String nomeCategoria = textField.getText();

            if (nomeCategoria != null && !nomeCategoria.trim().isEmpty()) { // Campo non vuoto
                List<Categoria> categorieEsistenti = controller.listCategorie(); // Recupera categorie esistenti
                boolean esisteGia = false; // Flag duplicati

                if (categorieEsistenti != null) {
                    for (Categoria cat : categorieEsistenti) { // Controlla duplicato case insensitive
                        if (cat.getNomeCategoria().equalsIgnoreCase(nomeCategoria.trim())) {
                            esisteGia = true;
                            break;
                        }
                    }
                }

                if (esisteGia) { // Se duplicato
                    showStyledMessage(
                        "La categoria '" + nomeCategoria.trim() + "' esiste già",
                        "Categoria duplicata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                boolean risultato = controller.aggiungiCategoria(nomeCategoria.trim()); // Aggiunge categoria

                if (risultato) { // Se aggiunta correttamente
                    showStyledMessage(
                        "La categoria è stata aggiunta con successo",
                        "Categoria aggiunta",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    caricaCategorie(); // Aggiorna tabella
                } else { // Errore aggiunta
                    showStyledMessage(
                        "Si è verificato un errore durante l'aggiunta",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }

    private void modificaCategoria() 
    {
        int selectedRow = table.getSelectedRow(); // Ottiene riga selezionata

        if (selectedRow == -1) { // Nessuna selezione
            showStyledMessage(
                "Seleziona una categoria dalla tabella",
                "Nessuna selezione",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String vecchioNome = (String) tableModel.getValueAt(selectedRow, 1); // Nome categoria selezionata

        try {
            if (controller.contaAnnunci(vecchioNome) > 0) { // Conta annunci attivi per categoria
                showStyledMessage(
                    "Ci sono annunci attivi in questa categoria",
                    "Impossibile modificare",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        } catch (Exception ex) { // Gestione errore
            showStyledMessage(
                "Errore durante il controllo degli annunci",
                "Errore",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Dialog personalizzato cambio nome categoria
        JPanel inputPanel = new JPanel();
        
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Modifica Categoria"); // Titolo dialogo
        
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        titleLabel.setForeground(new Color(0, 102, 204)); // Blu
        
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        inputPanel.add(titleLabel);

        inputPanel.add(Box.createVerticalStrut(10)); // Spazio

        JLabel descLabel = new JLabel("Inserisci il nuovo nome per la categoria:"); // Descrizione
        
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        descLabel.setForeground(new Color(60, 60, 60));
        
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        inputPanel.add(descLabel);

        inputPanel.add(Box.createVerticalStrut(8)); // Spazio

        JTextField textField = new JTextField(vecchioNome, 20); // Campo testo precompilato con vecchio nome
        
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        textField.setMaximumSize(new Dimension(400, 40));
        
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        inputPanel.add(textField);

        // Mostra dialogo conferma modifica
        int result = JOptionPane.showConfirmDialog(this, inputPanel, 
            "Modifica Categoria", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) 
        { // Se conferma
            String nuovoNome = textField.getText();

            if (nuovoNome != null && !nuovoNome.trim().isEmpty()) { // Non vuoto
                List<Categoria> categorieEsistenti = controller.listCategorie(); // Recupera tutte le categorie
                boolean esisteGia = false;

                if (categorieEsistenti != null) {
                    for (Categoria cat : categorieEsistenti) { // Controlla duplicati
                        if (cat.getNomeCategoria().equalsIgnoreCase(nuovoNome.trim()) && 
                            !cat.getNomeCategoria().equalsIgnoreCase(vecchioNome)) {
                            esisteGia = true;
                            break;
                        }
                    }
                }

                if (esisteGia) 
                { // Se duplicato trovato
                    showStyledMessage(
                        "La categoria '" + nuovoNome.trim() + "' esiste già",
                        "Categoria duplicata",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                boolean eliminato = controller.eliminaCategoria(vecchioNome); // Elimina vecchia categoria
                if (eliminato) 
                {
                    boolean aggiunto = controller.aggiungiCategoria(nuovoNome.trim()); // Aggiunge nuova categoria
                    if (aggiunto) {
                        showStyledMessage(
                            "La categoria è stata modificata con successo",
                            "Categoria modificata",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        caricaCategorie(); // Aggiorna tabella
                    } else {
                        controller.aggiungiCategoria(vecchioNome); // Ripristina vecchia categoria se errore
                        showStyledMessage(
                            "Errore durante la modifica. Categoria ripristinata",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        }
    }

    private void eliminaCategoria() 
    {
        int selectedRow = table.getSelectedRow(); // Riga selezionata

        if (selectedRow == -1) { // Se nessuna selezione
            showStyledMessage(
                "Seleziona una categoria dalla tabella",
                "Nessuna selezione",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String nomeCategoria = (String) tableModel.getValueAt(selectedRow, 1); // Nome categoria da eliminare

        // Dialog personalizzato per conferma eliminazione
        JPanel confirmPanel = new JPanel();
        
        confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.Y_AXIS));
        
        confirmPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel iconLabel = new JLabel("⚠"); // Icona di avviso
        
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 50));
        
        iconLabel.setForeground(new Color(255, 193, 7)); // Colore giallo/arancio
        
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        confirmPanel.add(iconLabel);

        confirmPanel.add(Box.createVerticalStrut(15)); // Spazio verticale

        JLabel titleLabel = new JLabel("Conferma Eliminazione"); // Titolo dialogo
        
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        titleLabel.setForeground(new Color(220, 53, 69)); // Rosso
        
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        confirmPanel.add(titleLabel);
        

        confirmPanel.add(Box.createVerticalStrut(10)); // Spazio

        JLabel messageLabel = new JLabel("Vuoi eliminare la categoria:"); // Domanda conferma
        
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        messageLabel.setForeground(new Color(60, 60, 60));
        
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        confirmPanel.add(messageLabel);

        confirmPanel.add(Box.createVerticalStrut(5)); // Spazio

        JLabel categoryLabel = new JLabel("\"" + nomeCategoria + "\""); // Nome categoria evidenziato
        
        categoryLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        
        categoryLabel.setForeground(new Color(220, 53, 69)); // Rosso
        
        categoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        confirmPanel.add(categoryLabel);

        confirmPanel.add(Box.createVerticalStrut(10)); // Spazio

        JLabel warningLabel = new JLabel("Questa operazione non può essere annullata!"); // Messaggio di avvertimento
        
        warningLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        
        warningLabel.setForeground(new Color(100, 100, 100));
        
        warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        confirmPanel.add(warningLabel);

        // Mostra dialogo di conferma Sì/No
        int conferma = JOptionPane.showConfirmDialog(this, confirmPanel,
            "Elimina Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (conferma == JOptionPane.YES_OPTION) 
        { // Se conferma scelta Sì
            boolean risultato = controller.eliminaCategoria(nomeCategoria); // Elimina categoria tramite controller

            if (risultato) 
            { // Se eliminazione avvenuta
                showStyledMessage(
                    "La categoria è stata eliminata con successo",
                    "Categoria eliminata",
                    JOptionPane.INFORMATION_MESSAGE
                );
                caricaCategorie(); // Aggiorna tabella
            } else { // Se fallisce eliminazione (es. annunci attivi)
                showStyledMessage(
                    "Ci sono annunci attivi in questa categoria",
                    "Impossibile eliminare",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void showStyledMessage(String message, String title, int messageType) 
    {
        JPanel panel = new JPanel(); // Pannello per messaggio personalizzato
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Layout verticale
        
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        JLabel iconLabel = new JLabel(); // Icona messaggio
        
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 40)); // Font grande
        
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra icona

        Color titleColor; // Colore da usare in base al tipo messaggio
        
        if (messageType == JOptionPane.ERROR_MESSAGE) { // Errore
            iconLabel.setText("✖");
            titleColor = new Color(220, 53, 69); // Rosso
        } else if (messageType == JOptionPane.WARNING_MESSAGE) { // Avviso
            iconLabel.setText("⚠");
            titleColor = new Color(255, 193, 7); // Giallo/arancio
        } else { // Informazione positiva
            iconLabel.setText("✓");
            titleColor = new Color(40, 167, 69); // Verde
        }
        
        iconLabel.setForeground(titleColor); // Colore icona
        
        panel.add(iconLabel); // Aggiunge icona al pannello

        panel.add(Box.createVerticalStrut(10)); // Spazio

        JLabel titleLabel = new JLabel(title); // Titolo messaggio
        
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        titleLabel.setForeground(titleColor); // Stessa colorazione
        
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        panel.add(titleLabel); // Aggiunge titolo

        panel.add(Box.createVerticalStrut(5)); // Spazio piccolo

        JLabel messageLabel = new JLabel(message); // Testo messaggio
        
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        messageLabel.setForeground(new Color(60, 60, 60)); // Colore testo grigio
        
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(messageLabel); // Aggiunge testo

        // Mostra dialogo personalizzato con il pannello fomattato
        JOptionPane.showMessageDialog(this, panel, title, JOptionPane.PLAIN_MESSAGE);
    }
}
