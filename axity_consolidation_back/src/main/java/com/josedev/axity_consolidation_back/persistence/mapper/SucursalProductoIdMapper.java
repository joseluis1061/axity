package com.josedev.axity_consolidation_back.persistence.mapper;

import com.josedev.axity_consolidation_back.domain.model.SucursalProductoId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interfaz que define los métodos de mapeo entre SucursalProductoId de entidad y dominio.
 * Utiliza MapStruct para la implementación automática de estos métodos.
 */
@Mapper(componentModel = "spring")
public interface SucursalProductoIdMapper {

    /**
     * Convierte un id compuesto de entidad a un id compuesto de dominio
     * @param entityId El id compuesto de la entidad
     * @return El id compuesto para el modelo de dominio
     */
    @Mapping(target = "codigoSucursal", source = "codigoSucursal")
    @Mapping(target = "codigoProducto", source = "codigoProducto")
    @Mapping(target = "codigoDocumento", source = "codigoDocumento")
    SucursalProductoId toSucursalProductoId(
            com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoId entityId);

    /**
     * Convierte un id compuesto de dominio a un id compuesto de entidad
     * @param domainId El id compuesto del modelo de dominio
     * @return El id compuesto para la entidad
     */
    @Mapping(target = "codigoSucursal", source = "codigoSucursal")
    @Mapping(target = "codigoProducto", source = "codigoProducto")
    @Mapping(target = "codigoDocumento", source = "codigoDocumento")
    com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoId toSucursalProductoEntityId(
            SucursalProductoId domainId);
}

    /*
    SucursalProductoIdMapper INSTANCE = Mappers.getMapper(SucursalProductoIdMapper.class);

    SucursalProductoId entityToModel(com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoId entity);

    com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoId modelToEntity(SucursalProductoId model);
*/