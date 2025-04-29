package com.josedev.axity_consolidation_back.domain.service;

import com.josedev.axity_consolidation_back.domain.model.Producto;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de servicio para la entidad Producto.
 */
public interface ProductoService {

    /**
     * Obtiene todos los productos disponibles
     *
     * @return Lista de productos
     */
    List<Producto> getAllProductos();

    /**
     * Busca un producto por su código
     *
     * @param codigoProducto Código único del producto
     * @return Optional con el producto si existe, empty si no
     */
    Optional<Producto> getProductoById(String codigoProducto);

    /**
     * Guarda un nuevo producto
     *
     * @param producto El producto a guardar
     * @return El producto guardado con posibles modificaciones (ej: ID generado)
     */
    Producto saveProducto(Producto producto);

    /**
     * Actualiza un producto existente
     *
     * @param codigoProducto Código del producto a actualizar
     * @param producto Datos actualizados del producto
     * @return El producto actualizado o null si no se encontró
     */
    Optional<Producto> updateProducto(String codigoProducto, Producto producto);

    /**
     * Elimina un producto por su código
     *
     * @param codigoProducto Código del producto a eliminar
     * @return true si se eliminó correctamente, false si no existe
     */
    boolean deleteProducto(String codigoProducto);

    /**
     * Verifica si existe un producto con el código especificado
     *
     * @param codigoProducto Código del producto a verificar
     * @return true si existe, false si no
     */
    boolean existsProducto(String codigoProducto);
}