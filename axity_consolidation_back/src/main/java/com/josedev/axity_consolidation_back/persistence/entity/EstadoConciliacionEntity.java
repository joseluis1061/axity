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
@Table(name = "estados_conciliacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoConciliacionEntity {

    @Id
    @Column(name = "codigo_estado", length = 2)
    private String codigoEstado;

    @Column(name = "descripcion", nullable = false, length = 50)
    private String descripcion;

    @OneToMany(mappedBy = "estadoConciliacion")
    @ToString.Exclude
    private List<ConciliacionEntity> conciliaciones;
}