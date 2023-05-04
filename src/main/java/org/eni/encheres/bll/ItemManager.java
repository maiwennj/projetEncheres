package org.eni.encheres.bll;



import java.util.ArrayList;
import java.util.List;

import org.eni.encheres.bo.Item;
import org.eni.encheres.dal.DaoFactory;

import lombok.Getter;

public class ItemManager {

	//singleton getInstance();
	@Getter public static ItemManager instance = new ItemManager(); //lazy singleton
	private ItemManager() {}
	
	//******************** CRUD
	//CREATE
	
	//READ
		public List<Item> selectAllItems() {
			return DaoFactory.getItemDao().selectAllItems();
		}
		
		public List<Item> selectItemsByState (String state){
			return DaoFactory.getItemDao().selectItemsByState(state);
		}

		/**
		 * Génère le résultat des recherches effectuées dans les cadres de recherche. Cette fonction vérifie si les deux paramètres
		 * sont empty ou blank, et redirige vers la méthode la plus adaptée.
		 * @param category 
		 * @param itemTitle 
		 * @return List<Item>
		 */
//		public List<Item> searchItems(String itemTitle, String category) {
			public List<Item> searchItems(String itemTitle, Integer category) {
			List<Item> listItems = new ArrayList<>();
			// is itemTitle blank or empty : YES
			if (itemTitle.isBlank() || itemTitle.isEmpty()) {
				if (category==0) {
					listItems = selectAllItems();
				}else {
					listItems = selectByCategory(category);
				}
			}else { // is itemTitle blank or empty : NO
				if (category==0) {
					listItems = selectByTitle(itemTitle);
				}else {
					listItems = selectByTitleByCategory(itemTitle, category);
				}
			}
			return listItems;
		}
		
		
		public List<Item> selectByCategory(Integer category) {
			return DaoFactory.getItemDao().selectByCategory(category);
		}
		
		public List<Item> selectByTitle(String itemTitle) {
			return DaoFactory.getItemDao().selectByTitle(itemTitle);
		}
		
		public List<Item> selectByTitleByCategory(String itemTitle, Integer category) {
			return DaoFactory.getItemDao().selectByTitleByCategory(itemTitle, category);
		}
		
	
	// UPDATE
	
	//DELETE
	
	

}
