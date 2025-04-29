package com.josedev.axity_consolidation_back.persistence.repository;

import com.josedev.axity_consolidation_back.persistence.entity.DocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz que define el repositorio para la entidad DocumentoEntity.
 * Extiende de JpaRepository para heredar métodos CRUD básicos.
 */
@Repository
public interface DocumentoRepository extends JpaRepository<DocumentoEntity, String> {

    /**
     * El repositorio utiliza String como tipo para la clave primaria (codigoDocumento)
     * No se requieren métodos adicionales ya que JpaRepository proporciona:
     * - findAll(): Obtener todos los documentos
     * - findById(String id): Buscar un documento por su código
     * - save(DocumentoEntity documento): Guardar o actualizar un documento
     * - deleteById(String id): Eliminar un documento por su código
     * - existsById(String id): Verificar si existe un documento por su código
     */
}