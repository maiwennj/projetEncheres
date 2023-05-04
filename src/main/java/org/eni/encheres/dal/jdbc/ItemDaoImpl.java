package org.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eni.encheres.bo.User;
import org.eni.encheres.bo.Category;
import org.eni.encheres.bo.Item;
import org.eni.encheres.config.ConnectionProvider;
import org.eni.encheres.dal.ItemDao;

public class ItemDaoImpl implements ItemDao {
	
	final String SELECT_ALL_ITEMS = "SELECT * FROM ARTICLES_VENDUS";
	final String SELECT_ITEMS_BY_STATE = "SELECT * FROM ARTICLES_VENDUS WHERE etat_vente LIKE ?";
	final String SELECT_ITEMS_BY_CATEGORY = "SELECT * FROM ARTICLES_VENDUS WHERE no_categorie=?";
	final String SELECT_ITEMS_BY_TITLE = "SELECT * FROM ARTICLES_VENDUS WHERE nom_article LIKE ?";
	final String SELECT_ITEMS_BY_TITLE_BY_CATEGORY = "SELECT * FROM ARTICLES_VENDUS WHERE no_categorie=? AND nom_article LIKE ?";

	/**
	 * Can be used to return an Item with all args.
	 * @param ResultSet rs
	 * @return Item
	 * @throws SQLException
	 */
	public Item mapItem(ResultSet rs) throws SQLException {
		return new Item(
				rs.getInt(1), 
				rs.getString(2), 
				rs.getString(3), 
				rs.getDate(4).toLocalDate(), 
				rs.getDate(5).toLocalDate(), 
				rs.getInt(6), 
				rs.getInt(7), 
				new User(rs.getInt(8)),
				new Category(rs.getInt(9)),
				rs.getString(10).charAt(0)
				);
	}
	
	public List<Item> selectAllItems() {
		List<Item> itemsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			Statement stmt = cnx.createStatement();
			ResultSet rs = stmt.executeQuery(SELECT_ALL_ITEMS);
			while (rs.next()) {
				itemsList.add(mapItem(rs));
			}
			return itemsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Item> selectByCategory(Integer category) {
		List<Item> itemsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ITEMS_BY_CATEGORY);
			pStmt.setInt(1, category);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				itemsList.add(mapItem(rs));
			}
			return itemsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Item> selectByTitle(String itemTitle) {
		List<Item> itemsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ITEMS_BY_TITLE);
			pStmt.setString(1, "%"+itemTitle+"%");
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				itemsList.add(mapItem(rs));
			}
			return itemsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Item> selectByTitleByCategory(String itemTitle,Integer category) {
		List<Item> itemsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){

			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ITEMS_BY_TITLE_BY_CATEGORY);
			pStmt.setInt(1, category);
			pStmt.setString(2, "%"+itemTitle+"%");

			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				itemsList.add(mapItem(rs));
			}

			return itemsList;	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Item> selectItemsByState(String state) {
		List<Item> itemsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ITEMS_BY_STATE);
			pStmt.setString(1, state);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				itemsList.add(mapItem(rs));
			}
			return itemsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}



}
