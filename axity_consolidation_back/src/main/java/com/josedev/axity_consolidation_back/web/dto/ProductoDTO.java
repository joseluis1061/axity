package com.josedev.axity_consolidation_back.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para la entidad Producto.
 * Define la estructura de los datos que se intercambian con los clientes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    /**
     * Código único del producto.
     * No puede estar vacío y debe tener un formato válido.
     */
    @NotBlank(message = "El código del producto es obligatorio")
    @Size(min = 2, max = 5, message = "El código del producto debe tener entre 2 y 5 caracteres")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "El código del producto debe contener solo letras mayúsculas y números")
    private String codigoProducto;

    /**
     * Nombre descriptivo del producto.
     * No puede estar vacío.
     */
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 50, message = "El nombre del producto no puede exceder los 50 caracteres")
    private String nombreProducto;
}