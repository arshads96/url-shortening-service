package com.arshad.urlshortener;

import com.arshad.urlshortener.contract.UrlService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ManagementEndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UrlService urlService;


    void testManagementEndpoints() throws Exception {

        List<String> domainMetrics = Arrays.asList("google : 3", "yahoo : 2", "bing : 1");
        Mockito.when(urlService.getDomainMetrics()).thenReturn(domainMetrics);
        String outputMetric = mockMvc.perform(get("http://localhost:8090/metrics/top-shortened-domains")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assert outputMetric.contains("google : 3");
        assert outputMetric.contains("yahoo : 2");
        assert outputMetric.contains("bing : 1");

    }


    void testManagementEndpoints2() throws Exception {
        String url1 = "http://www.google.com/";
        String url2 = "http://www.yahoo.com/";
        String url3 = "http://www.bing.com/";
        String url4 = "http://www.yahoo.in/";
        String url5 = "http://google.in/";
        String url6 = "http://www.google.us/test";

        mockMvc.perform(post("/shorten").contentType("application/json").content("{\"longUrl\":\"" + url1 + "\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/shorten").contentType("application/json").content("{\"longUrl\":\"" + url2 + "\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/shorten").contentType("application/json").content("{\"longUrl\":\"" + url3 + "\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/shorten").contentType("application/json").content("{\"longUrl\":\"" + url4 + "\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/shorten").contentType("application/json").content("{\"longUrl\":\"" + url5 + "\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/shorten").contentType("application/json").content("{\"longUrl\":\"" + url6 + "\"}"))
                .andExpect(status().isCreated());

        String outputMetric =mockMvc.perform(get("http://localhost:8090/metrics/top-shortened-domains")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assert outputMetric.contains("google : 3");
        assert outputMetric.contains("yahoo : 2");
        assert outputMetric.contains("bing : 1");




    }
}
