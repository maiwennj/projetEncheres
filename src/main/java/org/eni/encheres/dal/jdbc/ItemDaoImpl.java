package org.eni.encheres.dal.jdbc;

import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eni.encheres.bo.User;
import org.eni.encheres.bo.Auction;
import org.eni.encheres.bo.Category;
import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.ItemAllInformation;
import org.eni.encheres.config.ConnectionProvider;
import org.eni.encheres.dal.ItemDao;

public class ItemDaoImpl implements ItemDao {
	
	final String SELECT_ALL_ITEMS = "SELECT * FROM ARTICLES_VENDUS";
	final String SELECT_ITEMS_BY_STATE = "SELECT a.no_article, nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,a.no_utilisateur as vendeur,pseudo,e.no_utilisateur,montant_enchere "
			+ "	FROM ARTICLES_VENDUS a"
			+ "	INNER JOIN UTILISATEURS u ON a.no_utilisateur=u.no_utilisateur"
			+ "	LEFT JOIN ENCHERES e ON a.no_article=e.no_article"
			+ "	WHERE etat_vente LIKE ?";
	final String SELECT_ITEMS_BY_STATE_BY_TITLE = SELECT_ITEMS_BY_STATE+" AND nom_article LIKE ?";
	final String SELECT_ITEMS_BY_STATE_BY_CATEGORY = SELECT_ITEMS_BY_STATE+" AND no_categorie=?";
	final String SELECT_ITEMS_BY_STATE_BY_TITLE_BY_CATEGORY = SELECT_ITEMS_BY_STATE_BY_TITLE+" AND no_categorie=?";

	/**
	 * Returns ItemAllInformation needed for the search results. It's created with 3 Objects (Item,User,Auction)
	 */
	public List<ItemAllInformation> selectItemsByState(String itemState) {
		List<ItemAllInformation> itemsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ITEMS_BY_STATE);
			pStmt.setString(1, itemState);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				itemsList.add(mapItemAllInfo(rs));
			}
			return itemsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<ItemAllInformation> selectByTitle(String itemState,String itemTitle) {
		List<ItemAllInformation> itemsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ITEMS_BY_STATE_BY_TITLE);
			pStmt.setString(1, itemState);
			pStmt.setString(2, "%"+itemTitle+"%");
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				itemsList.add(mapItemAllInfo(rs));
			}
			return itemsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public List<ItemAllInformation> selectByCategory(String itemState,Integer category) {
		List<ItemAllInformation> itemsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ITEMS_BY_STATE_BY_CATEGORY);
			pStmt.setString(1, itemState);
			pStmt.setInt(2, category);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				itemsList.add(mapItemAllInfo(rs));
			}
			return itemsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectByTitleByCategory(String itemState,String itemTitle,Integer category) {
		List<ItemAllInformation> itemsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){

			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ITEMS_BY_STATE_BY_TITLE_BY_CATEGORY);
			pStmt.setString(1, itemState);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, category);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				itemsList.add(mapItemAllInfo(rs));
			}

			return itemsList;	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	

	
	
	private ItemAllInformation mapItemAllInfo(ResultSet rs) throws SQLException {
		Item item = new Item();
		ItemAllInformation itemAllInfo = new ItemAllInformation(
				new User(rs.getInt(7), rs.getString(8)),
				item = mapItem(rs), 
				(rs.getInt(10)==0?new Auction():new Auction(new User(9), item, rs.getInt(10)))
				);
		return itemAllInfo;
	}
	
	private Item mapItem (ResultSet rs) throws SQLException {
		return new Item(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4).toLocalDateTime(), rs.getTimestamp(5).toLocalDateTime(), rs.getInt(6));
	}

}
