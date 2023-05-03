package org.eni.encheres.dal;

import java.util.List;

import org.eni.encheres.bo.User;

public interface UserDao {
	List<User> selectAllUsers();
}
