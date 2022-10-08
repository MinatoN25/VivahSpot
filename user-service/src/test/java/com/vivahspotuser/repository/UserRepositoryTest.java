package com.vivahspotuser.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.vivahspotuser.model.Gender;
import com.vivahspotuser.model.Role;
import com.vivahspotuser.model.User;


@RunWith(SpringRunner.class)
@DataMongoTest
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	private User user;
	
	@BeforeEach
	public void setUp() throws Exception {
		user = new User();
		user.setUserId(123);
		user.setRole(Role.BOOKIE);
		user.setGender(Gender.MALE);
		user.setName("Stallin");
		user.setUserEmail("stallin@gmail.com");
		user.setPassword("Vivahspot123");
		user.setContact(9876543210L);
		user.setCity("Delhi");
		
		userRepository.deleteById(123);
		userRepository.deleteById(125);
	}
	
	@AfterEach
	public void tearDown() throws Exception {

		userRepository.deleteById(123);
		userRepository.deleteById(125);

	}
	
	@Test
	public void saveBookingTest() {
		userRepository.insert(user);
		User fetchuser = userRepository.findById(123).get();
		Assert.assertEquals(user.getUserEmail(), fetchuser.getUserEmail());
	}

	@Test
	public void deleteBookingByIdTest() {
		userRepository.insert(user);
		User fetchuser = userRepository.findById(123).get();
		Assert.assertEquals(user.getUserEmail(), fetchuser.getUserEmail());
		userRepository.deleteById(fetchuser.getUserId());
		assertThrows(NoSuchElementException.class, () -> {
			userRepository.findById(123).get();
		});
	}
	
	@Test
	public void findAllUserTest() {
		userRepository.insert(user);
		user.setUserId(125);
		userRepository.insert(user);
		List<User> fetchuser = userRepository.findAll();
		Assert.assertFalse(fetchuser.isEmpty());
		Assert.assertNotEquals(fetchuser, null);
	}

	@Test
	public void updateUserTest() {
		userRepository.insert(user);
		User fetcheduser = userRepository.findById(123).get();
		fetcheduser.setCity("Kolkata");;
		userRepository.save(fetcheduser);
		fetcheduser = userRepository.findById(123).get();
		Assert.assertEquals("Kolkata", fetcheduser.getCity());
	}

	@Test
	public void getUserByIdTest() {
		userRepository.insert(user);
		User fetcheduser = userRepository.findById(123).get();
		Assert.assertEquals(user.getUserId(), fetcheduser.getUserId());

	}
	
	@Test
	public void getUserByEmailTest() {
		userRepository.insert(user);
		User fetcheduser = userRepository.findByEmail(user.getUserEmail(), HttpStatus.OK);
		Assert.assertEquals(user.getUserEmail(), fetcheduser.getUserEmail());

	}

}
