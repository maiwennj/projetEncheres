package org.eni.encheres.ihm.auction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import lombok.NonNull;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import org.eni.encheres.bll.CategoryManager;
import org.eni.encheres.bll.ItemManager;
import org.eni.encheres.bll.exception.BLLException;
import org.eni.encheres.bo.Category;
import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.User;
import org.eni.encheres.helpers.Flash;



@WebServlet("/nouvelle-vente")
public class addItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Category> listCategories = CategoryManager.getInstance().selectAllCategories();
		request.setAttribute("listCategories", listCategories);
		
		request.getRequestDispatcher("/WEB-INF/jsp/user/addItem.jsp")
		.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		try {
			String itemTitle = request.getParameter("itemTitle");
			String description = request.getParameter("description");
			Category category = CategoryManager.getInstance().selectOneCategory(Integer.parseInt( request.getParameter("category")));
			LocalDateTime startDate =LocalDateTime.parse(request.getParameter("startDate"));
			LocalDateTime endDate =LocalDateTime.parse(request.getParameter("endDate"));
			Integer initialePrice=Integer.parseInt(request.getParameter("initialePrice"));
			User user = (User) request.getSession().getAttribute("user");
			Item  item = new Item(itemTitle, description, startDate, endDate, initialePrice, user, category);
			ItemManager.getInstance().addItem(item);
			if(item.getUser().getNoUser() != null) {		
				Flash.send("success", "l'article a bien été ajouté", request.getSession());
				response.sendRedirect(request.getContextPath()+"/profil/*"+item.getUser());
			}
//		} catch (BLLException e) {
//	
//
//			doGet(request, response);
//		}
	}

	
	
	
	

}
