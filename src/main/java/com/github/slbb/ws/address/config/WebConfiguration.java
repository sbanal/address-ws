package com.github.slbb.ws.address.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public Docket api() {
        ApiInfo apiInfo =  new ApiInfo(
                "AU Post Address REST API",
                "AU Post Address REST API supports suburb lookup using name or postcode.",
                "API v1",
                "",
                new Contact("Stephen Banal", "http://www.aupost.com.au", "stephen.banal@gmail.com"),
                "License of API", "API license URL",
                Collections.emptyList());
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/address/**"))
                .build()
                .apiInfo(apiInfo);
    }


}
