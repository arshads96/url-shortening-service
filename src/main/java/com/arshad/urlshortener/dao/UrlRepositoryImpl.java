package com.arshad.urlshortener.dao;

import com.arshad.urlshortener.contract.UrlRepository;
import com.arshad.urlshortener.exception.ShortUrlNotFoundException;
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

    @Override
    public String getLongUrl(String shortUrl) {
       if(shortUrlRepository.containsKey(shortUrl)) {
           return shortUrlRepository.get(shortUrl);
       } else {
           throw new ShortUrlNotFoundException("Short url "+shortUrl + " does not exist in database");
       }
    }


}
