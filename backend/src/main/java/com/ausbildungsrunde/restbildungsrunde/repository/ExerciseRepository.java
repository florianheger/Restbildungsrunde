package com.ausbildungsrunde.restbildungsrunde.repository;

import com.ausbildungsrunde.restbildungsrunde.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository  extends JpaRepository<Exercise, Integer> {

}
