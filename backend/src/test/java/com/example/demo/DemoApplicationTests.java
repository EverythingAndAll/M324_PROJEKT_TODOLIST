package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() {
		assertTrue(true, "alles gut");
	}

	@Test
	public void testExistingGetEndpoint() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk());
	}

	@Test
	public void testTaskCreationDate() {
		Task task = new Task();
		task.setTaskdescription("Test Task");
		org.junit.jupiter.api.Assertions.assertNotNull(task.getCreationDate(), "Creation date should not be null upon instantiation");
		org.junit.jupiter.api.Assertions.assertFalse(task.getCreationDate().isEmpty(), "Creation date should not be empty");
	}

	@Test
	public void testClearEndpoint() throws Exception {
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/tasks")
				.content("{\"taskdescription\":\"Task to clear\"}")
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/clear"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$").isEmpty());
	}

}
