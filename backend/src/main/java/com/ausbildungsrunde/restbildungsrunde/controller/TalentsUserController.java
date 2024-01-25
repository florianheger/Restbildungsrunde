package com.ausbildungsrunde.restbildungsrunde.controller;

import com.ausbildungsrunde.restbildungsrunde.model.TalentsUser;
import com.ausbildungsrunde.restbildungsrunde.repository.TalentsUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class TalentsUserController {
    private final TalentsUserRepository talentsUserRepository;

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody TalentsUser talentsUser, UriComponentsBuilder ucb) {
        TalentsUser newTalentsUser = talentsUserRepository.save(talentsUser);
        URI locationOfUser = ucb.path("api/user/{id}").buildAndExpand(newTalentsUser.getId()).toUri();
        return ResponseEntity.created(locationOfUser).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        talentsUserRepository.deleteById((int)id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updatePoints/{id}/{points}")
    public ResponseEntity<URI> updatePoints(@PathVariable long id, @PathVariable int points, UriComponentsBuilder ucb) {
        Optional<TalentsUser> userOptional = talentsUserRepository.findById((int)id);
        if (userOptional.isPresent()) {
            TalentsUser talentsUser = userOptional.get();
            talentsUser.setPoints(points);
            talentsUserRepository.save(talentsUser);
            URI locationOfUser = ucb.path("api/user/{id}").buildAndExpand(talentsUser.getId()).toUri();
            return ResponseEntity.ok(locationOfUser);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TalentsUser> getUser(@PathVariable long id) {
        Optional<TalentsUser> user = talentsUserRepository.findById((int)id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    private ResponseEntity<List<TalentsUser>> getAllUsers(Pageable pageable) {
        Page<TalentsUser> page = talentsUserRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.DESC, "points"))
                )
        );
        return ResponseEntity.ok(page.getContent());
    }
}
