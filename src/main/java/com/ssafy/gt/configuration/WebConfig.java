package com.ssafy.gt.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String rootDir = System.getProperty("user.dir");
        String uploadPath = "file:///" + rootDir.replace("\\", "/")
                + "/src/main/resources/static/upload/";

        System.out.println("Mapped Upload Path: " + uploadPath);

        registry.addResourceHandler("/upload/**")
                .addResourceLocations(uploadPath);

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}

