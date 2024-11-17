package com.arshad.urlshortener.contract;

import com.arshad.urlshortener.model.ShortenRequest;

import java.util.List;

public interface UrlService {

    String getShortUrl(ShortenRequest request);

    String getLongUrl(String shortUrl);

    List<String> getDomainMetrics();
}
