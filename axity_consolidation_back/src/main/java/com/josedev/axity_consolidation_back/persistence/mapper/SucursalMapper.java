package com.josedev.axity_consolidation_back.persistence.mapper;

import com.josedev.axity_consolidation_back.domain.model.Sucursal;
import com.josedev.axity_consolidation_back.persistence.entity.SucursalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SucursalMapper {

    @Mapping(source = "codigoSucursal", target = "codigoSucursal")
    @Mapping(source = "nombreSucursal", target = "nombreSucursal")
    Sucursal toSucursal(SucursalEntity entity);

    List<Sucursal> toSucursales(List<SucursalEntity> entities);

    @Mapping(source = "codigoSucursal", target = "codigoSucursal")
    @Mapping(source = "nombreSucursal", target = "nombreSucursal")
    SucursalEntity toSucursalEntity(Sucursal sucursal);
}
/*
    SucursalMapper INSTANCE = Mappers.getMapper(SucursalMapper.class);

    Sucursal entityToModel(SucursalEntity entity);

    SucursalEntity modelToEntity(Sucursal model);

    SucursalDTO modelToDto(Sucursal model);

    Sucursal dtoToModel(SucursalDTO dto);

    List<Sucursal> entityListToModelList(List<SucursalEntity> entities);

    List<SucursalDTO> modelListToDtoList(List<Sucursal> models);
 */