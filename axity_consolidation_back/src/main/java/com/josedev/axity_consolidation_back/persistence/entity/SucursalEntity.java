package com.josedev.axity_consolidation_back.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "sucursales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalEntity {

    @Id
    @Column(name = "codigo_sucursal", length = 5)
    private String codigoSucursal;

    @Column(name = "nombre_sucursal", nullable = false, length = 50)
    private String nombreSucursal;

    @OneToMany(mappedBy = "sucursal")
    @ToString.Exclude
    private List<SucursalProductoEntity> sucursalProductos;
}