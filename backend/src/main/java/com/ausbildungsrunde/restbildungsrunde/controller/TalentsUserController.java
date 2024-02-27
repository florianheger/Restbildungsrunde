package com.ausbildungsrunde.restbildungsrunde.controller;

import com.ausbildungsrunde.restbildungsrunde.model.TalentsUser;
import com.ausbildungsrunde.restbildungsrunde.payload.request.LoginRequest;
import com.ausbildungsrunde.restbildungsrunde.payload.request.SignupRequest;
import com.ausbildungsrunde.restbildungsrunde.payload.response.MessageResponse;
import com.ausbildungsrunde.restbildungsrunde.payload.response.UserInfoResponse;
import com.ausbildungsrunde.restbildungsrunde.repository.TalentsUserRepository;
import com.ausbildungsrunde.restbildungsrunde.security.jwt.JwtUtils;
import com.ausbildungsrunde.restbildungsrunde.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TalentsUserController {
    private final AuthenticationManager authenticationManager;
    private final TalentsUserRepository talentsUserRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        return ResponseEntity.ok().body(new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), jwtCookie.getValue()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, UriComponentsBuilder ucb) {
        if (talentsUserRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        TalentsUser user = new TalentsUser(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));
        TalentsUser newTalentsUser = talentsUserRepository.save(user);
        URI locationOfUser = ucb.path("api/user/{id}").buildAndExpand(newTalentsUser.getId()).toUri();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signUpRequest.getUsername(),
                        signUpRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        return ResponseEntity.created(locationOfUser).header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).build();
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        Long userId = getID();

        if (userId == null || userId != id) {
            return ResponseEntity.notFound().build();
        }
        talentsUserRepository.deleteById((int)id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<URI> updateUser(@RequestBody TalentsUser userUpdate, @PathVariable long id,  UriComponentsBuilder ucb) {
        Long userId = getID();
        Optional<TalentsUser> userOptional = talentsUserRepository.findById((int)id);
        if (userOptional.isPresent() && userId == id) {
            TalentsUser updatedUser = new TalentsUser(id, userUpdate.getUsername(), encoder.encode(userUpdate.getPassword()), userUpdate.getPoints(), userUpdate.getExercises());
            talentsUserRepository.save(updatedUser);
            URI locationOfUser = ucb.path("api/user/{id}").buildAndExpand(id).toUri();
            return ResponseEntity.ok(locationOfUser);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TalentsUser> getUser(@PathVariable long id) {
        Optional<TalentsUser> user = talentsUserRepository.findById((int)id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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

    public static Long getID() {
        Long id = null;
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            id = authentication.getPrincipal() instanceof UserDetailsImpl ?
                    ((UserDetailsImpl) authentication.getPrincipal()).getId() : null;
        }
        return id;
    }
}
