package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserDTO;
import it.softengunina.dietiestatesbackend.model.insertions.Address;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionDetails;

public interface InsertionDTO {
    Long getId();
    Address getAddress();
    InsertionDetails getDetails();
    UserDTO getUploader();
    RealEstateAgencyDTO getAgency();
}
