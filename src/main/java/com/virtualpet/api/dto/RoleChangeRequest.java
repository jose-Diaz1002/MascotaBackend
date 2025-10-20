package com.virtualpet.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para cambiar el rol de un usuario.")
public class RoleChangeRequest {
    @Schema(description = "El nuevo rol a asignar. Debe ser 'ROLE_USER' o 'ROLE_ADMIN'.",
            example = "ROLE_ADMIN")
    private String newRole;
}