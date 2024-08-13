package org.example.petwif;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //엔티티 객체가 생성되거나 변경될 때, 자동으로 값을 등록하게 함(createAt, updateAt 값 자동 등록)
public class PetwifApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetwifApplication.class, args);
	}

}
