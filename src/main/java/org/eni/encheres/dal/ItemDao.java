package org.eni.encheres.dal;

import java.util.List;

import org.eni.encheres.bo.Item;

public interface ItemDao {
	List<Item> selectAllItems();
	List<Item> selectByCategory(Integer category);
	List<Item> selectByTitle(String itemTitle);
	List<Item> selectByTitleByCategory(String itemTitle,Integer category);
	List<Item> selectItemsByState (String state);
}
