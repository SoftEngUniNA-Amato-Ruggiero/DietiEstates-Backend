package it.softengunina.dietiestatesbackend.model.insertions;

import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.model.users.User;

public interface Insertion {
    Long getId();
    Address getAddress();
    InsertionDetails getDetails();
    User getUploader();
    RealEstateAgency getAgency();
}
