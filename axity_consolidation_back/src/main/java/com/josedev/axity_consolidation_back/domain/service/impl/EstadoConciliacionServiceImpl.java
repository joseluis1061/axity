package com.josedev.axity_consolidation_back.domain.service.impl;

import com.josedev.axity_consolidation_back.domain.model.EstadoConciliacion;
import com.josedev.axity_consolidation_back.domain.service.EstadoConciliacionService;
import com.josedev.axity_consolidation_back.persistence.entity.EstadoConciliacionEntity;
import com.josedev.axity_consolidation_back.persistence.mapper.EstadoConciliacionMapper;
import com.josedev.axity_consolidation_back.persistence.repository.EstadoConciliacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz EstadoConciliacionService.
 * Proporciona la lógica de negocio para las operaciones relacionadas con estados de conciliación.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EstadoConciliacionServiceImpl implements EstadoConciliacionService {

    private final EstadoConciliacionRepository estadoConciliacionRepository;
    private final EstadoConciliacionMapper estadoConciliacionMapper;

    /**
     * Obtiene todos los estados de conciliación
     *
     * @return Lista de estados de conciliación
     */
    @Override
    @Transactional(readOnly = true)
    public List<EstadoConciliacion> getAllEstadosConciliacion() {
        log.info("Obteniendo todos los estados de conciliación");
        List<EstadoConciliacionEntity> entities = estadoConciliacionRepository.findAll();
        return estadoConciliacionMapper.toEstadoConciliacionList(entities);
    }

    /**
     * Busca un estado de conciliación por su código
     *
     * @param codigoEstado Código del estado
     * @return Optional con el estado si existe, empty si no
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EstadoConciliacion> getEstadoConciliacionById(String codigoEstado) {
        log.info("Buscando estado de conciliación con código: {}", codigoEstado);
        return estadoConciliacionRepository.findById(codigoEstado)
                .map(estadoConciliacionMapper::toEstadoConciliacion);
    }

    /**
     * Busca un estado de conciliación por su descripción
     *
     * @param descripcion Descripción del estado
     * @return Optional con el estado si existe, empty si no
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EstadoConciliacion> getEstadoConciliacionByDescripcion(String descripcion) {
        log.info("Buscando estado de conciliación con descripción: {}", descripcion);
        return estadoConciliacionRepository.findByDescripcion(descripcion)
                .map(estadoConciliacionMapper::toEstadoConciliacion);
    }

    /**
     * Guarda un nuevo estado de conciliación
     *
     * @param estadoConciliacion Estado a guardar
     * @return Estado guardado
     */
    @Override
    @Transactional
    public EstadoConciliacion saveEstadoConciliacion(EstadoConciliacion estadoConciliacion) {
        log.info("Guardando nuevo estado de conciliación: {}", estadoConciliacion);

        // Verificar si ya existe un estado con el mismo código
        if (existsEstadoConciliacion(estadoConciliacion.getCodigoEstado())) {
            log.warn("Ya existe un estado con el código: {}", estadoConciliacion.getCodigoEstado());
            return getEstadoConciliacionById(estadoConciliacion.getCodigoEstado())
                    .orElse(estadoConciliacion);
        }

        // Verificar si ya existe un estado con la misma descripción
        if (estadoConciliacionRepository.existsByDescripcion(estadoConciliacion.getDescripcion())) {
            log.warn("Ya existe un estado con la descripción: {}", estadoConciliacion.getDescripcion());
            return getEstadoConciliacionByDescripcion(estadoConciliacion.getDescripcion())
                    .orElse(estadoConciliacion);
        }

        // Convertir y guardar
        EstadoConciliacionEntity entity = estadoConciliacionMapper.toEstadoConciliacionEntity(estadoConciliacion);
        EstadoConciliacionEntity savedEntity = estadoConciliacionRepository.save(entity);
        return estadoConciliacionMapper.toEstadoConciliacion(savedEntity);
    }

    /**
     * Actualiza un estado de conciliación existente
     *
     * @param codigoEstado Código del estado a actualizar
     * @param estadoConciliacion Datos actualizados del estado
     * @return Optional con el estado actualizado, o empty si no existe
     */
    @Override
    @Transactional
    public Optional<EstadoConciliacion> updateEstadoConciliacion(
            String codigoEstado, EstadoConciliacion estadoConciliacion) {
        log.info("Actualizando estado de conciliación con código: {}", codigoEstado);

        // Verificar que el código del estado coincide
        if (!codigoEstado.equals(estadoConciliacion.getCodigoEstado())) {
            log.warn("El código del estado no coincide: {} vs {}",
                    codigoEstado, estadoConciliacion.getCodigoEstado());
            return Optional.empty();
        }

        return estadoConciliacionRepository.findById(codigoEstado)
                .map(existingEntity -> {
                    // Actualizar campos
                    existingEntity.setDescripcion(estadoConciliacion.getDescripcion());

                    // Guardar entidad actualizada
                    EstadoConciliacionEntity updatedEntity = estadoConciliacionRepository.save(existingEntity);
                    return estadoConciliacionMapper.toEstadoConciliacion(updatedEntity);
                });
    }

    /**
     * Elimina un estado de conciliación
     *
     * @param codigoEstado Código del estado a eliminar
     * @return true si se eliminó correctamente, false si no existe
     */
    @Override
    @Transactional
    public boolean deleteEstadoConciliacion(String codigoEstado) {
        log.info("Eliminando estado de conciliación con código: {}", codigoEstado);

        if (estadoConciliacionRepository.existsById(codigoEstado)) {
            estadoConciliacionRepository.deleteById(codigoEstado);
            return true;
        }

        return false;
    }

    /**
     * Obtiene el estado "Descuadrada" (código 'D')
     *
     * @return Optional con el estado "Descuadrada", o empty si no existe
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EstadoConciliacion> getEstadoDescuadrada() {
        log.info("Obteniendo estado 'Descuadrada'");
        return estadoConciliacionRepository.findByCodigoEstado("D")
                .map(estadoConciliacionMapper::toEstadoConciliacion);
    }

    /**
     * Verifica si existe un estado con el código especificado
     *
     * @param codigoEstado Código del estado a verificar
     * @return true si existe, false si no
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsEstadoConciliacion(String codigoEstado) {
        log.info("Verificando si existe estado con código: {}", codigoEstado);
        return estadoConciliacionRepository.existsById(codigoEstado);
    }
}