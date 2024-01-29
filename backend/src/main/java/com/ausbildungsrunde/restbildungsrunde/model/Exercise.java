package com.ausbildungsrunde.restbildungsrunde.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String title;

    String description;

    String solution;

    String language;

    String difficulty;

    String category;

    @ManyToOne
    TalentsUser author;

    int points;

}
