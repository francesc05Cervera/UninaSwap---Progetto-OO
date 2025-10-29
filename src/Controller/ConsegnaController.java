package Controller;

import DAO.ConsegnaDAO;
import entità.Consegna;
import entity.enums.TipoConsegna;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ConsegnaController 
{

    private ConsegnaDAO consegnaDAO;

    public ConsegnaController() {
        this.consegnaDAO = new ConsegnaDAO();
    }

    // Creazione nuova consegna
    public int creaConsegna(String destinatario, String note, TipoConsegna tipo, String sedeUni, LocalDate data, LocalTime oraInizio,LocalTime oraFine, String indirizzo, String civico, String corriere,String tracking, int idAnnuncio) {
        try {
            if (destinatario == null || destinatario.trim().isEmpty()) 
            {
                System.out.println("Destinatario non valido!");
                return -1;
            }

            Consegna nuovaConsegna = new Consegna();
            nuovaConsegna.setDestinatario(destinatario);
            nuovaConsegna.setNoteConsegna(note);
            nuovaConsegna.setTipoConsegna(tipo);
            nuovaConsegna.setSedeUniversitaria(sedeUni);
            nuovaConsegna.setData(data);
            nuovaConsegna.setOraInizioPref(oraInizio);
            nuovaConsegna.setOraFinePref(oraFine);
            nuovaConsegna.setIndirizzo(indirizzo);
            nuovaConsegna.setCivico(civico);
            nuovaConsegna.setCorriere(corriere);
            nuovaConsegna.setTrackingNumber(tracking);
            nuovaConsegna.setIdAnnuncio(idAnnuncio);

            int newId = consegnaDAO.insertIntoDB(nuovaConsegna);
            
            if (newId > 0) 
            {
                System.out.println("Consegna creata con ID: " + newId);
                return newId;
            }
          return -1;
        } catch (SQLException ex) 
        	{
            ex.printStackTrace();
            return -1;
        	}
    }

    // Modifica consegna
    public boolean modificaConsegna(Consegna consDaMod) {
        try {
            return consegnaDAO.update(consDaMod);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Elimina consegna
    public boolean eliminaConsegna(int idConsegna) {
        try {
            return consegnaDAO.delete(idConsegna);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Recupera tutte le consegne
    public List<Consegna> listaTutteConsegne() {
        try {
            return consegnaDAO.findAll();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Recupera consegne per annuncio
    public List<Consegna> listaConsegnePerAnnuncio(int idAnnuncio) {
        try {
            return consegnaDAO.findByAnnuncio(idAnnuncio);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
