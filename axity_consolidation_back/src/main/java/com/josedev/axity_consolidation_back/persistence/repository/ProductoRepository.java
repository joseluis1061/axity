package com.josedev.axity_consolidation_back.persistence.repository;

import com.josedev.axity_consolidation_back.persistence.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz que define el repositorio para la entidad ProductoEntity.
 * Extiende de JpaRepository para heredar métodos CRUD básicos.
 */
@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, String> {

    /**
     * El repositorio utiliza String como tipo para la clave primaria (codigoProducto)
     * No se requieren métodos adicionales ya que JpaRepository proporciona:
     * - findAll(): Obtener todos los productos
     * - findById(String id): Buscar un producto por su código
     * - save(ProductoEntity producto): Guardar o actualizar un producto
     * - deleteById(String id): Eliminar un producto por su código
     * - existsById(String id): Verificar si existe un producto por su código
     */
}