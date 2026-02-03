package com.listings_compare_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/api/**")
        // .allowedOrigins(
        //     "chrome-extension://nfbedmkdndpdobpafdgiongfiamlahch",
        //     "http://localhost:3000"
        // )
        .allowedOrigins("*")
        .allowedMethods("GET", "POST", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(false);
  }
}
