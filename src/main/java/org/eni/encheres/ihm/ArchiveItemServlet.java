package org.eni.encheres.ihm;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.eni.encheres.bll.ItemManager;
import org.eni.encheres.bll.UserManager;
import org.eni.encheres.bo.ItemAllInformation;
import org.eni.encheres.bo.User;

@WebServlet("/retrait/*")
public class ArchiveItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String params = request.getPathInfo();		
		Integer idItem = Integer.parseInt(params.substring(1));
		
		HttpSession session = request.getSession();
		User userConnected = (User) session.getAttribute("user");
		
		ItemAllInformation itemAllInf = ItemManager.getInstance().selectById(idItem);
		
		if (userConnected.getNoUser()== itemAllInf.getUser().getNoUser() && itemAllInf.getItem().getState().equals("T")) {
			ItemManager.getInstance().archiveItem(itemAllInf);
			session.setAttribute("user", UserManager.getInstance().selectOneUser(userConnected.getNoUser()));
			response.sendRedirect(request.getContextPath()+"/detail-vente/"+idItem);
		}else {
			response.sendRedirect(request.getContextPath());
		}
	}


}
