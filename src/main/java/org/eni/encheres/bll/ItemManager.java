package org.eni.encheres.bll;

import java.time.LocalDateTime;
import java.util.List;

import org.eni.encheres.bll.exception.BLLException;
import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.ItemAllInformation;
import org.eni.encheres.bo.User;
import org.eni.encheres.dal.DaoFactory;

import lombok.Getter;

public class ItemManager {

	//singleton getInstance();
	@Getter public static ItemManager instance = new ItemManager(); //lazy singleton
	private ItemManager() {}
	
	//CREATE
	public Item addItem(Item item) throws BLLException {
		checkFieldsItem(item);
		return DaoFactory.getItemDao().insertItem(item);
	}
	
	// READ
	
	/**
	 * Used directly on the home page, hence the "public" visibility
	 * @return List<ItemAllInformation> 
	 */
	public List<ItemAllInformation> selectAllCurrentAuctions() {
		return DaoFactory.getItemDao().selectAllCurrentAuctions();
	}

	/**
	 * Generates search results. 
	 * this methods checks :<br/>
	 * 1 : what boxes the user checked<br/>
	 * 2 : if itemTitle and category have been filled.<br/>
	 * and finally sends the informations to the most-suited method for the execution.<br/>
	 * @param category 
	 * @param itemTitle 
	 * @param idUser 
	 * @param itemState 
	 * @return List<Item>
	 */
	public List<ItemAllInformation> searchAuctions(Boolean currentAuctions, Boolean myBids, Boolean wonAuctions,String itemTitle, Integer idCategory, Integer idUser) {	
		// ----------------------- checked : (enchères ouvertes) OU (enchères ouvertes + mes enchères) --------------------------------
		if ((currentAuctions && !myBids && !wonAuctions) || (currentAuctions && myBids && !wonAuctions) ||(!currentAuctions && !myBids && !wonAuctions)) {
			// TITLE IS EMPTY
			if (itemTitle.isBlank() || itemTitle.isEmpty()) {
				if (idCategory==0) {
					// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
					return DaoFactory.getItemDao().selectAllCurrentAuctions();
				}else {
					// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
					return DaoFactory.getItemDao().selectCurrentAuctionsByCategory(idCategory);
				}
			}else { // is itemTitle blank or empty : NO
				if (idCategory==0) {
					return DaoFactory.getItemDao().selectCurrentAuctionsByTitle(itemTitle);
				}else {
					return DaoFactory.getItemDao().selectCurrentAuctionsByTitleCat(itemTitle,idCategory);
				}
			}

		// ----------------------- checked : mes enchères en cours  --------------------------------
		}else if (!currentAuctions && myBids && !wonAuctions) {
			// TITLE IS EMPTY
				if (itemTitle.isBlank() || itemTitle.isEmpty()) {
					if (idCategory==0) {
						// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
						return DaoFactory.getItemDao().selectMyCurrentAuctions(idUser);
					}else {
						// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
						return DaoFactory.getItemDao().selectMyCurrentAuctionsByCategory(idCategory,idUser);
					}
				}else { // is itemTitle blank or empty : NO
					if (idCategory==0) {
						return DaoFactory.getItemDao().selectMyCurrentAuctionsByTitle(itemTitle,idUser);
					}else {
						return DaoFactory.getItemDao().selectMyCurrentAuctionsByTitleCat(itemTitle,idCategory,idUser);
					}
				}
		// ----------------------- checked : mes enchères remportées  --------------------------------
		}else if (!currentAuctions && !myBids && wonAuctions) {
			// TITLE IS EMPTY
			if (itemTitle.isBlank() || itemTitle.isEmpty()) {
				if (idCategory==0) {
					// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
					return DaoFactory.getItemDao().selectMyWonAuctions(idUser);
				}else {
					// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
					return DaoFactory.getItemDao().selectMyWonAuctionsByCategory(idCategory,idUser);
				}
			}else { // is itemTitle blank or empty : NO
				if (idCategory==0) {
					return DaoFactory.getItemDao().selectMyWonAuctionsByTitle(itemTitle,idUser);
				}else {
					return DaoFactory.getItemDao().selectMyWonAuctionsByTitleCat(itemTitle,idCategory,idUser);
				}
			}
		// ------------------- checked : mes enchères remportées + mes enchères en cours  -----------------------
		}else if (!currentAuctions && myBids && wonAuctions) {
			// TITLE IS EMPTY
			if (itemTitle.isBlank() || itemTitle.isEmpty()) {
				if (idCategory==0) {
					// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
					return DaoFactory.getItemDao().selectMyWonAuctionsMyBids(idUser);
				}else {
					// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
					return DaoFactory.getItemDao().selectMyWonAuctionsMyBidsByCategory(idCategory,idUser);
				}
			}else { // is itemTitle blank or empty : NO
				if (idCategory==0) {
					return DaoFactory.getItemDao().selectMyWonAuctionsMyBidsByTitle(itemTitle,idUser);
				}else {
					return DaoFactory.getItemDao().selectMyWonAuctionsMyBidsByTitleCat(itemTitle,idCategory,idUser);
				}
			}
		// ------------------- checked : mes enchères remportées (3) + enchères en cours (1)  -----------------------
			}else if (currentAuctions && !myBids && wonAuctions) {
				// TITLE IS EMPTY
				if (itemTitle.isBlank() || itemTitle.isEmpty()) {
					if (idCategory==0) {
						// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
						return DaoFactory.getItemDao().selectMyWonAuctionsCurrentAuctions(idUser);
					}else {
						// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
						return DaoFactory.getItemDao().selectMyWonAuctionsCurrentAuctionsByCategory(idUser,idCategory);
					}
				}else { // is itemTitle blank or empty : NO
					if (idCategory==0) {
						return DaoFactory.getItemDao().selectMyWonAuctionsCurrentAuctionsByTitle(idUser,itemTitle);
					}else {
						return DaoFactory.getItemDao().selectMyWonAuctionsCurrentAuctionsByTitleCat(idUser,itemTitle,idCategory);
					}
				}
		// ------------------- checked : TOUT  -----------------------
			}else if (currentAuctions && myBids && wonAuctions) {
			// TITLE IS EMPTY
				if (itemTitle.isBlank() || itemTitle.isEmpty()) {
					if (idCategory==0) {
						// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
						return DaoFactory.getItemDao().selectAllCheckedAuctions(idUser);
					}else {
						// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
						return DaoFactory.getItemDao().selectAllCheckedAuctionsByCategory(idCategory,idUser);
					}
				}else { // is itemTitle blank or empty : NO
					if (idCategory==0) {
						return DaoFactory.getItemDao().selectAllCheckedAuctionsByTitle(itemTitle,idUser);
					}else {
						return DaoFactory.getItemDao().selectAllCheckedAuctionsByTitleCat(itemTitle,idCategory,idUser);
					}
				}
			}
		return null;
	}
	
	
	public List<ItemAllInformation> searchSales(Boolean currentSales, Boolean newSales, Boolean finishedSales,String itemTitle, Integer idCategory, Integer idUser) {
		// ----------------------- checked : ventes en cours	 --------------------------------
		if ((currentSales && !newSales && !finishedSales)||(!currentSales && !newSales && !finishedSales)) {
			// TITLE IS EMPTY
			if (itemTitle.isBlank() || itemTitle.isEmpty()) {
				if (idCategory==0) {
					// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
					return DaoFactory.getItemDao().selectAllCurrentSales(idUser);
				}else {
					// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
					return DaoFactory.getItemDao().selectAllCurrentSalesByCat(idUser,idCategory);
				}
			}else { // is itemTitle blank or empty : NO
				if (idCategory==0) {
					return DaoFactory.getItemDao().selectAllCurrentSalesByTitle(idUser,itemTitle);
				}else {
					return DaoFactory.getItemDao().selectAllCurrentSalesByTitleCat(idUser,itemTitle,idCategory);
				}
			}
		}
		// ----------------------- checked : ventes non débutées) --------------------------------
		if (!currentSales && newSales && !finishedSales) {
			// TITLE IS EMPTY
			if (itemTitle.isBlank() || itemTitle.isEmpty()) {
				if (idCategory==0) {
					// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
					return DaoFactory.getItemDao().selectNewSales(idUser);
				}else {
					// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
					return DaoFactory.getItemDao().selectNewSalesByCat(idUser,idCategory);
				}
			}else { // is itemTitle blank or empty : NO
				if (idCategory==0) {
					return DaoFactory.getItemDao().selectNewSalesByTitle(idUser,itemTitle);
				}else {
					return DaoFactory.getItemDao().selectNewSalesByTitleCat(idUser,itemTitle,idCategory);
				}
			}
		}
		// ----------------------- checked : (ventes terminées --------------------------------
		if (!currentSales && !newSales && finishedSales) {
			// TITLE IS EMPTY
			if (itemTitle.isBlank() || itemTitle.isEmpty()) {
				if (idCategory==0) {
					// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
					return DaoFactory.getItemDao().selectFinishedSales(idUser);
				}else {
					// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
					return DaoFactory.getItemDao().selectFinishedSalesByCat(idUser,idCategory);
				}
			}else { // is itemTitle blank or empty : NO
				if (idCategory==0) {
					return DaoFactory.getItemDao().selectFinishedSalesByTitle(idUser,itemTitle);
				}else {
					return DaoFactory.getItemDao().selectFinishedSalesByTitleCat(idUser,itemTitle,idCategory);
				}
			}
		}
		// ----------------------- checked : ventes nouvelles + ventes en cours --------------------------------
		if (currentSales && newSales && !finishedSales) {
			// TITLE IS EMPTY
			if (itemTitle.isBlank() || itemTitle.isEmpty()) {
				if (idCategory==0) {
					// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
					return DaoFactory.getItemDao().selectCurrentNewSales(idUser);
				}else {
					// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
					return DaoFactory.getItemDao().selectCurrentNewSalesByCat(idUser,idCategory);
				}
			}else { // is itemTitle blank or empty : NO
				if (idCategory==0) {
					return DaoFactory.getItemDao().selectCurrentNewSalesByTitle(idUser,itemTitle);
				}else {
					return DaoFactory.getItemDao().selectCurrentNewSalesByTitleCat(idUser,itemTitle,idCategory);
				}
			}
		}
		// ----------------------- checked : ventes en cours + ventes terminées --------------------------------
		if (currentSales && !newSales && finishedSales) {
			// TITLE IS EMPTY
			if (itemTitle.isBlank() || itemTitle.isEmpty()) {
				if (idCategory==0) {
					// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
					return DaoFactory.getItemDao().selectCurrentFinishedSales(idUser);
				}else {
					// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
					return DaoFactory.getItemDao().selectCurrentFinishedSalesByCat(idUser,idCategory);
				}
			}else { // is itemTitle blank or empty : NO
				if (idCategory==0) {
					return DaoFactory.getItemDao().selectCurrentFinishedSalesByTitle(idUser,itemTitle);
				}else {
					return DaoFactory.getItemDao().selectCurrentFinishedSalesByTitleCat(idUser,itemTitle,idCategory);
				}
			}
		}
		// ----------------------- checked : ventes nouvelles + ventes terminées  --------------------------------
		if (!currentSales && newSales && finishedSales) {
			// TITLE IS EMPTY
			if (itemTitle.isBlank() || itemTitle.isEmpty()) {
				if (idCategory==0) {
					// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
					return DaoFactory.getItemDao().selectNewFinishedSales(idUser);
				}else {
					// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
					return DaoFactory.getItemDao().selectNewFinishedSalesSalesByCat(idUser,idCategory);
				}
			}else { // is itemTitle blank or empty : NO
				if (idCategory==0) {
					return DaoFactory.getItemDao().selectNewFinishedSalesSalesByTitle(idUser,itemTitle);
				}else {
					return DaoFactory.getItemDao().selectNewFinishedSalesSalesByTitleCat(idUser,itemTitle,idCategory);
				}
			}
		}
		// ----------------------- checked : ventes nouvelles + ventes terminées + ventes en cours --------------------------------
		if (currentSales && newSales && finishedSales) {
			// TITLE IS EMPTY
			if (itemTitle.isBlank() || itemTitle.isEmpty()) {
				if (idCategory==0) {
					// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
					return DaoFactory.getItemDao().selectAllCheckedSales(idUser);
				}else {
					// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
					return DaoFactory.getItemDao().selectAllCheckedSalesByCat(idUser,idCategory);
				}
			}else { // is itemTitle blank or empty : NO
				if (idCategory==0) {
					return DaoFactory.getItemDao().selectAllCheckedSalesByTitle(idUser,itemTitle);
				}else {
					return DaoFactory.getItemDao().selectAllCheckedSalesByTitleCat(idUser,itemTitle,idCategory);
				}
			}
		}
		
		
		
		return null;
	}	
	
	

	
	public ItemAllInformation selectById(Integer id) {
		return DaoFactory.getItemDao().selectById(id);
	}
	
	private void checkFieldsItem(Item item) throws BLLException {
		BLLException bll = new BLLException();
		if (item.getItemTitle().isBlank() || item.getItemTitle().isEmpty()) {
			bll.addError("Le champ titre ne peut pas être vide");
		}
		if (item.getItemTitle().length()>30) {
			bll.addError("Le titre ne peut pas plus être long que 30 caractères.");
		}
		if (item.getDescription().isBlank() || item.getDescription().isEmpty()) {
			bll.addError("Le champ description ne peut pas être vide");
		}
		if (item.getItemTitle().length()>30) {
			bll.addError("La description ne peut pas être plus longue que 300 caractères.");
		}
		if (item.getCategory().getNoCategory()==null){
			bll.addError("Il faut sélectionner une catégorie.");
		}
		if (item.getStartDate()==null) {
			bll.addError("Le champ date de mise en vente ne peut pas être vide");
		}
		if (item.getStartDate().isBefore(LocalDateTime.now())) {
			bll.addError("La date de mise en vente ne peut pas être antidatée.");
		}
		if (item.getStartDate()==null) {
			bll.addError("Le champ date de fin d'enchères ne peut pas être vide");
		}
		if (item.getEndDate().isBefore(item.getStartDate())) {
			bll.addError("La date de fin d'enchères ne peut pas être antérieure à la date de mise en vente.");
		}
		if (item.getInitialPrice()<5) {
			bll.addError("Le prix initial de vente ne peut pas être inférieur à 5 crédits.");
			}				
			if (bll.getErreurs().size()>0) {
				throw bll;
			}
		}



	public List<ItemAllInformation> selectAuctionsByToBoDeletedUser(User user) {
		return DaoFactory.getItemDao().selectAuctionsByToBoDeletedUser(user.getNoUser());
	}

	public List<ItemAllInformation> selectSalesByToBoDeletedUser(User user) {
		return DaoFactory.getItemDao().selectSalesByToBoDeletedUser(user.getNoUser());}

	public void archiveItem(ItemAllInformation itemAllInf) {
		DaoFactory.getItemDao().archiveItem(itemAllInf);
	}

	
}
