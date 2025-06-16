package com.example.petmanagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@OpenAPIDefinition(info = @Info(title = "Pet Management API", description = "API for managing pets", version = "1.0.0"),
        servers = {@Server(url = "/", description = "Default Server URL")})
public class OpenAPIConfiguration {

}