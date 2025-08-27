package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;

public interface User {
    Long getId();
    String getUsername();
    String getCognitoSub();
    RealEstateAgency getAgency();
    String getRole();
}
