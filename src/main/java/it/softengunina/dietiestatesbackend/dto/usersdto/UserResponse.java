package it.softengunina.dietiestatesbackend.dto.usersdto;

import it.softengunina.dietiestatesbackend.model.users.Role;

import java.util.Set;

public interface UserResponse {
    Long getId();
    String getUsername();
    Set<Role> getRoles();
}
