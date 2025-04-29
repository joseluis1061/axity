package com.josedev.axity_consolidation_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SucursalProducto {

    private String codigoSucursal;
    private String codigoProducto;
    private String codigoDocumento;

    private Sucursal sucursal;
    private Producto producto;
    private Documento documento;

    // En el modelo de dominio podemos decidir si incluir o no la lista de conciliaciones
    // Dependiendo del caso de uso, podríamos querer separar esta relación
    // private List<Conciliacion> conciliaciones;
}

/*
    private SucursalProductoId id;
    private Sucursal sucursal;
    private Producto producto;
    private Documento documento;

    // Relaciones - añadidas para coincidir con la entidad
    private List<Conciliacion> conciliaciones;
 */