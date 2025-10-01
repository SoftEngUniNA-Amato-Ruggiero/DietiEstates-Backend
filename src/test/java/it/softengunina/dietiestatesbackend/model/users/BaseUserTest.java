package it.softengunina.dietiestatesbackend.model.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BaseUserTest {

    private BaseUser user;

    @BeforeEach
    void setUp() {
        user = new BaseUser("testUser", "testSub");
    }

    @Test
    void addRole() {
        Role role = new Role("USER");
        user.addRole(role);

        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(role));
    }

    @Test
    void removeRole() {
        Role role1 = new Role("USER");
        Role role2 = new Role("ADMIN");

        user.addRole(role1);
        user.addRole(role2);
        user.removeRole(role1);

        assertEquals(1, user.getRoles().size());
        assertFalse(user.getRoles().contains(role1));
    }

    @Test
    void getRolesTest() {
        Role role1 = new Role("USER");
        Role role2 = new Role("ADMIN");

        user.addRole(role1);
        user.addRole(role2);

        Set<Role> roles = user.getRoles();

        assertEquals(2, roles.size());
        assertTrue(roles.contains(role1));
        assertTrue(roles.contains(role2));
    }

    @Test
    void hasRole() {
        Role role1 = new Role("USER");
        Role role2 = new Role("ADMIN");

        user.addRole(role1);

        assertTrue(user.hasRole(role1));
        assertFalse(user.hasRole(role2));
    }

    @Test
    void hasRoleById() throws NoSuchFieldException, IllegalAccessException {
        Role role1 = new Role("USER");
        Field role1Id = Role.class.getDeclaredField("id");
        role1Id.setAccessible(true);
        role1Id.set(role1, 1L); // Manually set the ID for testing


        user.addRole(role1);


        assertTrue(user.hasRoleById(1L));
    }

    @Test
    void hasRoleByName() {
        Role role1 = new Role("USER");

        user.addRole(role1);

        assertTrue(user.hasRoleByName("USER"));
        assertFalse(user.hasRoleByName("ADMIN"));
    }

}
