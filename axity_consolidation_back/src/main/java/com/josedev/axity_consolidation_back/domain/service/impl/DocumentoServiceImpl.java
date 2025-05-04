package com.josedev.axity_consolidation_back.domain.service.impl;

import com.josedev.axity_consolidation_back.domain.model.Documento;
import com.josedev.axity_consolidation_back.domain.repository.DocumentoService;
import com.josedev.axity_consolidation_back.persistence.entity.DocumentoEntity;
import com.josedev.axity_consolidation_back.persistence.mapper.DocumentoMapper;
import com.josedev.axity_consolidation_back.persistence.repository.DocumentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz DocumentoService.
 * Proporciona la lógica de negocio para las operaciones relacionadas con documentos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentoServiceImpl implements DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final DocumentoMapper documentoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Documento> getAllDocumentos() {
        log.info("Obteniendo todos los documentos");
        return documentoMapper.toDocumentoList(documentoRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Documento> getDocumentoById(String codigoDocumento) {
        log.info("Buscando documento con código: {}", codigoDocumento);

        if (codigoDocumento == null || codigoDocumento.isEmpty()) {
            throw new IllegalArgumentException("El código de documento no puede ser nulo o vacío");
        }

        return documentoRepository.findById(codigoDocumento)
                .map(documentoMapper::toDocumento);
    }

    @Override
    @Transactional
    public Documento saveDocumento(Documento documento) {
        log.info("Guardando nuevo documento: {}", documento);

        if (documento == null) {
            throw new IllegalArgumentException("El documento no puede ser nulo");
        }

        if (documento.getCodigoDocumento() == null || documento.getCodigoDocumento().isEmpty()) {
            throw new IllegalArgumentException("El código de documento no puede ser nulo o vacío");
        }

        DocumentoEntity entidad = documentoMapper.toDocumentoEntity(documento);
        DocumentoEntity guardado = documentoRepository.save(entidad);

        log.info("Documento guardado con código: {}", guardado.getCodigoDocumento());
        return documentoMapper.toDocumento(guardado);
    }

    @Override
    @Transactional
    public Optional<Documento> updateDocumento(String codigoDocumento, Documento documento) {
        log.info("Actualizando documento con código: {}", codigoDocumento);

        if (codigoDocumento == null || codigoDocumento.isEmpty()) {
            throw new IllegalArgumentException("El código de documento no puede ser nulo o vacío");
        }

        if (documento == null) {
            throw new IllegalArgumentException("El documento no puede ser nulo");
        }

        if (!existsDocumento(codigoDocumento)) {
            log.warn("No se encontró documento con código: {}", codigoDocumento);
            return Optional.empty();
        }

        // Asegurarse de que el código en el objeto coincide con el código proporcionado
        documento.setCodigoDocumento(codigoDocumento);

        DocumentoEntity entidad = documentoMapper.toDocumentoEntity(documento);
        DocumentoEntity actualizado = documentoRepository.save(entidad);

        log.info("Documento actualizado correctamente");
        return Optional.of(documentoMapper.toDocumento(actualizado));
    }

    @Override
    @Transactional
    public boolean deleteDocumento(String codigoDocumento) {
        log.info("Eliminando documento con código: {}", codigoDocumento);

        if (codigoDocumento == null || codigoDocumento.isEmpty()) {
            throw new IllegalArgumentException("El código de documento no puede ser nulo o vacío");
        }

        if (!existsDocumento(codigoDocumento)) {
            log.warn("No se encontró documento con código: {}", codigoDocumento);
            return false;
        }

        try {
            documentoRepository.deleteById(codigoDocumento);
            log.info("Documento eliminado correctamente");
            return true;
        } catch (Exception e) {
            log.error("Error al eliminar documento: {}", e.getMessage());
            throw new IllegalStateException("No se puede eliminar el documento porque está siendo utilizado", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsDocumento(String codigoDocumento) {
        if (codigoDocumento == null || codigoDocumento.isEmpty()) {
            return false;
        }

        return documentoRepository.existsById(codigoDocumento);
    }
}


/*
private final DocumentoRepository documentoRepository;
    private final DocumentoMapper documentoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Documento> obtenerTodosLosDocumentos() {
        log.info("Obteniendo todos los documentos");
        return documentoMapper.entityListToModelList(documentoRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Documento> obtenerDocumentoPorCodigo(String codigoDocumento) {
        log.info("Obteniendo documento con código: {}", codigoDocumento);

        if (codigoDocumento == null || codigoDocumento.isEmpty()) {
            throw new IllegalArgumentException("El código de documento no puede ser nulo o vacío");
        }

        return documentoRepository.findById(codigoDocumento)
                .map(documentoMapper::entityToModel);
    }

    @Override
    @Transactional
    public Documento guardarDocumento(Documento documento) {
        log.info("Guardando documento: {}", documento);

        if (documento == null) {
            throw new IllegalArgumentException("El documento no puede ser nulo");
        }

        if (documento.getCodigoDocumento() == null || documento.getCodigoDocumento().isEmpty()) {
            throw new IllegalArgumentException("El código de documento no puede ser nulo o vacío");
        }

        DocumentoEntity entidad = documentoMapper.modelToEntity(documento);
        DocumentoEntity guardado = documentoRepository.save(entidad);

        log.info("Documento guardado con código: {}", guardado.getCodigoDocumento());
        return documentoMapper.entityToModel(guardado);
    }

    @Override
    @Transactional
    public boolean eliminarDocumento(String codigoDocumento) {
        log.info("Eliminando documento con código: {}", codigoDocumento);

        if (codigoDocumento == null || codigoDocumento.isEmpty()) {
            throw new IllegalArgumentException("El código de documento no puede ser nulo o vacío");
        }

        if (!documentoRepository.existsById(codigoDocumento)) {
            log.warn("No se encontró documento con código: {}", codigoDocumento);
            return false;
        }

        try {
            documentoRepository.deleteById(codigoDocumento);
            log.info("Documento eliminado correctamente");
            return true;
        } catch (Exception e) {
            log.error("Error al eliminar documento: {}", e.getMessage());
            throw new IllegalStateException("No se puede eliminar el documento porque está siendo utilizado", e);
        }
    }
 */