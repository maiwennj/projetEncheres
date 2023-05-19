package org.eni.encheres.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionProvider{

	private static DataSource dataSource;
	
	static {
			try {
				Context context = new InitialContext();
				dataSource = (DataSource) context.lookup("java:comp/env/jdbc/pool_cnx");
			} catch (NamingException e) {
				throw new RuntimeException("Impossible d'accéder à la base de données.");
			}
	}
	
	/**
	 * Cette méthode retourne une connexion issue d'une dataSource = du pool de connexions.
	 * @return une connexion
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
}