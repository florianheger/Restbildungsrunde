package com.ausbildungsrunde.restbildungsrunde.controller;

import com.ausbildungsrunde.restbildungsrunde.model.User;
import com.ausbildungsrunde.restbildungsrunde.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void createUser_ShouldCreateUser() {
        User newUser = new User();
        newUser.setUsername("Test Testermann");
        newUser.setPoints(5);

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/user", newUser, Void.class);
        ResponseEntity<UserEntity> newUserEntity = restTemplate.getForEntity(response.getHeaders().getLocation(), UserEntity.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(newUserEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(newUserEntity.getBody().getUsername()).isEqualTo(newUser.getUsername());
        assertThat(newUserEntity.getBody().getPoints()).isEqualTo(newUser.getPoints());
    }
}