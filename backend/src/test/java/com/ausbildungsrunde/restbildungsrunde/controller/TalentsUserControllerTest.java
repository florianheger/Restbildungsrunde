package com.ausbildungsrunde.restbildungsrunde.controller;

import com.ausbildungsrunde.restbildungsrunde.model.TalentsUser;
import com.ausbildungsrunde.restbildungsrunde.repository.TalentsUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TalentsUserControllerTest {
    @Autowired
    private TalentsUserRepository talentsUserRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createUser_ShouldCreateUser() {
        TalentsUser newTalentsUser = new TalentsUser();
        newTalentsUser.setUsername("Tom Testermann");
        newTalentsUser.setPoints(5);

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/user", newTalentsUser, Void.class);
        ResponseEntity<TalentsUser> newUserEntity = restTemplate.getForEntity(response.getHeaders().getLocation(), TalentsUser.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(newUserEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newUserEntity.getBody().getUsername()).isEqualTo(newTalentsUser.getUsername());
        assertThat(newUserEntity.getBody().getPoints()).isEqualTo(newTalentsUser.getPoints());
    }

    @Test
    public void deleteUser_ShouldDeleteUser() {
        TalentsUser newTalentsUser = new TalentsUser();
        newTalentsUser.setUsername("Tom Testermann");
        newTalentsUser.setPoints(5);

        long id = talentsUserRepository.save(newTalentsUser).getId();

        restTemplate.delete("/api/user/" + id);

        Optional<TalentsUser> result = talentsUserRepository.findById((int) id);

        assertTrue(result.isEmpty());
    }

    @Test
    public void updatePoints_ShouldUpdatePoints() {
        TalentsUser newTalentsUser = new TalentsUser();
        newTalentsUser.setUsername("Tom Testermann");
        newTalentsUser.setPoints(5);
        int expectedPoints = 10;

        long id = talentsUserRepository.save(newTalentsUser).getId();

        restTemplate.put("/api/user/updatePoints/" + id + "/" + expectedPoints, URI.class);

        Optional<TalentsUser> newUserInDb = talentsUserRepository.findById((int)id);

        assertTrue(newUserInDb.isPresent());
        assertThat(newUserInDb.get().getPoints()).isEqualTo(expectedPoints);
    }

    @Test
    public void getUser_WhenUserExists_ShouldReturnUser() {
        TalentsUser newTalentsUser = new TalentsUser();
        newTalentsUser.setUsername("Tom Testermann");
        newTalentsUser.setPoints(5);

        long id = talentsUserRepository.save(newTalentsUser).getId();

        ResponseEntity<TalentsUser> receivedUser = restTemplate.getForEntity("/api/user/" + id, TalentsUser.class);

        assertThat(receivedUser.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(receivedUser.getBody().getUsername()).isEqualTo(newTalentsUser.getUsername());
        assertThat(receivedUser.getBody().getPoints()).isEqualTo(newTalentsUser.getPoints());
        assertThat(receivedUser.getBody().getId()).isEqualTo(id);
    }

    @Test
    public void getUser_WhenUserDoesNotExist_ShouldReturnNoUser() {
        ResponseEntity<TalentsUser> receivedUser = restTemplate.getForEntity("/api/user/" + -1, TalentsUser.class);

        assertThat(receivedUser.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}