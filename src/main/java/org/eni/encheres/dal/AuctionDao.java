package org.eni.encheres.dal;

import org.eni.encheres.bo.Auction;
import org.eni.encheres.bo.Item;

public interface AuctionDao {


	void insertAuction(Auction auction);

	Auction selectOneAuctionByNoItem(Integer id);

}
