package com.ausbildungsrunde.restbildungsrunde.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

    String author;

    int points;

}
