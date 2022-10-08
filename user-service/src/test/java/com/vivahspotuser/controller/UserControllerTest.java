package com.vivahspotuser.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivahspotuser.exception.UserAlreadyExistException;
import com.vivahspotuser.exception.UserNotFoundException;
import com.vivahspotuser.model.Gender;
import com.vivahspotuser.model.Role;
import com.vivahspotuser.model.User;
import com.vivahspotuser.repository.UserRepository;
import com.vivahspotuser.service.UserService;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
public class UserControllerTest {
	
	@Autowired
	private UserController userController;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private RabbitTemplate rabbitTemplate;
	
	@MockBean
	private UserService userService;
	
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
		
	}
	
	@Test
	void testAddUserSuccess() throws Exception {
		String content = (new ObjectMapper()).writeValueAsString(user);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/user")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"userId\":123,\"role\":\"BOOKIE\",\"gender\":\"MALE\",\"name\":\"Stallin\",\"userEmail\":\"stallin@gmail.com\",\"password\":\"Vivahspot123\",\"contact\":9876543210,\"city\":\"Delhi\"}"));
	}
	
	@Test
	void testCreateSuccess() throws Exception {
		User user = new User();
		user.setCity("Delhi");
		user.setContact(9861690000L);
		user.setUserEmail("stallin@gmail.com");
		user.setGender(Gender.MALE);
		user.setName("Stallin");
		user.setPassword("Vivahspot123");
		user.setRole(Role.BOOKIE);
		user.setUserId(123);
		when(userService.addUser((User) any())).thenReturn(user);

		User user1 = new User();
		user1.setCity("Delhi");
		user1.setContact(9861690000L);
		user1.setUserEmail("stallin@gmail.com");
		user1.setGender(Gender.MALE);
		user1.setName("Stallin");
		user1.setPassword("Vivahspot123");
		user1.setRole(Role.BOOKIE);
		user1.setUserId(123);
		String content = (new ObjectMapper()).writeValueAsString(user1);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/user")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"userId\":123,\"role\":\"BOOKIE\",\"gender\":\"MALE\",\"name\":\"Stallin\",\"userEmail\":\"stallin@gmail.com\",\"password"
								+ "\":\"Vivahspot123\",\"contact\":9861690000,\"city\":\"Delhi\"}"));
	}

	@Test
	void testCreateFailure() throws Exception {
		when(userService.addUser((User) any())).thenThrow(new UserAlreadyExistException("?"));

		User user = new User();
		user.setCity("Delhi");
		user.setContact(9861690000L);
		user.setUserEmail("stallin@gmail.com");
		user.setGender(Gender.MALE);
		user.setName("Stallin");
		user.setPassword("Vivahspot123");
		user.setRole(Role.BOOKIE);
		user.setUserId(123);
		String content = (new ObjectMapper()).writeValueAsString(user);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/user")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().is(409))
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("User Already Exists"));
	}

	@Test
	void testDeleteSuccess() throws Exception {
		User user = new User();
		user.setCity("Delhi");
		user.setContact(9861690000L);
		user.setUserEmail("stallin@gmail.com");
		user.setGender(Gender.MALE);
		user.setName("Stallin");
		user.setPassword("Vivahspot123");
		user.setRole(Role.BOOKIE);
		user.setUserId(123);
		when(userService.deleteUser(anyInt())).thenReturn(user);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/user/{userId}", 123);
		MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"userId\":123,\"role\":\"BOOKIE\",\"gender\":\"MALE\",\"name\":\"Stallin\",\"userEmail\":\"stallin@gmail.com\",\"password"
								+ "\":\"Vivahspot123\",\"contact\":9861690000,\"city\":\"Delhi\"}"));
	}

	@Test
	void testDeleteFailure() throws Exception {
		when(userService.deleteUser(anyInt())).thenThrow(new UserNotFoundException("?"));
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/user/{userId}", 123);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("NO USER PRESENT WITH THIS ID"));
	}

	@Test
	void testGetByIdSuccess() throws Exception {
		User user = new User();
		user.setCity("Delhi");
		user.setContact(9861690000L);
		user.setUserEmail("stallin@gmail.com");
		user.setGender(Gender.MALE);
		user.setName("Stallin");
		user.setPassword("Vivahspot123");
		user.setRole(Role.BOOKIE);
		user.setUserId(123);
		when(userService.getById(anyInt())).thenReturn(user);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/getById/{userId}", 123);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"userId\":123,\"role\":\"BOOKIE\",\"gender\":\"MALE\",\"name\":\"Stallin\",\"userEmail\":\"stallin@gmail.com\",\"password"
								+ "\":\"Vivahspot123\",\"contact\":9861690000,\"city\":\"Delhi\"}"));
	}

	@Test
	void testGetByIdException() throws Exception {
		when(userService.getById(anyInt())).thenThrow(new UserNotFoundException("?"));
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/getById/{userId}", 123);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("User Not Found"));
	}

	@Test
	void testGetAllSuccess() throws Exception {
		when(userService.getAllUsers()).thenReturn(new ArrayList<>());
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/allUsers");
		MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string("[]"));
	}

	@Test
	void testGetAllFailure() throws Exception {
		when(userService.getAllUsers()).thenThrow(new UserNotFoundException("?"));
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/allUsers");
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("User Not Found"));
	}

	@Test
	void testGetByEmailSuccess() throws Exception {
		User user = new User();
		user.setCity("Delhi");
		user.setContact(9861690000L);
		user.setUserEmail("stallin@gmail.com");
		user.setGender(Gender.MALE);
		user.setName("Stallin");
		user.setPassword("Vivahspot123");
		user.setRole(Role.BOOKIE);
		user.setUserId(123);
		when(userService.findByEmail((String) any())).thenReturn(user);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/getByEmail/{userEmail}",
				"stallin@gmail.com");
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"userId\":123,\"role\":\"BOOKIE\",\"gender\":\"MALE\",\"name\":\"Stallin\",\"userEmail\":\"stallin@gmail.com\",\"password"
								+ "\":\"Vivahspot123\",\"contact\":9861690000,\"city\":\"Delhi\"}"));
	}

	@Test
	void testGetByEmailFailure() throws Exception {
		when(userService.findByEmail((String) any())).thenThrow(new UserNotFoundException("?"));
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/getByEmail/{userEmail}",
				"stallin@gmail.com");
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("User Not Found"));
	}

	@Test
	void testGetByNameSuccess() throws Exception {
		when(userService.findAllByName((String) any())).thenReturn(new ArrayList<>());
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/getByName/{name}", "Stallin");
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string("[]"));
	}

	@Test
	void testGetByNameFailure() throws Exception {
		when(userService.findAllByName((String) any())).thenThrow(new UserNotFoundException("?"));
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/getByName/{name}", "Stallin");
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("User Not Found for Stallin"));
	}

	@Test
	void testUpdate() throws Exception {
		User user = new User();
		user.setCity("Delhi");
		user.setContact(9861690000L);
		user.setUserEmail("stallin@gmail.com");
		user.setGender(Gender.MALE);
		user.setName("Stallin");
		user.setPassword("Vivahspot123");
		user.setRole(Role.BOOKIE);
		user.setUserId(123);
		when(userService.updateUser((User) any())).thenReturn(user);

		User user1 = new User();
		user1.setCity("Delhi");
		user1.setContact(9861690000L);
		user1.setUserEmail("stallin@gmail.com");
		user1.setGender(Gender.MALE);
		user1.setName("Stallin");
		user1.setPassword("Vivahspot123");
		user1.setRole(Role.BOOKIE);
		user1.setUserId(123);
		String content = (new ObjectMapper()).writeValueAsString(user1);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/user")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"userId\":123,\"role\":\"BOOKIE\",\"gender\":\"MALE\",\"name\":\"Stallin\",\"userEmail\":\"stallin@gmail.com\",\"password"
								+ "\":\"Vivahspot123\",\"contact\":9861690000,\"city\":\"Delhi\"}"));
	}

	@Test
	void testUpdateFailure() throws Exception {
		when(userService.updateUser((User) any())).thenThrow(new UserNotFoundException("?"));

		User user = new User();
		user.setCity("Delhi");
		user.setContact(9861690000L);
		user.setUserEmail("stallin@gmail.com");
		user.setGender(Gender.MALE);
		user.setName("Stallin");
		user.setPassword("Vivahspot123");
		user.setRole(Role.BOOKIE);
		user.setUserId(123);
		String content = (new ObjectMapper()).writeValueAsString(user);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/user")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("User Not Found"));
	}

}
