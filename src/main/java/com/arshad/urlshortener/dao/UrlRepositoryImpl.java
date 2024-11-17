package com.arshad.urlshortener.dao;

import com.arshad.urlshortener.contract.UrlRepository;
import com.arshad.urlshortener.exception.ShortUrlNotFoundException;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class UrlRepositoryImpl implements UrlRepository {

    Map<String,String> shortUrlRepository = new HashMap<>();
    Map<String, Integer> domainMetrics = new TreeMap<>();

    @Override
    public boolean saveShortUrl(String shortUrl, String longUrl) {
        boolean dataSaved= false;
        if(!shortUrlRepository.containsKey(shortUrl)) {
            shortUrlRepository.put(shortUrl, longUrl);
            dataSaved = true;
        }
       return dataSaved;
    }

    @Override
    public String getLongUrl(String shortUrl) {
       if(shortUrlRepository.containsKey(shortUrl)) {
           return shortUrlRepository.get(shortUrl);
       } else {
           throw new ShortUrlNotFoundException("Short url "+shortUrl + " does not exist in database");
       }
    }

    @Override
    public void saveDomainForMetrics(String domain) {
        if(domainMetrics.containsKey(domain)) {
            domainMetrics.put(domain, domainMetrics.get(domain) + 1);
        } else {
            domainMetrics.put(domain, 1);
        }
    }

    public Map<String, Integer> getDomainMetrics() {
        return domainMetrics;
    }


}
