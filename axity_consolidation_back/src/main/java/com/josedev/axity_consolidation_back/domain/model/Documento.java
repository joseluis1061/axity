package com.josedev.axity_consolidation_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Documento {

    private String codigoDocumento;
    private String descripcion;
}

/*
// Relaciones - añadidas para coincidir con la entidad
    private List<SucursalProducto> sucursalProductos;
 */