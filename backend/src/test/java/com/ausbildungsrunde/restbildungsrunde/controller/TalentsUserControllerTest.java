package com.ausbildungsrunde.restbildungsrunde.controller;

import com.ausbildungsrunde.restbildungsrunde.model.TalentsUser;
import com.ausbildungsrunde.restbildungsrunde.repository.TalentsUserRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
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
    @DirtiesContext
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
    @DirtiesContext
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
    @DirtiesContext
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
    @DirtiesContext
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