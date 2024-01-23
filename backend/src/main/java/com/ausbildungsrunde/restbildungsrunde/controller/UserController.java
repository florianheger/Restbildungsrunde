package com.ausbildungsrunde.restbildungsrunde.controller;

import com.ausbildungsrunde.restbildungsrunde.model.User;
import com.ausbildungsrunde.restbildungsrunde.model.UserEntity;
import com.ausbildungsrunde.restbildungsrunde.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserRepository userRepository;

    @PostMapping("/user")
    public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucb) {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity newUser = userRepository.save(modelMapper.map(user, UserEntity.class));
        URI locationOfUser = ucb.path("api/user/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(locationOfUser).build();
    }

    @DeleteMapping("/user")
    public void deleteUser(@RequestParam long id) {
        userRepository.deleteById((int)id);
    }

    @PutMapping("/user/updatePoints")
    public void updatePoints(@RequestParam long id, @RequestParam int points) {
        Optional<UserEntity> userEntityOptional = userRepository.findById((int)id);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            userEntity.setPoints(points);
            userRepository.save(userEntity);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable long id) {
        Optional<UserEntity> user = userRepository.findById((int)id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }
}
