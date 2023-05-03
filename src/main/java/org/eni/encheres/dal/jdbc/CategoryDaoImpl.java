package org.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eni.encheres.bo.Category;
import org.eni.encheres.config.ConnectionProvider;
import org.eni.encheres.dal.CategoryDao;

public class CategoryDaoImpl implements CategoryDao{

	final String SELECT_ALL_CATEGORIES = "SELECT * FROM CATEGORIES";
	final String SELECT_ONE_CATEGORY = "SELECT * FROM CATEGORIES WHERE no_categorie=?";
	
	@Override
	public List<Category> selectAllCategories() {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			List<Category> listCategories = new ArrayList<>();
			Statement stmt = cnx.createStatement();
			ResultSet rs = stmt.executeQuery(SELECT_ALL_CATEGORIES);
			while(rs.next()) {
				listCategories.add(new Category(rs.getInt(1), rs.getString(2)));
			}
			return listCategories;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Category selectOneCategory(Integer idCategory) {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			Category category = new Category();
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ONE_CATEGORY);
			pStmt.setInt(1, idCategory);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				category.setNoCategory(idCategory);
				category.setLibelle(rs.getString(2));
				System.out.println(rs.getString(2));
			}
			return category;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
