package com.example.demo.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:5173",
                        "http://localhost:8090",
                        "http://localhost:8090/api/clients/",
                        "http://192.168.100.13:8090",
                        "http://4.201.128.33:5173",
                        "http://4.201.128.33:8070"
                )
                .allowedMethods("GET","POST","PUT","DELETE", "OPTIONS")
                .allowCredentials(true);
    }
}
