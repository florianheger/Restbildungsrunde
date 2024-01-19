package com.ausbildungsrunde.restbildungsrunde.controller;

import com.ausbildungsrunde.restbildungsrunde.model.User;
import com.ausbildungsrunde.restbildungsrunde.model.UserEntity;
import com.ausbildungsrunde.restbildungsrunde.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserRepository userRepository;

    @PostMapping("/user")
    public void createUser(@RequestBody User user) {
        ModelMapper modelMapper = new ModelMapper();
        userRepository.save(modelMapper.map(user, UserEntity.class));
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
}
