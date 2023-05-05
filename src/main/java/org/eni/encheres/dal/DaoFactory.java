package org.eni.encheres.dal;


import org.eni.encheres.dal.jdbc.AuctionDaoImpl;
import org.eni.encheres.dal.jdbc.CategoryDaoImpl;
import org.eni.encheres.dal.jdbc.ItemDaoImpl;
import org.eni.encheres.dal.jdbc.UserDaoImpl;

public class DaoFactory {

	public static ItemDao getItemDao() {
		return new ItemDaoImpl();
	}

	public static UserDao getUserDao() {
		return new UserDaoImpl();
	}

	public static CategoryDao getCategoryDao() {
		return new CategoryDaoImpl();
	}
	
	public static AuctionDao getAuctionDao() {
		return new AuctionDaoImpl();
	}
	

}
