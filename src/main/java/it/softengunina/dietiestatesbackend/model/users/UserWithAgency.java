package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;

public interface UserWithAgency {
    RealEstateAgency getAgency();
    Long getId();
    String getUsername();
    String getCognitoSub();
    String getRole();
    boolean isManager();
}
