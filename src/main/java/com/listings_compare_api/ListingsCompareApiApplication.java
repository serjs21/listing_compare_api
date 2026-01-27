package com.listings_compare_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@SpringBootApplication
@ConfigurationPropertiesScan
public class ListingsCompareApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListingsCompareApiApplication.class, args);
	}

}
