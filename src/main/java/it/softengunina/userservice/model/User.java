package it.softengunina.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    @Getter
    @Setter
    private String email;

    @NotBlank
    @Column(unique = true, nullable = false)
    @Getter
    @Setter
    private String cognitoSub;

    public User(@NonNull String email, @NonNull String cognitoSub) {
        this.email = email;
        this.cognitoSub = cognitoSub;
    }
}
