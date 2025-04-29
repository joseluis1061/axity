package com.josedev.axity_consolidation_back.web.mapper;

import com.josedev.axity_consolidation_back.domain.model.SucursalProducto;
import com.josedev.axity_consolidation_back.web.dto.SucursalProductoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper para convertir entre SucursalProducto (modelo de dominio) y SucursalProductoDTO (web).
 */
@Mapper(
        componentModel = "spring",
        uses = {
                SucursalDTOMapper.class,
                ProductoDtoMapper.class,
                DocumentoDTOMapper.class
        }
)
public interface SucursalProductoDtoMapper {

    /**
     * Convierte un objeto SucursalProducto del dominio a un SucursalProductoDTO para la capa web
     * @param sucursalProducto Modelo de dominio
     * @return DTO para la capa web
     */
    @Mapping(source = "codigoSucursal", target = "codigoSucursal")
    @Mapping(source = "codigoProducto", target = "codigoProducto")
    @Mapping(source = "codigoDocumento", target = "codigoDocumento")
    @Mapping(source = "sucursal", target = "sucursal")
    @Mapping(source = "producto", target = "producto")
    @Mapping(source = "documento", target = "documento")
    SucursalProductoDTO toSucursalProductoDto(SucursalProducto sucursalProducto);

    /**
     * Convierte un SucursalProductoDTO de la capa web a un objeto SucursalProducto del dominio
     * @param sucursalProductoDto DTO de la capa web
     * @return Modelo de dominio
     */
    @Mapping(source = "codigoSucursal", target = "codigoSucursal")
    @Mapping(source = "codigoProducto", target = "codigoProducto")
    @Mapping(source = "codigoDocumento", target = "codigoDocumento")
    @Mapping(source = "sucursal", target = "sucursal")
    @Mapping(source = "producto", target = "producto")
    @Mapping(source = "documento", target = "documento")
    SucursalProducto toSucursalProducto(SucursalProductoDTO sucursalProductoDto);

    /**
     * Convierte una lista de objetos SucursalProducto a una lista de SucursalProductoDTO
     * @param sucursalProductos Lista de modelos de dominio
     * @return Lista de DTOs
     */
    List<SucursalProductoDTO> toSucursalProductoDtoList(List<SucursalProducto> sucursalProductos);
}