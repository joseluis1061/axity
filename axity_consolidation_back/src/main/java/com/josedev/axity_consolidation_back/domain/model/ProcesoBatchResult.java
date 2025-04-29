package com.josedev.axity_consolidation_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcesoBatchResult {
    private int year;
    private int month;
    private int totalProcesados;
    private int totalDescuadrados;
    private LocalDateTime fechaProceso;
    private List<Conciliacion> conciliacionesDescuadradas;
}