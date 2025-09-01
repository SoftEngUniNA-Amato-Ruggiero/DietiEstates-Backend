package it.softengunina.dietiestatesbackend.model.users;

import java.util.Set;

public interface User {
    Long getId();
    String getUsername();
    String getCognitoSub();
    Set<String> getRoles();
}
