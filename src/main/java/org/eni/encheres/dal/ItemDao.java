package org.eni.encheres.dal;

import java.util.List;

import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.ItemAllInformation;

public interface ItemDao {

	ItemAllInformation selectById(Integer id);
	Item insertItem(Item item);
	
	//	ENCHERES EN COURS : HOME, OU *, ou CAS : 1 || 1+2
	List<ItemAllInformation> selectAllCurrentAuctions();
	List<ItemAllInformation> selectCurrentAuctionsByCategory(Integer idCategory);
	List<ItemAllInformation> selectCurrentAuctionsByTitleCat(String itemTitle, Integer idCategory);
	List<ItemAllInformation> selectCurrentAuctionsByTitle(String itemTitle);
	
	
	// MES ENCHERES EN COURS : 2
	List<ItemAllInformation> selectMyCurrentAuctions(Integer idUser);
	List<ItemAllInformation> selectMyCurrentAuctionsByTitleCat(String itemTitle, Integer idCategory, Integer idUser);
	List<ItemAllInformation> selectMyCurrentAuctionsByTitle(String itemTitle, Integer idUser);
	List<ItemAllInformation> selectMyCurrentAuctionsByCategory(Integer idCategory, Integer idUser);
	
	// MES ENCHERES REMPORTEES : 3
	List<ItemAllInformation> selectMyWonAuctions(Integer idUser);
	List<ItemAllInformation> selectMyWonAuctionsByCategory(Integer idCategory, Integer idUser);
	List<ItemAllInformation> selectMyWonAuctionsByTitleCat(String itemTitle, Integer idCategory, Integer idUser);
	List<ItemAllInformation> selectMyWonAuctionsByTitle(String itemTitle, Integer idUser);
}
