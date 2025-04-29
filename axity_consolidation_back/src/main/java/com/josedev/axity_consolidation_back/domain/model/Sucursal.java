package com.josedev.axity_consolidation_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {
    private String codigoSucursal;
    private String nombreSucursal;
}

/*
    // Relaciones - añadidas para coincidir con la entidad
    private List<SucursalProducto> sucursalProductos;
 */