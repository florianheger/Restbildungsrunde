package com.ausbildungsrunde.restbildungsrunde.controller;

import com.ausbildungsrunde.restbildungsrunde.model.User;
import com.ausbildungsrunde.restbildungsrunde.model.UserEntity;
import com.ausbildungsrunde.restbildungsrunde.repository.UserRepository;
import lombok.AllArgsConstructor;
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
class UserControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createUser_ShouldCreateUser() {
        User newUser = new User();
        newUser.setUsername("Tom Testermann");
        newUser.setPoints(5);

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/user", newUser, Void.class);
        ResponseEntity<UserEntity> newUserEntity = restTemplate.getForEntity(response.getHeaders().getLocation(), UserEntity.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(newUserEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newUserEntity.getBody().getUsername()).isEqualTo(newUser.getUsername());
        assertThat(newUserEntity.getBody().getPoints()).isEqualTo(newUser.getPoints());
    }

    @Test
    public void deleteUser_ShouldDeleteUser() {
        UserEntity newUser = new UserEntity();
        newUser.setUsername("Tom Testermann");
        newUser.setPoints(5);

        long id = userRepository.save(newUser).getId();

        restTemplate.delete("/api/user/" + id);

        Optional<UserEntity> result = userRepository.findById((int) id);

        assertTrue(result.isEmpty());
    }

    @Test
    public void updatePoints_ShouldUpdatePoints() {
        UserEntity newUser = new UserEntity();
        newUser.setUsername("Tom Testermann");
        newUser.setPoints(5);
        int expectedPoints = 10;

        long id = userRepository.save(newUser).getId();

        restTemplate.put("/api/user/updatePoints/" + id + "/" + expectedPoints, URI.class);

        Optional<UserEntity> newUserInDb = userRepository.findById((int)id);

        assertTrue(newUserInDb.isPresent());
        assertThat(newUserInDb.get().getPoints()).isEqualTo(expectedPoints);
    }
}