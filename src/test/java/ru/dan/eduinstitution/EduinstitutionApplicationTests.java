package ru.dan.eduinstitution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.dan.eduinstitution.config.BaseTestWithContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EduinstitutionApplicationTests extends BaseTestWithContext {

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("dummy test")
	void dummyTest() {
		System.out.println(restTemplate.getRootUri());
		ResponseEntity<String> response =
				restTemplate.getForEntity(restTemplate.getRootUri() + "/hi", String.class);
		System.out.println(response.getBody());
		assertEquals("hi", response.getBody());
		assertThat(true).isTrue();
	}

}
