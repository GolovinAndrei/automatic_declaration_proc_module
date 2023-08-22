package by.btslogistics.autorelease.web.config;

import by.btslogistics.starter.openapi.springdoc.annotations.EnableSwaggerCustomizer;
import by.btslogistics.starter.openapi.springdoc.annotations.RestSwaggerOpenAPI;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@RestSwaggerOpenAPI
@Profile({"dev", "local"})
@EnableSwaggerCustomizer
@Configuration
public class SwaggerConfig {


    @Bean
    public GroupedOpenApi createApi() {
        return GroupedOpenApi.builder()
                .group("auto-release")
                .packagesToScan("by.btslogistics.autorelease.web")
                .build();
    }
}
