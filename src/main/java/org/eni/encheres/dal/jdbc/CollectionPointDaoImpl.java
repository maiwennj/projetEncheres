package org.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.eni.encheres.bo.CollectionPoint;
import org.eni.encheres.config.ConnectionProvider;
import org.eni.encheres.dal.CollectionPointDao;

public class CollectionPointDaoImpl implements CollectionPointDao {

	final String INSERT_COLLECTION_POINT = "INSERT INTO RETRAITS (no_article,rue,code_postal,ville) VALUES (?,?,?,?);";
	
	@Override
	public void insertCollectionPoint(CollectionPoint collectionPoint) {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement pStmt = cnx.prepareStatement(INSERT_COLLECTION_POINT);
			pStmt.setInt(1, collectionPoint.getItem().getNoItem());
			pStmt.setString(2, collectionPoint.getStreetCP());
			pStmt.setString(3, collectionPoint.getPostCodeCP());
			pStmt.setString(4, collectionPoint.getCityCP());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
