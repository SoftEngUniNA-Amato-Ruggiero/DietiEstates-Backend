package it.softengunina.dietiestatesbackend.listeners;

import it.softengunina.dietiestatesbackend.model.Role;
import it.softengunina.dietiestatesbackend.model.users.UserWithSpecificRole;
import it.softengunina.dietiestatesbackend.repository.RoleRepository;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class UserRoleListener {
    RoleRepository roleRepository;

    public UserRoleListener(@Lazy RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PrePersist
    public void assignRole(UserWithSpecificRole user)  {
        String roleName = user.getSpecificRoleName();
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Role not found: " + roleName));
        user.addRole(role);
    }

    @PreRemove
    public void removeRole(UserWithSpecificRole user)  {
        String roleName = user.getSpecificRoleName();
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Role not found: " + roleName));
        user.removeRole(role);
    }
}
