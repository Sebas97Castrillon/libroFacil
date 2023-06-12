package com.desarrollo.librofacil;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        String pathRecursos = Paths.get("uploads").toAbsolutePath().toUri().toString();
        log.info("pathRecursos: " + pathRecursos);

        registry.addResourceHandler("/uploads/**").addResourceLocations(pathRecursos);

    }

}
