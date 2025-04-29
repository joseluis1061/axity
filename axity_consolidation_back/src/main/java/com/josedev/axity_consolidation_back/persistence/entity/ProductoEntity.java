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
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoEntity {

    @Id
    @Column(name = "codigo_producto", length = 5)
    private String codigoProducto;

    @Column(name = "nombre_producto", nullable = false, length = 50)
    private String nombreProducto;

    @OneToMany(mappedBy = "producto")
    @ToString.Exclude
    private List<SucursalProductoEntity> sucursalProductos;
}