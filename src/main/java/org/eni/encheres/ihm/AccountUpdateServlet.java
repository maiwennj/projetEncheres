package org.eni.encheres.ihm;

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

import at.favre.lib.crypto.bcrypt.BCrypt;

@WebServlet("/modifier-profil")
public class AccountUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/jsp/user/account-update.jsp")
			.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			User oldUser = (User) session.getAttribute("user");
			String oldPassword = request.getParameter("old-password").trim();
			
			BCrypt.Result result = BCrypt.verifyer()
					.verify(oldPassword.toCharArray(),oldUser.getPassword());
			
			//si le mot de passe est valide on passe à la suite du traitement
			if(result.verified) {
				String newPassword = request.getParameter("new-password").trim();
				String newPasswordConf= request.getParameter("new-password-confirmation").trim();
				//si le nouveau mot de passe est égal à la vérif on passe à la suite du traitement
				if(newPassword.equals(newPasswordConf)) {
					String username = request.getParameter("username").trim();
					String lastname = request.getParameter("lastname").trim();
					String firstname = request.getParameter("firstname").trim();
					String email = request.getParameter("email").trim();
					String phoneNumber = request.getParameter("phone-number").trim();
					String street = request.getParameter("street").trim();
					String postCode = request.getParameter("post-code").trim();
					String city = request.getParameter("city").trim();
					User newUser = new User(username, firstname, lastname, email, phoneNumber, street, postCode, city, newPassword);
					//on paramètre le numéro de user pour plus tard et le isAdmin pour la session
					newUser.setNoUser(oldUser.getNoUser());
					newUser.setAdmin(oldUser.isAdmin());
					//on lance l'update de user
					UserManager.getInstance().updateUser(newUser);
					//on recupère user dans bdd pour avoir toutes les info à jour
					session.setAttribute("user", newUser);
					Flash.send("success", "Votre compte a bien été modifié ", request.getSession());
					response.sendRedirect(request.getContextPath() + "/profil/" + newUser.getNoUser());
				}else{
					BLLException bll = new BLLException();
					bll.addError("Le nouveau mot de passe et la vérification ne correspondent pas");
					throw bll;
				}	
			//sinon on retourne une erreur	
			}else{
				BLLException bll = new BLLException();
				bll.addError("Le mot de passe est erroné");
				throw bll;
			}
		} catch (BLLException e) {
			request.setAttribute("errors", e.getErreurs());
			doGet(request, response);
		}
		
		
		
		


	}

}
