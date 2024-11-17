package com.arshad.urlshortener.controller;

import com.arshad.urlshortener.contract.UrlService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
@Endpoint(id = "top-shortened-domains")
public class SystemManagementController {

    private final UrlService service;

    public SystemManagementController(UrlService service) {
        this.service = service;
    }

   @ReadOperation
    public String getTopShortenedDomain() {
        return service.getDomainMetrics().stream()
                .collect(Collectors.joining(System.lineSeparator()));
    }

}
