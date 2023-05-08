package org.eni.encheres.bll;

import java.util.List;

import org.eni.encheres.bll.exception.BLLException;
import org.eni.encheres.bo.Category;
import org.eni.encheres.dal.DaoFactory;

import lombok.Getter;

public class CategoryManager {

	@Getter public static CategoryManager instance = new CategoryManager();
	private CategoryManager() {}
	
	public List<Category> selectAllCategories() {
		return DaoFactory.getCategoryDao().selectAllCategories();
	}

	public Category selectOneCategory(Integer idCategory){
		if (idCategory==0) {
			return new Category();
		}
		return DaoFactory.getCategoryDao().selectOneCategory(idCategory);
	}
	
}
