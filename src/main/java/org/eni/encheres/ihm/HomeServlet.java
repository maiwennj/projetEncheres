package org.eni.encheres.ihm;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import org.eni.encheres.bll.CategoryManager;
import org.eni.encheres.bll.ItemManager;
import org.eni.encheres.bo.Category;
import org.eni.encheres.bo.ItemAllInformation;
import org.eni.encheres.bo.ItemsStates;

@WebServlet("")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		List<ItemAllInformation> itemsList = ItemManager.getInstance().selectAllCurrentAuctions();
		request.setAttribute("itemsList", itemsList);
		List<Category> listCategories = CategoryManager.getInstance().selectAllCategories();
		request.setAttribute("listCategories", listCategories);
		request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);
		
		
		
	}

}
