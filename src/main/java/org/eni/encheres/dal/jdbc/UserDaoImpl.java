package org.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.eni.encheres.bo.User;
import org.eni.encheres.config.ConnectionProvider;
import org.eni.encheres.dal.UserDao;

public class UserDaoImpl implements UserDao{

	/**
	 * Constantes utilisées pour la création des utilisateurs dans la BDD
	 */
	final Integer CREDIT = 0;
	final boolean IS_ADMIN = false;
	
	
	final String SELECT_ONE_USER = "SELECT * FROM UTILISATEURS WHERE no_utilisateur=?";
	final String INSERT_USER = "INSERT INTO UTILISATEURS (pseudo,nom,prenom,email,telephone,rue,code_postal,ville,mot_de_passe,credit,administrateur) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	final String SELECT_BY_USERNAME = "SELECT * FROM UTILISATEURS WHERE pseudo=?";
	final String SELECT_BY_MAIL = "SELECT * FROM UTILISATEURS WHERE email=?";
	
	
	@Override
	public List<User> selectAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private User mapUser(ResultSet rs) throws SQLException {
		return new User(
				rs.getInt(1),
				rs.getString(2), 
				rs.getString(3), 
				rs.getString(4), 
				rs.getString(5), 
				rs.getString(6), 
				rs.getString(7), 
				rs.getString(8), 
				rs.getString(9), 
				rs.getString(10), 
				rs.getInt(11), 
				rs.getBoolean(12)
				);
	}

	public User selectOneUser(Integer noUser) {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_ONE_USER);
			pStmt.setInt(1, noUser);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				return mapUser(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Créer un user dans la BDD, retourne la clé générée, récupère crédit et isAdmin 
	 * @param user
	 */
	public void insert(User user) {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement pStmt = cnx.prepareStatement(INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS);
			pStmt.setString(1, user.getUsername());
			pStmt.setString(2, user.getFirstName());
			pStmt.setString(3, user.getLastName());
			pStmt.setString(4, user.getEmail());
			pStmt.setString(5, user.getPhoneNumber());
			pStmt.setString(6, user.getStreet());
			pStmt.setString(7, user.getPostCode());
			pStmt.setString(8, user.getCity());
			pStmt.setString(9, user.getPassword());
			pStmt.setInt(10, CREDIT);
			pStmt.setBoolean(11, IS_ADMIN);
			
			pStmt.executeUpdate();
			ResultSet rs = pStmt.getGeneratedKeys();
			if (rs.next()) {
				user.setNoUser(rs.getInt(1));
				user.setCredit(CREDIT);
				user.setAdmin(IS_ADMIN);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	public User selectByUserName(String username) {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_BY_USERNAME);
			pStmt.setString(1, username);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				return mapUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public Object selectByEmail(String email) {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_BY_MAIL);
			pStmt.setString(1, email);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				return mapUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
