package com.vivahspotuser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.vivahspotuser.exception.UserAlreadyExistException;
import com.vivahspotuser.exception.UserNotFoundException;
import com.vivahspotuser.model.DatabaseSequence;
import com.vivahspotuser.model.Gender;
import com.vivahspotuser.model.Role;
import com.vivahspotuser.model.User;
import com.vivahspotuser.repository.UserRepository;

@ContextConfiguration(classes = { UserServiceImpl.class })
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
	@MockBean
	private MongoOperations mongoOperations;

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private UserServiceImpl userServiceImpl;
	
	private User user;

	@Test
	void testGetNextSequenceSuccess() {
		DatabaseSequence databaseSequence = new DatabaseSequence();
		databaseSequence.setId("42");
		databaseSequence.setSeq(1);
		when(mongoOperations.findAndModify((Query) any(), (UpdateDefinition) any(), (FindAndModifyOptions) any(),
				(Class<DatabaseSequence>) any())).thenReturn(databaseSequence);
		assertEquals(1, userServiceImpl.getNextSequence("Seq Name"));
		verify(mongoOperations).findAndModify((Query) any(), (UpdateDefinition) any(), (FindAndModifyOptions) any(),
				(Class<DatabaseSequence>) any());
	}

	@Test
	void testGetNextSequenceFailure() {
		when(mongoOperations.findAndModify((Query) any(), (UpdateDefinition) any(), (FindAndModifyOptions) any(),
				(Class<DatabaseSequence>) any())).thenThrow(new UserAlreadyExistException("_id"));
		assertThrows(UserAlreadyExistException.class, () -> userServiceImpl.getNextSequence("Seq Name"));
		verify(mongoOperations).findAndModify((Query) any(), (UpdateDefinition) any(), (FindAndModifyOptions) any(),
				(Class<DatabaseSequence>) any());
	}
	
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
		
	}
	
	@Test
	void testAddUserSuccess() {
		when(userRepository.save((User) any())).thenReturn(user);
		when(userRepository.findAll()).thenReturn(new ArrayList<>());

		DatabaseSequence databaseSequence = new DatabaseSequence();
		databaseSequence.setId("42");
		databaseSequence.setSeq(1);
		when(mongoOperations.findAndModify((Query) any(), (UpdateDefinition) any(), (FindAndModifyOptions) any(),
				(Class<DatabaseSequence>) any())).thenReturn(databaseSequence);
		assertSame(user, userServiceImpl.addUser(user));
		verify(userRepository).save((User) any());
		verify(userRepository).findAll();
		verify(mongoOperations).findAndModify((Query) any(), (UpdateDefinition) any(), (FindAndModifyOptions) any(),
				(Class<DatabaseSequence>) any());
		assertEquals(1, user.getUserId());
	}

	@Test
	void testAddUserFailure() {
		when(userRepository.save((User) any())).thenThrow(new UserAlreadyExistException("booking_sequence"));
		when(userRepository.findAll()).thenReturn(new ArrayList<>());

		DatabaseSequence databaseSequence = new DatabaseSequence();
		databaseSequence.setId("42");
		databaseSequence.setSeq(1);
		when(mongoOperations.findAndModify((Query) any(), (UpdateDefinition) any(), (FindAndModifyOptions) any(),
				(Class<DatabaseSequence>) any())).thenReturn(databaseSequence);

		assertThrows(UserAlreadyExistException.class, () -> userServiceImpl.addUser(user));
		verify(userRepository).save((User) any());
		verify(userRepository).findAll();
		verify(mongoOperations).findAndModify((Query) any(), (UpdateDefinition) any(), (FindAndModifyOptions) any(),
				(Class<DatabaseSequence>) any());
	}

	@Test
	void testUpdateUserSuccess() {
		Optional<User> ofResult = Optional.of(user);

		User user1 = new User();
		user1.setCity("Delhi");
		user1.setContact(9861690000L);
		user1.setGender(Gender.MALE);
		user1.setName("David");
		user1.setPassword("Vivahspot123");
		when(userRepository.save((User) any())).thenReturn(user);
		when(userRepository.findById((Integer) any())).thenReturn(ofResult);

		assertSame(user, userServiceImpl.updateUser(user1));
		verify(userRepository).save((User) any());
		verify(userRepository).findById((Integer) any());
	}

	@Test
	void testUpdateUserFailure() {
		Optional<User> ofResult = Optional.of(user);
		when(userRepository.save((User) any())).thenThrow(new UserNotFoundException("Msg"));
		when(userRepository.findById((Integer) any())).thenReturn(ofResult);

		User user1 = new User();
		user1.setCity("Delhi");
		user1.setContact(9861690000L);
		user1.setGender(Gender.MALE);
		user1.setName("David");
		user1.setPassword("Vivahspot123");
		assertThrows(UserNotFoundException.class, () -> userServiceImpl.updateUser(user1));
		verify(userRepository).save((User) any());
		verify(userRepository).findById((Integer) any());
	}

	@Test
	void testDeleteUserSuccess() {
		Optional<User> ofResult = Optional.of(user);
		doNothing().when(userRepository).deleteById((Integer) any());
		when(userRepository.findById((Integer) any())).thenReturn(ofResult);
		assertSame(user, userServiceImpl.deleteUser(123));
		verify(userRepository).findById((Integer) any());
		verify(userRepository).deleteById((Integer) any());
	}

	@Test
	void testDeleteUserFailure() {
		Optional<User> ofResult = Optional.of(user);
		doThrow(new UserNotFoundException("Msg")).when(userRepository).deleteById((Integer) any());
		when(userRepository.findById((Integer) any())).thenReturn(ofResult);
		assertThrows(UserNotFoundException.class, () -> userServiceImpl.deleteUser(123));
		verify(userRepository).findById((Integer) any());
		verify(userRepository).deleteById((Integer) any());
	}

	@Test
	void testGetAllUsersFailure() {
		when(userRepository.findAll()).thenReturn(new ArrayList<>());
		assertThrows(UserNotFoundException.class, () -> userServiceImpl.getAllUsers());
		verify(userRepository).findAll();
	}

	@Test
	void testGetAllUsersSuccess() {
		ArrayList<User> userList = new ArrayList<>();
		userList.add(user);
		when(userRepository.findAll()).thenReturn(userList);
		List<User> actualAllUsers = userServiceImpl.getAllUsers();
		assertSame(userList, actualAllUsers);
		assertEquals(1, actualAllUsers.size());
		verify(userRepository).findAll();
	}

	@Test
	void testGetByIdSuccess() {
		Optional<User> ofResult = Optional.of(user);
		when(userRepository.findById((Integer) any())).thenReturn(ofResult);
		assertSame(user, userServiceImpl.getById(123));
		verify(userRepository).findById((Integer) any());
	}

	@Test
	void testGetByIdFailure() {
		when(userRepository.findById((Integer) any())).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class, () -> userServiceImpl.getById(123));
		verify(userRepository).findById((Integer) any());
	}

	@Test
	void testFindAllByStallinFailure() {
		when(userRepository.findByName((String) any(), (HttpStatus) any())).thenReturn(new ArrayList<>());
		assertThrows(UserNotFoundException.class, () -> userServiceImpl.findAllByName("Stallin"));
		verify(userRepository).findByName((String) any(), (HttpStatus) any());
	}

	@Test
	void testFindAllByStallinSuccess() {
		ArrayList<User> userList = new ArrayList<>();
		userList.add(user);
		when(userRepository.findByName((String) any(), (HttpStatus) any())).thenReturn(userList);
		List<User> actualFindAllByStallinResult = userServiceImpl.findAllByName("Stallin");
		assertSame(userList, actualFindAllByStallinResult);
		assertEquals(1, actualFindAllByStallinResult.size());
		verify(userRepository).findByName((String) any(), (HttpStatus) any());
	}

	@Test
	void testFindByEmailSuccess() {
		when(userRepository.findByEmail((String) any(), (HttpStatus) any())).thenReturn(user);
		assertSame(user, userServiceImpl.findByEmail("stallin@gmail.com"));
		verify(userRepository).findByEmail((String) any(), (HttpStatus) any());
	}

	@Test
	void testFindByEmailFailure() {
		when(userRepository.findByEmail((String) any(), (HttpStatus) any()))
				.thenThrow(new UserNotFoundException("User Not Found !!"));
		assertThrows(UserNotFoundException.class, () -> userServiceImpl.findByEmail("stallin@gmail.com"));
		verify(userRepository).findByEmail((String) any(), (HttpStatus) any());
	}
}
