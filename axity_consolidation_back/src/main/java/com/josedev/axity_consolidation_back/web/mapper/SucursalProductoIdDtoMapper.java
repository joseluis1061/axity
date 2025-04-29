package com.josedev.axity_consolidation_back.web.mapper;

import com.josedev.axity_consolidation_back.domain.model.SucursalProductoId;
import com.josedev.axity_consolidation_back.web.dto.SucursalProductoIdDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre SucursalProductoId (modelo de dominio) y SucursalProductoIdDTO (web).
 */
@Mapper(componentModel = "spring")
public interface SucursalProductoIdDtoMapper {

    /**
     * Convierte un objeto SucursalProductoId del dominio a un SucursalProductoIdDTO para la capa web
     * @param id Modelo de dominio del identificador compuesto
     * @return DTO del identificador compuesto para la capa web
     */
    @Mapping(source = "codigoSucursal", target = "codigoSucursal")
    @Mapping(source = "codigoProducto", target = "codigoProducto")
    @Mapping(source = "codigoDocumento", target = "codigoDocumento")
    SucursalProductoIdDTO toSucursalProductoIdDto(SucursalProductoId id);

    /**
     * Convierte un SucursalProductoIdDTO de la capa web a un objeto SucursalProductoId del dominio
     * @param idDto DTO del identificador compuesto de la capa web
     * @return Modelo de dominio del identificador compuesto
     */
    @Mapping(source = "codigoSucursal", target = "codigoSucursal")
    @Mapping(source = "codigoProducto", target = "codigoProducto")
    @Mapping(source = "codigoDocumento", target = "codigoDocumento")
    SucursalProductoId toSucursalProductoId(SucursalProductoIdDTO idDto);
}