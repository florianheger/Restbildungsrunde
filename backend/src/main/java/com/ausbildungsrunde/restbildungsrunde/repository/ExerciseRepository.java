package com.ausbildungsrunde.restbildungsrunde.repository;

import com.ausbildungsrunde.restbildungsrunde.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository  extends JpaRepository<Exercise, Integer> {

}
