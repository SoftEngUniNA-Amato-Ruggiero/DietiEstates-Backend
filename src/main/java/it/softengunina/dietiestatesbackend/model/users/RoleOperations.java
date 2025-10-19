package it.softengunina.dietiestatesbackend.model.users;

import java.util.Set;

public interface RoleOperations {
    Set<Role> getRoles();
    boolean hasRole(Role role);
    boolean hasRoleById(Long roleId);
    boolean hasRoleByName(String roleName);
    boolean addRole(Role role);
    boolean removeRole(Role role);
}
