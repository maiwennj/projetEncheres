package org.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
	
	final String SELECT_ALL_USER = "SELECT * FROM UTILISATEURS";
	final String SELECT_ONE_USER = "SELECT * FROM UTILISATEURS WHERE no_utilisateur=?";
	final String INSERT_USER = "INSERT INTO UTILISATEURS (pseudo,nom,prenom,email,telephone,rue,code_postal,ville,mot_de_passe,credit,administrateur) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	final String SELECT_BY_USERNAME = "SELECT * FROM UTILISATEURS WHERE pseudo=?";
	final String SELECT_BY_MAIL = "SELECT * FROM UTILISATEURS WHERE email=?";
	final String SELECT_BY_LOGIN = "SELECT * FROM UTILISATEURS WHERE email=? OR pseudo=?";
	final String UPDATE_USER = "UPDATE UTILISATEURS SET pseudo=?,nom=?,prenom=?,email=?,telephone=?,rue=?,code_postal=?,ville=?,mot_de_passe=? WHERE no_utilisateur=?";
	//à simplifier
	final String CHECK_USERNAME_USED = "SELECT * FROM UTILISATEURS WHERE pseudo=? AND no_utilisateur!=?";
	final String CHECK_EMAIL_USED = "SELECT * FROM UTILISATEURS WHERE email=? AND no_utilisateur!=?";
	
	
	public List<User> selectAllUsers() {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			List<User> users = new ArrayList<>();	
			Statement stmt = cnx.createStatement();
			ResultSet rs = stmt.executeQuery(SELECT_ALL_USER);
			while(rs.next()) {
				users.add(mapUser(rs));
			}
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	public void insertUser(User user) {
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

	
	public User selectByEmail(String email) {
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

	public User selectByLogin(String login) {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement pStmt = cnx.prepareStatement(SELECT_BY_LOGIN);
			pStmt.setString(1, login);
			pStmt.setString(2, login);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				return mapUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object checkUsernameIsNotUsed(String username, int noUser) {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement pStmt = cnx.prepareStatement(CHECK_USERNAME_USED);
			pStmt.setString(1, username);
			pStmt.setInt(2, noUser);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				return mapUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	public Object checkEmailIsNotUsed(String email, int noUser) {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement pStmt = cnx.prepareStatement(CHECK_EMAIL_USED);
			pStmt.setString(1, email);
			pStmt.setInt(2, noUser);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				return mapUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateUser(User user, Integer noUser) {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement pStmt = cnx.prepareStatement(UPDATE_USER);
			pStmt.setString(1, user.getUsername());
			pStmt.setString(2, user.getLastName());
			pStmt.setString(3, user.getFirstName());
			pStmt.setString(4, user.getEmail());
			pStmt.setString(5, user.getPhoneNumber());
			pStmt.setString(6, user.getStreet());
			pStmt.setString(7, user.getPostCode());
			pStmt.setString(8, user.getCity());
			pStmt.setString(9, user.getPassword());
			pStmt.setInt(10, noUser);
			
			pStmt.executeUpdate();
			user = selectOneUser(noUser);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
