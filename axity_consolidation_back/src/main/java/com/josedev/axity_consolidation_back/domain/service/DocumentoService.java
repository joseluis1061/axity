package com.josedev.axity_consolidation_back.domain.service;

import com.josedev.axity_consolidation_back.domain.model.Documento;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de servicio para la entidad Documento.
 */
public interface DocumentoService {

    /**
     * Obtiene todos los documentos disponibles
     *
     * @return Lista de documentos
     */
    List<Documento> getAllDocumentos();

    /**
     * Busca un documento por su código
     *
     * @param codigoDocumento Código único del documento
     * @return Optional con el documento si existe, empty si no
     */
    Optional<Documento> getDocumentoById(String codigoDocumento);

    /**
     * Guarda un nuevo documento
     *
     * @param documento El documento a guardar
     * @return El documento guardado con posibles modificaciones
     */
    Documento saveDocumento(Documento documento);

    /**
     * Actualiza un documento existente
     *
     * @param codigoDocumento Código del documento a actualizar
     * @param documento Datos actualizados del documento
     * @return El documento actualizado o null si no se encontró
     */
    Optional<Documento> updateDocumento(String codigoDocumento, Documento documento);

    /**
     * Elimina un documento por su código
     *
     * @param codigoDocumento Código del documento a eliminar
     * @return true si se eliminó correctamente, false si no existe
     */
    boolean deleteDocumento(String codigoDocumento);

    /**
     * Verifica si existe un documento con el código especificado
     *
     * @param codigoDocumento Código del documento a verificar
     * @return true si existe, false si no
     */
    boolean existsDocumento(String codigoDocumento);
}

/*

List<Documento> obtenerTodosLosDocumentos();


Optional<Documento> obtenerDocumentoPorCodigo(String codigoDocumento);


Documento guardarDocumento(Documento documento);


boolean eliminarDocumento(String codigoDocumento);
 */