package com.josedev.axity_consolidation_back.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para la entidad Conciliacion.
 * Define la estructura de los datos que se intercambian con los clientes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConciliacionDTO {

    /**
     * Identificador único de la conciliación.
     */
    private Long idConciliacion;

    /**
     * Fecha de la conciliación.
     * No puede ser nula y debe ser una fecha pasada o presente.
     */
    @NotNull(message = "La fecha de conciliación es obligatoria")
    @PastOrPresent(message = "La fecha de conciliación no puede ser futura")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaConciliacion;

    /**
     * Código de la sucursal asociada a esta conciliación.
     * No puede estar vacío y debe tener un formato válido.
     */
    @NotBlank(message = "El código de sucursal es obligatorio")
    @Size(min = 2, max = 5, message = "El código de sucursal debe tener entre 2 y 5 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "El código de sucursal debe contener solo números")
    private String codigoSucursal;

    /**
     * Código del producto asociado a esta conciliación.
     * No puede estar vacío y debe tener un formato válido.
     */
    @NotBlank(message = "El código del producto es obligatorio")
    @Size(min = 2, max = 5, message = "El código del producto debe tener entre 2 y 5 caracteres")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "El código del producto debe contener solo letras mayúsculas y números")
    private String codigoProducto;

    /**
     * Código del documento asociado a esta conciliación.
     * No puede estar vacío y debe tener un formato válido.
     */
    @NotBlank(message = "El código del documento es obligatorio")
    @Size(min = 3, max = 10, message = "El código del documento debe tener entre 3 y 10 caracteres")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "El código del documento debe contener solo letras mayúsculas y números")
    private String codigoDocumento;

    /**
     * Diferencia física detectada en la conciliación.
     * Puede ser positiva o negativa.
     */
    @NotNull(message = "La diferencia física es obligatoria")
    private BigDecimal diferenciaFisica;

    /**
     * Diferencia de valor detectada en la conciliación.
     * Puede ser positiva o negativa.
     */
    @NotNull(message = "La diferencia de valor es obligatoria")
    private BigDecimal diferenciaValor;

    /**
     * Código del estado de la conciliación.
     * No puede estar vacío y debe ser uno de los valores válidos (A, B, C, D).
     */
    @NotBlank(message = "El código de estado es obligatorio")
    @Size(min = 1, max = 2, message = "El código de estado debe tener entre 1 y 2 caracteres")
    @Pattern(regexp = "^[A-D]$", message = "El código de estado debe ser uno de los siguientes valores: A, B, C, D")
    private String codigoEstado;

    /**
     * Descripción del estado de la conciliación (opcional).
     */
    private String descripcionEstado;

    /**
     * Fecha y hora de creación de la conciliación.
     * Se genera automáticamente en el servidor.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCreacion;

    /**
     * Información completa de la relación sucursal-producto (opcional en algunos casos de uso).
     */
    private SucursalProductoDTO sucursalProducto;

    /**
     * Información completa del estado de conciliación (opcional en algunos casos de uso).
     */
    private EstadoConciliacionDTO estadoConciliacion;
}