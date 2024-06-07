package com.algorhythm.booking_backend.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "First project APIs",
                description = "All APIs of this project",
                contact = @Contact(
                        name = "Marin Bulku"
                ),
                version = "1.0.0"
        ),
        servers = {
                @Server(
                        description = "LOCAL ENV",
                        url = "http://localhost:8081"
                )
        }
)
public class OpenApiConfiguration {
}
