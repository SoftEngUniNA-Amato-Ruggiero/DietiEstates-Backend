package it.softengunina.dietiestatesbackend.listeners;

import it.softengunina.dietiestatesbackend.model.Role;
import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.repository.RoleRepository;
import jakarta.persistence.PrePersist;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class UserRoleListener {
    RoleRepository roleRepository;

    public UserRoleListener(@Lazy RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PrePersist
    public void prePersist(User user) {
        Role role = roleRepository.findByName(user.getClass().getSimpleName())
                .orElseGet(() -> roleRepository.save(new Role(user.getClass().getSimpleName())));

        user.addRole(role);
    }
}
