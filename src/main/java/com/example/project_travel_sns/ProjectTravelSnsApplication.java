package com.example.project_travel_sns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProjectTravelSnsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectTravelSnsApplication.class, args);
	}

}
