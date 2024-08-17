package org.example.petwif;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Collections;

@SpringBootApplication
@EnableJpaAuditing
public class PetwifApplication {

	public static void main(String[] args) {

		String port = System.getenv("PORT");
		if (port == null) {
			port = "8080"; // 기본 포트 번호
		}

		SpringApplication app = new SpringApplication(PetwifApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", port));
		app.run(args);
	}

}