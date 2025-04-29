package com.josedev.axity_consolidation_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoConciliacion {

    private String codigoEstado;
    private String descripcion;

    // Esta lista es opcional y podría ignorarse en muchos casos de uso
    private List<Conciliacion> conciliaciones;
}