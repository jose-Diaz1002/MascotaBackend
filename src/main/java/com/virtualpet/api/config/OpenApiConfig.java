package com.virtualpet.api.config; // Ajusta el paquete

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Virtual Pet API",
                version = "1.0",
                description = "Documentación de la API para la aplicación Virtual Pet. ",
                contact = @Contact(
                        name = "Jose L Diaz I",
                        email = "jldi1002@hotmail.com"
                )
        ),
        // Esto añade el candado de seguridad a todos los endpoints por defecto
        security = @SecurityRequirement(name = "bearerAuth")
)
// Define cómo se ve el esquema de seguridad (el candado) en Swagger UI
@SecurityScheme(
        name = "bearerAuth", // Nombre que se usa en @SecurityRequirement
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
    // Esta clase no necesita código en el cuerpo,
    // las anotaciones hacen todo el trabajo de configuración.
}
