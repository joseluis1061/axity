package com.josedev.axity_consolidation_back.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ProcesoBatchResponseDTO {
    private int year;
    private int month;
    private int totalProcesados;
    private int totalDescuadrados;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaProceso;

    private List<ConciliacionDTO> conciliacionesDescuadradas;
}