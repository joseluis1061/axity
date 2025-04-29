package com.josedev.axity_consolidation_back.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para la entidad Documento.
 * Define la estructura de los datos que se intercambian con los clientes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoDTO {

    /**
     * Código único del documento.
     * No puede estar vacío y debe tener un formato válido.
     */
    @NotBlank(message = "El código del documento es obligatorio")
    @Size(min = 3, max = 10, message = "El código del documento debe tener entre 3 y 10 caracteres")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "El código del documento debe contener solo letras mayúsculas y números")
    private String codigoDocumento;

    /**
     * Descripción del documento.
     * No puede estar vacía.
     */
    @NotBlank(message = "La descripción del documento es obligatoria")
    @Size(max = 100, message = "La descripción del documento no puede exceder los 100 caracteres")
    private String descripcion;
}
 /*   private String codigoDocumento;
    private String descripcion;
*/