package it.softengunina.dietiestatesbackend.model.users;

public interface User extends RoleOperations {
    Long getId();
    String getUsername();
    String getCognitoSub();
}
