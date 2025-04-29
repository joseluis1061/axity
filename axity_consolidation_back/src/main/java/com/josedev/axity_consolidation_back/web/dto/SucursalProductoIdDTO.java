package com.josedev.axity_consolidation_back.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para el identificador compuesto de SucursalProducto.
 * Define la estructura del identificador compuesto que se usa para operaciones
 * de búsqueda, actualización y eliminación de relaciones entre sucursales, productos y documentos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SucursalProductoIdDTO {

    /**
     * Código de la sucursal.
     * No puede estar vacío y debe tener un formato válido.
     */
    @NotBlank(message = "El código de sucursal es obligatorio")
    @Size(min = 2, max = 5, message = "El código de sucursal debe tener entre 2 y 5 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "El código de sucursal debe contener solo números")
    private String codigoSucursal;

    /**
     * Código del producto.
     * No puede estar vacío y debe tener un formato válido.
     */
    @NotBlank(message = "El código del producto es obligatorio")
    @Size(min = 2, max = 5, message = "El código del producto debe tener entre 2 y 5 caracteres")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "El código del producto debe contener solo letras mayúsculas y números")
    private String codigoProducto;

    /**
     * Código del documento.
     * No puede estar vacío y debe tener un formato válido.
     */
    @NotBlank(message = "El código del documento es obligatorio")
    @Size(min = 3, max = 10, message = "El código del documento debe tener entre 3 y 10 caracteres")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "El código del documento debe contener solo letras mayúsculas y números")
    private String codigoDocumento;
}