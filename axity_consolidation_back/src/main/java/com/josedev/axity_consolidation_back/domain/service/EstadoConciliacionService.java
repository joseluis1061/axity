package com.josedev.axity_consolidation_back.domain.service;

import com.josedev.axity_consolidation_back.domain.model.EstadoConciliacion;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de servicio para los estados de conciliación.
 */
public interface EstadoConciliacionService {

    /**
     * Obtiene todos los estados de conciliación
     *
     * @return Lista de estados de conciliación
     */
    List<EstadoConciliacion> getAllEstadosConciliacion();

    /**
     * Busca un estado de conciliación por su código
     *
     * @param codigoEstado Código del estado
     * @return Optional con el estado si existe, empty si no
     */
    Optional<EstadoConciliacion> getEstadoConciliacionById(String codigoEstado);

    /**
     * Busca un estado de conciliación por su descripción
     *
     * @param descripcion Descripción del estado
     * @return Optional con el estado si existe, empty si no
     */
    Optional<EstadoConciliacion> getEstadoConciliacionByDescripcion(String descripcion);

    /**
     * Guarda un nuevo estado de conciliación
     *
     * @param estadoConciliacion Estado a guardar
     * @return Estado guardado
     */
    EstadoConciliacion saveEstadoConciliacion(EstadoConciliacion estadoConciliacion);

    /**
     * Actualiza un estado de conciliación existente
     *
     * @param codigoEstado Código del estado a actualizar
     * @param estadoConciliacion Datos actualizados del estado
     * @return Optional con el estado actualizado, o empty si no existe
     */
    Optional<EstadoConciliacion> updateEstadoConciliacion(
            String codigoEstado, EstadoConciliacion estadoConciliacion);

    /**
     * Elimina un estado de conciliación
     *
     * @param codigoEstado Código del estado a eliminar
     * @return true si se eliminó correctamente, false si no existe
     */
    boolean deleteEstadoConciliacion(String codigoEstado);

    /**
     * Obtiene el estado "Descuadrada" (código 'D')
     *
     * @return Optional con el estado "Descuadrada", o empty si no existe
     */
    Optional<EstadoConciliacion> getEstadoDescuadrada();

    /**
     * Verifica si existe un estado con el código especificado
     *
     * @param codigoEstado Código del estado a verificar
     * @return true si existe, false si no
     */
    boolean existsEstadoConciliacion(String codigoEstado);
}