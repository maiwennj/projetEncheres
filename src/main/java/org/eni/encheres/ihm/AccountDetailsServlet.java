package org.eni.encheres.ihm;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.eni.encheres.bll.UserManager;
import org.eni.encheres.bo.User;



@WebServlet("/profil/*")
public class AccountDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String params = request.getPathInfo();		
		int id = Integer.parseInt(params.substring(1));
		User user = UserManager.getInstance().getUser(id);
		request.setAttribute("user", user);
		request.getRequestDispatcher("/WEB-INF/jsp/user/account-details.jsp")
		.forward(request, response);
		
	}
	

}
