package com.arshad.urlshortener.contract;

import com.arshad.urlshortener.model.ShortenRequest;

public interface UrlService {

    String getShortUrl(ShortenRequest request);

    boolean validateLongURL(String longURL);
}
