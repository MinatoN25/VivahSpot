package com.authenticationservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.authenticationservice.models.User;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

class UserDetailsImplTest {
   
    @Test
    void testBuild() {
        User user = new User();
        user.setId(12L);
        user.setPassword("kingshuk");
        user.setRoles(new HashSet<>());
        user.setUserEmail("sajal.das@example.org");
        UserDetailsImpl buildResult = UserDetailsImpl.build(user);

        User user1 = new User();
        user1.setId(12L);
        user1.setPassword("kingshuk");
        user1.setRoles(new HashSet<>());
        user1.setUserEmail("sajal.das@example.org");
        UserDetailsImpl actualBuildResult = buildResult.build(user1);
        assertEquals(buildResult, actualBuildResult);
        assertEquals(12L, actualBuildResult.getId().longValue());
        assertEquals("kingshuk", actualBuildResult.getPassword());
        assertEquals("sajal.das@example.org", actualBuildResult.getUsername());
        assertTrue(actualBuildResult.isAccountNonExpired());
        assertTrue(actualBuildResult.isAccountNonLocked());
        assertTrue(actualBuildResult.isCredentialsNonExpired());
        assertTrue(actualBuildResult.isEnabled());
    }

    @Test
    void testEquals() {
        assertNotEquals(UserDetailsImpl.build(new User()), null);
        assertNotEquals(UserDetailsImpl.build(new User()), "Different type to UserDetailsImpl");
    }

    @Test
    void testEquals2() {
        UserDetailsImpl buildResult = UserDetailsImpl.build(new User());
        assertEquals(buildResult, buildResult);
        int expectedHashCodeResult = buildResult.hashCode();
        assertEquals(expectedHashCodeResult, buildResult.hashCode());
    }

    @Test
    void testEquals3() {
        UserDetailsImpl buildResult = UserDetailsImpl.build(new User());
        UserDetailsImpl buildResult1 = UserDetailsImpl.build(new User());
        assertEquals(buildResult, buildResult1);
        int notExpectedHashCodeResult = buildResult.hashCode();
        assertNotEquals(notExpectedHashCodeResult, buildResult1.hashCode());
    }

    @Test
    void testEquals4() {
        UserDetailsImpl buildResult = UserDetailsImpl
                .build(new User(12L, "sajal.das@example.org", "kingshuk", new HashSet<>()));
        assertNotEquals(buildResult, UserDetailsImpl.build(new User()));
    }

    @Test
    void testEquals5() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(12L);
        when(user.getPassword()).thenReturn("kingshuk");
        when(user.getUserEmail()).thenReturn("sajal.das@example.org");
        when(user.getRoles()).thenReturn(new HashSet<>());
        UserDetailsImpl buildResult = UserDetailsImpl.build(user);
        assertNotEquals(buildResult, UserDetailsImpl.build(new User()));
    }
}

