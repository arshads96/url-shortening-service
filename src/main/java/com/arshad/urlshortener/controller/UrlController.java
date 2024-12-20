package com.arshad.urlshortener.controller;


import com.arshad.urlshortener.api.ShortCodeApi;
import com.arshad.urlshortener.contract.UrlService;
import com.arshad.urlshortener.mapper.RequestMapper;
import com.arshad.urlshortener.model.ShortenPost201Response;
import com.arshad.urlshortener.model.ShortenPostRequest;
import com.arshad.urlshortener.api.ShortenApi;
import com.arshad.urlshortener.model.ShortenRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.util.Optional;

@RestController
public class UrlController implements ShortenApi, ShortCodeApi {

    private final RequestMapper requestMapper;
    private final UrlService urlService;

    public UrlController(RequestMapper requestMapper, UrlService urlService){
        this.requestMapper = requestMapper;
        this.urlService = urlService;
    }

    @Override
    public ResponseEntity<ShortenPost201Response> shortenPost(@Valid @RequestBody ShortenPostRequest shortenPostRequest) {
        ShortenRequest request = requestMapper.mapToShortenRequest(shortenPostRequest);
        String shortUrl = urlService.getShortUrl(request);
        ShortenPost201Response response = new ShortenPost201Response();
        response.setShortUrl("http://localhost:8080/"+shortUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ShortenApi.super.getRequest();
    }

    @Override
    public ResponseEntity<Void> shortCodeGet(String shortCode) {
        String longUrl = urlService.getLongUrl(shortCode);
       return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(longUrl)).build();
    }
}
