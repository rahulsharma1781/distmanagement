package com.tsd.dist.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.tsd.dist.registration.repo")
@EntityScan(basePackages = "org.tsd.sdk.model")
public class DistmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistmanagementApplication.class, args);
	}

}