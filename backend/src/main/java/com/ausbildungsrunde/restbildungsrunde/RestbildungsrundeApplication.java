package com.ausbildungsrunde.restbildungsrunde;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ausbildungsrunde.restbildungsrunde.repository"})
@EntityScan("com.ausbildungsrunde.restbildungsrunde.model")
public class RestbildungsrundeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestbildungsrundeApplication.class, args);
	}

}
