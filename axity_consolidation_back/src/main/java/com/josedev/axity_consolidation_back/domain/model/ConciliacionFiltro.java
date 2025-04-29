package com.josedev.axity_consolidation_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConciliacionFiltro {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String codigoSucursal;
    private String codigoProducto;
    private String codigoEstado;
}