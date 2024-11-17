package com.arshad.urlshortener.mapper;

import com.arshad.urlshortener.model.ShortenPostRequest;
import com.arshad.urlshortener.model.ShortenRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {

    public ShortenRequest mapToShortenRequest(ShortenPostRequest shortenPostRequest){

        ShortenRequest request = new ShortenRequest();
        request.setLongUrl(shortenPostRequest.getLongUrl().toString());
        return request;
    }
}
