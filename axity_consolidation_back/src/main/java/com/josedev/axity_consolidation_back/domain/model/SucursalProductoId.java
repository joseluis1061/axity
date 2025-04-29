package com.josedev.axity_consolidation_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalProductoId implements Serializable {

    private String codigoSucursal;
    private String codigoProducto;
    private String codigoDocumento;
}

    /*
    private String codigoSucursal;
    private String codigoProducto;
    private String codigoDocumento;
*/