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

@WebServlet("/supprimer-mon-compte")
public class AccountDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			String deletePassword = request.getParameter("deletePassword");
			
			//on check le mot de passe
			BCrypt.Result result = BCrypt.verifyer()
					.verify(deletePassword.toCharArray(),user.getPassword());
			if(result.verified) {
				UserManager.getInstance().deleteUser(user);
				if(UserManager.getInstance().selectOneUser(user.getNoUser())==null){
					session.invalidate();
					//redirige vers la home
					response.sendRedirect(request.getContextPath() + "/");	
				}else {//si l'user existe encore on retourne une erreur
					BLLException bll = new BLLException();
					bll.addError("Une erreur a eu lieu lors de la suppression.");
					throw bll;
				}
			}else {//si le mot de passe n'est pas bon on retourne une erreur
				BLLException bll = new BLLException();
				bll.addError("Le mot de passe est erroné");
				throw bll;
			}
		} catch (BLLException e) {//si une erreur est remonté on redirige vers la page update avec l'erreur 
			request.setAttribute("errors", e.getErreurs());
			request.getRequestDispatcher("/WEB-INF/jsp/user/account-update.jsp").forward(request, response);
		}
	}
}
