package com.arshad.urlshortener;


import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UrlShorteningServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testInvalidUrlThrowsBadRequestException() throws Exception {
		String invalidUrl = "https:google/abc/test";

		mockMvc.perform(post("/shorten").contentType("application/json").content("{\"longUrl\":\"" + invalidUrl + "\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testValidUrlReturnsShortUrl() throws Exception {
		String validUrl = "https://google.com";

		mockMvc.perform(post("/shorten").contentType("application/json").content("{\"longUrl\":\"" + validUrl + "\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.shortUrl").exists());
	}

	@Test
	void testRedirectionForValidUrl() throws Exception {
		String validUrl = "https://google.com";

		String responseContent = mockMvc.perform(post("/shorten").contentType("application/json").content("{\"longUrl\":\"" + validUrl + "\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.shortUrl").exists())
				.andReturn().getResponse().getContentAsString();

		String shortUrl = JsonPath.read(responseContent, "$.shortUrl");

		mockMvc.perform(get(shortUrl)).andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", validUrl));
	}

	@Test
	void testFetchUrlWithWrongShortUrlThrowsNotFoundException() throws Exception {
		mockMvc.perform(get("/dd12ee")).andExpect(status().isNotFound());
	}



}
