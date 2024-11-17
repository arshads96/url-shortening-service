package com.arshad.urlshortener.service;

import com.arshad.urlshortener.contract.UrlService;
import com.arshad.urlshortener.exception.InvalidUrlException;
import com.arshad.urlshortener.model.ShortenRequest;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UrlServiceImpl implements UrlService {

    @Override
    public String getShortUrl(ShortenRequest request) {
       if(!validateLongURL(request.getLongUrl())){
           throw new InvalidUrlException("Invalid URL : "+request.getLongUrl());
       }
        return "ars";
    }
    @Override
    public boolean validateLongURL(String longURL) {
        // Regex pattern for validating a URL
        String regex = "^(https?://)?" + // Optional scheme (http or https)
                "([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})" + // Domain name
                "(:\\d{1,5})?" + // Optional port
                "(/[^\\s]*)?$"; // Optional path

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(longURL);

        return matcher.matches();
    }
}
