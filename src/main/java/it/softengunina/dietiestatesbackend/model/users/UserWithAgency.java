package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;

public interface UserWithAgency extends User {
    RealEstateAgency getAgency();
}
