package com.arshad.urlshortener.contract;

import java.util.Map;

public interface UrlRepository {

    boolean saveShortUrl(String shortUrl, String longUrl);

    String getLongUrl(String shortUrl);

    void saveDomainForMetrics(String domain);

    Map<String, Integer> getDomainMetrics();
}
