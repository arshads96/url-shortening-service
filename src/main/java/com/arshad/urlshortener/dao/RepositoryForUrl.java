package com.arshad.urlshortener.dao;


import com.arshad.urlshortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryForUrl extends JpaRepository<Url, Long> {


    public Optional<Url> findByLongUrl(String longUrl);

    public Optional<Url> findByShortUrl(String shortUrl);


}
