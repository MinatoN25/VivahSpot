package com.authenticationservice.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;


import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;



import org.junit.Assert;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.authenticationservice.models.ERole;
import com.authenticationservice.models.Role;
import com.authenticationservice.models.User;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

	

	@Autowired
	private UserRepository userRepository;

	private User user;

	@BeforeEach
	public void setUp() throws Exception {
		user = new User();
		user.setId(1L);
		user.setUserEmail("sajal@gmail.com");
		user.setPassword("sajal");
		Set<Role> roles = new HashSet<>();
		roles.add (new Role(1,ERole.ROLE_BOOKIE));
		user.setRoles (new HashSet<>(roles));
		
		userRepository.deleteById(1L);
		userRepository.deleteById(2L);
	}

//	@AfterEach
//	public void tearDown() throws Exception {
//
//		userRepository.deleteById(123L);
//		userRepository.deleteById(135L);
//
//	}

	@Test
	public void saveUserTest() {
		userRepository.save(user);
		User fetchuser = userRepository.findByUserEmail("sajal@gmail.com").get();
		Assert.assertEquals(user.getUserEmail(), fetchuser.getUserEmail());
	}

	@Test
	public void deleteBookingByIdTest() {
		userRepository.save(user);
		User fetchuser = userRepository.findByUserEmail("sajal@gmail.com").get();
		Assert.assertEquals(user.getUserEmail(), fetchuser.getUserEmail());
		userRepository.deleteById(fetchuser.getId());
		assertThrows(NoSuchElementException.class, () -> {
			userRepository.findById(123L).get();
		});
	}
	
	@Test
	public void findAllUserTest() {
		userRepository.save(user);
		user.setUserEmail("sajal@gmail.com");;
		//userRepository.save(user);
		List<User> fetchuser = userRepository.findAll();
		Assert.assertFalse(fetchuser.isEmpty());
		Assert.assertNotEquals(fetchuser, null);
	}

	
//	@Test
//	public void getUserByEmailTest() {
//		userRepository.save(user);
//		User fetcheduser = userRepository.findByEmail(user.getUseremail(), HttpStatus.OK);
//		Assert.assertEquals(user.getUseremail(), fetcheduser.getUseremail());
//
//	}
}