package it.softengunina.dietiestatesbackend.repository.usersrepository;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@Primary
public interface BaseUserRepository<T extends BaseUser> extends JpaRepository<T, Long> {
    @Query("SELECT u FROM #{#entityName} u WHERE u.username = :username")
    Optional<T> findByUsername(String username);

    @Query("SELECT u FROM #{#entityName} u WHERE u.cognitoSub = :cognitoSub")
    Optional<T> findByCognitoSub(String cognitoSub);

    @Query("SELECT COUNT(u) > 0 FROM #{#entityName} u WHERE u.username = :username")
    boolean existsByUsername(String username);

    @Query("SELECT COUNT(u) > 0 FROM #{#entityName} u WHERE u.cognitoSub = :cognitoSub")
    boolean existsByCognitoSub(String cognitoSub);
}
