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
	
	//******************** CRUD
	//CREATE
	
	//READ
	
		public List<ItemAllInformation> selectItemsByState (String itemState){
			System.out.println("item manager");
			return DaoFactory.getItemDao().selectItemsByState(itemState);
		}

		/**
		 * Génère le résultat des recherches effectuées dans les cadres de recherche. Cette fonction vérifie si les deux paramètres
		 * sont empty ou blank, et redirige vers la méthode la plus adaptée.
		 * @param category 
		 * @param itemTitle 
		 * @param itemState 
		 * @return List<Item>
		 */
//		public List<Item> searchItems(String itemTitle, String category) {
			public List<ItemAllInformation> searchItems(String itemTitle, Integer category, String itemState) {
			List<ItemAllInformation> listItems = new ArrayList<>();
			// is itemTitle blank or empty : YES
			if (itemTitle.isBlank() || itemTitle.isEmpty()) {
				if (category==0) {
					listItems = selectItemsByState(itemState);
				}else {
					listItems = selectByCategory(itemState,category);
				}
			}else { // is itemTitle blank or empty : NO
				if (category==0) {
					listItems = selectByTitle(itemState,itemTitle);
				}else {
					listItems = selectByTitleByCategory(itemState,itemTitle, category);
				}
			}
			return listItems;
		}
		
		
		public List<ItemAllInformation> selectByCategory(String itemsState, Integer category) {
			return DaoFactory.getItemDao().selectByCategory(itemsState, category);
		}
		
		public List<ItemAllInformation> selectByTitle(String itemsState, String itemTitle) {
			return DaoFactory.getItemDao().selectByTitle(itemsState, itemTitle);
		}
		
		public List<ItemAllInformation> selectByTitleByCategory(String itemsState, String itemTitle, Integer category) {
			return DaoFactory.getItemDao().selectByTitleByCategory(itemsState, itemTitle, category);
		}

		public Item addItem(Item item) throws BLLException {
			checkFieldsItem(item);
			return DaoFactory.getItemDao().insertItem(item);
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
		
		
		
	
	// UPDATE
	
	//DELETE
	
	

}
