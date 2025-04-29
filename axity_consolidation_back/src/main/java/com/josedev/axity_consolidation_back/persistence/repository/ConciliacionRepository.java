package com.josedev.axity_consolidation_back.persistence.repository;

import com.josedev.axity_consolidation_back.persistence.entity.ConciliacionEntity;
import com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoEntity;
import com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define el repositorio para la entidad ConciliacionEntity.
 * Extiende de JpaRepository para heredar métodos CRUD básicos.
 */
@Repository
public interface ConciliacionRepository extends JpaRepository<ConciliacionEntity, Long> {

    /**
     * Método para encontrar todas las conciliaciones por fecha de conciliación
     * @param fechaConciliacion La fecha de conciliación
     * @return Lista de conciliaciones para la fecha especificada
     */
    List<ConciliacionEntity> findByFechaConciliacion(LocalDate fechaConciliacion);

    /**
     * Método para encontrar todas las conciliaciones para una sucursal específica
     * @param codigoSucursal El código de la sucursal
     * @return Lista de conciliaciones asociadas a la sucursal
     */
    @Query("SELECT c FROM ConciliacionEntity c WHERE c.sucursalProducto.id.codigoSucursal = :codigoSucursal")
    List<ConciliacionEntity> findBySucursal(@Param("codigoSucursal") String codigoSucursal);

    /**
     * Método para encontrar todas las conciliaciones para un producto específico
     * @param codigoProducto El código del producto
     * @return Lista de conciliaciones asociadas al producto
     */
    @Query("SELECT c FROM ConciliacionEntity c WHERE c.sucursalProducto.id.codigoProducto = :codigoProducto")
    List<ConciliacionEntity> findByProducto(@Param("codigoProducto") String codigoProducto);

    /**
     * Método para encontrar todas las conciliaciones por estado
     * @param codigoEstado El código del estado de conciliación
     * @return Lista de conciliaciones con el estado especificado
     */
    List<ConciliacionEntity> findByEstadoConciliacionCodigoEstado(String codigoEstado);

    /**
     * Método para encontrar conciliaciones por fecha y estado
     * @param fechaConciliacion La fecha de conciliación
     * @param codigoEstado El código del estado de conciliación
     * @return Lista de conciliaciones para la fecha y estado especificados
     */
    List<ConciliacionEntity> findByFechaConciliacionAndEstadoConciliacionCodigoEstado(
            LocalDate fechaConciliacion, String codigoEstado);

    /**
     * Método para encontrar una conciliación específica por fecha y sucursal-producto
     * @param fechaConciliacion La fecha de conciliación
     * @param sucursalProductoId El ID compuesto de la relación sucursal-producto
     * @return La conciliación encontrada, o un Optional vacío si no existe
     */
    Optional<ConciliacionEntity> findByFechaConciliacionAndSucursalProductoId(
            LocalDate fechaConciliacion, SucursalProductoId sucursalProductoId);

    /**
     * Método para encontrar conciliaciones descuadradas (estado 'D') por fecha
     * @param fechaConciliacion La fecha de conciliación
     * @return Lista de conciliaciones descuadradas para la fecha especificada
     */
    @Query("SELECT c FROM ConciliacionEntity c WHERE c.fechaConciliacion = :fecha AND c.estadoConciliacion.codigoEstado = 'D'")
    List<ConciliacionEntity> findDescuadradasByFecha(@Param("fecha") LocalDate fechaConciliacion);

    /**
     * Método para encontrar todas las conciliaciones asociadas a una entidad SucursalProducto
     * @param sucursalProducto La entidad SucursalProducto
     * @return Lista de conciliaciones asociadas a la relación sucursal-producto
     */
    List<ConciliacionEntity> findBySucursalProducto(SucursalProductoEntity sucursalProducto);
}