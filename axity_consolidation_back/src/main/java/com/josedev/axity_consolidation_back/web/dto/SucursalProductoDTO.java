package com.josedev.axity_consolidation_back.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para la relación SucursalProducto.
 * Define la estructura de los datos que se intercambian con los clientes para las relaciones
 * entre sucursales, productos y documentos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SucursalProductoDTO {

    /**
     * Código de la sucursal asociada a esta relación.
     * No puede estar vacío y debe tener un formato válido.
     */
    @NotBlank(message = "El código de sucursal es obligatorio")
    @Size(min = 2, max = 5, message = "El código de sucursal debe tener entre 2 y 5 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "El código de sucursal debe contener solo números")
    private String codigoSucursal;

    /**
     * Código del producto asociado a esta relación.
     * No puede estar vacío y debe tener un formato válido.
     */
    @NotBlank(message = "El código del producto es obligatorio")
    @Size(min = 2, max = 5, message = "El código del producto debe tener entre 2 y 5 caracteres")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "El código del producto debe contener solo letras mayúsculas y números")
    private String codigoProducto;

    /**
     * Código del documento asociado a esta relación.
     * No puede estar vacío y debe tener un formato válido.
     */
    @NotBlank(message = "El código del documento es obligatorio")
    @Size(min = 3, max = 10, message = "El código del documento debe tener entre 3 y 10 caracteres")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "El código del documento debe contener solo letras mayúsculas y números")
    private String codigoDocumento;

    /**
     * Información completa de la sucursal asociada (opcional en algunos casos de uso).
     */
    private SucursalDTO sucursal;

    /**
     * Información completa del producto asociado (opcional en algunos casos de uso).
     */
    private ProductoDTO producto;

    /**
     * Información completa del documento asociado (opcional en algunos casos de uso).
     */
    private DocumentoDTO documento;
}