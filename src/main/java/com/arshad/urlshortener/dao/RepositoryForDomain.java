package com.arshad.urlshortener.dao;

import com.arshad.urlshortener.model.DomainMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RepositoryForDomain extends JpaRepository<DomainMetrics, Long> {

    public Optional<DomainMetrics> findByDomain(String domain);

    @Query("SELECT d FROM DomainMetrics d ORDER BY d.count DESC limit :limit")
    public List<DomainMetrics> getTopDomains(int limit);

}
