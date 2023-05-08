package org.eni.encheres.ihm;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	    String nowDateTime = LocalDateTime.now().format(formatter);
	    System.out.println(nowDateTime);
	    nowDateTime.replace(" ", "T");
		System.out.println(nowDateTime);
		String inAWeekString = LocalDateTime.now().plusDays(7).format(formatter);
		
		List<ItemAllInformation> itemsList = ItemManager.getInstance().selectItemsByState(ItemsStates.UNDERWAY.getState());
		request.setAttribute("itemsList", itemsList);
		List<Category> listCategories = CategoryManager.getInstance().selectAllCategories();
		request.setAttribute("listCategories", listCategories);
		request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);
	}

}
