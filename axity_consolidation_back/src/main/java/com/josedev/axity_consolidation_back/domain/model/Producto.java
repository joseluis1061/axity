package com.josedev.axity_consolidation_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase de dominio que representa un producto bancario.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    /**
     * Código único que identifica al producto
     */
    private String codigoProducto;

    /**
     * Nombre descriptivo del producto bancario
     */
    private String nombreProducto;
}

/*
    private String codigoProducto;
    private String nombreProducto;

    // Relaciones - añadidas para coincidir con la entidad
    private List<SucursalProducto> sucursalProductos;
 */