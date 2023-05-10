package org.eni.encheres.bll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.eni.encheres.bll.exception.BLLException;
import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.ItemAllInformation;
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
	 * Génère le résultat des recherches effectuées dans les cadres de recherche. 
	 * Cette fonction vérifie :<br/>
	 * 1 : ce que l'utilisateur a coché comme cases ou non.<br/>
	 * 2 : si les deux paramètres itemTitle et category sont remplis<br/>
	 * puis redirige vers la méthode la plus adaptée.<br/>
	 * @param category 
	 * @param itemTitle 
	 * @param idUser 
	 * @param itemState 
	 * @return List<Item>
	 */
	public List<ItemAllInformation> searchAuctions(Boolean currentAuctions, Boolean myBids, Boolean wonAuctions,String itemTitle, Integer idCategory, Integer idUser) {	
		// ----------------------- checked : (enchères ouvertes) OU (enchères ouvertes + mes enchères) --------------------------------
		if ((currentAuctions && !myBids && !wonAuctions) || (currentAuctions && myBids && !wonAuctions)) {
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
		// TODO Auto-generated method stub
		return null;
	}
	
//	private List<ItemAllInformation> selectAllCheckedAuctionsByTitleCat(String itemTitle, Integer idCategory,Integer idUser) {
//		return DaoFactory.getItemDao().selectAllCheckedAuctionsByTitleCat(itemTitle,idCategory,idUser);
//	}

//	private List<ItemAllInformation> selectAllCheckedAuctionsByTitle(String itemTitle, Integer idUser) {
//		return DaoFactory.getItemDao().selectAllCheckedAuctionsByTitle(itemTitle,idUser);
//	}

//	private List<ItemAllInformation> selectAllCheckedAuctionsByCategory(Integer idCategory, Integer idUser) {
//		return DaoFactory.getItemDao().selectAllCheckedAuctionsByCategory(idCategory,idUser);
//	}

//	private List<ItemAllInformation> selectAllCheckedAuctions(Integer idUser) {
//		return DaoFactory.getItemDao().selectAllCheckedAuctions(idUser);
//	}

//	private List<ItemAllInformation> selectMyWonAuctionsCurrentAuctionsByTitleCat(String itemTitle, Integer idCategory,Integer idUser) {
//		return DaoFactory.getItemDao().selectMyWonAuctionsCurrentAuctionsByTitleCat(idUser,itemTitle,idCategory);
//	}

//	private List<ItemAllInformation> selectMyWonAuctionsCurrentAuctionsByTitle(String itemTitle, Integer idUser) {
//		return DaoFactory.getItemDao().selectMyWonAuctionsCurrentAuctionsByTitle(idUser,itemTitle);
//	}

//	private List<ItemAllInformation> selectMyWonAuctionsCurrentAuctionsByCategory(Integer idCategory, Integer idUser) {
//		return DaoFactory.getItemDao().selectMyWonAuctionsCurrentAuctionsByCategory(idUser,idCategory);
//	}

//	private List<ItemAllInformation> selectMyWonAuctionsCurrentAuctions(Integer idUser) {
//		return DaoFactory.getItemDao().selectMyWonAuctionsCurrentAuctions(idUser);
//	}

//	private List<ItemAllInformation> selectMyWonAuctionsMyBidsByTitleCat(String itemTitle, Integer idCategory,Integer idUser) {
//		return DaoFactory.getItemDao().selectMyWonAuctionsMyBidsByTitleCat(itemTitle,idCategory,idUser);
//	}

//	private List<ItemAllInformation> selectMyWonAuctionsMyBidsByTitle(String itemTitle, Integer idUser) {
//		return DaoFactory.getItemDao().selectMyWonAuctionsMyBidsByTitle(itemTitle,idUser);
//	}

//	private List<ItemAllInformation> selectMyWonAuctionsMyBidsByCategory(Integer idCategory, Integer idUser) {
//		return DaoFactory.getItemDao().selectMyWonAuctionsMyBidsByCategory(idCategory,idUser);
//	}

//	private List<ItemAllInformation> selectMyWonAuctionsMyBids(Integer idUser) {
//		return DaoFactory.getItemDao().selectMyWonAuctionsMyBids(idUser);
//	}

//	private List<ItemAllInformation> selectMyWonAuctionsByTitle(String itemTitle, Integer idUser) {
//		return DaoFactory.getItemDao().selectMyWonAuctionsByTitle(itemTitle,idUser);
//	}

//	private List<ItemAllInformation> selectMyWonAuctionsByTitleCat(String itemTitle, Integer idCategory,Integer idUser) {
//		return DaoFactory.getItemDao().selectMyWonAuctionsByTitleCat(itemTitle,idCategory,idUser);
//	}

//	private List<ItemAllInformation> selectMyWonAuctionsByCategory(Integer idCategory, Integer idUser) {
//		return DaoFactory.getItemDao().selectMyWonAuctionsByCategory(idCategory,idUser);
//	}

//	private List<ItemAllInformation> selectMyWonAuctions(Integer idUser) {
//		return DaoFactory.getItemDao().selectMyWonAuctions(idUser);
//	}

//	private List<ItemAllInformation> selectMyCurrentAuctionsByTitleCat(String itemTitle, Integer idCategory,Integer idUser) {
//		return DaoFactory.getItemDao().selectMyCurrentAuctionsByTitleCat(itemTitle,idCategory,idUser);
//	}

//	private List<ItemAllInformation> selectMyCurrentAuctionsByTitle(String itemTitle, Integer idUser) {
//		return DaoFactory.getItemDao().selectMyCurrentAuctionsByTitle(itemTitle,idUser);
//	}

//	private List<ItemAllInformation> selectMyCurrentAuctionsByCategory(Integer idCategory, Integer idUser) {
//		return DaoFactory.getItemDao().selectMyCurrentAuctionsByCategory(idCategory,idUser);
//	}

//	private List<ItemAllInformation> selectMyCurrentAuctions(Integer idUser) {
//		return DaoFactory.getItemDao().selectMyCurrentAuctions(idUser);
//	}

//	private List<ItemAllInformation> selectCurrentAuctionsByTitleCat(String itemTitle, Integer idCategory) {
//		return DaoFactory.getItemDao().selectCurrentAuctionsByTitleCat(itemTitle,idCategory);
//	}

//	private List<ItemAllInformation> selectCurrentAuctionsByTitle(String itemTitle) {
//		return DaoFactory.getItemDao().selectCurrentAuctionsByTitle(itemTitle);
//	}

//	private List<ItemAllInformation> selectCurrentAuctionsByCategory(Integer idCategory) {
//		return DaoFactory.getItemDao().selectCurrentAuctionsByCategory(idCategory);
//	}
	
	/**
	 * Used directly on the home page, hence the "public" visibility
	 * @return List<ItemAllInformation> 
	 */
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
