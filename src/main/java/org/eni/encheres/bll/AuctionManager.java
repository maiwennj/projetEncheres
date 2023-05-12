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

	/**
	 * Récupère la dernière enchère, fait les checks avec checkOffer() et lance le traitement de l'enchère avec placeABid() si pas de problème
	 * @param offer
	 * @param bidder
	 * @param itemBidded
	 * @throws BLLException
	 */
	public void placeABid(Integer offer, User bidder, Item itemBidded) throws BLLException {
		BLLException bll = new BLLException();
		Auction lastAuction= DaoFactory.getItemDao().selectById(itemBidded.getNoItem()).getAuction();
			//System.out.println("Auction manager - last bid: " + lastAuction);
			//System.out.println("Auction manager - initialPrice: " + itemBidded.getInitialPrice());
		checkOffer(offer,bidder,itemBidded,lastAuction,bll);
		if (bll.getErreurs().size()>0) {
			throw bll;
		}
		DaoFactory.getAuctionDao().placeABid(offer,bidder,itemBidded,lastAuction);
	}

	/**
	 * Checks l'offre: (ajoute des erreurs à bll ou lance bll suivant l'importance de l'erreur)<br/>
	 * 1. Si l'offre est null: lance une erreur. <br/>
	 * 2. Vérifi si il existe déjà une echère et traite en fonction. <br/>
	 * @param offer
	 * @param bidder
	 * @param itemBidded
	 * @param lastAuction
	 * @param bll
	 * @throws BLLException
	 */
	private void checkOffer(Integer offer, User bidder, Item itemBidded, Auction lastAuction, BLLException bll) throws BLLException {
		//si l'offre est null on retourne une erreur
		if (offer == null) {
			bll.addError("Vous n'avez pas fait d'offre.");
			throw bll;
		}
		
		//si il n'y a pas eu encore d'enchère
		if(lastAuction.getBid() == null) {
			//si l'offre est inférieure au crédit de l'utilisateur on ajoute une erreur
			if(offer > bidder.getCredit()) {
				bll.addError("Vous n'avez pas assez de crédits pour faire cette enchère.");
			}
			//si l'offre est inférieure au prix initial on ajoute une erreur
			if(offer < itemBidded.getInitialPrice()) {
				bll.addError("Vous ne pouvez pas faire une offre inférieure au prix initial.");
			}
		//sinon s'il existe déjà une enchère
		}else{
			//si la dernière enchère est de l'utilisateur retourne une erreur
			if(lastAuction.getUser().getNoUser() == bidder.getNoUser() ) {
				bll.addError("Vous avez déjà fait l'enchère la plus élevée sur cet article.");
				throw bll;
			}else {
				//si l'offre est inférieure à la dernière offre on ajoute une erreur
				if (offer <= lastAuction.getBid()  ) {
					bll.addError("Vous devez faire une offre supérieure à la dernière enchère.");
				}
				//si l'offer est inférieure au crédit de l'utilisateur on ajoute une erreur
				if(offer > bidder.getCredit()) {
					bll.addError("Vous n'avez pas assez de crédits pour faire cette enchère.");
				}
			}
		}	
	}	
}
