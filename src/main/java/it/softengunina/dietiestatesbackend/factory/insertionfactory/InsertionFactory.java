package it.softengunina.dietiestatesbackend.factory.insertionfactory;

import it.softengunina.dietiestatesbackend.dto.insertionsdto.requestdto.BaseInsertionRequestDTO;
import it.softengunina.dietiestatesbackend.model.insertions.Insertion;
import it.softengunina.dietiestatesbackend.model.users.UserWithAgency;

public interface InsertionFactory<T extends BaseInsertionRequestDTO> {
    Insertion createInsertion(T req, UserWithAgency uploader);
}
