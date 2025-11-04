package ru.dan.eduinstitution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.dan.eduinstitution.config.BaseTestWithContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EduinstitutionApplicationTests extends BaseTestWithContext {

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("dummy test")
	void dummyTest() {
		assertThat(true).isTrue();
	}

}
