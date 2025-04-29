package com.josedev.axity_consolidation_back.persistence.mapper;

import com.josedev.axity_consolidation_back.domain.model.Producto;
import com.josedev.axity_consolidation_back.persistence.entity.ProductoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Interfaz que define los métodos de mapeo entre ProductoEntity y Producto.
 * Utiliza MapStruct para la implementación automática de estos métodos.
 */
@Mapper(componentModel = "spring")
public interface ProductoMapper {

    /**
     * Convierte una entidad de producto a un modelo de dominio
     * @param productoEntity La entidad de producto proveniente de la base de datos
     * @return El modelo de dominio producto
     */
    @Mapping(target = "codigoProducto", source = "codigoProducto")
    @Mapping(target = "nombreProducto", source = "nombreProducto")
    Producto toProducto(ProductoEntity productoEntity);

    /**
     * Convierte un modelo de dominio producto a una entidad de producto
     * @param producto El modelo de dominio producto
     * @return La entidad de producto para persistir en base de datos
     */
    @Mapping(target = "codigoProducto", source = "codigoProducto")
    @Mapping(target = "nombreProducto", source = "nombreProducto")
    @Mapping(target = "sucursalProductos", ignore = true)
    ProductoEntity toProductoEntity(Producto producto);

    /**
     * Convierte una lista de entidades producto a una lista de modelos de dominio
     * @param productoEntities Lista de entidades producto
     * @return Lista de modelos de dominio producto
     */
    List<Producto> toProductoList(List<ProductoEntity> productoEntities);
}

/*
    ProductoMapper INSTANCE = Mappers.getMapper(ProductoMapper.class);

    Producto entityToModel(ProductoEntity entity);

    ProductoEntity modelToEntity(Producto model);

    ProductoDTO modelToDto(Producto model);

    Producto dtoToModel(ProductoDTO dto);

    List<Producto> entityListToModelList(List<ProductoEntity> entities);

    List<ProductoDTO> modelListToDtoList(List<Producto> models);
 */