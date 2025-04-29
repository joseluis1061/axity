package com.josedev.axity_consolidation_back.web.mapper;

import com.josedev.axity_consolidation_back.domain.model.EstadoConciliacion;
import com.josedev.axity_consolidation_back.web.dto.EstadoConciliacionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper para convertir entre EstadoConciliacion (modelo de dominio) y EstadoConciliacionDTO (web).
 */
@Mapper(componentModel = "spring")
public interface EstadoConciliacionDtoMapper {

    /**
     * Convierte un objeto EstadoConciliacion del dominio a un EstadoConciliacionDTO para la capa web
     * @param estadoConciliacion Modelo de dominio
     * @return DTO para la capa web
     */
    @Mapping(source = "codigoEstado", target = "codigoEstado")
    @Mapping(source = "descripcion", target = "descripcion")
    EstadoConciliacionDTO toEstadoConciliacionDto(EstadoConciliacion estadoConciliacion);

    /**
     * Convierte un EstadoConciliacionDTO de la capa web a un objeto EstadoConciliacion del dominio
     * @param estadoConciliacionDto DTO de la capa web
     * @return Modelo de dominio
     */
    @Mapping(source = "codigoEstado", target = "codigoEstado")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(target = "conciliaciones", ignore = true)
    EstadoConciliacion toEstadoConciliacion(EstadoConciliacionDTO estadoConciliacionDto);

    /**
     * Convierte una lista de objetos EstadoConciliacion a una lista de EstadoConciliacionDTO
     * @param estadosConciliacion Lista de modelos de dominio
     * @return Lista de DTOs
     */
    List<EstadoConciliacionDTO> toEstadoConciliacionDtoList(List<EstadoConciliacion> estadosConciliacion);
}