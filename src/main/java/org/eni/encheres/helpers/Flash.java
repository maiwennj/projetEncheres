package org.eni.encheres.helpers;

import jakarta.servlet.http.HttpSession;

public class Flash {
	
	public static void send(String label, String message, HttpSession session) {
		session.setAttribute(label, message);
	}
	
	public static String getMessage(String label,HttpSession session) {
		String message = (String) session.getAttribute(label);
		session.removeAttribute(label);
		return message;
	}
	
}
