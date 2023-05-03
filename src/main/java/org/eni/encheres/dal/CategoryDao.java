package org.eni.encheres.dal;

import java.util.List;

import org.eni.encheres.bo.Category;

public interface CategoryDao {

	//CRUD
	
	List<Category> selectAllCategories();
	Category selectOneCategory(Integer idCategory);
}
