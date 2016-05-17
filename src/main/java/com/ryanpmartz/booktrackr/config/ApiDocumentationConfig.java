package com.ryanpmartz.booktrackr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class ApiDocumentationConfig {

    /**
     * Configuration for Springfox and Swagger API documentation
     *
     * @return a configured <code>Docket</code> instance to inject
     * into the Spring context
     */
    @Bean
    public Docket apiDocketConfiguration() {
        ApiInfoBuilder apiBuilder = new ApiInfoBuilder();
        apiBuilder.title("Booktrackr API Documentation");

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiBuilder.build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ryanpmartz.booktrackr.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    SecurityConfiguration security() {
        return new SecurityConfiguration(null, null, null, null,
                "JSON Web token",
                ApiKeyVehicle.HEADER,
                "Authorization",
                ",");
    }
}
