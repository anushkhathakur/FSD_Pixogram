package com.pixogram.media;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pixogram.media.controller.MediaController;

@SpringBootTest
class MediaApplicationTests {

	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Before(value = "")
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(new MediaController()).build();
	}

	@Test
	public void testHomePage() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string("This is Home page"));
	}
}
