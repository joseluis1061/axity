package com.josedev.axity_consolidation_back.web.mapper;

import com.josedev.axity_consolidation_back.domain.model.Conciliacion;
import com.josedev.axity_consolidation_back.web.dto.ConciliacionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper para convertir entre Conciliacion (modelo de dominio) y ConciliacionDTO (web).
 */
@Mapper(
        componentModel = "spring",
        uses = {
                SucursalProductoDtoMapper.class,
                EstadoConciliacionDtoMapper.class
        }
)
public interface ConciliacionDtoMapper {

    /**
     * Convierte un objeto Conciliacion del dominio a un ConciliacionDTO para la capa web
     * @param conciliacion Modelo de dominio
     * @return DTO para la capa web
     */
    @Mapping(source = "idConciliacion", target = "idConciliacion")
    @Mapping(source = "fechaConciliacion", target = "fechaConciliacion")
    @Mapping(source = "codigoSucursal", target = "codigoSucursal")
    @Mapping(source = "codigoProducto", target = "codigoProducto")
    @Mapping(source = "codigoDocumento", target = "codigoDocumento")
    @Mapping(source = "diferenciaFisica", target = "diferenciaFisica")
    @Mapping(source = "diferenciaValor", target = "diferenciaValor")
    @Mapping(source = "codigoEstado", target = "codigoEstado")
    @Mapping(source = "estadoConciliacion.descripcion", target = "descripcionEstado")
    @Mapping(source = "fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "sucursalProducto", target = "sucursalProducto")
    @Mapping(source = "estadoConciliacion", target = "estadoConciliacion")
    ConciliacionDTO toConciliacionDto(Conciliacion conciliacion);

    /**
     * Convierte un ConciliacionDTO de la capa web a un objeto Conciliacion del dominio
     * @param conciliacionDto DTO de la capa web
     * @return Modelo de dominio
     */
    @Mapping(source = "idConciliacion", target = "idConciliacion")
    @Mapping(source = "fechaConciliacion", target = "fechaConciliacion")
    @Mapping(source = "codigoSucursal", target = "codigoSucursal")
    @Mapping(source = "codigoProducto", target = "codigoProducto")
    @Mapping(source = "codigoDocumento", target = "codigoDocumento")
    @Mapping(source = "diferenciaFisica", target = "diferenciaFisica")
    @Mapping(source = "diferenciaValor", target = "diferenciaValor")
    @Mapping(source = "codigoEstado", target = "codigoEstado")
    @Mapping(source = "fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "sucursalProducto", target = "sucursalProducto")
    @Mapping(source = "estadoConciliacion", target = "estadoConciliacion")
    Conciliacion toConciliacion(ConciliacionDTO conciliacionDto);

    /**
     * Convierte una lista de objetos Conciliacion a una lista de ConciliacionDTO
     * @param conciliaciones Lista de modelos de dominio
     * @return Lista de DTOs
     */
    List<ConciliacionDTO> toConciliacionDtoList(List<Conciliacion> conciliaciones);
}