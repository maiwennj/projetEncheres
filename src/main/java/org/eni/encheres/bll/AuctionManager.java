package org.eni.encheres.bll;

import org.eni.encheres.bo.Auction;
import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.User;
import org.eni.encheres.dal.DaoFactory;

import lombok.Getter;

public class AuctionManager {

	@Getter public static AuctionManager instance = new AuctionManager();
	private AuctionManager() {}
	
	public void addAuction(Auction auction) {
		DaoFactory.getAuctionDao().insertAuction(auction);
	}

	public void placeABid(Integer offer, User bidder, Item itemBidded) {
		DaoFactory.getAuctionDao().placeABid(offer,bidder,itemBidded);
		
	}

//	public Auction selectOneAuctionByNoItem(Integer id) {
//		return DaoFactory.getAuctionDao().selectOneAuctionByNoItem(id);
//	}
		
	
}