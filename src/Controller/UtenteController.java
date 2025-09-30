package Controller;

import DAO.UtenteDAO; //per utilizzare le funzioni di collegamento col database
import entità.Utente; //per i metodi e attributi di utente
import java.sql.SQLException; //per utilizzare l'eccezione SQL se qualcosa dovesse andare storto 
import java.util.List; //per utilizzare le List<> 

public class UtenteController 
{
	 
	private UtenteDAO utenteDAO; 
	
	public UtenteController() //costruttore del controller
	{
		this.utenteDAO = new UtenteDAO(); //crea un oggetto di tipo UtenteDAO per comunicare col DB
	}
	
	//Metodo 1: Registrazione dell'Utente al sistema
	
	public boolean registrazioneNuovoUtente(String username, String email, String Nome, String Cognome, String Password)
	{ 
		try
		{
			Utente esistente = utenteDAO.findByUser(username);
			Utente esistente2 = utenteDAO.findByMail(email);
			if(esistente != null) 
			{
				System.out.println("Il nome utente scelto è già occupato!");
				return false;
			}
			
			if(esistente2 != null)
			{
				System.out.println("La mail inserita è già associata a un account");
			}
			
			Utente nuovo = new Utente(username, email, Nome, Cognome, Password); 
			
			utenteDAO.InsertInDB(nuovo);
			System.out.println("L'utente è stato registrato. Username: " + nuovo.getUsername());
			return true;
		} catch(SQLException ex)
				{
					ex.printStackTrace();
					return false;
				}
	}
	
	//Metodo 2: LogIn al sistema per un utente registrato
	
	public Utente loginUtente(String username, String password)
	{
		
		try
		{
			Utente utente = utenteDAO.findByUser(username);
			
			if(utente.getPassword().equals(password))
			{
				System.out.println("L'utente " + utente.getUsername() + " ha completato l'accesso correttamente");
				return utente;
			}
			else
			{
				System.out.println("Attenzione, Username o Password errati!");
				return null;
			}
			
		} catch(SQLException ex)
				{
					ex.printStackTrace();
					return null;
				}
		
	}
	

	//Metodo 3: Recupero lista di tutti gli utenti 
	
	public List<Utente> listaUtentiRegistrati()
	{
		try
		{
			return utenteDAO.findAll();
		} catch(SQLException ex)
				{
					ex.printStackTrace();
					return null;
				}
	}
	
	//Metodo 4: Eliminazione di un utente
	
	public boolean eliminaUtenteDaSistema(String Username, String password)
	{
		try
		{
			Utente utente = utenteDAO.findByUser(Username); 
			
			if(utente.getPassword().equals(password))
				return utenteDAO.delete(Username);
			else 
			{
				System.out.println("Utente non trovato!"); 
				return false; 
			}
			
		} catch (SQLException ex)
				{
					ex.printStackTrace();
					return false;
				}
		
	}
	
	public Utente cercaUtente(String username) 
	{		
		try
		{
			Utente utente = utenteDAO.findByUser(username);
			
			if(utente != null)
				return utente;
			else return null;
		}
			catch(SQLException ex)
		{
				ex.printStackTrace();
				return null;
		}
	}
	


}
