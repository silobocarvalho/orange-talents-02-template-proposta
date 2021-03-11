package br.com.zup.orange.health;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class HealthControllerTest {

	@Autowired
	MockMvc mockMvc;

	String urlHost = "http://localhost:8080";

	@Test
	@DisplayName("Verify health service")
	void verifyHealthService() throws JsonProcessingException, Exception {

		mockMvc.perform(MockMvcRequestBuilders.get(urlHost + "/health").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	//Create another test to verify when server is down, how to break the system? I dont know yet.
	
}
