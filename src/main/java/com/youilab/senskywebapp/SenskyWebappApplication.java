package com.youilab.senskywebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

/**
 * This class is for starting the web application, part of SpringMVC
 */

@SpringBootApplication
@Configuration
public class SenskyWebappApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SenskyWebappApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SenskyWebappApplication.class, args);
    }


}
