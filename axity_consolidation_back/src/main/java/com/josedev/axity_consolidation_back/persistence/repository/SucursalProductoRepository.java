package com.josedev.axity_consolidation_back.persistence.repository;

import com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoEntity;
import com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interfaz que define el repositorio para la entidad SucursalProductoEntity.
 * Extiende de JpaRepository para heredar métodos CRUD básicos.
 */
@Repository
public interface SucursalProductoRepository extends JpaRepository<SucursalProductoEntity, SucursalProductoId> {

    /**
     * Método para encontrar todas las relaciones de sucursal-producto por código de sucursal
     * @param codigoSucursal El código de la sucursal
     * @return Lista de entidades SucursalProducto asociadas a la sucursal
     */
    List<SucursalProductoEntity> findByIdCodigoSucursal(String codigoSucursal);

    /**
     * Método para encontrar todas las relaciones de sucursal-producto por código de producto
     * @param codigoProducto El código del producto
     * @return Lista de entidades SucursalProducto asociadas al producto
     */
    List<SucursalProductoEntity> findByIdCodigoProducto(String codigoProducto);

    /**
     * Método para encontrar todas las relaciones de sucursal-producto por código de documento
     * @param codigoDocumento El código del documento
     * @return Lista de entidades SucursalProducto asociadas al documento
     */
    List<SucursalProductoEntity> findByIdCodigoDocumento(String codigoDocumento);

    /**
     * Método para verificar si existe una relación con los códigos especificados
     * @param codigoSucursal El código de la sucursal
     * @param codigoProducto El código del producto
     * @param codigoDocumento El código del documento
     * @return true si existe la relación, false en caso contrario
     */
    boolean existsByIdCodigoSucursalAndIdCodigoProductoAndIdCodigoDocumento(
            String codigoSucursal, String codigoProducto, String codigoDocumento);

    /**
     * Método para encontrar todas las relaciones de sucursal-producto para una combinación específica de sucursal y producto
     * @param codigoSucursal El código de la sucursal
     * @param codigoProducto El código del producto
     * @return Lista de entidades SucursalProducto asociadas a la combinación de sucursal y producto
     */
    List<SucursalProductoEntity> findByIdCodigoSucursalAndIdCodigoProducto(String codigoSucursal, String codigoProducto);
}