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
import org.eni.encheres.bo.Category;
import org.eni.encheres.bo.ItemAllInformation;
import org.eni.encheres.bo.User;

@WebServlet("/resultat-recherche")
public class SearchResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;   

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//--------------------------  liste de résultats de recherche ---------------------------------
		String itemTitle = request.getParameter("itemTitle").trim();
		Integer idCategory= Integer.parseInt(request.getParameter("category"));
		Integer idUser=null;
		if (request.getSession().getAttribute("user")!=null) {
			User user = (User) request.getSession().getAttribute("user");			
			idUser = user.getNoUser();
		}
//		String itemState = ItemsStates.UNDERWAY.getState();
//		List<ItemAllInformation> itemsList = ItemManager.getInstance().searchItems(itemTitle,idCategory,itemState);
//		request.setAttribute("itemsList", itemsList);
		
		// booléens de la recherche connectée
		Boolean auctionsIsChecked = false;
		Boolean salesIsChecked = false;
		Boolean currentAuctions = false;
		Boolean myBids = false;
		Boolean wonAuctions = false;
		Boolean currentSales = false;
		Boolean newSales = false;
		Boolean finishedSales = false;
		
		if (request.getParameter("typeItem") !=null && request.getParameter("typeItem").equals("auctions")) {
			auctionsIsChecked = true;
			if (request.getParameter("currentAuctions")!=null) {
				currentAuctions = true;
			}
			if (request.getParameter("myBids")!=null) {
				myBids = true;
			}
			if (request.getParameter("wonAuctions")!=null) {
				wonAuctions = true;
			}
			List<ItemAllInformation> auctionsList = ItemManager.getInstance().searchAuctions(currentAuctions,myBids,wonAuctions,itemTitle,idCategory,idUser);
			request.setAttribute("itemsList", auctionsList);
		}else if (request.getParameter("typeItem") !=null && request.getParameter("typeItem").equals("sales")) {
			salesIsChecked = true;
			System.out.println("SALES CHECKED : auction = "+auctionsIsChecked+" sales = "+salesIsChecked);
			if (request.getParameter("currentSales")!=null) {
				currentSales = true;
				System.out.println("Servlet : ventes en cours");
			}
			if (request.getParameter("newSales")!=null) {
				newSales = true;
				System.out.println("Servlet : mes nouvelles ventes");
			}
			if (request.getParameter("finishedSales")!=null) {
				finishedSales = true;
				System.out.println("Servlet : mes ventes terminées");
			}
			List<ItemAllInformation> salesList = ItemManager.getInstance().searchSales(currentSales,newSales,finishedSales,itemTitle,idCategory,idUser);
			request.setAttribute("itemsList", salesList);
		}else {
			currentAuctions = true;
			List<ItemAllInformation> allItemsList = ItemManager.getInstance().searchAuctions(currentAuctions,myBids,wonAuctions,itemTitle,idCategory, idUser);
			request.setAttribute("itemsList", allItemsList);
		}
		
		//pour afficher la recherche au-dessus des résultats
		request.setAttribute("category", CategoryManager.getInstance().selectOneCategory(idCategory));

		// sert à afficher la liste des catégories dans la recherche
		List<Category> listCategories = CategoryManager.getInstance().selectAllCategories();
		request.setAttribute("listCategories", listCategories);
		request.getRequestDispatcher("/WEB-INF/jsp/search.jsp").forward(request, response);
	}

}
