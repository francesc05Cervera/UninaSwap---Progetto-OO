package GUI;

import Controller.CategoriaController;
import entità.Categoria;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriaFrame extends JFrame {
    private CategoriaController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAggiungi, btnElimina, btnModifica, btnAggiorna, btnIndietro;
    private String usernameUtente;

    public CategoriaFrame(String usernameUtente) {
        this.usernameUtente = usernameUtente;
        this.controller = new CategoriaController();

        // Configurazione finestra
        setTitle("Gestione Categorie - Unina Swap");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principale
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titolo
        JLabel lblTitle = new JLabel("Gestione Categorie", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Tabella categorie
        String[] colonne = {"ID", "Nome Categoria"};
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabella non editabile direttamente
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnAggiungi = new JButton("Aggiungi Categoria");
        btnModifica = new JButton("Modifica Nome");
        btnElimina = new JButton("Elimina Categoria");
        btnAggiorna = new JButton("Aggiorna Lista");
        btnIndietro = new JButton("Indietro");

        buttonPanel.add(btnAggiungi);
        buttonPanel.add(btnModifica);
        buttonPanel.add(btnElimina);
        buttonPanel.add(btnAggiorna);
        buttonPanel.add(btnIndietro);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Carica le categorie all'avvio
        caricaCategorie();

        // Action Listeners
        btnAggiungi.addActionListener(e -> aggiungiCategoria());
        btnModifica.addActionListener(e -> modificaCategoria());
        btnElimina.addActionListener(e -> eliminaCategoria());
        btnAggiorna.addActionListener(e -> caricaCategorie());
        btnIndietro.addActionListener(e -> {
            dispose();
            new MenuFrame(usernameUtente);
        });

        setVisible(true);
    }

    private void caricaCategorie() {
        tableModel.setRowCount(0); // Pulisce la tabella
        List<Categoria> categorie = controller.listCategorie();

        if (categorie != null && !categorie.isEmpty()) {
            for (Categoria cat : categorie) {
                tableModel.addRow(new Object[]{
                    cat.getIdCategoria(),
                    cat.getNomeCategoria()
                });
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Nessuna categoria presente nel database.",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void aggiungiCategoria() {
        String nomeCategoria = JOptionPane.showInputDialog(this,
            "Inserisci il nome della nuova categoria:",
            "Aggiungi Categoria",
            JOptionPane.PLAIN_MESSAGE);

        if (nomeCategoria != null && !nomeCategoria.trim().isEmpty()) {
            boolean risultato = controller.aggiungiCategoria(nomeCategoria.trim());
            
            if (risultato) {
                JOptionPane.showMessageDialog(this,
                    "Categoria aggiunta con successo!",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);
                caricaCategorie();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Errore: la categoria esiste già o il nome non è valido.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modificaCategoria() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleziona una categoria dalla tabella.",
                "Attenzione",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String vecchioNome = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Verifica se ci sono annunci associati
        try {
            if (controller.contaAnnunci(vecchioNome) > 0) {
                JOptionPane.showMessageDialog(this,
                    "Impossibile modificare: ci sono annunci attivi in questa categoria.",
                    "Operazione non consentita",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Errore durante il controllo degli annunci.",
                "Errore",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuovoNome = JOptionPane.showInputDialog(this,
            "Inserisci il nuovo nome per la categoria:",
            vecchioNome);

        if (nuovoNome != null && !nuovoNome.trim().isEmpty()) {
            // Elimina la vecchia e crea la nuova
            boolean eliminato = controller.eliminaCategoria(vecchioNome);
            if (eliminato) {
                boolean aggiunto = controller.aggiungiCategoria(nuovoNome.trim());
                if (aggiunto) {
                    JOptionPane.showMessageDialog(this,
                        "Categoria modificata con successo!",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
                    caricaCategorie();
                } else {
                    // Ripristina la vecchia categoria
                    controller.aggiungiCategoria(vecchioNome);
                    JOptionPane.showMessageDialog(this,
                        "Errore durante la modifica. Categoria ripristinata.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void eliminaCategoria() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleziona una categoria dalla tabella.",
                "Attenzione",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nomeCategoria = (String) tableModel.getValueAt(selectedRow, 1);

        int conferma = JOptionPane.showConfirmDialog(this,
            "Sei sicuro di voler eliminare la categoria '" + nomeCategoria + "'?",
            "Conferma eliminazione",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (conferma == JOptionPane.YES_OPTION) {
            boolean risultato = controller.eliminaCategoria(nomeCategoria);
            
            if (risultato) {
                JOptionPane.showMessageDialog(this,
                    "Categoria eliminata con successo!",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);
                caricaCategorie();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Impossibile eliminare: ci sono annunci attivi in questa categoria.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}