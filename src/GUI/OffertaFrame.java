package GUI;

import Controller.OffertaController;
import Controller.AnnuncioController;
import entità.Offerta;
import entità.Annuncio;
import entity.enums.TipoAnnuncio;
import entity.enums.StatoOfferta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class OffertaFrame extends JFrame {

    private OffertaController offertaController;
    private AnnuncioController annuncioController;
    private JTable tabellaOfferteInviate, tabellaOfferteRicevute;
    private JComboBox<Annuncio> comboAnnunciDisponibili;
    private String currentUsername;
    
    public OffertaFrame(String username) {
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username non valido!");
            dispose();
            return;
        }
        
        this.currentUsername = username.trim();
        
        setTitle("Gestione Offerte - Unina Swap (" + currentUsername + ")");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        offertaController = new OffertaController();
        annuncioController = new AnnuncioController();

        initializeGUI();
        setVisible(true);
    }

    private void initializeGUI() {
        // === Barra superiore con pulsante Home ===
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnHome = new JButton("Home");
        btnHome.addActionListener(e -> {
            dispose();
            new MenuFrame(currentUsername);
        });
        topBar.add(btnHome);

        // === TabbedPane ===
        JTabbedPane tabs = new JTabbedPane();

        // === TAB 1: Le Mie Offerte (Inviate e Ricevute) ===
        JPanel tabVisualizza = createVisualizzaTab();
        tabs.addTab("Le mie Offerte", tabVisualizza);

        // === TAB 2: Crea Nuova Offerta ===
        JPanel tabCrea = createCreaOffertaTab();
        tabs.addTab("Crea Offerta", tabCrea);

        // === Layout principale ===
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topBar, BorderLayout.NORTH);
        getContentPane().add(tabs, BorderLayout.CENTER);
    }

    private JPanel createVisualizzaTab() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Titolo
        JLabel lblTitle = new JLabel("Gestione Offerte", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Split panel per dividere offerte inviate e ricevute
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(590);
        splitPane.setResizeWeight(0.5);

        // ===== PANEL OFFERTE INVIATE =====
        JPanel panelInviate = creaPanelOfferte(
            "Offerte Inviate",
            "Offerte che hai fatto ad altri",
            true
        );
        splitPane.setLeftComponent(panelInviate);

        // ===== PANEL OFFERTE RICEVUTE =====
        JPanel panelRicevute = creaPanelOfferte(
            "Offerte Ricevute",
            "Offerte ricevute sui tuoi annunci",
            false
        );
        splitPane.setRightComponent(panelRicevute);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel creaPanelOfferte(String titolo, String sottotitolo, boolean isInviate) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitolo = new JLabel(titolo, SwingConstants.CENTER);
        lblTitolo.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel lblSottotitolo = new JLabel(sottotitolo, SwingConstants.CENTER);
        lblSottotitolo.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblSottotitolo.setForeground(Color.GRAY);
        headerPanel.add(lblTitolo, BorderLayout.NORTH);
        headerPanel.add(lblSottotitolo, BorderLayout.CENTER);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Tabella
        String[] colonne = {"ID", "Annuncio", "Prezzo", "Stato", "Data"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(70);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);

        if (isInviate) {
            tabellaOfferteInviate = table;
        } else {
            tabellaOfferteRicevute = table;
        }

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        if (isInviate) {
            // Pulsanti per offerte inviate
            JButton btnDettagli = new JButton("Dettagli");
            JButton btnAggiorna = new JButton("Aggiorna");
            
            btnDettagli.addActionListener(e -> visualizzaDettagliOfferta(true));
            btnAggiorna.addActionListener(e -> aggiornaOfferte());
            
            buttonPanel.add(btnDettagli);
            buttonPanel.add(btnAggiorna);
        } else {
            // Pulsanti per offerte ricevute
            JButton btnAccetta = new JButton("Accetta");
            JButton btnRifiuta = new JButton("Rifiuta");
            JButton btnDettagli = new JButton("Dettagli");
            JButton btnAggiorna = new JButton("Aggiorna");
            
            btnAccetta.addActionListener(e -> gestisciOffertaRicevuta(true));
            btnRifiuta.addActionListener(e -> gestisciOffertaRicevuta(false));
            btnDettagli.addActionListener(e -> visualizzaDettagliOfferta(false));
            btnAggiorna.addActionListener(e -> aggiornaOfferte());
            
            buttonPanel.add(btnAccetta);
            buttonPanel.add(btnRifiuta);
            buttonPanel.add(btnDettagli);
            buttonPanel.add(btnAggiorna);
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCreaOffertaTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Annuncio
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Annuncio:"), gbc);

        comboAnnunciDisponibili = new JComboBox<>();
        comboAnnunciDisponibili.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Annuncio) {
                    Annuncio a = (Annuncio) value;
                    setText("ID:" + a.getIdAnnuncio() + " - " + a.getTitolo());
                }
                return this;
            }
        });
        caricaAnnunciDisponibili();
        gbc.gridx = 1;
        panel.add(comboAnnunciDisponibili, gbc);

        // Tipo Offerta
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Tipo Offerta:"), gbc);
        JComboBox<TipoAnnuncio> comboTipo = new JComboBox<>(TipoAnnuncio.values());
        gbc.gridx = 1;
        panel.add(comboTipo, gbc);

        // Prezzo
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Prezzo Proposto (€):"), gbc);
        JTextField txtPrezzo = new JTextField(15);
        txtPrezzo.setToolTipText("Inserire 0 se non si vuole specificare un prezzo");
        gbc.gridx = 1;
        panel.add(txtPrezzo, gbc);

        // Oggetti
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Oggetti offerti:"), gbc);
        JTextField txtOggetti = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtOggetti, gbc);

        // Come consegnare - NUOVO CAMPO
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Come consegnare:"), gbc);
        JTextArea txtComeConsegnare = new JTextArea(3, 15);
        txtComeConsegnare.setLineWrap(true);
        txtComeConsegnare.setWrapStyleWord(true);
        txtComeConsegnare.setToolTipText("Es: Di persona, Spedizione, Ritiro in zona, ecc.");
        gbc.gridx = 1;
        panel.add(new JScrollPane(txtComeConsegnare), gbc);

        // Messaggio
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Messaggio:"), gbc);
        JTextArea txtMessaggio = new JTextArea(3, 15);
        txtMessaggio.setLineWrap(true);
        txtMessaggio.setWrapStyleWord(true);
        gbc.gridx = 1;
        panel.add(new JScrollPane(txtMessaggio), gbc);

        // Bottone
        JButton btnInvia = new JButton("Invia Offerta");
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(btnInvia, gbc);

        // Action listener per il bottone
        btnInvia.addActionListener(e -> {
            Annuncio annuncioSelezionato = (Annuncio) comboAnnunciDisponibili.getSelectedItem();
            if (annuncioSelezionato == null) {
                JOptionPane.showMessageDialog(this, "Seleziona un annuncio valido!");
                return;
            }

            if (annuncioSelezionato.getUsername() != null && annuncioSelezionato.getUsername().equals(currentUsername)) {
                JOptionPane.showMessageDialog(this, "Non puoi fare offerte sui tuoi stessi annunci!");
                return;
            }

            double prezzo = 0;
            String prezzoText = txtPrezzo.getText();
            if (prezzoText != null && !prezzoText.trim().isEmpty()) {
                try { 
                    prezzo = Double.parseDouble(prezzoText.trim());
                    if (prezzo < 0) {
                        JOptionPane.showMessageDialog(this, "Il prezzo non può essere negativo!");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Inserire un prezzo valido!");
                    return;
                }
            }

            boolean esito = offertaController.creaOfferta(
                    LocalDate.now(),
                    (TipoAnnuncio) comboTipo.getSelectedItem(),
                    prezzo,
                    txtOggetti.getText() != null ? txtOggetti.getText().trim() : "",
                    txtMessaggio.getText() != null ? txtMessaggio.getText().trim() : "",
                    currentUsername,
                    annuncioSelezionato.getIdAnnuncio(),
                    txtComeConsegnare.getText() != null ? txtComeConsegnare.getText().trim() : ""
            );

            if (esito) {
                JOptionPane.showMessageDialog(this, "Offerta inviata con successo!");
                txtPrezzo.setText("");
                txtOggetti.setText("");
                txtComeConsegnare.setText("");
                txtMessaggio.setText("");
                caricaAnnunciDisponibili();
                aggiornaOfferte(); // Aggiorna anche la tab delle offerte
            } else {
                JOptionPane.showMessageDialog(this, "Errore nell'invio dell'offerta. Verifica i dati inseriti.");
            }
        });

        return panel;
    }

    private void aggiornaOfferte() {
        // Aggiorna offerte inviate
        DefaultTableModel modelInviate = (DefaultTableModel) tabellaOfferteInviate.getModel();
        modelInviate.setRowCount(0);
        
        List<Offerta> offerteInviate = offertaController.getOffertePerUtente(currentUsername);
        for (Offerta o : offerteInviate) {
            Object[] riga = {
                o.getIdOfferta(),
                "Ann. ID: " + o.getIdAnnuncio(),
                o.getPrezzoProposto() == 0 ? "N/A" : "€" + o.getPrezzoProposto(),
                o.getStatoOfferta() != null ? o.getStatoOfferta().toString() : "N/A",
                o.getDataOff() != null ? o.getDataOff().toString() : "N/A"
            };
            modelInviate.addRow(riga);
        }

        // Aggiorna offerte ricevute
        DefaultTableModel modelRicevute = (DefaultTableModel) tabellaOfferteRicevute.getModel();
        modelRicevute.setRowCount(0);
        
        try {
            List<Annuncio> mieiAnnunci = new ArrayList<>();
            List<Annuncio> tuttiAnnunci = annuncioController.mostraTuttiAnnunci();
            
            for (Annuncio a : tuttiAnnunci) {
                if (a.getUsername() != null && a.getUsername().equals(currentUsername)) {
                    mieiAnnunci.add(a);
                }
            }

            for (Annuncio annuncio : mieiAnnunci) {
                List<Offerta> offerteAnnuncio = offertaController.getOffertePerAnnuncio(annuncio.getIdAnnuncio());
                for (Offerta offerta : offerteAnnuncio) {
                    Object[] riga = {
                        offerta.getIdOfferta(),
                        "Ann. ID: " + offerta.getIdAnnuncio() + " da " + offerta.getUsername(),
                        offerta.getPrezzoProposto() == 0 ? "N/A" : "€" + offerta.getPrezzoProposto(),
                        offerta.getStatoOfferta() != null ? offerta.getStatoOfferta().toString() : "N/A",
                        offerta.getDataOff() != null ? offerta.getDataOff().toString() : "N/A"
                    };
                    modelRicevute.addRow(riga);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento delle offerte ricevute: " + e.getMessage());
        }
    }

    private void visualizzaDettagliOfferta(boolean isInviata) {
        JTable table = isInviata ? tabellaOfferteInviate : tabellaOfferteRicevute;
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleziona un'offerta dalla tabella.",
                "Attenzione",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idOfferta = (int) table.getValueAt(selectedRow, 0);
        
        // Cerca l'offerta
        List<Offerta> offerte = isInviata 
            ? offertaController.getOffertePerUtente(currentUsername)
            : getAllOfferteRicevute();
            
        Offerta offerta = offerte.stream()
            .filter(o -> o.getIdOfferta() == idOfferta)
            .findFirst()
            .orElse(null);

        if (offerta == null) {
            JOptionPane.showMessageDialog(this, "Offerta non trovata.", 
                "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder dettagli = new StringBuilder();
        dettagli.append("═══════════════════════════════\n");
        dettagli.append("      DETTAGLI OFFERTA\n");
        dettagli.append("═══════════════════════════════\n\n");
        
        dettagli.append("ID Offerta: ").append(offerta.getIdOfferta()).append("\n");
        dettagli.append("ID Annuncio: ").append(offerta.getIdAnnuncio()).append("\n");
        dettagli.append("Utente: ").append(offerta.getUsername()).append("\n");
        dettagli.append("Data: ").append(offerta.getDataOff()).append("\n");
        dettagli.append("Tipo: ").append(offerta.getTipoOfferta()).append("\n\n");
        
        dettagli.append("Prezzo Proposto: ");
        if (offerta.getPrezzoProposto() == 0) {
            dettagli.append("Non specificato\n");
        } else {
            dettagli.append("€").append(offerta.getPrezzoProposto()).append("\n");
        }
        
        dettagli.append("Oggetti offerti: ");
        if (offerta.getOggetti() == null || offerta.getOggetti().isEmpty()) {
            dettagli.append("Nessuno\n");
        } else {
            dettagli.append(offerta.getOggetti()).append("\n");
        }
        
        // Mostra come consegnare - NUOVO CAMPO NEI DETTAGLI
        dettagli.append("Come consegnare: ");
        if (offerta.getComeConsegnare() == null || offerta.getComeConsegnare().isEmpty()) {
            dettagli.append("Non specificato\n");
        } else {
            dettagli.append(offerta.getComeConsegnare()).append("\n");
        }
        
        dettagli.append("\nStato: ").append(offerta.getStatoOfferta()).append("\n");
        
        if (offerta.getMessaggio() != null && !offerta.getMessaggio().isEmpty()) {
            dettagli.append("\n--- Messaggio ---\n");
            dettagli.append(offerta.getMessaggio()).append("\n");
        }
        
        dettagli.append("\n═══════════════════════════════");

        JTextArea textArea = new JTextArea(dettagli.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 450));

        JOptionPane.showMessageDialog(this, scrollPane, 
            "Dettagli Offerta", JOptionPane.INFORMATION_MESSAGE);
    }

    private void gestisciOffertaRicevuta(boolean accetta) {
        int selectedRow = tabellaOfferteRicevute.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un'offerta dalla tabella.");
            return;
        }

        int idOfferta = (int) tabellaOfferteRicevute.getValueAt(selectedRow, 0);
        
        // Trova l'offerta
        List<Offerta> offerteRicevute = getAllOfferteRicevute();
        Offerta offerta = offerteRicevute.stream()
            .filter(o -> o.getIdOfferta() == idOfferta)
            .findFirst()
            .orElse(null);

        if (offerta == null) {
            JOptionPane.showMessageDialog(this, "Offerta non trovata.");
            return;
        }

        // Controlla se l'offerta è in sospeso
        if (offerta.getStatoOfferta() != StatoOfferta.IN_SOSPESO) {
            JOptionPane.showMessageDialog(this, "Puoi gestire solo offerte in sospeso!");
            return;
        }

        String azione = accetta ? "accettare" : "rifiutare";
        int conferma = JOptionPane.showConfirmDialog(
            this, 
            "Sei sicuro di voler " + azione + " questa offerta?", 
            "Conferma", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (conferma != JOptionPane.YES_OPTION) {
            return;
        }

        boolean esito;
        if (accetta) {
            esito = offertaController.accettaOfferta(idOfferta);
        } else {
            esito = offertaController.rifiutaOfferta(idOfferta);
        }

        if (esito) {
            String messaggio = accetta ? "Offerta accettata! L'annuncio è stato chiuso." : "Offerta rifiutata.";
            JOptionPane.showMessageDialog(this, messaggio);
            aggiornaOfferte();
            caricaAnnunciDisponibili();
        } else {
            JOptionPane.showMessageDialog(this, "Errore durante l'operazione. Riprova.");
        }
    }

    private List<Offerta> getAllOfferteRicevute() {
        List<Offerta> result = new ArrayList<>();
        try {
            List<Annuncio> mieiAnnunci = new ArrayList<>();
            List<Annuncio> tuttiAnnunci = annuncioController.mostraTuttiAnnunci();
            
            for (Annuncio a : tuttiAnnunci) {
                if (a.getUsername() != null && a.getUsername().equals(currentUsername)) {
                    mieiAnnunci.add(a);
                }
            }

            for (Annuncio annuncio : mieiAnnunci) {
                List<Offerta> offerteAnnuncio = offertaController.getOffertePerAnnuncio(annuncio.getIdAnnuncio());
                result.addAll(offerteAnnuncio);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void caricaAnnunciDisponibili() {
        comboAnnunciDisponibili.removeAllItems();
        List<Annuncio> tuttiAnnunci = annuncioController.mostraTuttiAnnunci();
        
        for (Annuncio a : tuttiAnnunci) {
            if (a.isDisponibile() && a.getUsername() != null && !a.getUsername().equals(currentUsername)) {
                comboAnnunciDisponibili.addItem(a);
            }
        }
        
        if (comboAnnunciDisponibili.getItemCount() == 0) {
            comboAnnunciDisponibili.addItem(null);
        }
    }
}