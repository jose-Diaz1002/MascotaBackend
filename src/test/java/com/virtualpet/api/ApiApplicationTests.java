package com.virtualpet.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles; // Importante

@SpringBootTest
@ActiveProfiles("test") // <--- ¡AÑADE O VERIFICA ESTO!
class ApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
