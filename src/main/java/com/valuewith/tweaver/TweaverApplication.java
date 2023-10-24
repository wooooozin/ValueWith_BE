package com.valuewith.tweaver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TweaverApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweaverApplication.class, args);
	}

}
