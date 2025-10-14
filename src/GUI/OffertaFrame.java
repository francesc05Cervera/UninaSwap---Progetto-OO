package GUI;

import Controller.OffertaController;
import Controller.AnnuncioController;
import entità.Offerta;
import entità.Annuncio;
import entity.enums.TipoAnnuncio;
import entity.enums.StatoOfferta;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class OffertaFrame extends JFrame 
{
    private OffertaController offertaController;
    private AnnuncioController annuncioController;
    private JTable tabellaOfferteInviate, tabellaOfferteRicevute;
    private JComboBox<Annuncio> comboAnnunciDisponibili;
    private String currentUsername;

    public OffertaFrame(String username) 
    {
        if (username == null || username.trim().isEmpty()) 
        {
            showMsg("Username non valido", "Errore", 0);
            dispose();
            return;
        }
        this.currentUsername = username.trim();
        this.offertaController = new OffertaController();
        this.annuncioController = new AnnuncioController();
        setTitle("Gestione Offerte - Unina Swap");
        setSize(1300, 750);
        setMinimumSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initializeGUI();
        setVisible(true);
    }

    private void initializeGUI() 
    {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(240, 242, 245));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));

        JLabel lblTitle = new JLabel("Gestione Offerte");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(40, 167, 69));

        JButton btnHome = createStyledButton("Indietro", new Color(108, 117, 125));
        btnHome.addActionListener(e -> {
            dispose();
            new MenuFrame(currentUsername);
        });

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnHome, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.setBackground(Color.WHITE);
        tabs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabs.addTab("Le mie Offerte", createVisualizzaTab());
        tabs.addTab("Crea Offerta", createCreaOffertaTab());

        mainPanel.add(tabs, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createVisualizzaTab() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(240, 242, 245));

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(new Color(240, 242, 245));
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(640);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);

        splitPane.setLeftComponent(creaPanelOfferte("Offerte Inviate", "Offerte che hai fatto", new Color(0, 102, 204), true));
        splitPane.setRightComponent(creaPanelOfferte("Offerte Ricevute", "Offerte sui tuoi annunci", new Color(40, 167, 69), false));

        centerWrapper.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        aggiornaOfferte();
        return mainPanel;
    }

    private JPanel creaPanelOfferte(String titolo, String sottotitolo, Color themeColor, boolean isInviate) {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(themeColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitolo = new JLabel(titolo);
        lblTitolo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitolo.setForeground(Color.WHITE);
        lblTitolo.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lblTitolo);
        headerPanel.add(Box.createVerticalStrut(5));

        JLabel lblSottotitolo = new JLabel(sottotitolo);
        lblSottotitolo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSottotitolo.setForeground(new Color(255, 255, 255, 200));
        lblSottotitolo.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lblSottotitolo);

        panel.add(headerPanel, BorderLayout.NORTH);

        String[] colonne = {"ID", "Annuncio", "Prezzo", "Stato", "Data"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setRowHeight(32);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), 50));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(230, 230, 230));

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(70);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(themeColor);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        if (isInviate) {
            tabellaOfferteInviate = table;
        } else {
            tabellaOfferteRicevute = table;
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 12));
        buttonPanel.setBackground(new Color(250, 250, 250));
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        if (isInviate) {
            JButton btnDettagli = createSmallButton("Dettagli", new Color(108, 117, 125));
            JButton btnAggiorna = createSmallButton("Aggiorna", new Color(108, 117, 125));
            btnDettagli.addActionListener(e -> visualizzaDettagliOfferta(true));
            btnAggiorna.addActionListener(e -> aggiornaOfferte());
            buttonPanel.add(btnDettagli);
            buttonPanel.add(btnAggiorna);
        } else {
            JButton btnAccetta = createSmallButton("Accetta", new Color(40, 167, 69));
            JButton btnRifiuta = createSmallButton("Rifiuta", new Color(220, 53, 69));
            JButton btnDettagli = createSmallButton("Dettagli", new Color(108, 117, 125));
            JButton btnAggiorna = createSmallButton("Aggiorna", new Color(108, 117, 125));
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
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        comboAnnunciDisponibili = new JComboBox<>();
        comboAnnunciDisponibili.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comboAnnunciDisponibili.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Annuncio) {
                    Annuncio a = (Annuncio) value;
                    setText(a.getTitolo() + " [" + a.getTipoAnnuncio() + "]");
                }
                return this;
            }
        });
        caricaAnnunciDisponibili();

        JTextField txtTipoOfferta = new JTextField(15);
        txtTipoOfferta.setEditable(false);
        txtTipoOfferta.setBackground(new Color(240, 240, 240));
        txtTipoOfferta.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JTextField txtPrezzo = createTextField("");
        JTextField txtOggetti = createTextField("");

        JTextArea txtComeConsegnare = new JTextArea(3, 15);
        txtComeConsegnare.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtComeConsegnare.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        txtComeConsegnare.setLineWrap(true);
        JScrollPane scrollCome = new JScrollPane(txtComeConsegnare);

        JTextArea txtMessaggio = new JTextArea(3, 15);
        txtMessaggio.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtMessaggio.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        txtMessaggio.setLineWrap(true);
        JScrollPane scrollMsg = new JScrollPane(txtMessaggio);

        JLabel lblPrezzo = createLabel("Prezzo (€):");
        JLabel lblOggetti = createLabel("Oggetti:");

        comboAnnunciDisponibili.addActionListener(e -> {
            Annuncio sel = (Annuncio) comboAnnunciDisponibili.getSelectedItem();
            if (sel != null) {
                TipoAnnuncio tipo = sel.getTipoAnnuncio();
                txtTipoOfferta.setText(tipo.toString());
                if (tipo == TipoAnnuncio.VENDITA) {
                    txtPrezzo.setEnabled(true);
                    txtOggetti.setEnabled(false);
                    txtOggetti.setText("");
                    lblPrezzo.setText("Prezzo (€)*:");
                    lblOggetti.setText("Oggetti:");
                } else if (tipo == TipoAnnuncio.SCAMBIO) {
                    txtPrezzo.setEnabled(false);
                    txtPrezzo.setText("");
                    txtOggetti.setEnabled(true);
                    lblPrezzo.setText("Prezzo (€):");
                    lblOggetti.setText("Oggetti*:");
                } else {
                    txtPrezzo.setEnabled(false);
                    txtPrezzo.setText("");
                    txtOggetti.setEnabled(false);
                    txtOggetti.setText("");
                    lblPrezzo.setText("Prezzo (€):");
                    lblOggetti.setText("Oggetti:");
                }
            }
        });

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        formPanel.add(createLabel("Annuncio*:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(comboAnnunciDisponibili, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        formPanel.add(createLabel("Tipo Offerta:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(txtTipoOfferta, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        formPanel.add(lblPrezzo, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(txtPrezzo, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        formPanel.add(lblOggetti, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(txtOggetti, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        formPanel.add(createLabel("Come consegnare:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(scrollCome, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        formPanel.add(createLabel("Messaggio:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(scrollMsg, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        JButton btnInvia = createStyledButton("Invia Offerta", new Color(40, 167, 69));
        btnInvia.setPreferredSize(new Dimension(200, 40));
        formPanel.add(btnInvia, gbc);

        btnInvia.addActionListener(e -> inviaOfferta(txtPrezzo, txtOggetti, txtComeConsegnare, txtMessaggio));

        if (comboAnnunciDisponibili.getItemCount() > 0) {
            comboAnnunciDisponibili.setSelectedIndex(0);
        }

        JScrollPane scrollForm = new JScrollPane(formPanel);
        scrollForm.setBorder(null);
        mainPanel.add(scrollForm, BorderLayout.CENTER);

        return mainPanel;
    }

    private void inviaOfferta(JTextField txtPrezzo, JTextField txtOggetti, JTextArea txtCome, JTextArea txtMsg) {
        Annuncio ann = (Annuncio) comboAnnunciDisponibili.getSelectedItem();
        if (ann == null) {
            showMsg("Seleziona un annuncio", "Errore", 2);
            return;
        }

        if (ann.getUsername() != null && ann.getUsername().equals(currentUsername)) {
            showMsg("Non puoi offrire sui tuoi annunci", "Errore", 0);
            return;
        }

        if (!ann.isDisponibile()) {
            showMsg("Annuncio non disponibile", "Errore", 0);
            return;
        }

        List<Offerta> esistenti = offertaController.getOffertePerAnnuncio(ann.getIdAnnuncio());
        for (Offerta o : esistenti) {
            if (o.getUsername().equals(currentUsername)) {
                showMsg("Hai già fatto un'offerta", "Errore", 0);
                return;
            }
        }

        TipoAnnuncio tipo = ann.getTipoAnnuncio();
        double prezzo = 0;
        String oggetti = "";

        if (tipo == TipoAnnuncio.VENDITA) {
            String p = txtPrezzo.getText().trim();
            if (p.isEmpty()) {
                showMsg("Inserisci il prezzo", "Errore", 0);
                return;
            }
            try {
                prezzo = Double.parseDouble(p);
                if (prezzo < 0) {
                    showMsg("Prezzo negativo", "Errore", 0);
                    return;
                }
                if (prezzo < ann.getPrezzoOffertaMinima()) {
                    showMsg("Prezzo minimo: €" + ann.getPrezzoOffertaMinima(), "Errore", 0);
                    return;
                }
            } catch (Exception ex) {
                showMsg("Prezzo non valido", "Errore", 0);
                return;
            }
        } else if (tipo == TipoAnnuncio.SCAMBIO) {
            oggetti = txtOggetti.getText().trim();
            if (oggetti.isEmpty()) {
                showMsg("Inserisci oggetti", "Errore", 0);
                return;
            }
        }

        if (offertaController.creaOfferta(LocalDate.now(), tipo, prezzo, oggetti, txtMsg.getText() != null ? txtMsg.getText().trim() : "", currentUsername, ann.getIdAnnuncio(), txtCome.getText() != null ? txtCome.getText().trim() : "")) {
            showMsg("Offerta inviata", "Successo", 1);
            txtPrezzo.setText("");
            txtOggetti.setText("");
            txtCome.setText("");
            txtMsg.setText("");
            caricaAnnunciDisponibili();
            aggiornaOfferte();
        } else {
            showMsg("Errore invio", "Errore", 0);
        }
    }

    private void aggiornaOfferte() {
        DefaultTableModel modelInv = (DefaultTableModel) tabellaOfferteInviate.getModel();
        modelInv.setRowCount(0);
        List<Offerta> inv = offertaController.getOffertePerUtente(currentUsername);
        for (Offerta o : inv) {
            modelInv.addRow(new Object[]{
                    o.getIdOfferta(),
                    "Ann. " + o.getIdAnnuncio(),
                    o.getPrezzoProposto() == 0 ? "N/A" : "€" + o.getPrezzoProposto(),
                    o.getStatoOfferta(),
                    o.getDataOff()
            });
        }

        DefaultTableModel modelRic = (DefaultTableModel) tabellaOfferteRicevute.getModel();
        modelRic.setRowCount(0);
        try {
            List<Annuncio> miei = new ArrayList<>();
            for (Annuncio a : annuncioController.mostraTuttiAnnunci()) {
                if (a.getUsername() != null && a.getUsername().equals(currentUsername)) {
                    miei.add(a);
                }
            }
            for (Annuncio a : miei) {
                for (Offerta o : offertaController.getOffertePerAnnuncio(a.getIdAnnuncio())) {
                    modelRic.addRow(new Object[]{
                            o.getIdOfferta(),
                            "Ann. " + o.getIdAnnuncio() + " da " + o.getUsername(),
                            o.getPrezzoProposto() == 0 ? "N/A" : "€" + o.getPrezzoProposto(),
                            o.getStatoOfferta(),
                            o.getDataOff()
                    });
                }
            }
        } catch (Exception e) {}
    }

    private void visualizzaDettagliOfferta(boolean isInv) {
        JTable t = isInv ? tabellaOfferteInviate : tabellaOfferteRicevute;
        int r = t.getSelectedRow();
        if (r == -1) {
            showMsg("Seleziona un'offerta", "Attenzione", 2);
            return;
        }
        int id = (int) t.getValueAt(r, 0);
        List<Offerta> lista = isInv ? offertaController.getOffertePerUtente(currentUsername) : getAllOfferteRicevute();
        Offerta off = lista.stream().filter(o -> o.getIdOfferta() == id).findFirst().orElse(null);
        if (off == null) {
            showMsg("Offerta non trovata", "Errore", 0);
            return;
        }

        StringBuilder det = new StringBuilder();
        det.append("ID: ").append(off.getIdOfferta()).append("\n");
        det.append("Annuncio: ").append(off.getIdAnnuncio()).append("\n");
        det.append("Utente: ").append(off.getUsername()).append("\n");
        det.append("Data: ").append(off.getDataOff()).append("\n");
        det.append("Tipo: ").append(off.getTipoOfferta()).append("\n\n");
        det.append("Prezzo: ").append(off.getPrezzoProposto() == 0 ? "N/A" : "€" + off.getPrezzoProposto()).append("\n");
        det.append("Oggetti: ").append(off.getOggetti() == null || off.getOggetti().isEmpty() ? "N/A" : off.getOggetti()).append("\n");
        det.append("Consegna: ").append(off.getComeConsegnare() == null || off.getComeConsegnare().isEmpty() ? "N/A" : off.getComeConsegnare()).append("\n");
        det.append("Stato: ").append(off.getStatoOfferta()).append("\n");
        if (off.getMessaggio() != null && !off.getMessaggio().isEmpty()) {
            det.append("\nMessaggio:\n").append(off.getMessaggio());
        }

        JTextArea area = new JTextArea(det.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(400, 350));
        JOptionPane.showMessageDialog(this, scroll, "Dettagli Offerta", JOptionPane.INFORMATION_MESSAGE);
    }

    private void gestisciOffertaRicevuta(boolean acc) {
        int r = tabellaOfferteRicevute.getSelectedRow();
        if (r == -1) {
            showMsg("Seleziona un'offerta", "Attenzione", 2);
            return;
        }
        int id = (int) tabellaOfferteRicevute.getValueAt(r, 0);
        List<Offerta> ric = getAllOfferteRicevute();
        Offerta off = ric.stream().filter(o -> o.getIdOfferta() == id).findFirst().orElse(null);
        if (off == null || off.getStatoOfferta() != StatoOfferta.IN_SOSPESO) {
            showMsg("Offerta non gestibile", "Errore", 0);
            return;
        }

        boolean ok = acc ? offertaController.accettaOfferta(id) : offertaController.rifiutaOfferta(id);
        if (ok) {
            showMsg(acc ? "Offerta accettata" : "Offerta rifiutata", "OK", 1);
            aggiornaOfferte();
            caricaAnnunciDisponibili();
        } else {
            showMsg("Errore operazione", "Errore", 0);
        }
    }

    private List<Offerta> getAllOfferteRicevute() {
        List<Offerta> res = new ArrayList<>();
        try {
            List<Annuncio> miei = new ArrayList<>();
            for (Annuncio a : annuncioController.mostraTuttiAnnunci()) {
                if (a.getUsername() != null && a.getUsername().equals(currentUsername)) {
                    miei.add(a);
                }
            }
            for (Annuncio a : miei) {
                res.addAll(offertaController.getOffertePerAnnuncio(a.getIdAnnuncio()));
            }
        } catch (Exception e) {}
        return res;
    }

    private void caricaAnnunciDisponibili() {
        comboAnnunciDisponibili.removeAllItems();
        for (Annuncio a : annuncioController.mostraTuttiAnnunci()) {
            if (a.isDisponibile() && a.getUsername() != null && !a.getUsername().equals(currentUsername)) {
                comboAnnunciDisponibili.addItem(a);
            }
        }
        if (comboAnnunciDisponibili.getItemCount() == 0) {
            comboAnnunciDisponibili.addItem(null);
        }
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    private JButton createSmallButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    private JTextField createTextField(String text) {
        JTextField field = new JTextField(text, 20);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return field;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private void showMsg(String msg, String title, int type) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel icon = new JLabel(type == 0 ? "✖" : type == 2 ? "⚠" : "✓");
        icon.setFont(new Font("SansSerif", Font.BOLD, 40));
        icon.setForeground(type == 0 ? new Color(220, 53, 69) : type == 2 ? new Color(255, 193, 7) : new Color(40, 167, 69));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(icon);
        p.add(Box.createVerticalStrut(10));

        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 16));
        t.setForeground(type == 0 ? new Color(220, 53, 69) : type == 2 ? new Color(255, 193, 7) : new Color(40, 167, 69));
        t.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(t);
        p.add(Box.createVerticalStrut(5));

        JLabel m = new JLabel(msg);
        m.setFont(new Font("SansSerif", Font.PLAIN, 13));
        m.setForeground(new Color(60, 60, 60));
        m.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(m);

        JOptionPane.showMessageDialog(this, p, title, JOptionPane.PLAIN_MESSAGE);
    }
}