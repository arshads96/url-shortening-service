package com.arshad.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.arshad.urlshortener")
public class UrlShorteningServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlShorteningServiceApplication.class, args);
	}

}
