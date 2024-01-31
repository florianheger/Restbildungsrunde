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
import org.springframework.test.annotation.DirtiesContext;
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
    @DirtiesContext
    public void createUser_ShouldCreateUser() {
        ResponseEntity<?> responseSignUp = restTemplate.postForEntity("/api/user/signup",
                new SignupRequest("Tom Testermann", "test"), Void.class);
        ResponseEntity<?> responseSignIn = restTemplate.postForEntity("/api/user/signin",
                new LoginRequest("Tom Testermann", "test"), Void.class);
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
        assertThat(newUserEntity.getBody().getUsername()).isEqualTo("Tom Testermann");
        assertThat(newUserEntity.getBody().getPoints()).isEqualTo(0);
    }

    @Test
    @DirtiesContext
    public void deleteUser_ShouldDeleteUser() {
        ResponseEntity<?> responseSignUp = restTemplate.postForEntity("/api/user/signup",
                new SignupRequest("Tom Testermann", "test"), Void.class);
        ResponseEntity<?> responseSignIn = restTemplate.postForEntity("/api/user/signin",
                new LoginRequest("Tom Testermann", "test"), Void.class);

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
    @DirtiesContext
    public void updateUser_ShouldUpdatePoints() {
        ResponseEntity<?> responseSignUp = restTemplate.postForEntity("/api/user/signup",
                new SignupRequest("Tom Testermann", "test"), Void.class);
        ResponseEntity<?> responseSignIn = restTemplate.postForEntity("/api/user/signin",
                new LoginRequest("Tom Testermann", "test"), Void.class);
        String cookie = responseSignIn.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie );

        Long id = restTemplate.exchange(responseSignUp.getHeaders().getLocation(),
                GET,
                new HttpEntity<>(null, headers),
                TalentsUser.class).getBody().getId();


        int expectedPoints = 10;
        TalentsUser userUpdate = new TalentsUser(id, "Tom", "NeuesPw", expectedPoints, new ArrayList<>());

        restTemplate.exchange("/api/user/" + id,
                PUT,
                new HttpEntity<>(userUpdate, headers),
                Void.class);


        Optional<TalentsUser> newUserInDb = talentsUserRepository.findById(id.intValue());

        assertTrue(newUserInDb.isPresent());
        assertThat(newUserInDb.get().getUsername()).isEqualTo("Tom");
        assertThat(newUserInDb.get().getPoints()).isEqualTo(expectedPoints);

        ResponseEntity<?> responsetestSignIn = restTemplate.postForEntity("/api/user/signin",
                new LoginRequest("Tom", "NeuesPw"), Void.class);
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
    @DirtiesContext
    public void getUser_WhenUserExists_ShouldReturnUser() {
        ResponseEntity<?> responseSignUp = restTemplate.postForEntity("/api/user/signup",
                new SignupRequest("Tom Testermann", "test"), Void.class);
        ResponseEntity<?> responseSignIn = restTemplate.postForEntity("/api/user/signin",
                new LoginRequest("Tom Testermann", "test"), Void.class);
        String cookie = responseSignIn.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie );

        ResponseEntity<TalentsUser> exisitingUser = restTemplate.exchange(responseSignUp.getHeaders().getLocation(),
                GET,
                new HttpEntity<>(null, headers),
                TalentsUser.class);

        assertThat(exisitingUser.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(exisitingUser.getBody());
        assertThat(exisitingUser.getBody().getUsername()).isEqualTo("Tom Testermann");
        assertThat(exisitingUser.getBody().getPoints()).isEqualTo(0);
    }

    @Test
    @DirtiesContext
    public void getUser_WhenUserDoesNotExist_ShouldReturnNoUser() {
        restTemplate.postForEntity("/api/user/signup",
                new SignupRequest("Tom Testermann", "test"), Void.class);
        ResponseEntity<?> responseSignIn = restTemplate.postForEntity("/api/user/signin",
                new LoginRequest("Tom Testermann", "test"), Void.class);
        String cookie = responseSignIn.getHeaders().getFirst("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie );

        ResponseEntity<TalentsUser> notExisitingUser = restTemplate.exchange("/api/user/-1",
                GET,
                new HttpEntity<>(null, headers),
                TalentsUser.class);

        assertThat(notExisitingUser.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /*@Test
    public void getAllUsers_shouldReturnASortedPageOfUsersWithNoParametersAnsUseDefaultValues() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/user", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext docContext = JsonPath.parse(response.getBody());
        int userCount = docContext.read("$.length()");
        assertThat(userCount).isEqualTo(3);

        JSONArray ids = docContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(-100, -200, -300);

        JSONArray usernames = docContext.read("$..username");
        assertThat(usernames).containsExactlyInAnyOrder("TestUser1", "TestUser2", "TestUser3");

        JSONArray points = docContext.read("$..points");
        assertThat(points).containsExactly(70, 20, 10);
    }

    @Test
    public void getAllUsers_shouldReturnSpecificPageOfUser() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/user?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext docContext = JsonPath.parse(response.getBody());
        JSONArray page = docContext.read("$[*]");
        assertThat(page.size()).isEqualTo(1);
    }*/
}