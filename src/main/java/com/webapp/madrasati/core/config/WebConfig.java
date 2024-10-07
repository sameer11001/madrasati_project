package com.webapp.madrasati.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/app/uploads/");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:/app/static/");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

    }
}
