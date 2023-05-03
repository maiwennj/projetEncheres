package org.eni.encheres.ihm.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.eni.encheres.bll.UserManager;
import org.eni.encheres.bll.exception.BLLException;
import org.eni.encheres.bo.User;
import org.eni.encheres.helpers.Flash;




@WebServlet("/inscription")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/security/register.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String username = request.getParameter("username").trim();
			String lastname = request.getParameter("lastname").trim();
			String firstname = request.getParameter("firstname").trim();
			String email = request.getParameter("email").trim();
			String phoneNumber = request.getParameter("phone-number").trim();
			String street = request.getParameter("street").trim();
			String postCode = request.getParameter("post-code").trim();
			String city = request.getParameter("city").trim();
			String password = request.getParameter("password").trim();
			String passwordConfirmation = request.getParameter("password-confirmation").trim();
			
			// vérifie si les deux mots de passe sont identiques
			if(password.equals (passwordConfirmation)) {
					User user = new User(username, firstname, lastname, email, phoneNumber, street, postCode, city, password);
					UserManager.getInstance().addUser(user);
					
					//si user number existe (user créé dans la BDD sans problème) alors créer la session et redirige
					if(user.getNoUser()>0) {
						HttpSession session = request.getSession();
						session.setAttribute("user", user);
						Flash.send("success", "Votre compte a bien été créé ", request.getSession());
						response.sendRedirect(request.getContextPath());
					}

			}else{ // sinon retourne une erreur
				BLLException bll = new BLLException();
				bll.addError("Les mots de passe ne correspondent pas");
				throw bll;
			}

		} catch (BLLException e) {
			request.setAttribute("errors", e.getErreurs());
			doGet(request, response);
		}
	}
		
}
