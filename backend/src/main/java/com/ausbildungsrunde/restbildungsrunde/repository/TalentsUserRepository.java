package com.ausbildungsrunde.restbildungsrunde.repository;

import com.ausbildungsrunde.restbildungsrunde.model.TalentsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TalentsUserRepository extends JpaRepository<TalentsUser, Integer> {

    Optional<TalentsUser> findByUsername(String username);

    Boolean existsByUsername(String username);
}
