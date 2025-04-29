package com.josedev.axity_consolidation_back.persistence.repository;

import com.josedev.axity_consolidation_back.persistence.entity.EstadoConciliacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interfaz que define el repositorio para la entidad EstadoConciliacionEntity.
 * Extiende de JpaRepository para heredar métodos CRUD básicos.
 */
@Repository
public interface EstadoConciliacionRepository extends JpaRepository<EstadoConciliacionEntity, String> {

    /**
     * Método para encontrar un estado de conciliación por su descripción
     * @param descripcion La descripción del estado
     * @return El estado de conciliación encontrado, o un Optional vacío si no existe
     */
    Optional<EstadoConciliacionEntity> findByDescripcion(String descripcion);

    /**
     * Método para verificar si existe un estado con la descripción especificada
     * @param descripcion La descripción del estado
     * @return true si existe el estado, false en caso contrario
     */
    boolean existsByDescripcion(String descripcion);

    /**
     * Método para encontrar el estado de conciliación "Descuadrada"
     * @return El estado de conciliación "Descuadrada", o un Optional vacío si no existe
     */
    Optional<EstadoConciliacionEntity> findByCodigoEstado(String codigoEstado);
}