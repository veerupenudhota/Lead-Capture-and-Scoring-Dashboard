package com.pvb.springboot.veeru.lead_capture.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // >>> IMPORTANT: Make sure http://localhost:3000 is here <<<
                .allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://localhost:3002") // Add or confirm 3000 is present
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}