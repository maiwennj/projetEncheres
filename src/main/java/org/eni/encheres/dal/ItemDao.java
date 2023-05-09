package org.eni.encheres.dal;

import java.util.List;

import org.eni.encheres.bo.Item;
import org.eni.encheres.bo.ItemAllInformation;

public interface ItemDao {

	List<ItemAllInformation> selectByCategory(String itemsState, Integer category);
	List<ItemAllInformation> selectByTitle(String itemsState, String itemTitle);
	List<ItemAllInformation> selectByTitleByCategory(String itemsState, String itemTitle,Integer category);
	List<ItemAllInformation> selectItemsByState (String itemsState);
	ItemAllInformation selectById(Integer id);
	Item insertItem(Item item);
}
