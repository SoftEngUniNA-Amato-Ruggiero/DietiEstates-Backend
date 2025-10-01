package it.softengunina.dietiestatesbackend.model.insertions;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Column(unique = true, nullable = false)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String name;

    public Tag(String name) {
        this.name = name;
    }

    public static Set<Tag> fromNames(Set<String> names) {
        return names.stream()
                .map(Tag::new)
                .collect(Collectors.toSet());
    }
}
