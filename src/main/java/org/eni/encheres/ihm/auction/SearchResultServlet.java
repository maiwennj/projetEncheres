package org.eni.encheres.ihm.auction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import org.eni.encheres.bll.CategoryManager;
import org.eni.encheres.bll.ItemManager;
import org.eni.encheres.bll.UserManager;
import org.eni.encheres.bo.Category;
import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.ItemAllInformation;
import org.eni.encheres.bo.ItemsStates;

@WebServlet("/resultat-recherche")
public class SearchResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
//	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// liste de résultats de recherche
		String itemTitle= request.getParameter("item-title").trim();
		Integer idCategory= Integer.parseInt(request.getParameter("category"));
		String itemState = ItemsStates.UNDERWAY.getState();
		List<ItemAllInformation> itemsList = ItemManager.getInstance().searchItems(itemTitle,idCategory,itemState);

		request.setAttribute("itemsList", itemsList);
		
		//pour afficher la recherche au-dessus des résultats
		request.setAttribute("category", CategoryManager.getInstance().selectOneCategory(idCategory));
		// sert à afficher la liste des catégories dans la recherche
		List<Category> listCategories = CategoryManager.getInstance().selectAllCategories();
		request.setAttribute("listCategories", listCategories);
		request.getRequestDispatcher("/WEB-INF/jsp/search.jsp").forward(request, response);
	}

}
