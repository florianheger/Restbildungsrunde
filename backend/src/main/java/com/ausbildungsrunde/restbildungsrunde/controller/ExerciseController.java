package com.ausbildungsrunde.restbildungsrunde.controller;

import com.ausbildungsrunde.restbildungsrunde.model.Exercise;
import com.ausbildungsrunde.restbildungsrunde.repository.ExerciseRepository;
import com.ausbildungsrunde.restbildungsrunde.repository.TalentsUserRepository;
import com.ausbildungsrunde.restbildungsrunde.security.service.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/exercise")
@CrossOrigin(origins = "http://localhost:3000")
public class ExerciseController {
    private final ExerciseRepository exerciseRepository;
    private final TalentsUserRepository talentsUserRepository;

    @PostMapping("")
    public ResponseEntity<Void> createExercise(@RequestBody Exercise exercise, UriComponentsBuilder ucb) {
        Long userId = getID();
        talentsUserRepository.findById(userId.intValue()).ifPresent(exercise::setAuthor);
        Exercise newExercise = exerciseRepository.save(exercise);
        URI locationOfExercise = ucb.path("api/exercise/{id}").buildAndExpand(newExercise.getId()).toUri();
        return ResponseEntity.created(locationOfExercise).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getExercise(@PathVariable long id) {
        Optional<Exercise> exercise = exerciseRepository.findById((int)id);
        return exercise.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable long id) {
        Optional<Exercise> exerciseOptional = exerciseRepository.findById((int)id);
        Long userId = getID();
        if (exerciseOptional.isPresent() && Objects.equals(exerciseOptional.get().getAuthor().getId(), userId)) {
            exerciseRepository.deleteById((int)id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<URI> updateExercise(@RequestBody Exercise exercise, @PathVariable long id) {
        Optional<Exercise> exerciseOptional = exerciseRepository.findById((int)id);
        Long userId = getID();

        if (exerciseOptional.isPresent() && Objects.equals(exerciseOptional.get().getAuthor().getId(), userId)) {
            exercise.setId(id);
            exercise.setAuthor(exerciseOptional.get().getAuthor());
            exerciseRepository.save(exercise);
            URI locationOfExercise = URI.create("/api/exercise/" + exercise.getId());
            return ResponseEntity.ok(locationOfExercise);
        }
        return ResponseEntity.notFound().build();
    }

    public static Long getID() {
        Long id = null;
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            id = authentication.getPrincipal() instanceof UserDetailsImpl ?
                    ((UserDetailsImpl) authentication.getPrincipal()).getId() : null;
        }
        return id;
    }
}
