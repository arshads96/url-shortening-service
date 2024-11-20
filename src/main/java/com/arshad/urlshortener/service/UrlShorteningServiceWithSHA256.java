package com.arshad.urlshortener.service;

import com.arshad.urlshortener.contract.UrlService;
import com.arshad.urlshortener.dao.RepositoryForDomain;
import com.arshad.urlshortener.dao.RepositoryForUrl;
import com.arshad.urlshortener.exception.ApplicationException;
import com.arshad.urlshortener.exception.InvalidUrlException;
import com.arshad.urlshortener.exception.ShortUrlNotFoundException;
import com.arshad.urlshortener.model.DomainMetrics;
import com.arshad.urlshortener.model.ShortenRequest;
import com.arshad.urlshortener.model.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Primary
@Service
public class UrlShorteningServiceWithSHA256 implements UrlService {

    private final RepositoryForUrl repositoryForUrl;

    private final RepositoryForDomain repositoryForDomain;

    public UrlShorteningServiceWithSHA256(RepositoryForUrl repositoryForUrl, RepositoryForDomain repositoryForDomainMetrics) {
        this.repositoryForUrl = repositoryForUrl;
        this.repositoryForDomain = repositoryForDomainMetrics;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlShorteningServiceWithSHA256.class);

    @Override
    public String getShortUrl(ShortenRequest request) {
        String longUrl = request.getLongUrl();
        if(!validateLongURL(longUrl)){
            throw new InvalidUrlException("Invalid URL : "+longUrl);
        }
        Optional<Url> existingRecord = repositoryForUrl.findByLongUrl(longUrl);
        if(existingRecord.isEmpty()){
            String shortUrl = generateShortUrl(longUrl);
            Url newRecord = new Url(shortUrl, longUrl);
            repositoryForUrl.save(newRecord);
            saveDomainMetrics(longUrl);
            return shortUrl;
        }else{
            return existingRecord.get().getShortUrl();
        }

    }


    /**
     * Validate a given long URL against a regex pattern.
     * The pattern will allow for the following:
     * - An optional scheme (http or https)
     * - A required domain name
     * - An optional port number
     * - An optional path
     *
     * @param longURL The URL to be validated.
     *
     * @return true if the URL is valid, false otherwise.
     */
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

    private String generateShortUrl(String longUrl) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(longUrl.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.substring(0,6);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error while generating short url", e);
            throw new ApplicationException(e.getMessage());
        }
    }

    @Override
    public String getLongUrl(String shortUrl) {
        Optional<Url> url = repositoryForUrl.findByShortUrl(shortUrl);
        if(url.isEmpty()){
            throw new ShortUrlNotFoundException("Short url "+shortUrl + " does not exist in database");
        }else{
            return url.get().getLongUrl();
        }
    }

    private void saveDomainMetrics(String longUrl) {
        try{
            URI uri = new URI(longUrl);
            String host = uri.getHost();
            if(host != null) {
                String domain = getDomainNameFromHost(host);
                Optional<DomainMetrics> existingRecord = repositoryForDomain.findByDomain(domain);
                if (existingRecord.isPresent()) {
                    DomainMetrics domainEntry = existingRecord.get();
                    domainEntry.setCount(domainEntry.getCount() + 1);
                    repositoryForDomain.save(domainEntry);
                } else {
                    DomainMetrics domainEntry = new DomainMetrics();
                    domainEntry.setDomain(domain);
                    domainEntry.setCount(1);
                    repositoryForDomain.save(domainEntry);
                }
            }else{
                LOGGER.error("Error while saving domain metrics");
            }
        } catch (URISyntaxException e) {
            LOGGER.error("Error while saving domain metrics", e);
            throw new ApplicationException(e.getMessage());
        }
    }


    /**
     * This method takes a host string and returns the domain name.
     * A domain name is a string which is the first part of a host string
     * up to the first dot (.) or the second last part of a host string
     * if the host string has three parts and the first part is not "www".
     * For example, if the host string is "example.com", the domain name
     * is "example". If the host string is "sub.example.com", the domain
     * name is "sub.example". If the host string is "www.example.com",
     * the domain name is "example".
     * @param host The host string
     * @return The domain name
     */

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
    public List<String> getDomainMetrics() {
       List<DomainMetrics> domainMetrics = repositoryForDomain.getTopDomains(3);
        return domainMetrics.stream().sorted(Comparator.comparing(DomainMetrics::getCount, Comparator.reverseOrder()))
                .map(domainMetrics1 -> domainMetrics1.getDomain() + " : " + domainMetrics1.getCount()).toList();

    }
}
