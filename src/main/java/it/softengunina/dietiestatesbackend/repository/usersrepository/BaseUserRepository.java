package it.softengunina.dietiestatesbackend.repository.usersrepository;

import it.softengunina.dietiestatesbackend.dto.usersdto.UserResponseDTO;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {
    Optional<UserResponseDTO> findDTOById(Long id);

    Optional<BaseUser> findByUsername(String username);
    Optional<UserResponseDTO> findDTOByUsername(String username);

    Optional<BaseUser> findByCognitoSub(String cognitoSub);
    Optional<UserResponseDTO> findDTOByCognitoSub(String cognitoSub);
}
