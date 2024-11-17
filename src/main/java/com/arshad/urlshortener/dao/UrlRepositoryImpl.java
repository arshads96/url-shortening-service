package com.arshad.urlshortener.dao;

import com.arshad.urlshortener.contract.UrlRepository;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UrlRepositoryImpl implements UrlRepository {

    Map<String,String> shortUrlRepository = new HashMap<>();

    @Override
    public void saveShortUrl(String shortUrl, String longUrl) {
       shortUrlRepository.computeIfAbsent(shortUrl, k -> longUrl);
    }
}
