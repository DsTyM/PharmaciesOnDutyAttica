package com.dstym.pharmaciesondutyattica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
//Test
@SpringBootApplication
@EnableSwagger2
public class PharmaciesOnDutyAtticaApplication {
    public static void main(String[] args) {
        SpringApplication.run(PharmaciesOnDutyAtticaApplication.class, args);
    }

    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("com.dstym"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Pharmacies On Duty Attica API",
                "Getting the available pharmacies on duty in Attica, Greece.",
                "1.0",
                "",
                new Contact("", "", ""),
                "",
                "",
                Collections.emptyList());
    }
}
