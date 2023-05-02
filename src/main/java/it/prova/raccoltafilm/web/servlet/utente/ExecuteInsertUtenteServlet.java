package it.prova.raccoltafilm.web.servlet.utente;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import it.prova.raccoltafilm.model.Ruolo;
import it.prova.raccoltafilm.model.Utente;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RuoloService;
import it.prova.raccoltafilm.service.UtenteService;
import it.prova.raccoltafilm.utility.UtilityForm;

@WebServlet("/admin/ExecuteInsertUtenteServlet")
public class ExecuteInsertUtenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UtenteService utenteService;
	private RuoloService ruoloService;

	public ExecuteInsertUtenteServlet() {
		super();
		this.utenteService = MyServiceFactory.getUtenteServiceInstance();
		this.ruoloService = MyServiceFactory.getRuoloServiceInstance();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Binding
		String usernameParam = request.getParameter("username");
		String nomeParam = request.getParameter("nome");
		String cognomeParam = request.getParameter("cognome");

		String passwordParam = request.getParameter("password");
		String passwordConfirmParam = request.getParameter("passwordConfirm");
		
		String[] ruoliIdParam = request.getParameterValues("ruolo");

		// Creazione istanza Utente
		Utente utenteInstance = UtilityForm.createUtenteFromParams(
				usernameParam, 
				nomeParam, 
				cognomeParam);

		try {
			
			if(ruoliIdParam==null) {
				request.setAttribute("insert_utente_attr", utenteInstance);
				request.setAttribute("ruolo_list_attribute", ruoloService.listAll());
				request.setAttribute("errorMessage", "Completare tutti i campi");
				request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
				return;
			}
			
			Set<Ruolo> ruoliInstance = new HashSet<Ruolo>(0);
			
			for(String ruoliId : ruoliIdParam) {
					ruoliInstance.add(ruoloService.caricaSingoloElemento(Long.parseLong(ruoliId)));
			}
			
			utenteInstance.setRuoli(ruoliInstance);

			// Validazione della password
			if (!passwordParam.equals(passwordConfirmParam)) {
				request.setAttribute("insert_utente_attr", utenteInstance);
				request.setAttribute("ruolo_list_attribute", ruoloService.listAll());
				request.setAttribute("errorMessage", "Attenzione le password non corrispondono");
				request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
				return;
			}
			
			utenteInstance.setPassword(passwordParam);

			// Validazione del campo utente
			if (!UtilityForm.validateUtenteBean(utenteInstance)) {
				request.setAttribute("insert_utente_attr", utenteInstance);
				request.setAttribute("ruolo_list_attribute", ruoloService.listAll());
				request.setAttribute("errorMessage", "Completare tutti i campi");
				request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
				return;
			}

			utenteService.inserisciNuovo(utenteInstance);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
			return;
		}

	}

}
