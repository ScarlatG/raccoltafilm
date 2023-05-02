package it.prova.raccoltafilm.web.servlet.regista;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RegistaService;
import it.prova.raccoltafilm.utility.UtilityForm;


@WebServlet("/PrepareUpdateRegistaServlet")
public class PrepareUpdateRegistaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// injection del Service
	private RegistaService registaService;
	
	public PrepareUpdateRegistaServlet() {
		// TODO Auto-generated constructor stub
		this.registaService = MyServiceFactory.getRegistaServiceInstance();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String idRegista = request.getParameter("idRegista");
		
		try {
			request.setAttribute("update_regista_attr", registaService.caricaSingoloElemento(UtilityForm.parseIntegerRegistaIdFromString(idRegista)));
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("home").forward(request, response);
			return;
		}

		request.getRequestDispatcher("/regista/update.jsp").forward(request, response);
	}

}