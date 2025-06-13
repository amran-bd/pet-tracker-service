package com.tractive.pet.tracker.swagger;

import com.tractive.pet.tracker.constant.ApiBaseURL;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Md Amran Hossain on 13/6/2025 AD
 * @Project pet-tracker-service
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI petTrackerService(){
        Info info = new Info().title("Pet Tracker API")
                .version("1.0.0")
                .description("Pet Tracker REST API Documentation");
        return new OpenAPI().info(info);
    }

    @Bean
    public GroupedOpenApi petTrackerGroup(){
        String[] apis = new String[]{ApiBaseURL.PET_TRACKER_BASE_URL.concat("/**")};
        return GroupedOpenApi.builder()
                .group("Pet-Tracker-Service")
                .pathsToMatch(apis)
                .packagesToScan("com.tractive.pet.tracker.controller")
                .build();
    }
}
