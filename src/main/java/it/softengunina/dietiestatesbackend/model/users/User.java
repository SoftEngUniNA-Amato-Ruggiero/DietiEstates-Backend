package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.Role;

import java.util.Set;

public interface User {
    Long getId();
    String getUsername();
    String getCognitoSub();
    Set<Role> getRoles();
    boolean hasRole(Role role);
    boolean hasRoleById(Long roleId);
    boolean hasRoleByName(String roleName);
    boolean addRole(Role role);
    boolean removeRole(Role role);
}
