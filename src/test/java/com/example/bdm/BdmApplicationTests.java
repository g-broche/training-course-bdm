package com.example.bdm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@Import(TestConfig.class)
class BdmApplicationTests {
	@Test
	void contextLoads() {
	}

}
