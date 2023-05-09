package org.eni.encheres.dal;

import org.eni.encheres.bo.Auction;
import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.User;

public interface AuctionDao {


	void insertAuction(Auction auction);

	void placeABid(Integer offer, User bidder, Item itemBidded);



//	Auction selectOneAuctionByNoItem(Integer id);

}
