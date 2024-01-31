package com.ausbildungsrunde.restbildungsrunde.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@Entity
@Table(name = "talents_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TalentsUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String username;

    String password;

    int points;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Exercise> exercises;

    public TalentsUser(String username, String encode) {
        this.username = username;
        this.password = encode;
    }
}