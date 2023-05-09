package org.eni.encheres.ihm.auction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.eni.encheres.bll.ItemManager;
import org.eni.encheres.bo.ItemAllInformation;


@WebServlet("/detail-vente/*")
public class DetailItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String params = request.getPathInfo();		
		Integer id = Integer.parseInt(params.substring(1));
		ItemAllInformation itemAllInfo = ItemManager.getInstance().selectById(id);
			System.out.println("servlet "+itemAllInfo);
		request.setAttribute("itemAllInfo", itemAllInfo);
			
		request.getRequestDispatcher("/WEB-INF/jsp/auction/item-details.jsp")
		.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
