package com.diana.auditinsightbackendspringboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {

        String uploadPath =
                System.getProperty("user.home")
                        + "/auditinsight-uploads/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(
                        "file:" + uploadPath
                );
    }
}