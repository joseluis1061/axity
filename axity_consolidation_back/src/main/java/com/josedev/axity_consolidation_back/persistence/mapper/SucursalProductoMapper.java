package com.josedev.axity_consolidation_back.persistence.mapper;

import com.josedev.axity_consolidation_back.domain.model.SucursalProducto;
import com.josedev.axity_consolidation_back.domain.model.SucursalProductoId;
import com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Interfaz que define los métodos de mapeo entre SucursalProductoEntity y SucursalProducto.
 * Utiliza MapStruct para la implementación automática de estos métodos.
 */
@Mapper(
        componentModel = "spring",
        uses = {
                SucursalMapper.class,
                ProductoMapper.class,
                DocumentoMapper.class,
                SucursalProductoIdMapper.class
        }
)
public interface SucursalProductoMapper {

    /**
     * Convierte una entidad de SucursalProducto a un modelo de dominio
     * @param entity La entidad de SucursalProducto proveniente de la base de datos
     * @return El modelo de dominio SucursalProducto
     */
    @Mapping(target = "codigoSucursal", source = "id.codigoSucursal")
    @Mapping(target = "codigoProducto", source = "id.codigoProducto")
    @Mapping(target = "codigoDocumento", source = "id.codigoDocumento")
    @Mapping(target = "sucursal", source = "sucursal")
    @Mapping(target = "producto", source = "producto")
    @Mapping(target = "documento", source = "documento")
    SucursalProducto toSucursalProducto(SucursalProductoEntity entity);

    /**
     * Convierte un modelo de dominio SucursalProducto a una entidad de SucursalProducto
     * @param model El modelo de dominio SucursalProducto
     * @return La entidad de SucursalProducto para persistir en base de datos
     */
    @Mapping(target = "id.codigoSucursal", source = "codigoSucursal")
    @Mapping(target = "id.codigoProducto", source = "codigoProducto")
    @Mapping(target = "id.codigoDocumento", source = "codigoDocumento")
    @Mapping(target = "sucursal", source = "sucursal")
    @Mapping(target = "producto", source = "producto")
    @Mapping(target = "documento", source = "documento")
    @Mapping(target = "conciliaciones", ignore = true)
    SucursalProductoEntity toSucursalProductoEntity(SucursalProducto model);

    // Los métodos para mapeo de IDs se delegan a SucursalProductoIdMapper

    /**
     * Convierte una lista de entidades a una lista de modelos de dominio
     * @param entities Lista de entidades SucursalProducto
     * @return Lista de modelos de dominio SucursalProducto
     */
    List<SucursalProducto> toSucursalProductoList(List<SucursalProductoEntity> entities);
}

    /*
    SucursalProductoMapper INSTANCE = Mappers.getMapper(SucursalProductoMapper.class);

    SucursalProducto entityToModel(SucursalProductoEntity entity);

    SucursalProductoEntity modelToEntity(SucursalProducto model);

    List<SucursalProducto> entityListToModelList(List<SucursalProductoEntity> entities);
*/