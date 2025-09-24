package it.softengunina.dietiestatesbackend.model.users;

public interface UserWithSpecificRole extends User {
    String getSpecificRoleName();
}
