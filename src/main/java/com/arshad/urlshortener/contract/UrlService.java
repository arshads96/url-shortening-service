package com.arshad.urlshortener.contract;

import com.arshad.urlshortener.model.ShortenRequest;

public interface UrlService {

    public String getShortUrl(ShortenRequest request);

    public boolean validateLongURL(String longURL);
}
