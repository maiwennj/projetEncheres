package org.eni.encheres.dal;
import java.util.List;

import org.eni.encheres.bo.User;

public interface UserDao {
	List<User> selectAllUsers();
	User selectOneUser(Integer noUser);
	void insertUser(User user);
	Object selectByUserName(String field);
	Object selectByEmail(String field);
	User selectByLogin(String login);
	Object checkUsernameIsNotUsed(String username, int noUser);
	Object checkEmailIsNotUsed(String email, int noUser);
	void updateUser(User user, Integer noUser);

}
