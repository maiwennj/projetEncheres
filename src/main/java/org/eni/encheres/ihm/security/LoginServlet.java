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

@WebServlet("/connexion")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/security/login.jsp")
		.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String login = request.getParameter("login").trim();
			String password = request.getParameter("password").trim();

			User user = UserManager.getInstance().login(login,password);
			
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			response.sendRedirect(request.getContextPath());
			
		} catch (BLLException e) {
			request.setAttribute("errors", e.getErreurs());
			doGet(request, response);
		}
		
		
	}

}
