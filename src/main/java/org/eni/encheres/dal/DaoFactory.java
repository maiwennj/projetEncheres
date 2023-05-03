package org.eni.encheres.dal;

import org.eni.encheres.bll.CategoryManager;
import org.eni.encheres.bll.UserManager;
import org.eni.encheres.dal.jdbc.CategoryDaoImpl;
import org.eni.encheres.dal.jdbc.ItemDaoImpl;
import org.eni.encheres.dal.jdbc.UserDaoImpl;

public class DaoFactory {

	public static ItemDaoImpl getItemDao() {
		return new ItemDaoImpl();
	}

	public static UserDaoImpl getUserDao() {
		return new UserDaoImpl();
	}

	public static CategoryDaoImpl getCategoryDao() {
		return new CategoryDaoImpl();
	}
	

}
