package GUI;

import Controller.AnnuncioController;
import Controller.CategoriaController;
import Controller.ConsegnaController;
import Controller.OffertaController;
import entità.Annuncio;
import entità.Categoria;
import entità.Offerta;
import entity.enums.TipoAnnuncio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AnnuncioFrame extends JFrame {

    private String username;
    private AnnuncioController annuncioCtrl;
    private CategoriaController categoriaCtrl;
    private OffertaController offertaCtrl;

    private JTable tabella;
    private DefaultTableModel modelloTabella;

    private JComboBox<Annuncio> comboModifica;
    private JComboBox<Annuncio> comboElimina;

    public AnnuncioFrame(String username) {
        this.username = username;
        this.annuncioCtrl = new AnnuncioController();
        this.categoriaCtrl = new CategoriaController();
        this.offertaCtrl = new OffertaController();

        setTitle("Gestione Annunci - Utente: " + username);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superiore con bottone Home
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnHome = new JButton("🏠 Torna alla Home");
        btnHome.setPreferredSize(new Dimension(150, 30));
        btnHome.addActionListener(e -> {
            this.dispose(); // Chiude la finestra corrente
            // Apri la finestra del menu principale
            SwingUtilities.invokeLater(() -> {
                try {
                    // Assumendo che tu abbia una classe MenuFrame
                    new MenuFrame(username).setVisible(true);
                } catch (Exception ex) {
                    // Se MenuFrame non esiste, almeno chiude la finestra
                    System.out.println("MenuFrame non trovato: " + ex.getMessage());
                }
            });
        });
        topPanel.add(btnHome);
        add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        // --- TAB 1: Visualizza ---
        JPanel tabVisualizza = new JPanel(new BorderLayout());
        modelloTabella = new DefaultTableModel(
                new Object[]{"ID", "Titolo", "Descrizione", "Tipo", "Prezzo Min", "Prezzo Vendita", "Oggetti", "Disponibile", "Categoria"}, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabella non editabile
            }
        };
        tabella = new JTable(modelloTabella);
        refreshTabella();
        tabVisualizza.add(new JScrollPane(tabella), BorderLayout.CENTER);
        tabs.add("Visualizza Annunci", tabVisualizza);

        // --- TAB 2: Crea ---
        JPanel tabCrea = creaFormAnnuncio();
        tabs.add("Crea Annuncio", tabCrea);

        // --- TAB 3: Modifica ---
        JPanel tabModifica = creaFormModifica();
        tabs.add("Modifica Annuncio", tabModifica);

        // --- TAB 4: Elimina ---
        JPanel tabElimina = creaFormElimina();
        tabs.add("Elimina Annuncio", tabElimina);

        // Carica gli annunci dopo aver creato tutti i combo
        caricaAnnunci();

        add(tabs, BorderLayout.CENTER);
        setVisible(true);
    }

    // ================== METODI ==================

    private JPanel creaFormAnnuncio() {
        JPanel panel = new JPanel(new GridLayout(10, 2, 5, 5));

        JTextField txtTitolo = new JTextField();
        JTextArea txtDescrizione = new JTextArea(3, 20);
        JComboBox<TipoAnnuncio> comboTipo = new JComboBox<>(TipoAnnuncio.values());
        JTextField txtPrezzoMin = new JTextField();
        JTextField txtPrezzoVendita = new JTextField();
        JTextField txtOggetti = new JTextField();

        JComboBox<Categoria> comboCategoria = new JComboBox<>();
        try {
            for (Categoria c : categoriaCtrl.listCategorie()) {
                comboCategoria.addItem(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton btnCrea = new JButton("Crea Annuncio");

        panel.add(new JLabel("Titolo:"));
        panel.add(txtTitolo);
        panel.add(new JLabel("Descrizione:"));
        panel.add(new JScrollPane(txtDescrizione));
        panel.add(new JLabel("Tipo:"));
        panel.add(comboTipo);
        panel.add(new JLabel("Prezzo Offerta Minima:"));
        panel.add(txtPrezzoMin);
        panel.add(new JLabel("Prezzo Vendita:"));
        panel.add(txtPrezzoVendita);
        panel.add(new JLabel("Oggetti da Scambiare:"));
        panel.add(txtOggetti);
        panel.add(new JLabel("Categoria:"));
        panel.add(comboCategoria);
        panel.add(new JLabel(""));
        panel.add(btnCrea);

        // gestione creazione
        btnCrea.addActionListener(e -> {
            try {
                String titolo = txtTitolo.getText().trim();
                String descrizione = txtDescrizione.getText().trim();
                
                // Validazioni
                if (titolo.length() < 3) {
                    JOptionPane.showMessageDialog(this, "Il titolo deve avere almeno 3 caratteri.");
                    return;
                }
                
                if (descrizione.length() < 10) {
                    JOptionPane.showMessageDialog(this, "La descrizione deve avere almeno 10 caratteri.");
                    return;
                }
                
                TipoAnnuncio tipo = (TipoAnnuncio) comboTipo.getSelectedItem();
                Categoria cat = (Categoria) comboCategoria.getSelectedItem();

                double prezzoMin = 0;
                double prezzoVendita = 0;
                String oggetti = "";

                if (tipo == TipoAnnuncio.VENDITA) {
                    prezzoMin = Double.parseDouble(txtPrezzoMin.getText().trim());
                    prezzoVendita = Double.parseDouble(txtPrezzoVendita.getText().trim());
                } else if (tipo == TipoAnnuncio.SCAMBIO) {
                    oggetti = txtOggetti.getText().trim();
                }

                boolean ok = annuncioCtrl.creaNuovoAnnuncio(
                        titolo, descrizione, tipo, oggetti, prezzoMin, prezzoVendita, username, cat.getNomeCategoria()
                );

                if (ok) {
                    JOptionPane.showMessageDialog(this, "Annuncio creato con successo!");
                    refreshTabella();
                    caricaAnnunci();
                    // Reset dei campi dopo la creazione
                    txtTitolo.setText("");
                    txtDescrizione.setText("");
                    txtPrezzoMin.setText("");
                    txtPrezzoVendita.setText("");
                    txtOggetti.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Errore nella creazione.");
                }

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Errore nei valori numerici inseriti.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel creaFormModifica() {
        JPanel panel = new JPanel(new GridLayout(9, 2, 5, 5));

        comboModifica = new JComboBox<>();
        
        JTextField txtTitolo = new JTextField();
        JTextArea txtDescrizione = new JTextArea(3, 20);
        JComboBox<TipoAnnuncio> comboTipo = new JComboBox<>(TipoAnnuncio.values());
        JTextField txtPrezzoMin = new JTextField();
        JTextField txtPrezzoVendita = new JTextField();
        JTextField txtOggetti = new JTextField();
        JCheckBox chkDisponibile = new JCheckBox("Disponibile");

        JButton btnModifica = new JButton("Salva Modifiche");

        panel.add(new JLabel("Seleziona Annuncio:"));
        panel.add(comboModifica);
        panel.add(new JLabel("Titolo:"));
        panel.add(txtTitolo);
        panel.add(new JLabel("Descrizione:"));
        panel.add(new JScrollPane(txtDescrizione));
        panel.add(new JLabel("Tipo:"));
        panel.add(comboTipo);
        panel.add(new JLabel("Prezzo Offerta Minima:"));
        panel.add(txtPrezzoMin);
        panel.add(new JLabel("Prezzo Vendita:"));
        panel.add(txtPrezzoVendita);
        panel.add(new JLabel("Oggetti da Scambiare:"));
        panel.add(txtOggetti);
        panel.add(new JLabel(""));
        panel.add(chkDisponibile);
        panel.add(new JLabel(""));
        panel.add(btnModifica);

        // Listener per caricare i dati quando si seleziona un annuncio
        comboModifica.addActionListener(e -> {
            Annuncio annuncio = (Annuncio) comboModifica.getSelectedItem();
            if (annuncio != null) {
                // Valorizza tutti i campi con i dati originali
                txtTitolo.setText(annuncio.getTitolo());
                txtDescrizione.setText(annuncio.getDescrizione());
                comboTipo.setSelectedItem(annuncio.getTipoAnnuncio());
                txtPrezzoMin.setText(String.valueOf(annuncio.getPrezzoOffertaMinima()));
                txtPrezzoVendita.setText(String.valueOf(annuncio.getPrezzoVendita()));
                txtOggetti.setText(annuncio.getOggettiDaScambiare());
                chkDisponibile.setSelected(annuncio.isDisponibile());
                
                // Abilita/disabilita campi in base al tipo
                boolean isVendita = annuncio.getTipoAnnuncio() == TipoAnnuncio.VENDITA;
                txtPrezzoMin.setEnabled(isVendita);
                txtPrezzoVendita.setEnabled(isVendita);
                txtOggetti.setEnabled(!isVendita);
            }
        });

        // Listener per abilitare/disabilitare campi quando cambia il tipo
        comboTipo.addActionListener(e -> {
            TipoAnnuncio tipo = (TipoAnnuncio) comboTipo.getSelectedItem();
            boolean isVendita = tipo == TipoAnnuncio.VENDITA;
            txtPrezzoMin.setEnabled(isVendita);
            txtPrezzoVendita.setEnabled(isVendita);
            txtOggetti.setEnabled(!isVendita);
        });

        btnModifica.addActionListener(e -> {
            Annuncio sel = (Annuncio) comboModifica.getSelectedItem();
            if (sel == null) {
                JOptionPane.showMessageDialog(this, "Nessun annuncio selezionato.");
                return;
            }
            try {
                // Validazioni
                String titolo = txtTitolo.getText().trim();
                String descrizione = txtDescrizione.getText().trim();
                
                if (titolo.length() < 3) {
                    JOptionPane.showMessageDialog(this, "Il titolo deve avere almeno 3 caratteri.");
                    return;
                }
                
                if (descrizione.length() < 10) {
                    JOptionPane.showMessageDialog(this, "La descrizione deve avere almeno 10 caratteri.");
                    return;
                }

                sel.setTitolo(titolo);
                sel.setDescrizione(descrizione);
                sel.setTipoAnnuncio((TipoAnnuncio) comboTipo.getSelectedItem());
                sel.setDisponibile(chkDisponibile.isSelected());
                
                // Aggiorna campi specifici per tipo
                TipoAnnuncio tipo = (TipoAnnuncio) comboTipo.getSelectedItem();
                if (tipo == TipoAnnuncio.VENDITA) {
                    sel.setPrezzoOffertaMinima(Double.parseDouble(txtPrezzoMin.getText().trim()));
                    sel.setPrezzoVendita(Double.parseDouble(txtPrezzoVendita.getText().trim()));
                    sel.setOggettiDaScambiare("");
                } else if (tipo == TipoAnnuncio.SCAMBIO) {
                    sel.setOggettiDaScambiare(txtOggetti.getText().trim());
                    sel.setPrezzoOffertaMinima(0);
                    sel.setPrezzoVendita(0);
                }

                boolean ok = annuncioCtrl.modificaAnnuncio(sel);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Annuncio modificato!");
                    refreshTabella();
                    caricaAnnunci();
                } else {
                    JOptionPane.showMessageDialog(this, "Errore modifica.");
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Errore nei valori numerici inseriti.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        return panel;
    }

    private JPanel creaFormElimina() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel principale centrato
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(new JLabel("Seleziona Annuncio:"), gbc);
        
        // ComboBox
        comboElimina = new JComboBox<>();
        comboElimina.setPreferredSize(new Dimension(300, 25));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(comboElimina, gbc);
        
        // Bottone Elimina
        JButton btnElimina = new JButton("Elimina");
        btnElimina.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(btnElimina, gbc);
        
        // Aggiungi il panel principale al centro
        panel.add(mainPanel, BorderLayout.CENTER);
        
        btnElimina.addActionListener(e -> eliminaAnnuncio());

        return panel;
    }

    // ================== FUNZIONI SUPPORTO ==================

    private void refreshTabella() {
        modelloTabella.setRowCount(0);
        try {
            List<Annuncio> annunci = annuncioCtrl.mostraTuttiAnnunci();
            for (Annuncio a : annunci) {
                Categoria c = categoriaCtrl.searchByID(a.getIdCategoria());
                modelloTabella.addRow(new Object[]{
                        a.getIdAnnuncio(),
                        a.getTitolo(),
                        a.getDescrizione(),
                        a.getTipoAnnuncio(),
                        a.getPrezzoOffertaMinima(),
                        a.getPrezzoVendita(),
                        a.getOggettiDaScambiare(),
                        a.isDisponibile(),
                        c != null ? c.getNomeCategoria() : "?"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo per caricare annunci nella combo di Modifica ed Elimina
    private void caricaAnnunci() {
        if (comboModifica != null) {
            comboModifica.removeAllItems();
        }
        if (comboElimina != null) {
            comboElimina.removeAllItems();
        }

        try {
            // Workaround per il bug nel controller: usiamo tutti gli annunci e filtriamo
            List<Annuncio> tuttiAnnunci = annuncioCtrl.mostraTuttiAnnunci();
            List<Annuncio> annunci = new ArrayList<>();
            
            if (tuttiAnnunci != null) {
                for (Annuncio a : tuttiAnnunci) {
                    if (username.equals(a.getUsername())) {
                        annunci.add(a);
                    }
                }
            }

            if (annunci.isEmpty()) {
                // Aggiungiamo un annuncio "fittizio" per indicare che non ce ne sono
                // L'utente non potrà selezionarlo per le operazioni
                return;
            }

            for (Annuncio a : annunci) {
                if (comboModifica != null) {
                    comboModifica.addItem(a);
                }
                if (comboElimina != null) {
                    comboElimina.addItem(a);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo per estrarre l'ID dalla stringa del combo
    private int estraiIdDaStringa(String str) {
        try {
            int start = str.lastIndexOf("ID: ") + 4;
            int end = str.lastIndexOf(")");
            if (start < 4 || end == -1 || end <= start) {
                return -1;
            }
            return Integer.parseInt(str.substring(start, end));
        } catch (Exception e) {
            return -1;
        }
    }

    // Metodo per trovare un annuncio per ID
    private Annuncio trovaAnnuncioPerId(int id) {
        try {
            // Workaround: usiamo mostraTuttiAnnunci e filtriamo manualmente
            List<Annuncio> tuttiAnnunci = annuncioCtrl.mostraTuttiAnnunci();
            if (tuttiAnnunci != null) {
                for (Annuncio a : tuttiAnnunci) {
                    if (a.getIdAnnuncio() == id && username.equals(a.getUsername())) {
                        return a;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void eliminaAnnuncio() {
        Annuncio sel = (Annuncio) comboElimina.getSelectedItem();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Nessun annuncio selezionato.");
            return;
        }
        try {
            // Controllo offerte
            List<Offerta> offerte = offertaCtrl.getOffertePerAnnuncio(sel.getIdAnnuncio());
            if (!offerte.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Impossibile eliminare: ci sono offerte attive.");
                return;
            }

            // Controllo consegne
            ConsegnaController consCtrl = new ConsegnaController();
            if (!consCtrl.listaConsegnePerAnnuncio(sel.getIdAnnuncio()).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Impossibile eliminare: ci sono consegne attive.");
                return;
            }

            boolean ok = annuncioCtrl.eliminaAnnuncio(sel.getIdAnnuncio());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Annuncio eliminato!");
                refreshTabella();
                caricaAnnunci();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}