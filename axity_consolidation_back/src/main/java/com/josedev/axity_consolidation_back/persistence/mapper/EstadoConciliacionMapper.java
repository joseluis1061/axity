package com.josedev.axity_consolidation_back.persistence.mapper;

import com.josedev.axity_consolidation_back.domain.model.EstadoConciliacion;
import com.josedev.axity_consolidation_back.persistence.entity.EstadoConciliacionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Interfaz que define los métodos de mapeo entre EstadoConciliacionEntity y EstadoConciliacion.
 * Utiliza MapStruct para la implementación automática de estos métodos.
 */
@Mapper(componentModel = "spring")
public interface EstadoConciliacionMapper {

    /**
     * Convierte una entidad de EstadoConciliacion a un modelo de dominio
     * @param entity La entidad de EstadoConciliacion proveniente de la base de datos
     * @return El modelo de dominio EstadoConciliacion
     */
    @Mapping(target = "codigoEstado", source = "codigoEstado")
    @Mapping(target = "descripcion", source = "descripcion")
    // Ignoramos la lista de conciliaciones ya que es una relación bidireccional
    @Mapping(target = "conciliaciones", ignore = true)
    EstadoConciliacion toEstadoConciliacion(EstadoConciliacionEntity entity);

    /**
     * Convierte un modelo de dominio EstadoConciliacion a una entidad de EstadoConciliacion
     * @param model El modelo de dominio EstadoConciliacion
     * @return La entidad de EstadoConciliacion para persistir en base de datos
     */
    @Mapping(target = "codigoEstado", source = "codigoEstado")
    @Mapping(target = "descripcion", source = "descripcion")
    // Ignoramos la lista de conciliaciones para evitar ciclos
    @Mapping(target = "conciliaciones", ignore = true)
    EstadoConciliacionEntity toEstadoConciliacionEntity(EstadoConciliacion model);

    /**
     * Convierte una lista de entidades a una lista de modelos de dominio
     * @param entities Lista de entidades EstadoConciliacion
     * @return Lista de modelos de dominio EstadoConciliacion
     */
    List<EstadoConciliacion> toEstadoConciliacionList(List<EstadoConciliacionEntity> entities);
}