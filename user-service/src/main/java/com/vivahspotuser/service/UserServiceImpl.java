package com.vivahspotuser.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vivahspotuser.exception.UserAlreadyExistException;
import com.vivahspotuser.exception.UserNotFoundException;
import com.vivahspotuser.model.DatabaseSequence;
import com.vivahspotuser.model.User;
import com.vivahspotuser.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private MongoOperations mongo;

	private UserRepository userRepository;

	public UserServiceImpl() {
		super();
	}

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public int getNextSequence(String seqName) {
		DatabaseSequence counter = mongo.findAndModify(query(where("_id").is(seqName)), new Update().inc("seq", 1),
				options().returnNew(true).upsert(true), DatabaseSequence.class);
		return counter.getSeq();
	}

	@Override
	public User addUser(User user) {
		List<User> users = userRepository.findAll();
		for (User user2 : users) {
			if (user2.getUserEmail().equals(user.getUserEmail())) {
				throw new UserAlreadyExistException("User Already exists for "+user.getUserEmail());
			}
		}
		user.setUserId(getNextSequence(User.SEQUENCE_NAME));
		return userRepository.save(user);
	}

	@Override
	public User updateUser(User user) {
		User existingUser = userRepository.findById(user.getUserId()).orElse(null);
		if (existingUser == null)
			throw new UserNotFoundException("No Such User exists!!");
		else {
			existingUser.setGender(user.getGender());
			existingUser.setName(user.getName());
			existingUser.setPassword(user.getPassword());
			existingUser.setContact(user.getContact());
			existingUser.setCity(user.getCity());

			return userRepository.save(existingUser);
		}
	}

	@Override
	public User deleteUser(int userId) {
		Optional<User> findById = userRepository.findById(userId);
		if (findById.isPresent() == false) {
			throw new UserNotFoundException("User Not Found !!");
		} else {
			userRepository.deleteById(userId);
		}
		User user = findById.get();
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = userRepository.findAll();
		if (users.isEmpty()) {
			throw new UserNotFoundException("User Not Found !!");
		}
		System.out.println("Getting data from DB : " + users);
		return users;
	}

	@Override
	public User getById(int userId) {
		Optional<User> findById = userRepository.findById(userId);
		if (findById.isPresent()) {
			findById.get();
		} else {
			throw new UserNotFoundException("User Not Found !!");
		}
		return findById.get();

	}

	@Override
	public List<User> findAllByName(String name) {
		List<User> users = userRepository.findByName(name, HttpStatus.OK);
		if (users.isEmpty()) {
			throw new UserNotFoundException("User Not Found !!");
		}
		return users;
	}

	@Override
	public User findByEmail(String userEmail) {
		User user = userRepository.findByEmail(userEmail, HttpStatus.OK);
		if (user == null) {
			throw new UserNotFoundException("User Not Found !!");
		}
		return user;
	}
}
