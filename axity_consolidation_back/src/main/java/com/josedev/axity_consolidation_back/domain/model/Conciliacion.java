package com.josedev.axity_consolidation_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conciliacion {

    private Long idConciliacion;
    private LocalDate fechaConciliacion;

    // Relación con SucursalProducto
    private SucursalProducto sucursalProducto;

    // También podemos incluir los códigos directamente para facilitar algunas operaciones
    private String codigoSucursal;
    private String codigoProducto;
    private String codigoDocumento;

    private BigDecimal diferenciaFisica;
    private BigDecimal diferenciaValor;

    // Relación con EstadoConciliacion
    private EstadoConciliacion estadoConciliacion;
    private String codigoEstado;

    private LocalDateTime fechaCreacion;
}