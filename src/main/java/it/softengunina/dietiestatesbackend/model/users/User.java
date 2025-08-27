package it.softengunina.dietiestatesbackend.model.users;

public interface User {
    Long getId();

    String getUsername();

    String getCognitoSub();
}
