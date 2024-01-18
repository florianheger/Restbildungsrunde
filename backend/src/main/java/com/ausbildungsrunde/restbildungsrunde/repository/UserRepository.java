package com.ausbildungsrunde.restbildungsrunde.repository;

import com.ausbildungsrunde.restbildungsrunde.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}
