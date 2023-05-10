package org.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Timestamp;
import java.time.LocalDateTime;


import org.eni.encheres.bo.Auction;
import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.User;
import org.eni.encheres.config.ConnectionProvider;
import org.eni.encheres.dal.AuctionDao;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;

public class AuctionDaoImpl implements AuctionDao {

	final String INSERT_AUCTION = "INSERT INTO ENCHERES (no_utilisateur,no_article,date_enchere,montant_enchere) VALUES (?,?,?,?);";
	final String SELECT_BY_NO_ITEM = "SELECT * FROM ENCHERES WHERE no_article=? And montant_enchere=(SELECT MAX(montant_enchere) FROM ENCHERES WHERE  no_article=? )";
	final String UPDATE_BIDDER = "UPDATE UTILISATEURS SET credit = credit-? WHERE no_utilisateur=?";
	final String UPDATE_LAST_BIDDER = "UPDATE UTILISATEURS SET credit = credit+? WHERE no_utilisateur=?";
	
	@Override
	public void insertAuction(Auction auction) {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement pStmt = cnx.prepareStatement(INSERT_AUCTION);
			pStmt.setInt(1, auction.getUser().getNoUser());
			pStmt.setInt(2, auction.getItem().getNoItem());
			pStmt.setTimestamp(3,Timestamp.valueOf(auction.getAuctionDate()));
			pStmt.setInt(4, auction.getBid());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void placeABid(Integer offer, User bidder, Item itemBidded, Auction lastAuction) {

		try(Connection cnx = ConnectionProvider.getConnection()){
			cnx.setAutoCommit(false);
			try {
				PreparedStatement updateBidder = cnx.prepareStatement(UPDATE_BIDDER);
				cnx.setAutoCommit(false);
				updateBidder.setInt(1, offer);
//					System.out.println("offer : " + offer);
				updateBidder.setInt(2,bidder.getNoUser());
//					System.out.println(bidder.getNoUser());
				updateBidder.executeUpdate();
				
				if (lastAuction.getBid()!=null) {
					PreparedStatement updateLastBidder = cnx.prepareStatement(UPDATE_LAST_BIDDER);
//					updateLastBidder.setInt(1, selectAuctionById(itemBidded.getNoItem()).getBid());
//					updateLastBidder.setInt(2, selectAuctionById(itemBidded.getNoItem()).getUser().getNoUser());
					updateLastBidder.setInt(1, lastAuction.getBid());
					updateLastBidder.setInt(2, lastAuction.getUser().getNoUser());
//						System.out.println( "updateLastBidder nouser: "+lastAuction.getUser().getNoUser());
					updateLastBidder.executeUpdate();
				}
				
				PreparedStatement insertAuction = cnx.prepareStatement(INSERT_AUCTION, PreparedStatement.RETURN_GENERATED_KEYS);
				insertAuction.setInt(1,bidder.getNoUser());
				insertAuction.setInt(2,itemBidded.getNoItem());
				insertAuction.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
				insertAuction.setInt(4,offer);
//					System.out.println("insertAuction :" +  itemBidded.getNoItem() +" , " + Timestamp.valueOf(LocalDateTime.now()) + " , "+  offer);
				insertAuction.executeUpdate();
				cnx.commit();	
			} catch (Exception e) {
				System.err.println("rollback");
				e.printStackTrace();
				cnx.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	
	public Auction selectAuctionById(Integer id) {
		try(Connection cnx = ConnectionProvider.getConnection()){
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_BY_NO_ITEM);
			pStmt.setInt(1,id);
			pStmt.setInt(2,id);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				return new Auction( new User(rs.getInt(1)), new Item(), rs.getInt(4) ) ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}return null;
	}

}
