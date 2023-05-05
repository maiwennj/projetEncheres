package org.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.eni.encheres.bo.Auction;
import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.User;
import org.eni.encheres.config.ConnectionProvider;
import org.eni.encheres.dal.AuctionDao;

public class AuctionDaoImpl implements AuctionDao {

	final String INSERT_AUCTION = "INSERT INTO ENCHERES (no_utilisateur,no_article,date_enchere,montant_enchere) VALUES (?,?,?,?);";
	final String SELECT_BY_NO_ITEM = "SELECT * FROM ENCHERES WHERE no_article=? AND montant_enchere =(SELECT MAX(montant_enchere)FROM ENCHERES);";
	
	@Override
	public void insertAuction(Auction auction) {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement pStmt = cnx.prepareStatement(INSERT_AUCTION);
			pStmt.setInt(1, 3);
			pStmt.setInt(2, 6);
			pStmt.setTimestamp(3,Timestamp.valueOf(auction.getAuctionDate()));
			pStmt.setInt(4, auction.getBid());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

//	@Override
//	public Auction selectOneAuctionByNoItem(Integer id) {
//		try (Connection cnx = ConnectionProvider.getConnection()) {
//			PreparedStatement pStmt = cnx.prepareStatement(SELECT_BY_NO_ITEM);
//			pStmt.setInt(1, id);
//			ResultSet rs = pStmt.executeQuery();
//			if (rs.next()) {
//				return new Auction(
//						new User(rs.getInt(1)),
//						new Item(rs.getInt(2)),
//						rs.getTimestamp(3).toLocalDateTime(),
//						rs.getInt(4));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

}
