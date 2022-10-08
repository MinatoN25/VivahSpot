package com.vivahspotuser.service;

import java.util.List;

import com.vivahspotuser.model.User;

public interface UserService {
	User addUser(User user);

	User updateUser(User user);

	User deleteUser(int userId);

	User getById(int userId);

	List<User> findAllByName(String name);

	User findByEmail(String userEmail);

	List<User> getAllUsers();
}
