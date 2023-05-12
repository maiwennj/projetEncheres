package org.eni.encheres.ihm.auction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import org.eni.encheres.bll.AuctionManager;
import org.eni.encheres.bll.CategoryManager;
import org.eni.encheres.bll.ItemManager;
import org.eni.encheres.bll.UserManager;
import org.eni.encheres.bll.exception.BLLException;
import org.eni.encheres.bo.Category;
import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.ItemAllInformation;
import org.eni.encheres.bo.User;
import org.eni.encheres.helpers.Flash;


@WebServlet("/detail-vente/*")
public class DetailItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String params = request.getPathInfo();		
		Integer id = Integer.parseInt(params.substring(1));
		ItemAllInformation itemAllInfo = ItemManager.getInstance().selectById(id);
		request.setAttribute("itemAllInfo", itemAllInfo);
		List<Category> listCategories = CategoryManager.getInstance().selectAllCategories();
		request.setAttribute("listCategories", listCategories);
		if (itemAllInfo.getItem().getState().equals("N")) {
			request.getRequestDispatcher("/WEB-INF/jsp/user/updateItem.jsp")
			.forward(request, response);
		}else {
			request.getRequestDispatcher("/WEB-INF/jsp/auction/item-details.jsp")
			.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			Integer offer = Integer.parseInt(request.getParameter("offer")) ;
			User bidder = (User) session.getAttribute("user");
			Integer noItemBidded = Integer.parseInt(request.getPathInfo().substring(1));
			ItemAllInformation itemBidded = ItemManager.getInstance().selectById(noItemBidded);
			AuctionManager.getInstance().placeABid(offer,bidder,itemBidded.getItem());

			bidder.setCredit(bidder.getCredit()-offer);
			Flash.send("success", "Votre enchère a bien été prise en compte.", request.getSession());
			response.sendRedirect(request.getContextPath()+"/detail-vente/" + noItemBidded);

		} catch (BLLException e) {
			request.setAttribute("errors", e.getErreurs());
			doGet(request, response);
		}
		
		
	}

}
