package com.josedev.axity_consolidation_back.persistence.mapper;

import com.josedev.axity_consolidation_back.domain.model.Conciliacion;
import com.josedev.axity_consolidation_back.persistence.entity.ConciliacionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Interfaz que define los métodos de mapeo entre ConciliacionEntity y Conciliacion.
 * Utiliza MapStruct para la implementación automática de estos métodos.
 */
@Mapper(
        componentModel = "spring",
        uses = {
                SucursalProductoMapper.class,
                EstadoConciliacionMapper.class
        }
)
public interface ConciliacionMapper {

    /**
     * Convierte una entidad de Conciliacion a un modelo de dominio
     * @param entity La entidad de Conciliacion proveniente de la base de datos
     * @return El modelo de dominio Conciliacion
     */
    @Mapping(target = "sucursalProducto", source = "sucursalProducto")
    @Mapping(target = "codigoSucursal", source = "sucursalProducto.id.codigoSucursal")
    @Mapping(target = "codigoProducto", source = "sucursalProducto.id.codigoProducto")
    @Mapping(target = "codigoDocumento", source = "sucursalProducto.id.codigoDocumento")
    @Mapping(target = "estadoConciliacion", source = "estadoConciliacion")
    @Mapping(target = "codigoEstado", source = "estadoConciliacion.codigoEstado")
    Conciliacion toConciliacion(ConciliacionEntity entity);

    /**
     * Convierte un modelo de dominio Conciliacion a una entidad de Conciliacion
     * @param model El modelo de dominio Conciliacion
     * @return La entidad de Conciliacion para persistir en base de datos
     */
    @Mapping(target = "sucursalProducto", source = "sucursalProducto")
    @Mapping(target = "estadoConciliacion", source = "estadoConciliacion")
    ConciliacionEntity toConciliacionEntity(Conciliacion model);

    /**
     * Convierte una lista de entidades a una lista de modelos de dominio
     * @param entities Lista de entidades Conciliacion
     * @return Lista de modelos de dominio Conciliacion
     */
    List<Conciliacion> toConciliacionList(List<ConciliacionEntity> entities);
}