package com.ausbildungsrunde.restbildungsrunde.controller;

import com.ausbildungsrunde.restbildungsrunde.model.User;
import com.ausbildungsrunde.restbildungsrunde.model.UserEntity;
import com.ausbildungsrunde.restbildungsrunde.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/hello")
    public String hello() {

        return "Hello World!";
    }

    @PostMapping("/user")
    public void createUser(@RequestBody User user) {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        userRepository.save(modelMapper.map(user, UserEntity.class));
    }
}
