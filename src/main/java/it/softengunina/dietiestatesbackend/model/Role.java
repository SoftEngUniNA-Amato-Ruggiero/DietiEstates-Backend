package it.softengunina.dietiestatesbackend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dietiestates_roles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Column(unique = true, nullable = false)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
