package com.josedev.axity_consolidation_back.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalProductoId implements Serializable {

    @Column(name = "codigo_sucursal", length = 5)
    private String codigoSucursal;

    @Column(name = "codigo_producto", length = 5)
    private String codigoProducto;

    @Column(name = "codigo_documento", length = 10)
    private String codigoDocumento;
}