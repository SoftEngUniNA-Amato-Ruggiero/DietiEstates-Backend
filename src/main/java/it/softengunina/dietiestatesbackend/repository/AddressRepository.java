package it.softengunina.dietiestatesbackend.repository;

import it.softengunina.dietiestatesbackend.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
