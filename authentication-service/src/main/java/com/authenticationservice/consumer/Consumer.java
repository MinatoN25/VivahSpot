package com.authenticationservice.consumer;

import java.util.HashSet;
import java.util.Set;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.authenticationservice.config.RabbitMQConfig;
import com.authenticationservice.models.ERole;
import com.authenticationservice.models.Role;
import com.authenticationservice.models.User;
import com.authenticationservice.models.UserInfo;
import com.authenticationservice.repository.RoleRepository;
import com.authenticationservice.repository.UserRepository;

@Component
public class Consumer {

	@Autowired
	PasswordEncoder encoder;
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	@RabbitListener(queues = RabbitMQConfig.QUEUE)
	public void consumeMessageFromQueue(UserInfo userInfo) {
		System.out.println("Message recieved from queue : " + userInfo);
		User user = new User(null, userInfo.getUserEmail(),

				encoder.encode(userInfo.getPassword()), null);

		String strRoles = userInfo.getRole();

		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
	    	
	      Role userRole = roleRepository.findByName(ERole.ROLE_BOOKIE)
	          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	      roles.add(userRole);
	    } else {
		switch (strRoles) {
		case "VENUEOWNER":
			Role operatorRole = roleRepository.findByName(ERole.ROLE_VENUEOWNER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(operatorRole);

			break;

		default:
			Role userRole = roleRepository.findByName(ERole.ROLE_BOOKIE)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		}
	    }
		user.setRoles(roles);
		userRepository.save(user);

	}

}
