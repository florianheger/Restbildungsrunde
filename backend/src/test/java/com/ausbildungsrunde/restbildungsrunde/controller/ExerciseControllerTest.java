package com.ausbildungsrunde.restbildungsrunde.controller;

import com.ausbildungsrunde.restbildungsrunde.model.Exercise;
import com.ausbildungsrunde.restbildungsrunde.payload.request.LoginRequest;
import com.ausbildungsrunde.restbildungsrunde.payload.request.SignupRequest;
import com.ausbildungsrunde.restbildungsrunde.repository.ExerciseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ExerciseControllerTest {
    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    //TODO: Es fehlen noch Tests zwecks Abfrage, ob eine Aufgabe bspw nicht gelöscht wird, wenn ein User es versucht, der nicht der Author ist.

    @Test
    @DirtiesContext
    void createExercise_ShouldCreateExercise() {
        Exercise newExercise = buildTestExercise();
        HttpHeaders cookieHeader = signUpLoginAndReturnCookie();

        ResponseEntity<Void> response = createExerciseWithHeader(cookieHeader, newExercise);

        ResponseEntity<Exercise> newExerciseEntity = getExerciseWithLocation(response.getHeaders().getLocation(), cookieHeader);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(newExerciseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(newExerciseEntity.getBody());
        assertThat(newExerciseEntity.getBody().getAuthor().getUsername()).isEqualTo("Tom Testermann");
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
        HttpHeaders cookieHeader = signUpLoginAndReturnCookie();

        ResponseEntity<Void> createResponse = createExerciseWithHeader(cookieHeader, newExercise);
        ResponseEntity<Exercise> getExerciseEntity = getExerciseWithLocation(createResponse.getHeaders().getLocation(), cookieHeader);

        assertNotNull(getExerciseEntity.getBody());

        restTemplate.exchange("/api/exercise/" + getExerciseEntity.getBody().getId(),
                DELETE,
                new HttpEntity<>(newExercise, cookieHeader),
                Void.class);

        assertFalse(exerciseRepository.existsById(getExerciseEntity.getBody().getId().intValue()));
    }

    @Test
    @DirtiesContext
    void updateExercise_ShouldUpdateExercise() {
        Exercise newExercise = buildTestExercise();
        HttpHeaders cookieHeader = signUpLoginAndReturnCookie();
        ResponseEntity<Void> createResponse = createExerciseWithHeader(cookieHeader, newExercise);
        ResponseEntity<Exercise> getExerciseEntity = getExerciseWithLocation(createResponse.getHeaders().getLocation(), cookieHeader);

        assertNotNull(getExerciseEntity.getBody());
        Long id = getExerciseEntity.getBody().getId();

        Exercise updatedExercise = Exercise.builder()
                .id(id)
                .author(getExerciseEntity.getBody().getAuthor())
                .title("Test")
                .description("This is a new test description")
                .points(20)
                .solution("1, 5, 7")
                .category("Spring")
                .difficulty("hard")
                .language("Java")
                .build();

        restTemplate.exchange("/api/exercise/" + getExerciseEntity.getBody().getId(),
                PUT,
                new HttpEntity<>(updatedExercise, cookieHeader),
                Void.class);

        Optional<Exercise> result = exerciseRepository.findById(id.intValue());

        assertTrue(result.isPresent());
        assertThat(result.get().getAuthor().getUsername()).isEqualTo("Tom Testermann");
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
        HttpHeaders cookieHeader = signUpLoginAndReturnCookie();
        ResponseEntity<Void> createResponse = createExerciseWithHeader(cookieHeader, newExercise);
        ResponseEntity<Exercise> getExerciseEntity = getExerciseWithLocation(createResponse.getHeaders().getLocation(), cookieHeader);

        assertThat(getExerciseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(getExerciseEntity.getBody());
        assertThat(getExerciseEntity.getBody().getAuthor().getUsername()).isEqualTo("Tom Testermann");
        assertThat(getExerciseEntity.getBody().getTitle()).isEqualTo(newExercise.getTitle());
        assertThat(getExerciseEntity.getBody().getDescription()).isEqualTo(newExercise.getDescription());
        assertThat(getExerciseEntity.getBody().getPoints()).isEqualTo(newExercise.getPoints());
        assertThat(getExerciseEntity.getBody().getSolution()).isEqualTo(newExercise.getSolution());
        assertThat(getExerciseEntity.getBody().getCategory()).isEqualTo(newExercise.getCategory());
        assertThat(getExerciseEntity.getBody().getDifficulty()).isEqualTo(newExercise.getDifficulty());
    }

    @Test
    @DirtiesContext
    void getExercise_WhenExerciseDoesNotExist_ShouldReturnExercise() throws URISyntaxException {
        HttpHeaders cookieHeader = signUpLoginAndReturnCookie();
        ResponseEntity<Exercise> getExerciseEntity = getExerciseWithLocation(new URI("/api/exercise/-1"), cookieHeader);

        assertThat(getExerciseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Exercise buildTestExercise() {
        return Exercise.builder()
                .id(1L)
                .title("Test")
                .description("This is a test description")
                .points(5)
                .solution("1, 5, 7")
                .category("Spring")
                .difficulty("easy")
                .language("Java")
                .build();
    }

    private HttpHeaders signUpLoginAndReturnCookie() {
        restTemplate.postForEntity("/api/auth/signup", new SignupRequest("Tom Testermann", "test"), Void.class);
        ResponseEntity<?> responsetest = restTemplate
                .postForEntity("/api/auth/signin", new LoginRequest("Tom Testermann", "test"), Void.class);

        String cookie = responsetest.getHeaders().getFirst("Set-Cookie");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie );
        return headers;
    }

    private ResponseEntity<Void> createExerciseWithHeader(HttpHeaders headers, Exercise newExercise) {
        return restTemplate.exchange("/api/exercise",
                POST,
                new HttpEntity<>(newExercise, headers),
                Void.class);
    }

    private ResponseEntity<Exercise> getExerciseWithLocation(URI location, HttpHeaders headers) {
        return restTemplate.exchange(location,
                GET,
                new HttpEntity<>(headers),
                Exercise.class);
    }
}