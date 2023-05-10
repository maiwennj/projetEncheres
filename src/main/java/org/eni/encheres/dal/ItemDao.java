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
}
