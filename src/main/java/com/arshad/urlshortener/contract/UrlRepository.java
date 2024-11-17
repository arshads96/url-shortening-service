package com.arshad.urlshortener.contract;

public interface UrlRepository {

    void saveShortUrl(String shortUrl, String longUrl);
}
