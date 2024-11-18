package com.arshad.urlshortener;

import com.arshad.urlshortener.contract.UrlRepository;
import com.arshad.urlshortener.contract.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ManagementEndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UrlService urlService;

    @MockBean
    UrlRepository urlRepository;


    @Test
    void testManagementEndpoints2() throws Exception {
        Map<String, Integer> mockResponse = new HashMap<>();
        mockResponse.put("google", 10);
        mockResponse.put("yahoo", 5);
        mockResponse.put("hotmail", 2);
        mockResponse.put("msn", 1);

        List<String> mockList = Arrays.asList("google : 10", "yahoo : 5", "hotmail : 2");

        when(urlRepository.getDomainMetrics()).thenReturn(mockResponse);
        when(urlService.getDomainMetrics()).thenReturn(mockList);

        String response = mockMvc.perform(get("/metrics/top-shortened-domains")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assert response.contains("google : 10");
        assert response.contains("yahoo : 5");
        assert response.contains("hotmail : 2");


    }
}
