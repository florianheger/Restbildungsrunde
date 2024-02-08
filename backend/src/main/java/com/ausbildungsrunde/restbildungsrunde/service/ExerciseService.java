package com.ausbildungsrunde.restbildungsrunde.service;

import com.ausbildungsrunde.restbildungsrunde.model.Exercise;
import com.ausbildungsrunde.restbildungsrunde.model.TalentsUser;
import com.ausbildungsrunde.restbildungsrunde.repository.ExerciseRepository;
import com.ausbildungsrunde.restbildungsrunde.repository.TalentsUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ExerciseService {
    private ExerciseRepository exerciseRepository;
    private TalentsUserRepository talentsUserRepository;

    public boolean checkSolution(long userId, long exerciseId, String solution) {
        Optional<Exercise> exerciseOptional = exerciseRepository.findById((int) exerciseId);
        if (exerciseOptional.isEmpty()) {
            return false;
        }
        Exercise exercise = exerciseOptional.get();
        if (!exercise.getSolution().equals(solution)) {
            return false;
        }
        Optional<TalentsUser> userOptional = talentsUserRepository.findById((int) userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        TalentsUser user = userOptional.get();
        user.addPoints(exercise.getPoints());
        talentsUserRepository.save(user);
        return true;
    }
}
