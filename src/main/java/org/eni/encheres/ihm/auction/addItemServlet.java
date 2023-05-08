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
import java.time.format.DateTimeFormatter;
import java.util.List;


import org.eni.encheres.bll.CategoryManager;
import org.eni.encheres.bll.CollectionPointManager;
import org.eni.encheres.bll.ItemManager;
import org.eni.encheres.bll.exception.BLLException;
import org.eni.encheres.bo.Category;
import org.eni.encheres.bo.CollectionPoint;
import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.ItemsStates;
import org.eni.encheres.bo.User;
import org.eni.encheres.helpers.Flash;



@WebServlet("/nouvelle-vente")
public class AddItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Category> listCategories = CategoryManager.getInstance().selectAllCategories();
		request.setAttribute("listCategories", listCategories);
		request.getRequestDispatcher("/WEB-INF/jsp/user/addItem.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// creating an Item			
			String itemTitle = request.getParameter("itemTitle");
			String description = request.getParameter("description");
			Category category = CategoryManager.getInstance().selectOneCategory(Integer.parseInt(request.getParameter("category")));
			System.out.println(category);
			Integer initialPrice=Integer.parseInt(request.getParameter("initialPrice"));
			LocalDateTime startDate =LocalDateTime.parse(request.getParameter("startDate"));
			LocalDateTime endDate =LocalDateTime.parse(request.getParameter("endDate"));
			User seller = (User) request.getSession().getAttribute("user");
			Item  item = new Item(itemTitle, description, startDate, endDate, initialPrice, seller, category,ItemsStates.NEW.getState());
			ItemManager.getInstance().addItem(item);
			
			// creating a Collection Point
			CollectionPointManager.getInstance().addCollectionPoint(new CollectionPoint(
					item, 
					request.getParameter("streetCP"), 
					request.getParameter("postCodeCP"), 
					request.getParameter("cityCP")));
			
			if(item.getUser().getNoUser() != null) {
				Flash.send("success", "l'article a bien été ajouté", request.getSession());
				response.sendRedirect(request.getContextPath()+"/detail-vente/"+item.getNoItem());
			}
		} catch (BLLException e) {
			request.setAttribute("errors", e.getErreurs());
			doGet(request, response);
		}
	}

	
	
	
	

}
