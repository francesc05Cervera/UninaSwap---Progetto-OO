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
import java.util.ArrayList;

public class OffertaController {
    private OffertaDAO offertaDAO;
    private AnnuncioDAO annuncioDAO;

    public OffertaController() {
        this.offertaDAO = new OffertaDAO();
        this.annuncioDAO = new AnnuncioDAO();
    }

    public String validaECreaOfferta(LocalDate data, TipoAnnuncio tipo, double prezzoProposto, 
                                      String oggetti, String messaggio, String username, 
                                      int idAnnuncio, String comeConsegnare) {
        try {
            Annuncio annuncio = annuncioDAO.findByID(idAnnuncio);
            if (annuncio == null) {
                return "Annuncio non trovato";
            }
            
            if (annuncio.getUsername() != null && annuncio.getUsername().equals(username)) {
                return "Non puoi fare offerte sui tuoi annunci";
            }
            
            if (!annuncio.isDisponibile()) {
                return "Annuncio non più disponibile";
            }
            
            List<Offerta> offerteEsistenti = offertaDAO.getAllByAnnuncio(idAnnuncio);
            for (Offerta o : offerteEsistenti) {
                if (o.getUsername().equals(username)) {
                    return "Hai già fatto un'offerta per questo annuncio";
                }
            }
            
            if (tipo == TipoAnnuncio.VENDITA) {
                if (prezzoProposto < 0) {
                    return "Il prezzo non può essere negativo";
                }
                if (prezzoProposto < annuncio.getPrezzoOffertaMinima()) {
                    return "Prezzo minimo richiesto: €" + annuncio.getPrezzoOffertaMinima();
                }
            } else if (tipo == TipoAnnuncio.SCAMBIO) {
                if (oggetti == null || oggetti.trim().isEmpty()) {
                    return "Devi specificare gli oggetti da scambiare";
                }
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
                return null;
            }
            return "Errore durante la creazione dell'offerta";
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Errore di database: " + ex.getMessage();
        }
    }

    public boolean creaOfferta(LocalDate data, TipoAnnuncio tipo, double prezzoProposto, 
                               String oggetti, String messaggio, String username, 
                               int idAnnuncio, String comeConsegnare) {
        String risultato = validaECreaOfferta(data, tipo, prezzoProposto, oggetti, 
                                               messaggio, username, idAnnuncio, comeConsegnare);
        return risultato == null;
    }

    public List<Offerta> getOfferteRicevutePerUtente(String username) {
        try {
            List<Offerta> offerteRicevute = new ArrayList<>();
            List<Annuncio> annunciUtente = annuncioDAO.cercaAnnunciPerUtente(username);
            
            for (Annuncio annuncio : annunciUtente) {
                List<Offerta> offerteAnnuncio = offertaDAO.getAllByAnnuncio(annuncio.getIdAnnuncio());
                offerteRicevute.addAll(offerteAnnuncio);
            }
            
            return offerteRicevute;
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean accettaOfferta(int idOfferta) {
        try {
            Offerta offerta = offertaDAO.getByID(idOfferta);
            if (offerta == null) {
                System.out.println("Offerta non trovata.");
                return false;
            }

            offertaDAO.updateStato(idOfferta, StatoOfferta.ACCETTATA);

            List<Offerta> tutte = offertaDAO.getAllByAnnuncio(offerta.getIdAnnuncio());
            for (Offerta o : tutte) {
                if (o.getIdOfferta() != idOfferta) {
                    offertaDAO.updateStato(o.getIdOfferta(), StatoOfferta.RIFIUTATA);
                }
            }

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

    public boolean rifiutaOfferta(int idOfferta) {
        try {
            return offertaDAO.updateStato(idOfferta, StatoOfferta.RIFIUTATA);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Offerta> getOffertePerAnnuncio(int idAnnuncio) {
        try {
            return offertaDAO.getAllByAnnuncio(idAnnuncio);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Offerta> getOffertePerUtente(String username) {
        try {
            return offertaDAO.getAllByUsername(username);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
