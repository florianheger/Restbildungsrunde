package com.ausbildungsrunde.restbildungsrunde.controller;

import com.ausbildungsrunde.restbildungsrunde.model.Exercise;
import com.ausbildungsrunde.restbildungsrunde.repository.ExerciseRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {
    private final ExerciseRepository exerciseRepository;

    @PostMapping
    public ResponseEntity<Void> createExercise(@RequestBody Exercise exercise, UriComponentsBuilder ucb) {
        Exercise newExercise = exerciseRepository.save(exercise);
        URI locationOfExercise = ucb.path("api/exercise/{id}").buildAndExpand(newExercise.getId()).toUri();
        return ResponseEntity.created(locationOfExercise).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getExercise(@PathVariable long id) {
        Optional<Exercise> exercise = exerciseRepository.findById((int)id);
        if (exercise.isPresent()) {
            return ResponseEntity.ok(exercise.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable long id) {
        exerciseRepository.deleteById((int)id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<URI> updateExercise(@RequestBody Exercise exercise, @PathVariable long id) {
        Optional<Exercise> exerciseOptional = exerciseRepository.findById((int)id);
        if (exerciseOptional.isPresent()) {
            Exercise exerciseToUpdate = exerciseOptional.get();
            exerciseToUpdate.setAuthor(exercise.getAuthor());
            exerciseToUpdate.setTitle(exercise.getTitle());
            exerciseToUpdate.setDescription(exercise.getDescription());
            exerciseToUpdate.setPoints(exercise.getPoints());
            exerciseToUpdate.setSolution(exercise.getSolution());
            exerciseToUpdate.setCategory(exercise.getCategory());
            exerciseToUpdate.setDifficulty(exercise.getDifficulty());
            exerciseRepository.save(exerciseToUpdate);
            URI locationOfExercise = URI.create("/api/exercise/" + exerciseToUpdate.getId());
            return ResponseEntity.ok(locationOfExercise);
        }
        return ResponseEntity.notFound().build();
    }
}
