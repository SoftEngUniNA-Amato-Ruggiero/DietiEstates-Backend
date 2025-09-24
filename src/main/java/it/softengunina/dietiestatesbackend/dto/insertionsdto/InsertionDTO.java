package it.softengunina.dietiestatesbackend.dto.insertionsdto;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyRequestDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserResponseDTO;
import it.softengunina.dietiestatesbackend.model.Address;
import it.softengunina.dietiestatesbackend.model.insertions.InsertionDetails;

public interface InsertionDTO {
    Long getId();
    Address getAddress();
    InsertionDetails getDetails();
    UserResponseDTO getUploader();
    RealEstateAgencyRequestDTO getAgency();
}
