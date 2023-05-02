package it.prova.raccoltafilm.web.servlet.regista;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.boot.model.source.spi.PluralAttributeElementSourceOneToMany;

import it.prova.raccoltafilm.exceptions.ElementNotFoundException;
import it.prova.raccoltafilm.exceptions.RegistaConFilmException;
import it.prova.raccoltafilm.model.Film;
import it.prova.raccoltafilm.model.Regista;
import it.prova.raccoltafilm.service.FilmService;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RegistaService;

/**
 * Servlet implementation class PrepareDeleteRegistaServlet
 */
@WebServlet("/admin/PrepareDeleteRegistaServlet")
public class PrepareDeleteRegistaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private FilmService filmService;
	private RegistaService registaService;
	
    public PrepareDeleteRegistaServlet() {
    	this.filmService = MyServiceFactory.getFilmServiceInstance();
    	this.registaService = MyServiceFactory.getRegistaServiceInstance();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String idRegistaParam = request.getParameter("idRegista");
		
		if (!NumberUtils.isCreatable(idRegistaParam)) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("home").forward(request, response);
			return;
		}
		
		try {
			
			Regista registaInstance = registaService.caricaSingoloElementoConFilms(Long.parseLong(idRegistaParam));

			if (registaInstance == null)
				throw new ElementNotFoundException("Il regista che sta cercando di eliminare ha dei films.");
			
			if (registaInstance.getFilms().size() != 0)
				throw new RegistaConFilmException("Il regista che sta cercando di eliminare ha dei films.");

			request.setAttribute("delete_regista_attr", registaInstance);
			
		} 
		catch (RegistaConFilmException e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Il regista che sta cercando di eliminare ha dei films.");
			request.getRequestDispatcher("ExecuteListRegistaServlet?operationResult=NOT_FOUND").forward(request,
					response);
			return;
			
		}catch (ElementNotFoundException e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Elemento non trovato.");
			request.getRequestDispatcher("ExecuteListRegistaServlet?operationResult=NOT_FOUND").forward(request,
					response);
			return;
			
		}catch (Exception e) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("home").forward(request, response);
			return;
		}

		request.getRequestDispatcher("/regista/delete.jsp").forward(request, response);

	}

}
