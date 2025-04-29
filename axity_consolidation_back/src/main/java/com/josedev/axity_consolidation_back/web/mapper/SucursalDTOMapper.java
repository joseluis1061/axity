package com.josedev.axity_consolidation_back.web.mapper;

import com.josedev.axity_consolidation_back.domain.model.Sucursal;
import com.josedev.axity_consolidation_back.web.dto.SucursalDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper para convertir entre el modelo de dominio Sucursal y SucursalDTO.
 */
@Mapper(componentModel = "spring")
public interface SucursalDTOMapper {

    // Domain to DTO
    SucursalDTO toSucursalDTO(Sucursal sucursal);
    List<SucursalDTO> toSucursalDTOs(List<Sucursal> sucursales);

    // DTO to Domain
    Sucursal toSucursal(SucursalDTO sucursalDTO);
}