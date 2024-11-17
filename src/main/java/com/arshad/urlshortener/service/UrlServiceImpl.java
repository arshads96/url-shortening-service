package com.arshad.urlshortener.service;

import com.arshad.urlshortener.contract.UrlRepository;
import com.arshad.urlshortener.contract.UrlService;
import com.arshad.urlshortener.exception.ApplicationException;
import com.arshad.urlshortener.exception.InvalidUrlException;
import com.arshad.urlshortener.model.ShortenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UrlServiceImpl implements UrlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlServiceImpl.class);

    private final UrlRepository urlRepository;

    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public String getShortUrl(ShortenRequest request) {
       if(!validateLongURL(request.getLongUrl())){
           throw new InvalidUrlException("Invalid URL : "+request.getLongUrl());
       }
       String shortUrl = generateShortUrl(request.getLongUrl());
       boolean isDataSaved = urlRepository.saveShortUrl(shortUrl, request.getLongUrl());
       if(isDataSaved){
           saveDomainMetrics(request.getLongUrl());
       }
       return shortUrl;
    }

    private String generateShortUrl(String longUrl) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(longUrl.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().substring(0, 6);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error while generating short url", e);
            throw new ApplicationException(e.getMessage());
        }
    }


    private boolean validateLongURL(String longURL) {
        // Regex pattern for validating a URL
        String regex = "^(https?://)?" + // Optional scheme (http or https)
                "([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})" + // Domain name
                "(:\\d{1,5})?" + // Optional port
                "(/[^\\s]*)?$"; // Optional path

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(longURL);

        return matcher.matches();
    }

    private void saveDomainMetrics(String longUrl) {
        try{
            URI uri = new URI(longUrl);
            String host = uri.getHost();

            if(host != null){
                String domain = getDomainNameFromHost(host);
                urlRepository.saveDomainForMetrics(domain);
            }else{
                LOGGER.error("Error while saving domain metrics");
            }
        } catch (URISyntaxException e) {
            LOGGER.error("Error while saving domain metrics", e);
            throw new ApplicationException(e.getMessage());
        }
    }

    private String getDomainNameFromHost(String host) {
        String domain;
        String[] domainParts = host.split("\\.");
        if(domainParts.length == 3 && !domainParts[0].equals("www")) {
            domain = domainParts[0] + "." + domainParts[1];
        }else if(domainParts.length == 2 && !domainParts[0].equals("www")) {
            domain = domainParts[0];
        }else{
            domain = domainParts[1];
        }
        return domain;
    }

    @Override
    public String getLongUrl(String shortUrl) {
        return urlRepository.getLongUrl(shortUrl);
    }

    @Override
    public List<String> getDomainMetrics() {
        Map<String, Integer> domainMetrics = urlRepository.getDomainMetrics();
        return getTopShortenedDomains(domainMetrics);
    }

    private List<String> getTopShortenedDomains(Map<String, Integer> domainMetrics) {
       return  domainMetrics.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .map(entry -> entry.getKey() + " : " + entry.getValue())
                .toList();
    }


}
