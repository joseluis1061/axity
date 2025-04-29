package com.josedev.axity_consolidation_back.domain.service;

import com.josedev.axity_consolidation_back.domain.model.Conciliacion;
import com.josedev.axity_consolidation_back.domain.model.SucursalProductoId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de servicio para las conciliaciones.
 */
public interface ConciliacionService {

    /**
     * Obtiene todas las conciliaciones
     *
     * @return Lista de conciliaciones
     */
    List<Conciliacion> getAllConciliaciones();

    /**
     * Busca una conciliación por su ID
     *
     * @param id ID de la conciliación
     * @return Optional con la conciliación si existe, empty si no
     */
    Optional<Conciliacion> getConciliacionById(Long id);

    /**
     * Obtiene las conciliaciones para una fecha específica
     *
     * @param fecha Fecha de conciliación
     * @return Lista de conciliaciones para la fecha indicada
     */
    List<Conciliacion> getConciliacionesByFecha(LocalDate fecha);

    /**
     * Obtiene las conciliaciones para una sucursal específica
     *
     * @param codigoSucursal Código de la sucursal
     * @return Lista de conciliaciones para la sucursal
     */
    List<Conciliacion> getConciliacionesBySucursal(String codigoSucursal);

    /**
     * Obtiene las conciliaciones para un producto específico
     *
     * @param codigoProducto Código del producto
     * @return Lista de conciliaciones para el producto
     */
    List<Conciliacion> getConciliacionesByProducto(String codigoProducto);

    /**
     * Obtiene las conciliaciones por estado
     *
     * @param codigoEstado Código del estado de conciliación
     * @return Lista de conciliaciones con el estado indicado
     */
    List<Conciliacion> getConciliacionesByEstado(String codigoEstado);

    /**
     * Obtiene las conciliaciones por fecha y estado
     *
     * @param fecha Fecha de conciliación
     * @param codigoEstado Código del estado
     * @return Lista de conciliaciones para la fecha y estado indicados
     */
    List<Conciliacion> getConciliacionesByFechaAndEstado(LocalDate fecha, String codigoEstado);

    /**
     * Obtiene una conciliación específica por fecha y relación sucursal-producto
     *
     * @param fecha Fecha de conciliación
     * @param sucursalProductoId ID compuesto de la relación sucursal-producto
     * @return Optional con la conciliación si existe, empty si no
     */
    Optional<Conciliacion> getConciliacionByFechaAndSucursalProducto(
            LocalDate fecha, SucursalProductoId sucursalProductoId);

    /**
     * Guarda una nueva conciliación
     *
     * @param conciliacion Conciliación a guardar
     * @return Conciliación guardada con su ID asignado
     */
    Conciliacion saveConciliacion(Conciliacion conciliacion);

    /**
     * Actualiza una conciliación existente
     *
     * @param id ID de la conciliación a actualizar
     * @param conciliacion Datos actualizados de la conciliación
     * @return Optional con la conciliación actualizada, o empty si no existe
     */
    Optional<Conciliacion> updateConciliacion(Long id, Conciliacion conciliacion);

    /**
     * Elimina una conciliación
     *
     * @param id ID de la conciliación a eliminar
     * @return true si se eliminó correctamente, false si no existe
     */
    boolean deleteConciliacion(Long id);

    /**
     * Obtiene las conciliaciones descuadradas (estado 'D') para una fecha específica
     *
     * @param fecha Fecha de conciliación
     * @return Lista de conciliaciones descuadradas
     */
    List<Conciliacion> getConciliacionesDescuadradasByFecha(LocalDate fecha);

    /**
     * Procesa las conciliaciones para una fecha, identificando y marcando las descuadradas
     * Este método simula el proceso batch mencionado en los requisitos
     *
     * @param fecha Fecha para procesar las conciliaciones
     * @return Número de conciliaciones procesadas
     */
    int procesarConciliaciones(LocalDate fecha);
}