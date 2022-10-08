package com.vivahspotuser.controller;

import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vivahspotuser.config.MQConfig;
import com.vivahspotuser.constants.UserConstants;
import com.vivahspotuser.exception.UserAlreadyExistException;
import com.vivahspotuser.exception.UserNotFoundException;
import com.vivahspotuser.model.User;
import com.vivahspotuser.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1")
@Api(value = "API to deal with User Profile", description = "This API provides the capability to Register an user and Update the profile", produces = "application/json")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved"),
		@ApiResponse(code = 404, message = "User Not Found") })
public class UserController {

	UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	RabbitTemplate rabbitTemplate;

	@ApiOperation(value = "Register User by providing required details", produces = "application/json")
	@PostMapping("/user")
	public ResponseEntity<?> create(@RequestBody User user, HttpSession session) {
		try {
			if (user.getRole() == null || user.getGender() == null || user.getName() == null || user.getUserEmail() == null
					|| user.getPassword() == null || user.getContact() == null || user.getCity() == null || user.getName().isEmpty()
					|| user.getUserEmail().isEmpty() || user.getPassword().isEmpty() || user.getCity().isEmpty()) {
				return new ResponseEntity<>(UserConstants.USER_INFO_MSG, HttpStatus.BAD_GATEWAY);
			}
			if(String.valueOf(user.getContact()).length() != 10) {
				return new ResponseEntity<>("Contact can only 10 digits",HttpStatus.BAD_GATEWAY);
			}
			if(!Pattern.compile("^(.+)@(.+)$").matcher(user.getUserEmail()).matches()) {
				return new ResponseEntity<>("Please enter valid email",HttpStatus.BAD_GATEWAY);
			}
			if(!Pattern.compile("(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{5,15})$").matcher(user.getPassword()).matches()) {
				return new ResponseEntity<>("Please enter valid Password",HttpStatus.BAD_GATEWAY);
			}
			userService.addUser(user);
			rabbitTemplate.convertAndSend(MQConfig.EXCHANGE, MQConfig.ROUTING_KEY, user);
		} catch (UserAlreadyExistException userExist) {
			return new ResponseEntity<String>("User Already Exists", HttpStatus.CONFLICT);
		}
		return new ResponseEntity<User>(user, HttpStatus.CREATED);

	}

	@ApiOperation(value = "Update User Details by providing User_Id", produces = "application/json")
	@PutMapping("/user")
	public ResponseEntity<?> update(@RequestBody User user, HttpSession session) {
		try {
			return new ResponseEntity<User>(userService.updateUser(user), HttpStatus.CREATED);

		} catch (UserNotFoundException userNotFound) {
			return new ResponseEntity<String>("User Not Found", HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Delete User Details by Id", produces = "application/json")
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<?> delete(@PathVariable int userId) {
		try {
			return new ResponseEntity<User>(userService.deleteUser(userId), HttpStatus.OK);
		} catch (UserNotFoundException userNotFound) {
			return new ResponseEntity<String>("NO USER PRESENT WITH THIS ID", HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Get All User Details", produces = "application/json")
	@GetMapping("/allUsers")
	public ResponseEntity<?> getAll() {
		try {
			return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
		} catch (UserNotFoundException userNotFound) {
			return new ResponseEntity<String>("User Not Found", HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Get User details by Id", produces = "application/json")
	@GetMapping("/getById/{userId}")
	public ResponseEntity<?> getById(@PathVariable int userId, HttpSession session) {
		try {
			return new ResponseEntity<User>(userService.getById(userId), HttpStatus.CREATED);

		} catch (UserNotFoundException userNotFound) {
			return new ResponseEntity<String>("User Not Found", HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Get User details by Name", produces = "application/json")
	@GetMapping("/getByName/{name}")
	public ResponseEntity<?> getByName(@PathVariable String name, HttpSession session) {
		try {
			return new ResponseEntity<>(userService.findAllByName(name), HttpStatus.CREATED);
		} catch (UserNotFoundException userNotFound) {
			return new ResponseEntity<String>("User Not Found for " + name, HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Get User details by Email", produces = "application/json")
	@GetMapping("/getByEmail/{userEmail}")
	public ResponseEntity<?> getByEmail(@PathVariable String userEmail, HttpSession session) {
		try {
			return new ResponseEntity<>(userService.findByEmail(userEmail), HttpStatus.CREATED);
		} catch (UserNotFoundException userNotFound) {
			return new ResponseEntity<String>("User Not Found", HttpStatus.NOT_FOUND);
		}
	}
}
