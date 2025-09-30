package GUI;

import Controller.ConsegnaController;
import entità.Consegna;
import entity.enums.TipoConsegna;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConsegnaFrame extends JFrame {
    private ConsegnaController controller;
    private JTable tableRicevute, tableInviate;
    private DefaultTableModel modelRicevute, modelInviate;
    private JButton btnModificaRic, btnDettagliRic, btnAggiornaRic;
    private JButton btnModificaInv, btnDettagliInv, btnAggiornaInv;
    private JButton btnIndietro;
    private String usernameUtente;

    public ConsegnaFrame(String usernameUtente) {
        this.usernameUtente = usernameUtente;
        this.controller = new ConsegnaController();

        // Configurazione finestra
        setTitle("Gestione Consegne - Unina Swap");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principale con split
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titolo
        JLabel lblTitle = new JLabel("Gestione Consegne", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Split panel per le due sezioni
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(590);
        splitPane.setResizeWeight(0.5);

        // ===== PANEL CONSEGNE DA RICEVERE =====
        JPanel panelRicevute = creaPanelConsegne(
            "Consegne da Ricevere",
            "Annunci che hai acquistato",
            true
        );
        splitPane.setLeftComponent(panelRicevute);

        // ===== PANEL CONSEGNE DA INVIARE =====
        JPanel panelInviate = creaPanelConsegne(
            "Consegne da Inviare",
            "Annunci che ti hanno acquistato",
            false
        );
        splitPane.setRightComponent(panelInviate);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Pulsante Indietro
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnIndietro = new JButton("Indietro");
        btnIndietro.addActionListener(e -> {
            dispose();
            new MenuFrame(usernameUtente);
        });
        bottomPanel.add(btnIndietro);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Carica le consegne
        caricaConsegne();

        setVisible(true);
    }

    private JPanel creaPanelConsegne(String titolo, String sottotitolo, boolean isRicevute) {
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
        String[] colonne = {"ID", "Destinatario", "Tipo", "Data", "ID Ann."};
        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);

        if (isRicevute) {
            tableRicevute = table;
            modelRicevute = model;
        } else {
            tableInviate = table;
            modelInviate = model;
        }

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton btnModifica = new JButton("Modifica");
        JButton btnDettagli = new JButton("Dettagli");
        JButton btnAggiorna = new JButton("Aggiorna");

        buttonPanel.add(btnModifica);
        buttonPanel.add(btnDettagli);
        buttonPanel.add(btnAggiorna);

        if (isRicevute) {
            btnModificaRic = btnModifica;
            btnDettagliRic = btnDettagli;
            btnAggiornaRic = btnAggiorna;

            btnModificaRic.addActionListener(e -> modificaConsegna(true));
            btnDettagliRic.addActionListener(e -> visualizzaDettagli(true));
            btnAggiornaRic.addActionListener(e -> caricaConsegne());
        } else {
            btnModificaInv = btnModifica;
            btnDettagliInv = btnDettagli;
            btnAggiornaInv = btnAggiorna;

            btnModificaInv.addActionListener(e -> modificaConsegna(false));
            btnDettagliInv.addActionListener(e -> visualizzaDettagli(false));
            btnAggiornaInv.addActionListener(e -> caricaConsegne());
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void caricaConsegne() {
        modelRicevute.setRowCount(0);
        modelInviate.setRowCount(0);
        
        List<Consegna> tutte = controller.listaTutteConsegne();

        if (tutte != null && !tutte.isEmpty()) {
            for (Consegna c : tutte) {
                Object[] row = new Object[]{
                    c.getIdConsegna(),
                    c.getDestinatario(),
                    c.getTipoConsegna().toString(),
                    c.getData(),
                    c.getIdAnnuncio()
                };

                // TODO: Implementa la logica per distinguere se l'annuncio è dell'utente o no
                // Per ora mettiamo tutto in "da ricevere" come esempio
                // Dovresti verificare se l'annuncio appartiene all'utente loggato
                
                // ESEMPIO: Se l'utente è il venditore -> consegne da inviare
                // Se l'utente è l'acquirente -> consegne da ricevere
                
                // Placeholder: metti metà e metà per test
                if (c.getIdConsegna() % 2 == 0) {
                    modelRicevute.addRow(row);
                } else {
                    modelInviate.addRow(row);
                }
            }
        }
    }

    private void modificaConsegna(boolean isRicevute) {
        JTable table = isRicevute ? tableRicevute : tableInviate;
        DefaultTableModel model = isRicevute ? modelRicevute : modelInviate;
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleziona una consegna dalla tabella.",
                "Attenzione",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idConsegna = (int) model.getValueAt(selectedRow, 0);
        
        List<Consegna> tutte = controller.listaTutteConsegne();
        Consegna consegna = tutte.stream()
            .filter(c -> c.getIdConsegna() == idConsegna)
            .findFirst()
            .orElse(null);

        if (consegna == null) {
            JOptionPane.showMessageDialog(this, "Consegna non trovata.", 
                "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Modifica Consegna", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField txtDestinatario = new JTextField(consegna.getDestinatario(), 20);
        JTextArea txtNote = new JTextArea(consegna.getNoteConsegna(), 3, 20);
        JScrollPane scrollNote = new JScrollPane(txtNote);
        JComboBox<TipoConsegna> cmbTipo = new JComboBox<>(TipoConsegna.values());
        cmbTipo.setSelectedItem(consegna.getTipoConsegna());
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        JTextField txtData = new JTextField(consegna.getData().format(dateFormatter), 20);
        JTextField txtOraInizio = new JTextField(
            consegna.getOraInizioPref() != null ? consegna.getOraInizioPref().format(timeFormatter) : "", 20);
        JTextField txtOraFine = new JTextField(
            consegna.getOraFinePref() != null ? consegna.getOraFinePref().format(timeFormatter) : "", 20);

        JTextField txtSedeUni = new JTextField(consegna.getSedeUniversitaria() != null ? consegna.getSedeUniversitaria() : "", 20);
        JTextField txtIndirizzo = new JTextField(consegna.getIndirizzo() != null ? consegna.getIndirizzo() : "", 20);
        JTextField txtCivico = new JTextField(consegna.getCivico() != null ? consegna.getCivico() : "", 20);
        JTextField txtCorriere = new JTextField(consegna.getCorriere() != null ? consegna.getCorriere() : "", 20);
        JTextField txtTracking = new JTextField(consegna.getTrackingNumber() != null ? consegna.getTrackingNumber() : "", 20);

        JPanel campiDinamici = new JPanel(new GridBagLayout());
        
        cmbTipo.addActionListener(e -> {
            campiDinamici.removeAll();
            GridBagConstraints gbcDin = new GridBagConstraints();
            gbcDin.fill = GridBagConstraints.HORIZONTAL;
            gbcDin.insets = new Insets(5, 5, 5, 5);
            gbcDin.gridx = 0;
            gbcDin.gridy = 0;

            if (cmbTipo.getSelectedItem() == TipoConsegna.A_MANO) {
                campiDinamici.add(new JLabel("Sede Universitaria:"), gbcDin);
                gbcDin.gridx = 1;
                campiDinamici.add(txtSedeUni, gbcDin);
            } else if (cmbTipo.getSelectedItem() == TipoConsegna.SPEDIZIONE) {
                campiDinamici.add(new JLabel("Indirizzo:"), gbcDin);
                gbcDin.gridx = 1;
                campiDinamici.add(txtIndirizzo, gbcDin);
                
                gbcDin.gridx = 0;
                gbcDin.gridy = 1;
                campiDinamici.add(new JLabel("Civico:"), gbcDin);
                gbcDin.gridx = 1;
                campiDinamici.add(txtCivico, gbcDin);
                
                gbcDin.gridx = 0;
                gbcDin.gridy = 2;
                campiDinamici.add(new JLabel("Corriere (opzionale):"), gbcDin);
                gbcDin.gridx = 1;
                campiDinamici.add(txtCorriere, gbcDin);
                
                gbcDin.gridx = 0;
                gbcDin.gridy = 3;
                campiDinamici.add(new JLabel("Tracking (opzionale):"), gbcDin);
                gbcDin.gridx = 1;
                campiDinamici.add(txtTracking, gbcDin);
            }
            
            campiDinamici.revalidate();
            campiDinamici.repaint();
        });

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Destinatario*:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDestinatario, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Tipo Consegna*:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbTipo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(campiDinamici, gbc);
        gbc.gridwidth = 1;

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Data (yyyy-MM-dd)*:"), gbc);
        gbc.gridx = 1;
        panel.add(txtData, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Ora Inizio (HH:mm):"), gbc);
        gbc.gridx = 1;
        panel.add(txtOraInizio, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Ora Fine (HH:mm):"), gbc);
        gbc.gridx = 1;
        panel.add(txtOraFine, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Note:"), gbc);
        gbc.gridx = 1;
        panel.add(scrollNote, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnSalva = new JButton("Salva");
        JButton btnAnnulla = new JButton("Annulla");
        btnPanel.add(btnSalva);
        btnPanel.add(btnAnnulla);
        panel.add(btnPanel, gbc);

        cmbTipo.setSelectedItem(consegna.getTipoConsegna());

        btnSalva.addActionListener(e -> {
            try {
                consegna.setDestinatario(txtDestinatario.getText().trim());
                consegna.setNoteConsegna(txtNote.getText().trim());
                consegna.setTipoConsegna((TipoConsegna) cmbTipo.getSelectedItem());
                consegna.setData(LocalDate.parse(txtData.getText().trim()));
                
                if (!txtOraInizio.getText().trim().isEmpty()) {
                    consegna.setOraInizioPref(LocalTime.parse(txtOraInizio.getText().trim()));
                }
                if (!txtOraFine.getText().trim().isEmpty()) {
                    consegna.setOraFinePref(LocalTime.parse(txtOraFine.getText().trim()));
                }

                TipoConsegna tipo = (TipoConsegna) cmbTipo.getSelectedItem();
                consegna.setSedeUniversitaria(tipo == TipoConsegna.A_MANO ? txtSedeUni.getText().trim() : null);
                consegna.setIndirizzo(tipo == TipoConsegna.SPEDIZIONE ? txtIndirizzo.getText().trim() : null);
                consegna.setCivico(tipo == TipoConsegna.SPEDIZIONE ? txtCivico.getText().trim() : null);
                consegna.setCorriere(tipo == TipoConsegna.SPEDIZIONE ? txtCorriere.getText().trim() : null);
                consegna.setTrackingNumber(tipo == TipoConsegna.SPEDIZIONE ? txtTracking.getText().trim() : null);

                boolean result = controller.modificaConsegna(consegna);

                if (result) {
                    JOptionPane.showMessageDialog(dialog, "Consegna modificata con successo!", 
                        "Successo", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    caricaConsegne();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Errore nella modifica della consegna.", 
                        "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Errore: " + ex.getMessage(), 
                    "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAnnulla.addActionListener(e -> dialog.dispose());

        dialog.add(new JScrollPane(panel));
        dialog.setVisible(true);
    }

    private void visualizzaDettagli(boolean isRicevute) {
        JTable table = isRicevute ? tableRicevute : tableInviate;
        DefaultTableModel model = isRicevute ? modelRicevute : modelInviate;
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleziona una consegna dalla tabella.",
                "Attenzione",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idConsegna = (int) model.getValueAt(selectedRow, 0);
        
        List<Consegna> tutte = controller.listaTutteConsegne();
        Consegna consegna = tutte.stream()
            .filter(c -> c.getIdConsegna() == idConsegna)
            .findFirst()
            .orElse(null);

        if (consegna == null) {
            JOptionPane.showMessageDialog(this, "Consegna non trovata.", 
                "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder dettagli = new StringBuilder();
        dettagli.append("═══════════════════════════════\n");
        dettagli.append("      DETTAGLI CONSEGNA\n");
        dettagli.append("═══════════════════════════════\n\n");
        
        dettagli.append("ID Consegna: ").append(consegna.getIdConsegna()).append("\n");
        dettagli.append("Destinatario: ").append(consegna.getDestinatario()).append("\n");
        dettagli.append("Tipo: ").append(consegna.getTipoConsegna().toString()).append("\n\n");
        
        dettagli.append("Data: ").append(consegna.getData()).append("\n");
        
        if (consegna.getOraInizioPref() != null) {
            dettagli.append("Ora Inizio Preferita: ").append(consegna.getOraInizioPref()).append("\n");
        }
        if (consegna.getOraFinePref() != null) {
            dettagli.append("Ora Fine Preferita: ").append(consegna.getOraFinePref()).append("\n");
        }
        
        dettagli.append("\n--- Dettagli Luogo ---\n");
        
        if (consegna.getTipoConsegna() == TipoConsegna.A_MANO) {
            dettagli.append("Sede Universitaria: ").append(consegna.getSedeUniversitaria()).append("\n");
        } else if (consegna.getTipoConsegna() == TipoConsegna.SPEDIZIONE) {
            dettagli.append("Indirizzo: ").append(consegna.getIndirizzo()).append(" ")
                    .append(consegna.getCivico()).append("\n");
            if (consegna.getCorriere() != null && !consegna.getCorriere().isEmpty()) {
                dettagli.append("Corriere: ").append(consegna.getCorriere()).append("\n");
            }
            if (consegna.getTrackingNumber() != null && !consegna.getTrackingNumber().isEmpty()) {
                dettagli.append("Tracking Number: ").append(consegna.getTrackingNumber()).append("\n");
            }
        }
        
        dettagli.append("\nID Annuncio: ").append(consegna.getIdAnnuncio()).append("\n");
        
        if (consegna.getNoteConsegna() != null && !consegna.getNoteConsegna().isEmpty()) {
            dettagli.append("\n--- Note ---\n");
            dettagli.append(consegna.getNoteConsegna()).append("\n");
        }
        
        dettagli.append("\n═══════════════════════════════");

        JTextArea textArea = new JTextArea(dettagli.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 400));

        JOptionPane.showMessageDialog(this, scrollPane, 
            "Dettagli Consegna", JOptionPane.INFORMATION_MESSAGE);
    }
}