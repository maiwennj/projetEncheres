package org.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.eni.encheres.bo.User;
import org.eni.encheres.bo.Auction;

import org.eni.encheres.bo.Category;
import org.eni.encheres.bo.CollectionPoint;
import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.ItemAllInformation;
import org.eni.encheres.config.ConnectionProvider;
import org.eni.encheres.dal.ItemDao;



public class ItemDaoImpl implements ItemDao {
	
//	final String SELECT_ALL_ITEMS = "SELECT * FROM ARTICLES_VENDUS";
//	final String SELECT_ITEMS_BY_STATE = "SELECT a.no_article, nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,a.no_utilisateur as vendeur,pseudo,e.no_utilisateur,montant_enchere "
//			+ "	FROM ARTICLES_VENDUS a"
//			+ "	INNER JOIN UTILISATEURS u ON a.no_utilisateur=u.no_utilisateur"
//			+ "	LEFT JOIN ENCHERES e ON a.no_article=e.no_article"
//			+ "	WHERE etat_vente LIKE ?";
//	final String SELECT_ITEMS_BY_STATE_BY_TITLE = SELECT_ITEMS_BY_STATE+" AND nom_article LIKE ?";
//	final String SELECT_ITEMS_BY_STATE_BY_CATEGORY = SELECT_ITEMS_BY_STATE+" AND no_categorie=?";
//	final String SELECT_ITEMS_BY_STATE_BY_TITLE_BY_CATEGORY = SELECT_ITEMS_BY_STATE_BY_TITLE+" AND no_categorie=?";
	final String SELECT_BY_ID = "SELECT TOP (1) a.no_article, nom_article,description,libelle,date_debut_encheres,date_fin_encheres,prix_initial,a.etat_vente,r.rue,r.code_postal,r.ville,a.no_utilisateur as vendeur, "
			+ "u.pseudo,e.no_utilisateur as acquereur,u2.pseudo,MAX(montant_enchere) as enchere_max "
			+ "            FROM ARTICLES_VENDUS a "
			+ "            INNER JOIN UTILISATEURS u ON a.no_utilisateur=u.no_utilisateur "
			+ "            INNER JOIN CATEGORIES c on c.no_categorie=a.no_categorie "
			+ "            LEFT JOIN ENCHERES e ON a.no_article=e.no_article "
			+ "            LEFT JOIN UTILISATEURS u2 on u2.no_utilisateur=e.no_utilisateur "
			+ "            LEFT JOIN RETRAITS r on r.no_article = a.no_article "
			+ "            WHERE a.no_article=? "
			+ "            GROUP BY a.no_article, nom_article,description,libelle,date_debut_encheres,date_fin_encheres,prix_initial,a.etat_vente,r.rue,r.code_postal,r.ville,a.no_utilisateur,\r\n"
			+ "                u.pseudo,e.no_utilisateur,u2.pseudo,montant_enchere "
			+ "            ORDER BY  montant_enchere DESC";
	final String INSERT_ITEM = "INSERT INTO ARTICLES_VENDUS (nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,no_utilisateur,no_categorie,etat_vente) "
			+ "VALUES (?,?,?,?,?,?,?,?)";
	
	
	
	// PARTS
	final String PART_TITLE = " AND nom_article LIKE ?";
	final String PART_CATEGORY = " AND no_categorie=?";
	final String PART_AUCTION_USER = " AND e.no_utilisateur=?";
	
	// BASES
	final String SELECT_ALL_CURRENT_AUCTIONS = "SELECT a.no_article, nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,a.no_utilisateur as vendeur,pseudo,e.no_utilisateur,montant_enchere"
			+ "	FROM ARTICLES_VENDUS a"
			+ "	INNER JOIN UTILISATEURS u ON a.no_utilisateur=u.no_utilisateur"
			+ "	LEFT JOIN ENCHERES e ON a.no_article=e.no_article"
			+ "	WHERE etat_vente LIKE 'E'";
	final String SELECT_FINISHED_AUCTIONS = "SELECT a.no_article, nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,a.no_utilisateur as vendeur,pseudo,e.no_utilisateur,montant_enchere"
			+ "	FROM ARTICLES_VENDUS a"
			+ "	INNER JOIN UTILISATEURS u ON a.no_utilisateur=u.no_utilisateur"
			+ "	LEFT JOIN ENCHERES e ON a.no_article=e.no_article"
			+ "	WHERE etat_vente IN ('A','T') and montant_enchere=prix_vente";
	
	// FINALES
	final String SELECT_CURRENT_AUCTIONS_BY_CAT = SELECT_ALL_CURRENT_AUCTIONS+PART_CATEGORY;
	final String SELECT_CURRENT_AUCTIONS_BY_TITLE = SELECT_ALL_CURRENT_AUCTIONS+PART_TITLE;
	final String SELECT_CURRENT_AUCTIONS_BY_TITLE_CAT = SELECT_ALL_CURRENT_AUCTIONS+PART_TITLE+PART_CATEGORY;
	final String SELECT_MY_CURRENT_AUCTIONS = SELECT_ALL_CURRENT_AUCTIONS+PART_AUCTION_USER;
	final String SELECT_MY_CURRENT_AUCTIONS_BY_TITLE_CAT = SELECT_CURRENT_AUCTIONS_BY_TITLE_CAT+PART_AUCTION_USER;
	final String SELECT_MY_CURRENT_AUCTIONS_BY_TITLE = SELECT_CURRENT_AUCTIONS_BY_TITLE+PART_AUCTION_USER;
	final String SELECT_MY_CURRENT_AUCTIONS_BY_CAT = SELECT_CURRENT_AUCTIONS_BY_CAT+PART_AUCTION_USER;
	
	final String SELECT_MY_WON_AUCTIONS = SELECT_FINISHED_AUCTIONS+PART_AUCTION_USER;
	final String SELECT_MY_WON_AUCTIONS_BY_CAT = SELECT_FINISHED_AUCTIONS+PART_CATEGORY+PART_AUCTION_USER;
	final String SELECT_MY_WON_AUCTIONS_BY_TITLE = SELECT_FINISHED_AUCTIONS+PART_TITLE+PART_AUCTION_USER;
	final String SELECT_MY_WON_AUCTIONS_BY_TITLE_CAT = SELECT_FINISHED_AUCTIONS+PART_TITLE+PART_CATEGORY+PART_AUCTION_USER;
	
	

	
	
	
	
	
	
	
	
	@Override
	public ItemAllInformation selectById(Integer id) {
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_BY_ID);
			pStmt.setInt(1,id);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				return mapItemAllInfo5(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Item insertItem(Item item) {
			try(Connection connection = ConnectionProvider.getConnection()){
				System.out.println("ItemDaoImpl"+item);
				PreparedStatement pStmt = connection.prepareStatement(INSERT_ITEM,PreparedStatement.RETURN_GENERATED_KEYS);
				pStmt.setString(1,item.getItemTitle());
				pStmt.setString(2,item.getDescription());
				pStmt.setTimestamp(3,(Timestamp.valueOf(item.getStartDate())));
				pStmt.setTimestamp(4,(Timestamp.valueOf(item.getEndDate())));
				pStmt.setInt(5,item.getInitialPrice());
				pStmt.setInt(6,item.getUser().getNoUser());
				pStmt.setInt(7,item.getCategory().getNoCategory());
				pStmt.setString(8, item.getState());
				pStmt.executeUpdate();
				ResultSet rs = pStmt.getGeneratedKeys();
				if(rs.next()) {
					item.setNoItem(rs.getInt(1));
				}
				return item;
			}catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
	
	private ItemAllInformation mapItemAllInfo5(ResultSet rs) throws SQLException {
		Item item = new Item(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getTimestamp(5).toLocalDateTime(), rs.getTimestamp(6).toLocalDateTime(), rs.getInt(7),rs.getString(8));
		ItemAllInformation itemAllInfo = new ItemAllInformation(
				item,
				new User(rs.getInt(12),rs.getString(13)),
				rs.getInt(16)==0?new Auction():new Auction(new User(rs.getInt(14),rs.getString(15)), item, rs.getInt(16)),
				new Category(rs.getString(4)),
				new CollectionPoint(item, rs.getString(9), rs.getString(10), rs.getString(11))
				);
		System.out.println(itemAllInfo);
		return itemAllInfo;
	}
	
	private ItemAllInformation mapItemAllInfo3(ResultSet rs) throws SQLException {
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

	
	
	// 			LES BONNES REQUÊTES !!!!
	
	
	/**
	 * Returns ItemAllInformation3 needed for the search results. It's created with 3 Objects (Item,User,Auction)
	 */
	@Override
	public List<ItemAllInformation> selectAllCurrentAuctions() {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			Statement stmt = cnx.createStatement();
			ResultSet rs = stmt.executeQuery(SELECT_ALL_CURRENT_AUCTIONS);
			while (rs.next()) {
				auctionsList.add(mapItemAllInfo3(rs));
			}
			return auctionsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectCurrentAuctionsByCategory(Integer idCategory) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try  (Connection cnx = ConnectionProvider.getConnection()){	
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_CURRENT_AUCTIONS_BY_CAT);
			pStmt.setInt(1, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				auctionsList.add(mapItemAllInfo3(rs));
			}
			return auctionsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectCurrentAuctionsByTitleCat(String itemTitle, Integer idCategory) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_CURRENT_AUCTIONS_BY_TITLE_CAT);
			pStmt.setString(1, "%"+itemTitle+"%");
			pStmt.setInt(2, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				auctionsList.add(mapItemAllInfo3(rs));
			}
			return auctionsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectCurrentAuctionsByTitle(String itemTitle) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_CURRENT_AUCTIONS_BY_TITLE);
			pStmt.setString(1, "%"+itemTitle+"%");
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				auctionsList.add(mapItemAllInfo3(rs));
			}
			return auctionsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectMyCurrentAuctions(Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_CURRENT_AUCTIONS);
			pStmt.setInt(1, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				auctionsList.add(mapItemAllInfo3(rs));
			}
			return auctionsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectMyCurrentAuctionsByTitleCat(String itemTitle, Integer idCategory,Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_CURRENT_AUCTIONS_BY_TITLE_CAT);
			pStmt.setString(1, "%"+itemTitle+"%");
			pStmt.setInt(2, idCategory);
			pStmt.setInt(3, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				auctionsList.add(mapItemAllInfo3(rs));
			}
			return auctionsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectMyCurrentAuctionsByTitle(String itemTitle, Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_CURRENT_AUCTIONS_BY_TITLE);
			pStmt.setString(1, "%"+itemTitle+"%");
			pStmt.setInt(2, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				auctionsList.add(mapItemAllInfo3(rs));
			}
			return auctionsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectMyCurrentAuctionsByCategory(Integer idCategory, Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_CURRENT_AUCTIONS_BY_CAT);
			pStmt.setInt(1,idCategory);
			pStmt.setInt(2, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				auctionsList.add(mapItemAllInfo3(rs));
			}
			return auctionsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectMyWonAuctions(Integer idUser) {
		System.out.println("IMPL AVANT TRYCATCH");
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_WON_AUCTIONS);
			pStmt.setInt(1, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				auctionsList.add(mapItemAllInfo3(rs));
			}
			return auctionsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectMyWonAuctionsByCategory(Integer idCategory, Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_WON_AUCTIONS_BY_CAT);
			pStmt.setInt(1, idCategory);
			pStmt.setInt(2, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				auctionsList.add(mapItemAllInfo3(rs));
			}
			return auctionsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectMyWonAuctionsByTitleCat(String itemTitle, Integer idCategory,Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_WON_AUCTIONS_BY_TITLE_CAT);
			pStmt.setString(1, "%"+itemTitle+"%");
			pStmt.setInt(2, idCategory);
			pStmt.setInt(3, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				auctionsList.add(mapItemAllInfo3(rs));
			}
			return auctionsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectMyWonAuctionsByTitle(String itemTitle, Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_WON_AUCTIONS_BY_TITLE);
			pStmt.setString(1, "%"+itemTitle+"%");
			pStmt.setInt(2, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				auctionsList.add(mapItemAllInfo3(rs));
			}
			return auctionsList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
