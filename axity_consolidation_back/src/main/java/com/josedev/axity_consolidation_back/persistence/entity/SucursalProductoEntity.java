package com.josedev.axity_consolidation_back.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "sucursal_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalProductoEntity {

    @EmbeddedId
    private SucursalProductoId id;

    @ManyToOne
    @MapsId("codigoSucursal")
    @JoinColumn(name = "codigo_sucursal")
    private SucursalEntity sucursal;

    @ManyToOne
    @MapsId("codigoProducto")
    @JoinColumn(name = "codigo_producto")
    private ProductoEntity producto;

    @ManyToOne
    @MapsId("codigoDocumento")
    @JoinColumn(name = "codigo_documento")
    private DocumentoEntity documento;

    @OneToMany(mappedBy = "sucursalProducto")
    @ToString.Exclude
    private List<ConciliacionEntity> conciliaciones;
}