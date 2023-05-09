package org.eni.encheres.bll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.eni.encheres.bll.exception.BLLException;
import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.ItemAllInformation;
import org.eni.encheres.bo.ItemsStates;
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
	 * Génère le résultat des recherches effectuées dans les cadres de recherche. Cette fonction vérifie si les deux paramètres
	 * sont empty ou blank, et redirige vers la méthode la plus adaptée.
	 * @param category 
	 * @param itemTitle 
	 * @param idUser 
	 * @param itemState 
	 * @return List<Item>
	 */
	public List<ItemAllInformation> searchAuctions(Boolean currentAuctions, Boolean myBids, Boolean wonAuctions,String itemTitle, Integer idCategory, Integer idUser) {
		List<ItemAllInformation> auctionsList = new ArrayList<>();
		
		// ----------------------- checked : (enchères ouvertes) OU (enchères ouvertes + mes enchères) --------------------------------
		if ((currentAuctions && !myBids && !wonAuctions) || (currentAuctions && myBids && !wonAuctions)) {
			// TITLE IS EMPTY
			if (itemTitle.isBlank() || itemTitle.isEmpty()) {
				if (idCategory==0) {
					// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
					auctionsList = selectAllCurrentAuctions();
				}else {
					// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
					auctionsList = selectCurrentAuctionsByCategory(idCategory);
				}
			}else { // is itemTitle blank or empty : NO
				if (idCategory==0) {
					auctionsList = selectCurrentAuctionsByTitle(itemTitle);
				}else {
					auctionsList = selectCurrentAuctionsByTitleCat(itemTitle, idCategory);
				}
			}
		// ----------------------- checked : mes enchères en cours  --------------------------------
		}else if (!currentAuctions && myBids && !wonAuctions) {
			// TITLE IS EMPTY
				if (itemTitle.isBlank() || itemTitle.isEmpty()) {
					if (idCategory==0) {
						// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
						auctionsList = selectMyCurrentAuctions(idUser);
					}else {
						// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
						auctionsList = selectMyCurrentAuctionsByCategory(idCategory,idUser);
					}
				}else { // is itemTitle blank or empty : NO
					if (idCategory==0) {
						auctionsList = selectMyCurrentAuctionsByTitle(itemTitle,idUser);
					}else {
						auctionsList = selectMyCurrentAuctionsByTitleCat(itemTitle, idCategory,idUser);
					}
				}
		// ----------------------- checked : mes enchères remportées  --------------------------------
		}else if (!currentAuctions && !myBids && wonAuctions) {
			// TITLE IS EMPTY
			if (itemTitle.isBlank() || itemTitle.isEmpty()) {
				if (idCategory==0) {
					// CATEGORY IS EMPTY TOO --> SELECT ALL, MY FRIEND
					auctionsList = selectMyWonAuctions(idUser);
				}else {
					// CATEGORY ISN'T NULL SO YOU SELECT ALL WITH A CATEGORY
					auctionsList = selectMyWonAuctionsByCategory(idCategory,idUser);
				}
			}else { // is itemTitle blank or empty : NO
				if (idCategory==0) {
					auctionsList = selectMyWonAuctionsByTitle(itemTitle,idUser);
				}else {
					auctionsList = selectMyWonAuctionsByTitleCat(itemTitle, idCategory,idUser);
				}
			}
	}
		
		
		
		return auctionsList;
	}
	
	
	
	private List<ItemAllInformation> selectMyWonAuctionsByTitle(String itemTitle, Integer idUser) {
		return DaoFactory.getItemDao().selectMyWonAuctionsByTitle(itemTitle,idUser);
	}

	private List<ItemAllInformation> selectMyWonAuctionsByTitleCat(String itemTitle, Integer idCategory,Integer idUser) {
		return DaoFactory.getItemDao().selectMyWonAuctionsByTitleCat(itemTitle,idCategory,idUser);
	}

	private List<ItemAllInformation> selectMyWonAuctionsByCategory(Integer idCategory, Integer idUser) {
		return DaoFactory.getItemDao().selectMyWonAuctionsByCategory(idCategory,idUser);
	}

	private List<ItemAllInformation> selectMyWonAuctions(Integer idUser) {
		return DaoFactory.getItemDao().selectMyWonAuctions(idUser);
	}

	private List<ItemAllInformation> selectMyCurrentAuctionsByTitleCat(String itemTitle, Integer idCategory,Integer idUser) {
		return DaoFactory.getItemDao().selectMyCurrentAuctionsByTitleCat(itemTitle,idCategory,idUser);
	}

	private List<ItemAllInformation> selectMyCurrentAuctionsByTitle(String itemTitle, Integer idUser) {
		return DaoFactory.getItemDao().selectMyCurrentAuctionsByTitle(itemTitle,idUser);
	}

	private List<ItemAllInformation> selectMyCurrentAuctionsByCategory(Integer idCategory, Integer idUser) {
		return DaoFactory.getItemDao().selectMyCurrentAuctionsByCategory(idCategory,idUser);
	}

	private List<ItemAllInformation> selectMyCurrentAuctions(Integer idUser) {
		return DaoFactory.getItemDao().selectMyCurrentAuctions(idUser);
	}

	private List<ItemAllInformation> selectCurrentAuctionsByTitleCat(String itemTitle, Integer idCategory) {
		return DaoFactory.getItemDao().selectCurrentAuctionsByTitleCat(itemTitle,idCategory);
	}

	private List<ItemAllInformation> selectCurrentAuctionsByTitle(String itemTitle) {
		return DaoFactory.getItemDao().selectCurrentAuctionsByTitle(itemTitle);
	}

	private List<ItemAllInformation> selectCurrentAuctionsByCategory(Integer idCategory) {
		return DaoFactory.getItemDao().selectCurrentAuctionsByCategory(idCategory);
	}

	public List<ItemAllInformation> selectAllCurrentAuctions() {
		return DaoFactory.getItemDao().selectAllCurrentAuctions();
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
}
