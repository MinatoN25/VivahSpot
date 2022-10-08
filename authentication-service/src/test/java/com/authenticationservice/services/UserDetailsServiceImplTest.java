package com.authenticationservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.authenticationservice.models.ERole;
import com.authenticationservice.models.Role;
import com.authenticationservice.models.User;
import com.authenticationservice.repository.UserRepository;

import java.util.Collection;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserDetailsServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserDetailsServiceImplTest {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        User user = new User();
        user.setId(12L);
        user.setPassword("kingshuk");
        user.setRoles(new HashSet<>());
        user.setUserEmail("sajal.das@example.org");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByUserEmail((String) any())).thenReturn(ofResult);
        UserDetails actualLoadUserByUsernameResult = userDetailsServiceImpl.loadUserByUsername("sajal.das@example.org");
        assertTrue(actualLoadUserByUsernameResult.getAuthorities().isEmpty());
        assertEquals("sajal.das@example.org", actualLoadUserByUsernameResult.getUsername());
        assertEquals("kingshuk", actualLoadUserByUsernameResult.getPassword());
        assertEquals(12L, ((UserDetailsImpl) actualLoadUserByUsernameResult).getId().longValue());
        verify(userRepository).findByUserEmail((String) any());
    }

    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        Role role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_BOOKIE);

        HashSet<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        User user = new User();
        user.setId(12L);
        user.setPassword("kingshuk");
        user.setRoles(roleSet);
        user.setUserEmail("sajal.das@example.org");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByUserEmail((String) any())).thenReturn(ofResult);
        UserDetails actualLoadUserByUsernameResult = userDetailsServiceImpl.loadUserByUsername("sajal.das@example.org");
        Collection<? extends GrantedAuthority> authorities = actualLoadUserByUsernameResult.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals(12L, ((UserDetailsImpl) actualLoadUserByUsernameResult).getId().longValue());
        assertEquals("sajal.das@example.org", actualLoadUserByUsernameResult.getUsername());
        assertEquals("kingshuk", actualLoadUserByUsernameResult.getPassword());
        assertEquals("ROLE_BOOKIE", ((List<? extends GrantedAuthority>) authorities).get(0).getAuthority());
        verify(userRepository).findByUserEmail((String) any());
    }

    @Test
    void testLoadUserByUsername3() throws UsernameNotFoundException {
        when(userRepository.findByUserEmail((String) any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsServiceImpl.loadUserByUsername("sajal.das@example.org"));
        verify(userRepository).findByUserEmail((String) any());
    }

    @Test
    void testLoadUserByUsername4() throws UsernameNotFoundException {
        when(userRepository.findByUserEmail((String) any())).thenThrow(new UsernameNotFoundException("Msg"));
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsServiceImpl.loadUserByUsername("sajal.das@example.org"));
        verify(userRepository).findByUserEmail((String) any());
    }

    @Test
    void testLoadUserByUsername5() throws UsernameNotFoundException {
        Role role = mock(Role.class);
        when(role.getName()).thenReturn(ERole.ROLE_BOOKIE);
        doNothing().when(role).setId((Integer) any());
        doNothing().when(role).setName((ERole) any());
        role.setId(1);
        role.setName(ERole.ROLE_BOOKIE);

        Role role1 = new Role();
        role1.setId(1);
        role1.setName(ERole.ROLE_BOOKIE);

        HashSet<Role> roleSet = new HashSet<>();
        roleSet.add(role1);
        roleSet.add(role);

        User user = new User();
        user.setId(12L);
        user.setPassword("kingshuk");
        user.setRoles(roleSet);
        user.setUserEmail("sajal.das@example.org");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByUserEmail((String) any())).thenReturn(ofResult);
        UserDetails actualLoadUserByUsernameResult = userDetailsServiceImpl.loadUserByUsername("sajal.das@example.org");
        Collection<? extends GrantedAuthority> authorities = actualLoadUserByUsernameResult.getAuthorities();
        assertEquals(2, authorities.size());
        assertEquals(12L, ((UserDetailsImpl) actualLoadUserByUsernameResult).getId().longValue());
        assertEquals("sajal.das@example.org", actualLoadUserByUsernameResult.getUsername());
        assertEquals("kingshuk", actualLoadUserByUsernameResult.getPassword());
        assertEquals("ROLE_BOOKIE", ((List<? extends GrantedAuthority>) authorities).get(1).toString());
        assertEquals("ROLE_BOOKIE", ((List<? extends GrantedAuthority>) authorities).get(0).toString());
        verify(userRepository).findByUserEmail((String) any());
        verify(role).getName();
        verify(role).setId((Integer) any());
        verify(role).setName((ERole) any());
    }
}

