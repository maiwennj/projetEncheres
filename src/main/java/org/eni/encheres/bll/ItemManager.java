package org.eni.encheres.bll;



import java.util.ArrayList;
import java.util.List;

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
//		public List<ItemAllInformation> selectAllItems() {
//			return DaoFactory.getItemDao().selectAllItems();
//		}
//		
		public List<ItemAllInformation> selectItemsByState (String itemState){
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

		public ItemAllInformation selectById(Integer id) {
			return DaoFactory.getItemDao().selectById(id);
		}
		
		
		
	
	// UPDATE
	
	//DELETE
	
	

}
