package com.josedev.axity_consolidation_back.domain.service;

import com.josedev.axity_consolidation_back.domain.model.SucursalProducto;
import com.josedev.axity_consolidation_back.domain.model.SucursalProductoId;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de servicio para la entidad SucursalProducto.
 */
public interface SucursalProductoService {

    /**
     * Obtiene todas las relaciones de sucursal-producto-documento
     *
     * @return Lista de relaciones sucursal-producto-documento
     */
    List<SucursalProducto> getAllSucursalProductos();

    /**
     * Busca una relación por su identificador compuesto
     *
     * @param id Identificador compuesto (codigoSucursal, codigoProducto, codigoDocumento)
     * @return Optional con la relación si existe, empty si no
     */
    Optional<SucursalProducto> getSucursalProductoById(SucursalProductoId id);

    /**
     * Busca una relación por sus códigos componentes
     *
     * @param codigoSucursal Código de la sucursal
     * @param codigoProducto Código del producto
     * @param codigoDocumento Código del documento
     * @return Optional con la relación si existe, empty si no
     */
    Optional<SucursalProducto> getSucursalProductoById(String codigoSucursal, String codigoProducto, String codigoDocumento);

    /**
     * Guarda una nueva relación sucursal-producto-documento
     *
     * @param sucursalProducto La relación a guardar
     * @return La relación guardada
     */
    SucursalProducto saveSucursalProducto(SucursalProducto sucursalProducto);

    /**
     * Actualiza una relación existente
     *
     * @param id Identificador compuesto de la relación a actualizar
     * @param sucursalProducto Datos actualizados de la relación
     * @return La relación actualizada o empty si no se encontró
     */
    Optional<SucursalProducto> updateSucursalProducto(SucursalProductoId id, SucursalProducto sucursalProducto);

    /**
     * Elimina una relación por su identificador compuesto
     *
     * @param id Identificador compuesto de la relación a eliminar
     * @return true si se eliminó correctamente, false si no existe
     */
    boolean deleteSucursalProducto(SucursalProductoId id);

    /**
     * Verifica si existe una relación con el identificador especificado
     *
     * @param id Identificador compuesto a verificar
     * @return true si existe, false si no
     */
    boolean existsSucursalProducto(SucursalProductoId id);

    /**
     * Obtiene todas las relaciones para una sucursal específica
     *
     * @param codigoSucursal Código de la sucursal
     * @return Lista de relaciones para la sucursal
     */
    List<SucursalProducto> getSucursalProductosBySucursal(String codigoSucursal);

    /**
     * Obtiene todas las relaciones para un producto específico
     *
     * @param codigoProducto Código del producto
     * @return Lista de relaciones para el producto
     */
    List<SucursalProducto> getSucursalProductosByProducto(String codigoProducto);

    /**
     * Obtiene todas las relaciones para un documento específico
     *
     * @param codigoDocumento Código del documento
     * @return Lista de relaciones para el documento
     */
    List<SucursalProducto> getSucursalProductosByDocumento(String codigoDocumento);
}

/*
List<SucursalProducto> obtenerTodasLasRelaciones();
Optional<SucursalProducto> obtenerRelacionPorId(SucursalProductoId id);
List<SucursalProducto> obtenerRelacionesPorSucursal(String codigoSucursal);
List<SucursalProducto> obtenerRelacionesPorProducto(String codigoProducto);
SucursalProducto guardarRelacion(SucursalProducto sucursalProducto);
boolean eliminarRelacion(SucursalProductoId id);
 */