package com.authenticationservice.conrollers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.authenticationservice.jwt.JwtUtils;
import com.authenticationservice.models.ERole;
import com.authenticationservice.models.Role;
import com.authenticationservice.models.User;
import com.authenticationservice.repository.RoleRepository;
import com.authenticationservice.repository.UserRepository;
import com.authenticationservice.request.LoginRequest;
import com.authenticationservice.request.SignupRequest;
import com.authenticationservice.services.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthController.class})
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    @Autowired
    private AuthController authController;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testAuthenticateUser() throws Exception {
        when(jwtUtils.generateJwtToken((Authentication) any())).thenReturn("ABC12");
        when(authenticationManager.authenticate((Authentication) any())).thenReturn(new TestingAuthenticationToken(
                new UserDetailsImpl(12L, "sajal.das@example.org", "kingshuk", new ArrayList<>()), "Credentials"));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("kingshuk");
        loginRequest.setUsername("sajal.das@example.org");
        String content = (new ObjectMapper()).writeValueAsString(loginRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
   "{\"id\":12,\"userEmail\":\"sajal.das@example.org\",\"roles\":[],\"accessToken\":\"ABC12\",\"tokenType\":\"Bearer\"}"));
    }
    
    @Test
    void testRegisterUser() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_BOOKIE);
        Optional<Role> ofResult = Optional.of(role);
        when(roleRepository.findByName((ERole) any())).thenReturn(ofResult);

        User user = new User();
        user.setId(12L);
        user.setPassword("kingshuk");
        user.setRoles(new HashSet<>());
        user.setUserEmail("sajal.das@example.org");
        when(userRepository.existsByUserEmail((String) any())).thenReturn(true);
        when(userRepository.save((User) any())).thenReturn(user);

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setPassword("kingshuk");
        signupRequest.setRole("Role");
        signupRequest.setUserEmail("sajal.das@example.org");
        String content = (new ObjectMapper()).writeValueAsString(signupRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Error: Useremail is already exist!\"}"));
    }

    @Test
    void testRegisterUser2() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_BOOKIE);
        Optional<Role> ofResult = Optional.of(role);
        when(roleRepository.findByName((ERole) any())).thenReturn(ofResult);

        User user = new User();
        user.setId(12L);
        user.setPassword("kingshuk");
        user.setRoles(new HashSet<>());
        user.setUserEmail("sajal.das@example.org");
        when(userRepository.existsByUserEmail((String) any())).thenReturn(false);
        when(userRepository.save((User) any())).thenReturn(user);
        when(passwordEncoder.encode((CharSequence) any())).thenReturn("secret");

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setPassword("kingshuk");
        signupRequest.setRole("Role");
        signupRequest.setUserEmail("sajal.das@example.org");
        String content = (new ObjectMapper()).writeValueAsString(signupRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"User signup successfully!\"}"));
    }
}

