package com.arshad.urlshortener.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class DomainMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String domain;

    private Integer count;
}
