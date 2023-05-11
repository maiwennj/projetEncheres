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
	
	//OK POUR MERGE
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

	// ***********************************  AUCTIONS ************************************
	// PARTS
	final String PART_TITLE = " AND nom_article LIKE ?";
	final String PART_CATEGORY = " AND no_categorie=?";
	final String PART_AUCTION_USER = " AND e.no_utilisateur=?";
	
	// BASES
	final String SELECT_ALL_CURRENT_AUCTIONS = "SELECT a.no_article, nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,a.no_utilisateur as vendeur,pseudo,e.no_utilisateur as enchérisseur,montant_enchere"
			+ " FROM ARTICLES_VENDUS a"
			+ " INNER JOIN UTILISATEURS u ON a.no_utilisateur=u.no_utilisateur"
			+ " LEFT JOIN ENCHERES e ON a.no_article=e.no_article"
			+ " WHERE etat_vente LIKE 'E' AND (montant_enchere=(SELECT MAX(montant_enchere) FROM ENCHERES WHERE no_article=e.no_article) OR montant_enchere IS null)";
	final String SELECT_FINISHED_AUCTIONS = "SELECT a.no_article, nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,a.no_utilisateur as vendeur,pseudo,e.no_utilisateur,montant_enchere"
			+ "	FROM ARTICLES_VENDUS a"
			+ "	INNER JOIN UTILISATEURS u ON a.no_utilisateur=u.no_utilisateur"
			+ "	LEFT JOIN ENCHERES e ON a.no_article=e.no_article"
			+ "	WHERE etat_vente IN ('A','T') AND (montant_enchere=(SELECT MAX(montant_enchere) FROM ENCHERES WHERE no_article=e.no_article) OR montant_enchere IS null)";
	
	// FINALES 
	// enchères en cours
	final String SELECT_CURRENT_AUCTIONS_BY_CAT = SELECT_ALL_CURRENT_AUCTIONS+PART_CATEGORY;
	final String SELECT_CURRENT_AUCTIONS_BY_TITLE = SELECT_ALL_CURRENT_AUCTIONS+PART_TITLE;
	final String SELECT_CURRENT_AUCTIONS_BY_TITLE_CAT = SELECT_ALL_CURRENT_AUCTIONS+PART_TITLE+PART_CATEGORY;
	// mes enchères en cours
	final String SELECT_MY_CURRENT_AUCTIONS = "SELECT e.no_article,nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,a.no_utilisateur as vendeur_id,pseudo as vendeur_pseudo,e.no_utilisateur as ench_id, montant_enchere"
			+ " FROM ENCHERES as e "
			+ " INNER JOIN(SELECT no_article, MAX(montant_enchere) as max_bid FROM ENCHERES group by no_article) as mb on e.no_article = mb.no_article and e.montant_enchere = mb.max_bid"
			+ " INNER JOIN(SELECT no_article,date_enchere FROM ENCHERES WHERE no_utilisateur = ?) as e_user ON e.no_article = e_user.no_article"
			+ " left join ARTICLES_VENDUS a on a.no_article=e.no_article"
			+ " left join UTILISATEURS u on u.no_utilisateur=a.no_utilisateur"
			+ " where etat_vente LIKE 'E'";
	final String SELECT_MY_CURRENT_AUCTIONS_BY_TITLE_CAT = SELECT_MY_CURRENT_AUCTIONS+PART_TITLE+PART_CATEGORY;
	final String SELECT_MY_CURRENT_AUCTIONS_BY_TITLE = SELECT_MY_CURRENT_AUCTIONS+PART_TITLE;
	final String SELECT_MY_CURRENT_AUCTIONS_BY_CAT = SELECT_MY_CURRENT_AUCTIONS+PART_CATEGORY;
	// mes enchères remportées
	final String SELECT_MY_WON_AUCTIONS = SELECT_FINISHED_AUCTIONS+PART_AUCTION_USER;
	final String SELECT_MY_WON_AUCTIONS_BY_CAT = SELECT_FINISHED_AUCTIONS+PART_CATEGORY+PART_AUCTION_USER;
	final String SELECT_MY_WON_AUCTIONS_BY_TITLE = SELECT_FINISHED_AUCTIONS+PART_TITLE+PART_AUCTION_USER;
	final String SELECT_MY_WON_AUCTIONS_BY_TITLE_CAT = SELECT_FINISHED_AUCTIONS+PART_TITLE+PART_CATEGORY+PART_AUCTION_USER;
	// mes enchères remportées + mes enchères en cours
	final String SELECT_MY_WON_AUCTIONS_MY_BIDS = SELECT_MY_CURRENT_AUCTIONS+" UNION "+SELECT_MY_WON_AUCTIONS;
	final String SELECT_MY_WON_AUCTIONS_MY_BIDS_BY_TITLE = SELECT_MY_CURRENT_AUCTIONS+PART_TITLE+" UNION "+SELECT_MY_WON_AUCTIONS+PART_TITLE;
	final String SELECT_MY_WON_AUCTIONS_MY_BIDS_BY_CAT = SELECT_MY_CURRENT_AUCTIONS+PART_CATEGORY+" UNION "+SELECT_MY_WON_AUCTIONS+PART_CATEGORY;
	final String SELECT_MY_WON_AUCTIONS_MY_BIDS_BY_TITLE_CAT = SELECT_MY_CURRENT_AUCTIONS+PART_TITLE+PART_CATEGORY+" UNION "+SELECT_MY_WON_AUCTIONS+PART_TITLE+PART_CATEGORY;
	// mes enchères remportées + enchères en cours
	final String SELECT_MY_WON_CURRENT_AUCTIONS = SELECT_MY_WON_AUCTIONS+" UNION "+SELECT_ALL_CURRENT_AUCTIONS;
	final String SELECT_MY_WON_CURRENT_AUCTIONS_TITLE = SELECT_MY_WON_AUCTIONS+PART_TITLE+" UNION "+SELECT_ALL_CURRENT_AUCTIONS+PART_TITLE;
	final String SELECT_MY_WON_CURRENT_AUCTIONS_CAT = SELECT_MY_WON_AUCTIONS+PART_CATEGORY+" UNION "+SELECT_ALL_CURRENT_AUCTIONS+PART_CATEGORY;
	final String SELECT_MY_WON_CURRENT_AUCTIONS_TITLE_CAT = SELECT_MY_WON_AUCTIONS+PART_TITLE+PART_CATEGORY+" UNION "+SELECT_ALL_CURRENT_AUCTIONS+PART_TITLE+PART_CATEGORY;
	// enchères en cours + mes enchères en cours + mes enchères remportées
	final String SELECT_ALL_CHECKED_AUCTIONS = SELECT_ALL_CURRENT_AUCTIONS+" UNION "+SELECT_MY_WON_AUCTIONS;
	final String SELECT_ALL_CHECKED_AUCTIONS_TITLE = SELECT_ALL_CURRENT_AUCTIONS+PART_TITLE+" UNION "+SELECT_MY_WON_AUCTIONS+PART_TITLE;
	final String SELECT_ALL_CHECKED_AUCTIONS_CAT = SELECT_ALL_CURRENT_AUCTIONS+PART_CATEGORY+" UNION "+SELECT_MY_WON_AUCTIONS+PART_CATEGORY;
	final String SELECT_ALL_CHECKED_AUCTIONS_TITLE_CAT = SELECT_ALL_CURRENT_AUCTIONS+PART_TITLE+PART_CATEGORY+" UNION "+SELECT_MY_WON_AUCTIONS+PART_TITLE+PART_CATEGORY;


	// ***********************************  SALES ************************************		
	// BASES
	final String SELECT_CURRENT_SALES = "SELECT a.no_article, nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,a.no_utilisateur as vendeur,pseudo,e.no_utilisateur as enchérisseur,montant_enchere"
			+ " FROM ARTICLES_VENDUS a"
			+ " INNER JOIN UTILISATEURS u ON a.no_utilisateur=u.no_utilisateur"
			+ " LEFT JOIN ENCHERES e ON a.no_article=e.no_article"
			+ " WHERE etat_vente LIKE 'E' AND (montant_enchere=(SELECT MAX(montant_enchere) FROM ENCHERES WHERE no_article=e.no_article) OR montant_enchere IS null) AND a.no_utilisateur=?";
	final String SELECT_NEW_SALES = "SELECT a.no_article, nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,a.no_utilisateur as vendeur,pseudo,e.no_utilisateur as enchérisseur,montant_enchere"
			+ " FROM ARTICLES_VENDUS a"
			+ " INNER JOIN UTILISATEURS u ON a.no_utilisateur=u.no_utilisateur"
			+ " LEFT JOIN ENCHERES e ON a.no_article=e.no_article"
			+ " WHERE etat_vente LIKE 'N' AND (montant_enchere=(SELECT MAX(montant_enchere) FROM ENCHERES WHERE no_article=e.no_article) OR montant_enchere IS null) AND a.no_utilisateur=?";
	final String SELECT_FINISHED_SALES = "SELECT a.no_article, nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,a.no_utilisateur as vendeur,pseudo,e.no_utilisateur,montant_enchere"
			+ "	FROM ARTICLES_VENDUS a"
			+ "	INNER JOIN UTILISATEURS u ON a.no_utilisateur=u.no_utilisateur"
			+ "	LEFT JOIN ENCHERES e ON a.no_article=e.no_article"
			+ "	WHERE etat_vente IN ('A','T') AND (montant_enchere=(SELECT MAX(montant_enchere) FROM ENCHERES WHERE no_article=e.no_article) OR montant_enchere IS null) AND a.no_utilisateur=?";
	final String SELECT_ALL_CHECKED_SALES = "SELECT a.no_article, nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,a.no_utilisateur as vendeur,pseudo,e.no_utilisateur as enchérisseur,montant_enchere"
			+ " FROM ARTICLES_VENDUS a"
			+ " INNER JOIN UTILISATEURS u ON a.no_utilisateur=u.no_utilisateur"
			+ " LEFT JOIN ENCHERES e ON a.no_article=e.no_article"
			+ " WHERE (montant_enchere=(SELECT MAX(montant_enchere) FROM ENCHERES WHERE no_article=e.no_article) OR montant_enchere IS null) and a.no_utilisateur=?";
	
	// FINALES
	// mes ventes en cours
	final String SELECT_CURRENT_SALES_BY_TITLE = SELECT_CURRENT_SALES+PART_TITLE;
	final String SELECT_CURRENT_SALES_BY_CAT = SELECT_CURRENT_SALES+PART_CATEGORY;
	final String SELECT_CURRENT_SALES_BY_TITLE_CAT = SELECT_CURRENT_SALES+PART_TITLE+PART_CATEGORY;
	// mes nouvelles ventes
	final String SELECT_NEW_SALES_BY_TITLE = SELECT_NEW_SALES+PART_TITLE;
	final String SELECT_NEW_SALES_BY_CAT = SELECT_NEW_SALES+PART_CATEGORY;
	final String SELECT_NEW_SALES_BY_TITLE_CAT = SELECT_NEW_SALES+PART_TITLE+PART_CATEGORY;
	// mes ventes terminées
	final String SELECT_FINISHED_SALES_BY_TITLE = SELECT_FINISHED_SALES+PART_TITLE;
	final String SELECT_FINISHED_SALES_BY_CAT = SELECT_FINISHED_SALES+PART_CATEGORY;
	final String SELECT_FINISHED_SALES_BY_TITLE_CAT = SELECT_FINISHED_SALES+PART_TITLE+PART_CATEGORY;
	
	// mes ventes en cours + mes ventes non débutées
	final String SELECT_NEW_CURRENT_SALES = SELECT_CURRENT_SALES+" UNION "+SELECT_NEW_SALES;
	final String SELECT_NEW_CURRENT_SALES_BY_TITLE = SELECT_CURRENT_SALES+PART_TITLE+" UNION "+SELECT_NEW_SALES+PART_TITLE;
	final String SELECT_NEW_CURRENT_SALES_BY_CAT = SELECT_CURRENT_SALES+PART_CATEGORY+" UNION "+SELECT_NEW_SALES+PART_CATEGORY;
	final String SELECT_NEW_CURRENT_SALES_BY_TITLE_CAT = SELECT_CURRENT_SALES+PART_TITLE+PART_CATEGORY+" UNION "+SELECT_NEW_SALES+PART_TITLE+PART_CATEGORY;
	
	// mes ventes en cours + mes ventes terminées
	final String SELECT_CURRENT_FINISHED_SALES = SELECT_CURRENT_SALES+" UNION "+SELECT_FINISHED_SALES;
	final String SELECT_CURRENT_FINISHED_SALES_BY_TITLE = SELECT_CURRENT_SALES+PART_TITLE+" UNION "+SELECT_FINISHED_SALES+PART_TITLE;
	final String SELECT_CURRENT_FINISHED_SALES_BY_CAT = SELECT_CURRENT_SALES+PART_CATEGORY+" UNION "+SELECT_FINISHED_SALES+PART_CATEGORY;
	final String SELECT_CURRENT_FINISHED_SALES_BY_TITLE_CAT = SELECT_CURRENT_SALES+PART_TITLE+PART_CATEGORY+" UNION "+SELECT_FINISHED_SALES+PART_TITLE+PART_CATEGORY;
	
	// mes ventes nouvelles + ventes terminées
	final String SELECT_NEW_FINISHED_SALES = SELECT_NEW_SALES+" UNION "+SELECT_FINISHED_SALES;
	final String SELECT_NEW_FINISHED_SALES_BY_TITLE = SELECT_NEW_SALES+PART_TITLE+" UNION "+SELECT_FINISHED_SALES+PART_TITLE;
	final String SELECT_NEW_FINISHED_SALES_BY_CAT = SELECT_NEW_SALES+PART_CATEGORY+" UNION "+SELECT_FINISHED_SALES+PART_CATEGORY;
	final String SELECT_NEW_FINISHED_SALES_BY_TITLE_CAT = SELECT_NEW_SALES+PART_TITLE+PART_CATEGORY+" UNION "+SELECT_FINISHED_SALES+PART_TITLE+PART_CATEGORY;
	
	// all sales : everything is checked
	final String SELECT_ALL_CHECKED_SALES_BY_TITLE = SELECT_ALL_CHECKED_SALES+PART_TITLE;
	final String SELECT_ALL_CHECKED_SALES_BY_CAT = SELECT_ALL_CHECKED_SALES+PART_CATEGORY;
	final String SELECT_ALL_CHECKED_SALES_BY_TITLE_CAT = SELECT_ALL_CHECKED_SALES+PART_TITLE+PART_CATEGORY;
	
	final String SELECT_AUCTIONS_USER_TO_BE_DELETED = "SELECT a.no_article, nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,a.no_utilisateur as vendeur,pseudo,e.no_utilisateur as enchérisseur,montant_enchere\"\r\n"
			+ " FROM ARTICLES_VENDUS a"
			+ " INNER JOIN UTILISATEURS u ON a.no_utilisateur=u.no_utilisateur"
			+ " LEFT JOIN ENCHERES e ON a.no_article=e.no_article"
			+ "	WHERE etat_vente LIKE 'E' AND (montant_enchere=(SELECT MAX(montant_enchere) FROM ENCHERES WHERE no_article=e.no_article) OR montant_enchere IS null) AND e.no_utilisateur=?;";
	
	final String SELECT_AUCTIONS_BY_USER_TO_DELETE = "SELECT a.no_article, nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,a.no_utilisateur as vendeur,pseudo,e.no_utilisateur as enchérisseur,montant_enchere"
			+ " FROM ARTICLES_VENDUS a"
			+ " INNER JOIN UTILISATEURS u ON a.no_utilisateur=u.no_utilisateur"
			+ " LEFT JOIN ENCHERES e ON a.no_article=e.no_article"
			+ " WHERE etat_vente IN ('E','T') AND e.no_utilisateur=?";
	final String SELECT_SALES_BY_USER_TO_DELETE = "SELECT a.no_article, nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,a.no_utilisateur as vendeur,pseudo,e.no_utilisateur as enchérisseur,montant_enchere"
			+ " FROM ARTICLES_VENDUS a INNER JOIN UTILISATEURS u ON a.no_utilisateur=u.no_utilisateur"
			+ " LEFT JOIN ENCHERES e ON a.no_article=e.no_article"
			+ " WHERE etat_vente in ('E','T') AND a.no_utilisateur=?";
	
	
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
				//System.out.println("ItemDaoImpl"+item);
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
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, idCategory);
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
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
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
			pStmt.setInt(1, idUser);
			pStmt.setInt(2,idCategory);
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

	@Override
	public List<ItemAllInformation> selectMyWonAuctionsMyBidsByTitleCat(String itemTitle, Integer idCategory,Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_WON_AUCTIONS_MY_BIDS_BY_TITLE_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, idCategory);
			pStmt.setInt(4, idUser);
			pStmt.setString(5, "%"+itemTitle+"%");
			pStmt.setInt(6, idCategory);
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
	public List<ItemAllInformation> selectMyWonAuctionsMyBidsByTitle(String itemTitle, Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_WON_AUCTIONS_MY_BIDS_BY_TITLE);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, idUser);
			pStmt.setString(4, "%"+itemTitle+"%");
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
	public List<ItemAllInformation> selectMyWonAuctionsMyBidsByCategory(Integer idCategory, Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_WON_AUCTIONS_MY_BIDS_BY_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setInt(2, idCategory);
			pStmt.setInt(3, idUser);
			pStmt.setInt(4, idCategory);
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
	public List<ItemAllInformation> selectMyWonAuctionsMyBids(Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_WON_AUCTIONS_MY_BIDS);
			pStmt.setInt(1, idUser);
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
	public List<ItemAllInformation> selectMyWonAuctionsCurrentAuctionsByTitleCat(Integer idUser, String itemTitle,Integer idCategory) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_WON_CURRENT_AUCTIONS_TITLE_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, idCategory);
			pStmt.setString(4, "%"+itemTitle+"%");
			pStmt.setInt(5, idCategory);
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
	public List<ItemAllInformation> selectMyWonAuctionsCurrentAuctionsByCategory(Integer idUser, Integer idCategory) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_WON_CURRENT_AUCTIONS_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setInt(2, idCategory);
			pStmt.setInt(3, idCategory);
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
	public List<ItemAllInformation> selectMyWonAuctionsCurrentAuctionsByTitle(Integer idUser, String itemTitle) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_WON_CURRENT_AUCTIONS_TITLE);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setString(3, "%"+itemTitle+"%");
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
	public List<ItemAllInformation> selectMyWonAuctionsCurrentAuctions(Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_MY_WON_CURRENT_AUCTIONS);
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
	public List<ItemAllInformation> selectAllCheckedAuctionsByCategory(Integer idCategory, Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ALL_CHECKED_AUCTIONS_CAT);
			pStmt.setInt(1, idCategory);
			pStmt.setInt(2, idUser);
			pStmt.setInt(3, idCategory);
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
	public List<ItemAllInformation> selectAllCheckedAuctions(Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ALL_CHECKED_AUCTIONS);
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
	public List<ItemAllInformation> selectAllCheckedAuctionsByTitle(String itemTitle, Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ALL_CHECKED_AUCTIONS_TITLE);
			pStmt.setString(1,"%"+itemTitle+"%");
			pStmt.setInt(2, idUser);
			pStmt.setString(3,"%"+itemTitle+"%");
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
	public List<ItemAllInformation> selectAllCheckedAuctionsByTitleCat(String itemTitle, Integer idCategory,Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ALL_CHECKED_AUCTIONS_TITLE_CAT);
			pStmt.setString(1,"%"+itemTitle+"%");
			pStmt.setInt(2, idCategory);
			pStmt.setInt(3, idUser);
			pStmt.setString(4,"%"+itemTitle+"%");
			pStmt.setInt(5, idCategory);
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
	public List<ItemAllInformation> selectAllCurrentSales(Integer idUser) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_CURRENT_SALES);
			pStmt.setInt(1, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectAllCurrentSalesByCat(Integer idUser, Integer idCategory) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_CURRENT_SALES_BY_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setInt(2, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectAllCurrentSalesByTitle(Integer idUser, String itemTitle) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_CURRENT_SALES_BY_TITLE);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectAllCurrentSalesByTitleCat(Integer idUser, String itemTitle,Integer idCategory) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_CURRENT_SALES_BY_TITLE_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectNewSales(Integer idUser) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_NEW_SALES);
			pStmt.setInt(1, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectNewSalesByCat(Integer idUser, Integer idCategory) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_NEW_SALES_BY_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setInt(2, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectNewSalesByTitle(Integer idUser, String itemTitle) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_NEW_SALES_BY_TITLE);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectNewSalesByTitleCat(Integer idUser, String itemTitle, Integer idCategory) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_NEW_SALES_BY_TITLE_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectFinishedSales(Integer idUser) {  
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_FINISHED_SALES);
			pStmt.setInt(1, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectFinishedSalesByCat(Integer idUser, Integer idCategory) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_FINISHED_SALES_BY_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setInt(2, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectFinishedSalesByTitle(Integer idUser, String itemTitle) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_FINISHED_SALES_BY_TITLE);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectFinishedSalesByTitleCat(Integer idUser, String itemTitle,Integer idCategory) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_FINISHED_SALES_BY_TITLE_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectCurrentNewSales(Integer idUser) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_NEW_CURRENT_SALES);
			pStmt.setInt(1, idUser);
			pStmt.setInt(2, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectCurrentNewSalesByCat(Integer idUser, Integer idCategory) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_NEW_CURRENT_SALES_BY_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setInt(2, idCategory);
			pStmt.setInt(3, idUser);
			pStmt.setInt(4, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectCurrentNewSalesByTitle(Integer idUser, String itemTitle) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_NEW_CURRENT_SALES_BY_TITLE);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, idUser);
			pStmt.setString(4, "%"+itemTitle+"%");
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectCurrentNewSalesByTitleCat(Integer idUser, String itemTitle,Integer idCategory) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_NEW_CURRENT_SALES_BY_TITLE_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, idCategory);
			pStmt.setInt(4, idUser);
			pStmt.setString(5, "%"+itemTitle+"%");
			pStmt.setInt(6, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectCurrentFinishedSales(Integer idUser) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_CURRENT_FINISHED_SALES);
			pStmt.setInt(1, idUser);
			pStmt.setInt(2, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectCurrentFinishedSalesByCat(Integer idUser, Integer idCategory) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_CURRENT_FINISHED_SALES_BY_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setInt(2, idCategory);
			pStmt.setInt(3, idUser);
			pStmt.setInt(4, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectCurrentFinishedSalesByTitle(Integer idUser, String itemTitle) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_CURRENT_FINISHED_SALES_BY_TITLE);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, idUser);
			pStmt.setString(4, "%"+itemTitle+"%");
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectCurrentFinishedSalesByTitleCat(Integer idUser, String itemTitle,Integer idCategory) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_CURRENT_FINISHED_SALES_BY_TITLE_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, idCategory);
			pStmt.setInt(4, idUser);
			pStmt.setString(5, "%"+itemTitle+"%");
			pStmt.setInt(6, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectNewFinishedSales(Integer idUser) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_NEW_FINISHED_SALES);
			pStmt.setInt(1, idUser);
			pStmt.setInt(2, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectNewFinishedSalesSalesByCat(Integer idUser, Integer idCategory) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_NEW_FINISHED_SALES_BY_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setInt(2, idCategory);
			pStmt.setInt(3, idUser);
			pStmt.setInt(4, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectNewFinishedSalesSalesByTitle(Integer idUser, String itemTitle) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_NEW_FINISHED_SALES_BY_TITLE);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, idUser);
			pStmt.setString(4, "%"+itemTitle+"%");
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectNewFinishedSalesSalesByTitleCat(Integer idUser, String itemTitle,Integer idCategory) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_NEW_FINISHED_SALES_BY_TITLE_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, idCategory);
			pStmt.setInt(4, idUser);
			pStmt.setString(5, "%"+itemTitle+"%");
			pStmt.setInt(6, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectAllCheckedSales(Integer idUser) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ALL_CHECKED_SALES);
			pStmt.setInt(1, idUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectAllCheckedSalesByCat(Integer idUser, Integer idCategory) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ALL_CHECKED_SALES_BY_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setInt(2, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectAllCheckedSalesByTitle(Integer idUser, String itemTitle) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ALL_CHECKED_SALES_BY_TITLE);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectAllCheckedSalesByTitleCat(Integer idUser, String itemTitle,Integer idCategory) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ALL_CHECKED_SALES_BY_TITLE_CAT);
			pStmt.setInt(1, idUser);
			pStmt.setString(2, "%"+itemTitle+"%");
			pStmt.setInt(3, idCategory);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ItemAllInformation> selectAuctionsByToBoDeletedUser(Integer noUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_AUCTIONS_BY_USER_TO_DELETE);
			pStmt.setInt(1, noUser);
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
	public List<ItemAllInformation> selectSalesByToBoDeletedUser(Integer noUser) {
		List<ItemAllInformation> salesList = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_SALES_BY_USER_TO_DELETE);
			pStmt.setInt(1, noUser);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				salesList.add(mapItemAllInfo3(rs));
			}
			return salesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
