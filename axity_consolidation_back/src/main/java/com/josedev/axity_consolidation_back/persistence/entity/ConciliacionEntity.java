package com.josedev.axity_consolidation_back.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "conciliaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConciliacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conciliacion")
    private Long idConciliacion;

    @Column(name = "fecha_conciliacion", nullable = false)
    private LocalDate fechaConciliacion;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "codigo_sucursal", referencedColumnName = "codigo_sucursal"),
            @JoinColumn(name = "codigo_producto", referencedColumnName = "codigo_producto"),
            @JoinColumn(name = "codigo_documento", referencedColumnName = "codigo_documento")
    })
    @ToString.Exclude
    private SucursalProductoEntity sucursalProducto;

    @Column(name = "diferencia_fisica", nullable = false, precision = 12, scale = 2)
    private BigDecimal diferenciaFisica;

    @Column(name = "diferencia_valor", nullable = false, precision = 20, scale = 2)
    private BigDecimal diferenciaValor;

    @ManyToOne
    @JoinColumn(name = "codigo_estado", nullable = false)
    private EstadoConciliacionEntity estadoConciliacion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}