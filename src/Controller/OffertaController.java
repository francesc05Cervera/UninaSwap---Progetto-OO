package Controller;
import DAO.OffertaDAO;
import DAO.AnnuncioDAO;
import entità.Offerta;
import entità.Annuncio;
import entity.enums.StatoOfferta;
import entity.enums.TipoAnnuncio;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class OffertaController {
    private OffertaDAO offertaDAO;
    private AnnuncioDAO annuncioDAO;

    public OffertaController() {
        this.offertaDAO = new OffertaDAO();
        this.annuncioDAO = new AnnuncioDAO();
    }

    // Creazione di una nuova offerta
    public boolean creaOfferta(LocalDate data, TipoAnnuncio tipo, double prezzoProposto, String oggetti, String messaggio, String username, int idAnnuncio, String comeConsegnare) {
        try {
            Annuncio annuncio = annuncioDAO.findByID(idAnnuncio);
            if (annuncio == null || !annuncio.isDisponibile()) {
                System.out.println("Annuncio non valido o non disponibile.");
                return false;
            }

            Offerta offerta = new Offerta();
            offerta.setDataOff(data);
            offerta.setStatoOfferta(StatoOfferta.IN_SOSPESO);
            offerta.setTipoOfferta(tipo);
            offerta.setPrezzoProposto(prezzoProposto);
            offerta.setOggetti(oggetti);
            offerta.setMessaggio(messaggio);
            offerta.setUsername(username);
            offerta.setIdAnnuncio(idAnnuncio);
            offerta.setComeConsegnare(comeConsegnare);

            int newID = offertaDAO.insertIntoDB(offerta);
            if (newID > 0) {
                System.out.println("Offerta creata con ID: " + newID);
                return true;
            }
            return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Accetta un'offerta e rifiuta tutte le altre
    public boolean accettaOfferta(int idOfferta) {
        try {
            Offerta offerta = offertaDAO.getByID(idOfferta);
            if (offerta == null) {
                System.out.println("Offerta non trovata.");
                return false;
            }

            // Aggiorna stato accettata
            offertaDAO.updateStato(idOfferta, StatoOfferta.ACCETTATA);

            // Rifiuta tutte le altre offerte sullo stesso annuncio
            List<Offerta> tutte = offertaDAO.getAllByAnnuncio(offerta.getIdAnnuncio());
            for (Offerta o : tutte) {
                if (o.getIdOfferta() != idOfferta) {
                    offertaDAO.updateStato(o.getIdOfferta(), StatoOfferta.RIFIUTATA);
                }
            }

            // Marca l'annuncio come non disponibile
            Annuncio annuncio = annuncioDAO.findByID(offerta.getIdAnnuncio());
            if (annuncio != null) {
                annuncio.setDisponibile(false);
                annuncioDAO.update(annuncio);
            }

            System.out.println("Offerta accettata e annuncio chiuso.");
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Rifiuta un'offerta specifica
    public boolean rifiutaOfferta(int idOfferta) {
        try {
            return offertaDAO.updateStato(idOfferta, StatoOfferta.RIFIUTATA);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Recupera offerte per annuncio
    public List<Offerta> getOffertePerAnnuncio(int idAnnuncio) {
        try {
            return offertaDAO.getAllByAnnuncio(idAnnuncio);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Recupera offerte per utente
    public List<Offerta> getOffertePerUtente(String username) {
        try {
            return offertaDAO.getAllByUsername(username);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}