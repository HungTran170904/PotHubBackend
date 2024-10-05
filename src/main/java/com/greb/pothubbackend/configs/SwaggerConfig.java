package com.greb.pothubbackend.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "PotHub Backend API",
                description = "PotHub API documentation",
                version = "1.0"
        ),
        security = @SecurityRequirement(name = "oauth2_bearer"),
        servers = {
                @Server(
                        url = "${server.servlet.context-path}",
                        description = "Default Server URL"
                )
        }
)
public class SwaggerConfig {
}
