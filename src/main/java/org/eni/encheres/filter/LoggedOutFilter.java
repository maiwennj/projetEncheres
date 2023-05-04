package org.eni.encheres.filter;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.eni.encheres.bo.User;

@WebFilter(
		dispatcherTypes = DispatcherType.REQUEST,
		urlPatterns = {
				"/connexion"
		}
	)
public class LoggedOutFilter extends HttpFilter implements Filter {
       
   
	public void destroy() {
	}

	
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		// place your code here
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		
		//GUARD
		if(user!=null) {
			response.sendRedirect(request.getContextPath());
			return;
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	
	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("Init filter LoggedOut");
	}

}
