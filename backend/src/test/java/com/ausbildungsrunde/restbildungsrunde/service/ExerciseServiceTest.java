package com.ausbildungsrunde.restbildungsrunde.service;

import com.ausbildungsrunde.restbildungsrunde.model.Exercise;
import com.ausbildungsrunde.restbildungsrunde.model.TalentsUser;
import com.ausbildungsrunde.restbildungsrunde.repository.ExerciseRepository;
import com.ausbildungsrunde.restbildungsrunde.repository.TalentsUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {
    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private TalentsUserRepository talentsUserRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    @Test
    public void checkSolution_IfCorrectSolution_ShouldReturnTrue() {
        Exercise exercise = new Exercise();
        exercise.setPoints(5);
        exercise.setSolution("Solution");

        TalentsUser user = new TalentsUser();
        user.setPoints(0);

        doReturn(Optional.of(exercise)).when(exerciseRepository).findById(1);
        doReturn(Optional.of(user)).when(talentsUserRepository).findById(2);

        boolean actual = exerciseService.checkSolution(2, 1, "Solution");

        assertTrue(actual);
        verify(exerciseRepository, times(1)).findById(1);
        verify(talentsUserRepository, times(1)).findById(2);
        assertEquals(5, user.getPoints());
    }

    @Test
    public void checkSolution_IfFalseSolution_ShouldReturnFalse() {
        Exercise exercise = new Exercise();
        exercise.setPoints(5);
        exercise.setSolution("Solution");

        TalentsUser user = new TalentsUser();
        user.setPoints(0);

        doReturn(Optional.of(exercise)).when(exerciseRepository).findById(1);

        boolean actual = exerciseService.checkSolution(2, 1, "Wrong Solution");

        assertFalse(actual);
        verify(exerciseRepository, times(1)).findById(1);
        verify(talentsUserRepository, never()).findById(2);
        assertEquals(0, user.getPoints());
    }
}