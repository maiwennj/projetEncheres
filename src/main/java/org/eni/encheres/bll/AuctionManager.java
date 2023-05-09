package org.eni.encheres.bll;

import org.eni.encheres.bll.exception.BLLException;
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

	public void placeABid(Integer offer, User bidder, Item itemBidded) throws BLLException {
		BLLException bll = new BLLException();
		Auction lastAuction= DaoFactory.getAuctionDao().selectAuctionById(itemBidded.getNoItem());
		System.out.println(lastAuction);
		//checks
		if (offer<=lastAuction.getBid()  ) {
			bll.addError("Vous ne pouvez pas faire une offre inférieure à la dernière enchère");
		}
		if(offer>bidder.getCredit()) {
			bll.addError("Vous n'avez pas assez de crédit pour faire cette enchère");
		}
		if (bll.getErreurs().size()>0) {
			throw bll;
		}
		DaoFactory.getAuctionDao().placeABid(offer,bidder,itemBidded,lastAuction);
	}	
}