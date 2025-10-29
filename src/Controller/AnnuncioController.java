package Controller;

import DAO.AnnuncioDAO;
import DAO.CategoriaDAO;
import DAO.UtenteDAO;
import entità.Annuncio;
import entità.Utente;
import entità.Categoria;
import entity.enums.TipoAnnuncio;

import java.sql.SQLException;
import java.util.List;

public class AnnuncioController {

    private AnnuncioDAO annuncioDAO;
    private UtenteDAO utenteDAO;
    private CategoriaDAO categoriaDAO;

    public AnnuncioController() {
        this.annuncioDAO = new AnnuncioDAO();
        this.utenteDAO = new UtenteDAO();
        this.categoriaDAO = new CategoriaDAO();
    }

    // CREA NUOVO ANNUNCIO
    public boolean creaNuovoAnnuncio(String titolo, String descrizione, TipoAnnuncio tipo,
                                     String oggettiDaScambiare, double prezzoOffertaMinima,
                                     double prezzoVendita, String username, String nomeCategoria) {
        try {
            if (titolo == null || titolo.trim().isEmpty() ||
                descrizione == null || descrizione.trim().isEmpty()) {
                System.out.println("Titolo e descrizione obbligatori.");
                return false;
            }

            Utente utente = utenteDAO.findByUser(username);
            Categoria categoria = categoriaDAO.findbyNome(nomeCategoria);

            if (utente == null || categoria == null) {
                System.out.println("Utente o categoria non trovati.");
                return false;
            }

            Annuncio nuovo = null;

            switch (tipo) {
                case VENDITA:
                    if (prezzoVendita <= 0) {
                        System.out.println("Prezzo di vendita non valido.");
                        return false;
                    }
                    nuovo = new Annuncio(titolo, descrizione, tipo, null,
                            prezzoOffertaMinima, prezzoVendita, true,
                            utente.getUsername(), categoria.getIdCategoria());
                    break;

                case SCAMBIO:
                    if (oggettiDaScambiare == null || oggettiDaScambiare.trim().isEmpty()) {
                        System.out.println("Devi indicare gli oggetti da scambiare.");
                        return false;
                    }
                    nuovo = new Annuncio(titolo, descrizione, tipo, oggettiDaScambiare,
                            0.0, 0.0, true,
                            utente.getUsername(), categoria.getIdCategoria());
                    break;

                case REGALO:
                    nuovo = new Annuncio(titolo, descrizione, tipo, null,
                            0.0, 0.0, true,
                            utente.getUsername(), categoria.getIdCategoria());
                    break;
            }

            annuncioDAO.insertIntoDB(nuovo);
            System.out.println("Annuncio creato con ID: " + nuovo.getIdAnnuncio());
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // LISTA ANNUNCI
    public List<Annuncio> mostraTuttiAnnunci() {
        try {
            return annuncioDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // CERCA PER ID
    public Annuncio cercaPerId(int id) {
        try {
            return annuncioDAO.findByID(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // MODIFICA ANNUNCIO
    public boolean modificaAnnuncio(Annuncio a) { 
        try {
            return annuncioDAO.update(a);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ELIMINA ANNUNCIO
    public boolean eliminaAnnuncio(int id) {
        try {
            return annuncioDAO.delete(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Annuncio> cercaPerUser(String us)
    {
    	try
    	{
    		return annuncioDAO.cercaAnnunciPerUtente(us);
    	} catch(SQLException ex)
    	{
    		ex.printStackTrace();
    		return null; 
    	}
    }
}
