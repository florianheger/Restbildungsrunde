package com.ausbildungsrunde.restbildungsrunde.controller;

import com.ausbildungsrunde.restbildungsrunde.model.TalentsUser;
import com.ausbildungsrunde.restbildungsrunde.payload.request.LoginRequest;
import com.ausbildungsrunde.restbildungsrunde.payload.request.SignupRequest;
import com.ausbildungsrunde.restbildungsrunde.repository.TalentsUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TalentsUserControllerTest {
    @Autowired
    private TalentsUserRepository talentsUserRepository;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void createUser_ShouldCreateUser() {
        ResponseEntity<?> responseSignUp = restTemplate.postForEntity("/api/user/signup",
                new SignupRequest("Create User", "test"), Void.class);
        ResponseEntity<?> responseSignIn = restTemplate.postForEntity("/api/user/signin",
                new LoginRequest("Create User", "test"), Void.class);
        String cookie = responseSignIn.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie );

        ResponseEntity<TalentsUser> newUserEntity = restTemplate.exchange(responseSignUp.getHeaders().getLocation(),
                GET,
                new HttpEntity<>(null, headers),
                TalentsUser.class);

        assertThat(responseSignUp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(newUserEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(newUserEntity.getBody());
        assertThat(newUserEntity.getBody().getUsername()).isEqualTo("Create User");
        assertThat(newUserEntity.getBody().getPoints()).isEqualTo(0);
    }

    @Test
    public void deleteUser_ShouldDeleteUser() {
        ResponseEntity<?> responseSignUp = restTemplate.postForEntity("/api/user/signup",
                new SignupRequest("Delete User", "test"), Void.class);
        ResponseEntity<?> responseSignIn = restTemplate.postForEntity("/api/user/signin",
                new LoginRequest("Delete User", "test"), Void.class);

        String cookie = responseSignIn.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie );

        Long id = restTemplate.exchange(responseSignUp.getHeaders().getLocation(),
                GET,
                new HttpEntity<>(null, headers),
                TalentsUser.class).getBody().getId();

        restTemplate.exchange(responseSignUp.getHeaders().getLocation(),
                DELETE,
                new HttpEntity<>(null, headers),
                Void.class);

        talentsUserRepository.findById(id.intValue()).ifPresentOrElse(
                talentsUser -> fail("User should not exist anymore"),
                () -> assertTrue(true)
        );
    }

    @Test
    public void updateUser_ShouldUpdatePoints() {
        ResponseEntity<?> responseSignUp = restTemplate.postForEntity("/api/user/signup",
                new SignupRequest("Update User", "test"), Void.class);
        ResponseEntity<?> responseSignIn = restTemplate.postForEntity("/api/user/signin",
                new LoginRequest("Update User", "test"), Void.class);
        String cookie = responseSignIn.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie );

        Long id = restTemplate.exchange(responseSignUp.getHeaders().getLocation(),
                GET,
                new HttpEntity<>(null, headers),
                TalentsUser.class).getBody().getId();


        int expectedPoints = 10;
        TalentsUser userUpdate = new TalentsUser(id, "Updated User", "NeuesPw", expectedPoints, new ArrayList<>());

        restTemplate.exchange("/api/user/" + id,
                PUT,
                new HttpEntity<>(userUpdate, headers),
                Void.class);


        Optional<TalentsUser> newUserInDb = talentsUserRepository.findById(id.intValue());

        assertTrue(newUserInDb.isPresent());
        assertThat(newUserInDb.get().getUsername()).isEqualTo("Updated User");
        assertThat(newUserInDb.get().getPoints()).isEqualTo(expectedPoints);

        ResponseEntity<?> responsetestSignIn = restTemplate.postForEntity("/api/user/signin",
                new LoginRequest("Updated User", "NeuesPw"), Void.class);
        String cookieSignIn = responsetestSignIn.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headersSignIn = new HttpHeaders();
        headersSignIn.add("Cookie", cookieSignIn );

        ResponseEntity<TalentsUser> newUserEntity = restTemplate.exchange(responseSignUp.getHeaders().getLocation(),
                GET,
                new HttpEntity<>(null, headersSignIn),
                TalentsUser.class);

        assertThat(newUserEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getUser_WhenUserExists_ShouldReturnUser() {
        ResponseEntity<?> responseSignUp = restTemplate.postForEntity("/api/user/signup",
                new SignupRequest("Exisitng User", "test"), Void.class);
        ResponseEntity<?> responseSignIn = restTemplate.postForEntity("/api/user/signin",
                new LoginRequest("Exisitng User", "test"), Void.class);
        String cookie = responseSignIn.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie );

        ResponseEntity<TalentsUser> exisitingUser = restTemplate.exchange(responseSignUp.getHeaders().getLocation(),
                GET,
                new HttpEntity<>(null, headers),
                TalentsUser.class);

        assertThat(exisitingUser.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(exisitingUser.getBody());
        assertThat(exisitingUser.getBody().getUsername()).isEqualTo("Exisitng User");
        assertThat(exisitingUser.getBody().getPoints()).isEqualTo(0);
    }

    @Test
    public void getUser_WhenUserDoesNotExist_ShouldReturnNoUser() {
        restTemplate.postForEntity("/api/user/signup",
                new SignupRequest("No User", "test"), Void.class);
        ResponseEntity<?> responseSignIn = restTemplate.postForEntity("/api/user/signin",
                new LoginRequest("No User", "test"), Void.class);
        String cookie = responseSignIn.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie );

        ResponseEntity<TalentsUser> notExisitingUser = restTemplate.exchange("/api/user/-2",
                GET,
                new HttpEntity<>(null, headers),
                TalentsUser.class);

        assertThat(notExisitingUser.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}