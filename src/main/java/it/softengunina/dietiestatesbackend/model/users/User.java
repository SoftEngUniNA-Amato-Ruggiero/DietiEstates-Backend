package it.softengunina.dietiestatesbackend.model.users;

import lombok.NonNull;

import java.util.Set;

public interface User {
    Long getId();
    String getUsername();
    String getCognitoSub();
    Set<String> getRoles();
    boolean addRole(@NonNull String role);
    boolean removeRole(@NonNull String role);
    boolean hasRole(@NonNull String role);
}
