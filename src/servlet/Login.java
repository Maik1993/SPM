package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * Get Methode - wird hier nicht gebaraucht
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * Einlog Methode
	 * Passwort wird abgefragt, Session wird angleget, weiterleitung zur bestimmten
	 * Seite wenn Passwort übereinstimmt oder nicht
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String passwort = request.getParameter("passwort");
		String passwort_right = "123";
		String address = "";
		String user = "KaufDort";
		
		if(!passwort.equals(passwort_right))
		{
			address = "login.jsp";
			HttpSession session = request.getSession(true); 
			session.setAttribute("error", true);
		} else
		{
			address = "startseite.jsp";
			HttpSession session = request.getSession(true); 
			session.setAttribute("name", user);
			

		}
		
		request.setAttribute("passwort", passwort);
		RequestDispatcher req = request.getRequestDispatcher(address);
		req.forward(request, response);
		
	}

}
