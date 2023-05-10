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
	
	// MES ENCHERES REMPORTEES + MES ENCHERES EN COURS : 2+3
	List<ItemAllInformation> selectMyWonAuctionsMyBidsByTitleCat(String itemTitle, Integer idCategory, Integer idUser);
	List<ItemAllInformation> selectMyWonAuctionsMyBidsByTitle(String itemTitle, Integer idUser);
	List<ItemAllInformation> selectMyWonAuctionsMyBidsByCategory(Integer idCategory, Integer idUser);
	List<ItemAllInformation> selectMyWonAuctionsMyBids(Integer idUser);
	
	// MES ENCHERES REMPORTEES + ENCHERES EN COURS : 1+3
	List<ItemAllInformation> selectMyWonAuctionsCurrentAuctionsByTitleCat(Integer idUser, String itemTitle,Integer idCategory);
	List<ItemAllInformation> selectMyWonAuctionsCurrentAuctionsByCategory(Integer idUser, Integer idCategory);
	List<ItemAllInformation> selectMyWonAuctionsCurrentAuctionsByTitle(Integer idUser, String itemTitle);
	List<ItemAllInformation> selectMyWonAuctionsCurrentAuctions(Integer idUser);
	
	// MES ENCHERES REMPORTEES + ENCHERES EN COURS + MES ENCHERES EN COURS : 1+2+3
	List<ItemAllInformation> selectAllCheckedAuctionsByCategory(Integer idCategory, Integer idUser);
	List<ItemAllInformation> selectAllCheckedAuctions(Integer idUser);
	List<ItemAllInformation> selectAllCheckedAuctionsByTitle(String itemTitle, Integer idUser);
	List<ItemAllInformation> selectAllCheckedAuctionsByTitleCat(String itemTitle, Integer idCategory, Integer idUser);
	
	// MES VENTES EN COURS : 1
	List<ItemAllInformation> selectAllCurrentSales(Integer idUser);
	List<ItemAllInformation> selectAllCurrentSalesByCat(Integer idUser,Integer idCategory);
	List<ItemAllInformation> selectAllCurrentSalesByTitle(Integer idUser,String itemTitle);
	List<ItemAllInformation> selectAllCurrentSalesByTitleCat(Integer idUser,String itemTitle, Integer idCategory);
	
	// MES VENTES NON DEBUTEES :2
	List<ItemAllInformation> selectNewSales(Integer idUser);
	List<ItemAllInformation> selectNewSalesByCat(Integer idUser, Integer idCategory);
	List<ItemAllInformation> selectNewSalesByTitle(Integer idUser, String itemTitle);
	List<ItemAllInformation> selectNewSalesByTitleCat(Integer idUser, String itemTitle, Integer idCategory);
	
	// MES VENTES TERMINEES : 3
	List<ItemAllInformation> selectFinishedSales(Integer idUser);
	List<ItemAllInformation> selectFinishedSalesByCat(Integer idUser, Integer idCategory);
	List<ItemAllInformation> selectFinishedSalesByTitle(Integer idUser, String itemTitle);
	List<ItemAllInformation> selectFinishedSalesByTitleCat(Integer idUser, String itemTitle, Integer idCategory);
	
	// VENTES EN COURS + VENTES NON DEBUTEES : 1 + 2 
	List<ItemAllInformation> selectCurrentNewSales(Integer idUser);
	List<ItemAllInformation> selectCurrentNewSalesByCat(Integer idUser, Integer idCategory);
	List<ItemAllInformation> selectCurrentNewSalesByTitle(Integer idUser, String itemTitle);
	List<ItemAllInformation> selectCurrentNewSalesByTitleCat(Integer idUser, String itemTitle, Integer idCategory);
	
	// VENTES EN COURS + VENTES TERMINEES : 1+ 3
	List<ItemAllInformation> selectCurrentFinishedSales(Integer idUser);
	List<ItemAllInformation> selectCurrentFinishedSalesByCat(Integer idUser, Integer idCategory);
	List<ItemAllInformation> selectCurrentFinishedSalesByTitle(Integer idUser, String itemTitle);
	List<ItemAllInformation> selectCurrentFinishedSalesByTitleCat(Integer idUser, String itemTitle, Integer idCategory);
	
	// VENTES NON DEBUTEES + VENTES TERMINEES : 2 + 3
	List<ItemAllInformation> selectNewFinishedSales(Integer idUser);
	List<ItemAllInformation> selectNewFinishedSalesSalesByCat(Integer idUser, Integer idCategory);
	List<ItemAllInformation> selectNewFinishedSalesSalesByTitle(Integer idUser, String itemTitle);
	List<ItemAllInformation> selectNewFinishedSalesSalesByTitleCat(Integer idUser, String itemTitle,Integer idCategory);
	
	// TOUTES MES VENTES, ALL CHECKED.
	List<ItemAllInformation> selectAllCheckedSales(Integer idUser);
	List<ItemAllInformation> selectAllCheckedSalesByCat(Integer idUser, Integer idCategory);
	List<ItemAllInformation> selectAllCheckedSalesByTitle(Integer idUser, String itemTitle);
	List<ItemAllInformation> selectAllCheckedSalesByTitleCat(Integer idUser, String itemTitle, Integer idCategory);
	
	
}
