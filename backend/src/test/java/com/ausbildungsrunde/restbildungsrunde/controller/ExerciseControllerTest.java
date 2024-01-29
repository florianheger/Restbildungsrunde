package com.ausbildungsrunde.restbildungsrunde.controller;

import com.ausbildungsrunde.restbildungsrunde.model.Exercise;
import com.ausbildungsrunde.restbildungsrunde.model.TalentsUser;
import com.ausbildungsrunde.restbildungsrunde.repository.ExerciseRepository;
import com.ausbildungsrunde.restbildungsrunde.repository.TalentsUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ExerciseControllerTest {
    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private TalentsUserRepository talentsUserRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    void createExercise_ShouldCreateExercise() {
        Exercise newExercise = buildTestExercise();

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/exercise/" + newExercise.getAuthor().getId(), newExercise, Void.class);
        ResponseEntity<Exercise> newExerciseEntity = restTemplate.getForEntity(response.getHeaders().getLocation(), Exercise.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(newExerciseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newExerciseEntity.getBody().getAuthor().getId()).isEqualTo(newExercise.getAuthor().getId());
        assertThat(newExerciseEntity.getBody().getTitle()).isEqualTo(newExercise.getTitle());
        assertThat(newExerciseEntity.getBody().getDescription()).isEqualTo(newExercise.getDescription());
        assertThat(newExerciseEntity.getBody().getPoints()).isEqualTo(newExercise.getPoints());
        assertThat(newExerciseEntity.getBody().getSolution()).isEqualTo(newExercise.getSolution());
        assertThat(newExerciseEntity.getBody().getCategory()).isEqualTo(newExercise.getCategory());
        assertThat(newExerciseEntity.getBody().getDifficulty()).isEqualTo(newExercise.getDifficulty());
    }

    @Test
    @DirtiesContext
    void deleteExercise_ShouldDeleteExercise() {
        Exercise newExercise = buildTestExercise();
        long id = exerciseRepository.save(newExercise).getId();

        restTemplate.delete("/api/exercise/" + id);

        assertFalse(exerciseRepository.existsById((int) id));
    }

    @Test
    @DirtiesContext
    void updateExercise_ShouldUpdateExercise() {
        Exercise newExercise = buildTestExercise();
        long id = exerciseRepository.save(newExercise).getId();

        Exercise updatedExercise = Exercise.builder()
                .id(id)
                .author(newExercise.getAuthor())
                .title("Test")
                .description("This is a new test description")
                .points(20)
                .solution("1, 5, 7")
                .category("Spring")
                .difficulty("hard")
                .language("Java")
                .build();

        restTemplate.put("/api/exercise/" + id, updatedExercise);

        Optional<Exercise> result = exerciseRepository.findById((int) id);

        assertTrue(result.isPresent());
        assertThat(result.get().getAuthor().getId()).isEqualTo(updatedExercise.getAuthor().getId());
        assertThat(result.get().getTitle()).isEqualTo(updatedExercise.getTitle());
        assertThat(result.get().getDescription()).isEqualTo(updatedExercise.getDescription());
        assertThat(result.get().getPoints()).isEqualTo(updatedExercise.getPoints());
        assertThat(result.get().getSolution()).isEqualTo(updatedExercise.getSolution());
        assertThat(result.get().getCategory()).isEqualTo(updatedExercise.getCategory());
        assertThat(result.get().getDifficulty()).isEqualTo(updatedExercise.getDifficulty());
    }

    @Test
    @DirtiesContext
    void getExercise_WhenExerciseExists_ShouldReturnExercise() {
        Exercise newExercise = buildTestExercise();
        long id = exerciseRepository.save(newExercise).getId();

        ResponseEntity<Exercise> response = restTemplate.getForEntity("/api/exercise/" + id, Exercise.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getAuthor().getId()).isEqualTo(newExercise.getAuthor().getId());
        assertThat(response.getBody().getTitle()).isEqualTo(newExercise.getTitle());
        assertThat(response.getBody().getDescription()).isEqualTo(newExercise.getDescription());
        assertThat(response.getBody().getPoints()).isEqualTo(newExercise.getPoints());
        assertThat(response.getBody().getSolution()).isEqualTo(newExercise.getSolution());
        assertThat(response.getBody().getCategory()).isEqualTo(newExercise.getCategory());
        assertThat(response.getBody().getDifficulty()).isEqualTo(newExercise.getDifficulty());
    }

    @Test
    @DirtiesContext
    void getExercise_WhenExerciseDoesNotExist_ShouldReturnExercise() {
        ResponseEntity<Exercise> response = restTemplate.getForEntity("/api/exercise/" + -1, Exercise.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Exercise buildTestExercise() {
        TalentsUser user = talentsUserRepository.save(
                TalentsUser.builder()
                        .username("Tom Testermann")
                        .points(5)
                        .exercises(new ArrayList<>())
                        .build()
        );

        return Exercise.builder()
                .id(1L)
                .author(user)
                .title("Test")
                .description("This is a test description")
                .points(5)
                .solution("1, 5, 7")
                .category("Spring")
                .difficulty("easy")
                .language("Java")
                .build();
    }
}