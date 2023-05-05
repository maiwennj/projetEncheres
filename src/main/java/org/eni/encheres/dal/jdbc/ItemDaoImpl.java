package org.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.eni.encheres.bo.User;
import org.apache.tomcat.dbcp.dbcp2.PStmtKey;
import org.apache.tomcat.jakartaee.bcel.classfile.StackMapType;
import org.eni.encheres.bo.Category;
import org.eni.encheres.bo.Item;
import org.eni.encheres.config.ConnectionProvider;
import org.eni.encheres.dal.ItemDao;



public class ItemDaoImpl implements ItemDao {
	
	final String SELECT_ALL_ITEMS = "SELECT * FROM ARTICLES_VENDUS";
	final String SELECT_BY_CATEGORY = "SELECT * FROM ARTICLES_VENDUS WHERE no_categorie=?";
	final String SELECT_BY_TITLE = "SELECT * FROM ARTICLES_VENDUS WHERE nom_article LIKE ?";
	final String SELECT_BY_TITLE_BY_CATEGORY = "SELECT * FROM ARTICLES_VENDUS WHERE no_categorie=? AND nom_article LIKE ?";
	final String ADD_ITEM="INSERT INTO ARTICLES_VENDUS (nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,prix_vente,no_utilisateur,no_categorie,etat_vente) VALUES(?,?,?,?,?,?,?,?)";
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
				rs.getString(10)
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
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_BY_CATEGORY);
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
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_BY_TITLE);
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

			PreparedStatement pStmt = cnx.prepareStatement(SELECT_BY_TITLE_BY_CATEGORY);
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

	@Override
	public void addItem(Item item) {
		

			
			try(Connection connection = ConnectionProvider.getConnection()){
				
				PreparedStatement  stmt = connection.prepareStatement(ADD_ITEM,PreparedStatement.RETURN_GENERATED_KEYS
											);
				stmt.setString(1, item.getItemTitle());
				stmt.setString(2, item.getDescription());
				// uniquement la date pas heure
				stmt.setDate(3,(item.getStartDate().toLocalTime()));
				stmt.setDate(4,Date.valueOf(item.getEndDate().toLocalDate()));
				stmt.setInt(5,item.getInitialePrice());
				stmt.setInt(6, item.getSellingPrice());
				stmt.setInt(7,item.getCategory().getNoCategory());
			
				stmt.executeUpdate();
				ResultSet rs = stmt.getGeneratedKeys();
				if(rs.next()) {
					item.setNoItem(1);
				}
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}




