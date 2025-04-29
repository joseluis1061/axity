package com.josedev.axity_consolidation_back.web.mapper;

import com.josedev.axity_consolidation_back.domain.model.Producto;
import com.josedev.axity_consolidation_back.web.dto.ProductoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper para convertir entre Producto (modelo de dominio) y ProductoDTO (web).
 */
@Mapper(componentModel = "spring")
public interface ProductoDtoMapper {

    /**
     * Convierte un objeto Producto del dominio a un ProductoDTO para la capa web
     * @param producto Modelo de dominio
     * @return DTO para la capa web
     */
    @Mapping(source = "codigoProducto", target = "codigoProducto")
    @Mapping(source = "nombreProducto", target = "nombreProducto")
    ProductoDTO toProductoDto(Producto producto);

    /**
     * Convierte un ProductoDTO de la capa web a un objeto Producto del dominio
     * @param productoDto DTO de la capa web
     * @return Modelo de dominio
     */
    @Mapping(source = "codigoProducto", target = "codigoProducto")
    @Mapping(source = "nombreProducto", target = "nombreProducto")
    Producto toProducto(ProductoDTO productoDto);

    /**
     * Convierte una lista de objetos Producto a una lista de ProductoDTO
     * @param productos Lista de modelos de dominio
     * @return Lista de DTOs
     */
    List<ProductoDTO> toProductoDtoList(List<Producto> productos);
}