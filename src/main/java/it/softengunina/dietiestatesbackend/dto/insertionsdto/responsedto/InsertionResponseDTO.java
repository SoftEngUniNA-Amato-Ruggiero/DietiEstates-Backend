package it.softengunina.dietiestatesbackend.dto.insertionsdto.responsedto;

import it.softengunina.dietiestatesbackend.dto.RealEstateAgencyRequestDTO;
import it.softengunina.dietiestatesbackend.dto.usersdto.UserResponseDTO;
import it.softengunina.dietiestatesbackend.model.Address;

import java.util.Set;

public interface InsertionResponseDTO {
    Long getId();
    String getDescription();
    Set<String> getTags();
    Address getAddress();
    UserResponseDTO getUploader();
    RealEstateAgencyRequestDTO getAgency();
}
