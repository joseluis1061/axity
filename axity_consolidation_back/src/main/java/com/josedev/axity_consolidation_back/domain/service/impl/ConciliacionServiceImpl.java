package com.josedev.axity_consolidation_back.domain.service.impl;

import com.josedev.axity_consolidation_back.domain.model.Conciliacion;
import com.josedev.axity_consolidation_back.domain.model.EstadoConciliacion;
import com.josedev.axity_consolidation_back.domain.model.SucursalProducto;
import com.josedev.axity_consolidation_back.domain.model.SucursalProductoId;
import com.josedev.axity_consolidation_back.domain.service.ConciliacionService;
import com.josedev.axity_consolidation_back.domain.service.EstadoConciliacionService;
import com.josedev.axity_consolidation_back.domain.service.SucursalProductoService;
import com.josedev.axity_consolidation_back.persistence.entity.ConciliacionEntity;
import com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoEntity;
import com.josedev.axity_consolidation_back.persistence.mapper.ConciliacionMapper;
import com.josedev.axity_consolidation_back.persistence.mapper.SucursalProductoIdMapper;
import com.josedev.axity_consolidation_back.persistence.repository.ConciliacionRepository;
import com.josedev.axity_consolidation_back.persistence.repository.SucursalProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación de la interfaz ConciliacionService.
 * Proporciona la lógica de negocio para las operaciones relacionadas con conciliaciones.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConciliacionServiceImpl implements ConciliacionService {

    private final ConciliacionRepository conciliacionRepository;
    private final SucursalProductoRepository sucursalProductoRepository;
    private final ConciliacionMapper conciliacionMapper;
    private final SucursalProductoIdMapper sucursalProductoIdMapper;
    private final SucursalProductoService sucursalProductoService;
    private final EstadoConciliacionService estadoConciliacionService;

    /**
     * Obtiene todas las conciliaciones
     *
     * @return Lista de conciliaciones
     */
    @Override
    @Transactional(readOnly = true)
    public List<Conciliacion> getAllConciliaciones() {
        log.info("Obteniendo todas las conciliaciones");
        List<ConciliacionEntity> entities = conciliacionRepository.findAll();
        return conciliacionMapper.toConciliacionList(entities);
    }

    /**
     * Busca una conciliación por su ID
     *
     * @param id ID de la conciliación
     * @return Optional con la conciliación si existe, empty si no
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Conciliacion> getConciliacionById(Long id) {
        log.info("Buscando conciliación con ID: {}", id);
        return conciliacionRepository.findById(id)
                .map(conciliacionMapper::toConciliacion);
    }

    /**
     * Obtiene las conciliaciones para una fecha específica
     *
     * @param fecha Fecha de conciliación
     * @return Lista de conciliaciones para la fecha indicada
     */
    @Override
    @Transactional(readOnly = true)
    public List<Conciliacion> getConciliacionesByFecha(LocalDate fecha) {
        log.info("Obteniendo conciliaciones para la fecha: {}", fecha);
        List<ConciliacionEntity> entities = conciliacionRepository.findByFechaConciliacion(fecha);
        return conciliacionMapper.toConciliacionList(entities);
    }

    /**
     * Obtiene las conciliaciones para una sucursal específica
     *
     * @param codigoSucursal Código de la sucursal
     * @return Lista de conciliaciones para la sucursal
     */
    @Override
    @Transactional(readOnly = true)
    public List<Conciliacion> getConciliacionesBySucursal(String codigoSucursal) {
        log.info("Obteniendo conciliaciones para la sucursal: {}", codigoSucursal);
        List<ConciliacionEntity> entities = conciliacionRepository.findBySucursal(codigoSucursal);
        return conciliacionMapper.toConciliacionList(entities);
    }

    /**
     * Obtiene las conciliaciones para un producto específico
     *
     * @param codigoProducto Código del producto
     * @return Lista de conciliaciones para el producto
     */
    @Override
    @Transactional(readOnly = true)
    public List<Conciliacion> getConciliacionesByProducto(String codigoProducto) {
        log.info("Obteniendo conciliaciones para el producto: {}", codigoProducto);
        List<ConciliacionEntity> entities = conciliacionRepository.findByProducto(codigoProducto);
        return conciliacionMapper.toConciliacionList(entities);
    }

    /**
     * Obtiene las conciliaciones por estado
     *
     * @param codigoEstado Código del estado de conciliación
     * @return Lista de conciliaciones con el estado indicado
     */
    @Override
    @Transactional(readOnly = true)
    public List<Conciliacion> getConciliacionesByEstado(String codigoEstado) {
        log.info("Obteniendo conciliaciones con estado: {}", codigoEstado);
        List<ConciliacionEntity> entities = conciliacionRepository.findByEstadoConciliacionCodigoEstado(codigoEstado);
        return conciliacionMapper.toConciliacionList(entities);
    }

    /**
     * Obtiene las conciliaciones por fecha y estado
     *
     * @param fecha Fecha de conciliación
     * @param codigoEstado Código del estado
     * @return Lista de conciliaciones para la fecha y estado indicados
     */
    @Override
    @Transactional(readOnly = true)
    public List<Conciliacion> getConciliacionesByFechaAndEstado(LocalDate fecha, String codigoEstado) {
        log.info("Obteniendo conciliaciones para fecha: {} y estado: {}", fecha, codigoEstado);
        List<ConciliacionEntity> entities = conciliacionRepository.findByFechaConciliacionAndEstadoConciliacionCodigoEstado(
                fecha, codigoEstado);
        return conciliacionMapper.toConciliacionList(entities);
    }

    /**
     * Obtiene una conciliación específica por fecha y relación sucursal-producto
     *
     * @param fecha Fecha de conciliación
     * @param sucursalProductoId ID compuesto de la relación sucursal-producto
     * @return Optional con la conciliación si existe, empty si no
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Conciliacion> getConciliacionByFechaAndSucursalProducto(
            LocalDate fecha, SucursalProductoId sucursalProductoId) {
        log.info("Buscando conciliación para fecha: {} y sucursal-producto: {}", fecha, sucursalProductoId);

        com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoId entityId =
                sucursalProductoIdMapper.toSucursalProductoEntityId(sucursalProductoId);

        return conciliacionRepository.findByFechaConciliacionAndSucursalProductoId(fecha, entityId)
                .map(conciliacionMapper::toConciliacion);
    }

    /**
     * Guarda una nueva conciliación
     *
     * @param conciliacion Conciliación a guardar
     * @return Conciliación guardada con su ID asignado
     */
    @Override
    @Transactional
    public Conciliacion saveConciliacion(Conciliacion conciliacion) {
        log.info("Guardando nueva conciliación: {}", conciliacion);

        // Verificar que existe la relación sucursal-producto
        SucursalProductoId sucursalProductoId = new SucursalProductoId(
                conciliacion.getCodigoSucursal(),
                conciliacion.getCodigoProducto(),
                conciliacion.getCodigoDocumento()
        );

        if (!sucursalProductoService.existsSucursalProducto(sucursalProductoId)) {
            log.error("No existe la relación sucursal-producto: {}", sucursalProductoId);
            throw new IllegalArgumentException("La relación sucursal-producto no existe");
        }

        // Verificar que existe el estado de conciliación
        if (!estadoConciliacionService.existsEstadoConciliacion(conciliacion.getCodigoEstado())) {
            log.error("No existe el estado de conciliación: {}", conciliacion.getCodigoEstado());
            throw new IllegalArgumentException("El estado de conciliación no existe");
        }

        // Verificar si ya existe una conciliación para esta fecha y relación
        Optional<Conciliacion> existingConciliacion = getConciliacionByFechaAndSucursalProducto(
                conciliacion.getFechaConciliacion(), sucursalProductoId);

        if (existingConciliacion.isPresent()) {
            log.warn("Ya existe una conciliación para esta fecha y relación. ID: {}",
                    existingConciliacion.get().getIdConciliacion());
            return existingConciliacion.get();
        }

        // Establecer fecha de creación si no está definida
        if (conciliacion.getFechaCreacion() == null) {
            conciliacion.setFechaCreacion(LocalDateTime.now());
        }

        // Convertir y guardar
        ConciliacionEntity entity = conciliacionMapper.toConciliacionEntity(conciliacion);
        ConciliacionEntity savedEntity = conciliacionRepository.save(entity);
        return conciliacionMapper.toConciliacion(savedEntity);
    }

    /**
     * Actualiza una conciliación existente
     *
     * @param id ID de la conciliación a actualizar
     * @param conciliacion Datos actualizados de la conciliación
     * @return Optional con la conciliación actualizada, o empty si no existe
     */
    @Override
    @Transactional
    public Optional<Conciliacion> updateConciliacion(Long id, Conciliacion conciliacion) {
        log.info("Actualizando conciliación con ID: {}", id);

        // Verificar que el ID coincide
        if (conciliacion.getIdConciliacion() != null && !id.equals(conciliacion.getIdConciliacion())) {
            log.warn("El ID de la conciliación no coincide: {} vs {}", id, conciliacion.getIdConciliacion());
            return Optional.empty();
        }

        // Verificar que existe el estado de conciliación
        if (!estadoConciliacionService.existsEstadoConciliacion(conciliacion.getCodigoEstado())) {
            log.error("No existe el estado de conciliación: {}", conciliacion.getCodigoEstado());
            throw new IllegalArgumentException("El estado de conciliación no existe");
        }

        return conciliacionRepository.findById(id)
                .map(existingEntity -> {
                    // Actualizar campos
                    existingEntity.setFechaConciliacion(conciliacion.getFechaConciliacion());

                    // No actualizamos la relación sucursal-producto, ya que es parte de la identidad del registro

                    existingEntity.setDiferenciaFisica(conciliacion.getDiferenciaFisica());
                    existingEntity.setDiferenciaValor(conciliacion.getDiferenciaValor());

                    // Actualizar estado (asumiendo que el mapper puede manejar esto correctamente)
                    if (conciliacion.getEstadoConciliacion() != null) {
                        existingEntity.setEstadoConciliacion(
                                conciliacionMapper.toConciliacionEntity(conciliacion).getEstadoConciliacion());
                    }

                    // Guardar entidad actualizada
                    ConciliacionEntity updatedEntity = conciliacionRepository.save(existingEntity);
                    return conciliacionMapper.toConciliacion(updatedEntity);
                });
    }

    /**
     * Elimina una conciliación
     *
     * @param id ID de la conciliación a eliminar
     * @return true si se eliminó correctamente, false si no existe
     */
    @Override
    @Transactional
    public boolean deleteConciliacion(Long id) {
        log.info("Eliminando conciliación con ID: {}", id);

        if (conciliacionRepository.existsById(id)) {
            conciliacionRepository.deleteById(id);
            return true;
        }

        return false;
    }

    /**
     * Obtiene las conciliaciones descuadradas (estado 'D') para una fecha específica
     *
     * @param fecha Fecha de conciliación
     * @return Lista de conciliaciones descuadradas
     */
    @Override
    @Transactional(readOnly = true)
    public List<Conciliacion> getConciliacionesDescuadradasByFecha(LocalDate fecha) {
        log.info("Obteniendo conciliaciones descuadradas para la fecha: {}", fecha);
        List<ConciliacionEntity> entities = conciliacionRepository.findDescuadradasByFecha(fecha);
        return conciliacionMapper.toConciliacionList(entities);
    }

    /**
     * Procesa las conciliaciones para una fecha, identificando y marcando las descuadradas
     * Este método simula el proceso batch mencionado en los requisitos
     *
     * @param fecha Fecha para procesar las conciliaciones
     * @return Número de conciliaciones procesadas
     */
    @Override
    @Transactional
    public int procesarConciliaciones(LocalDate fecha) {
        log.info("Procesando conciliaciones para la fecha: {}", fecha);

        // Obtener todas las conciliaciones para la fecha indicada
        List<ConciliacionEntity> conciliaciones = conciliacionRepository.findByFechaConciliacion(fecha);

        if (conciliaciones.isEmpty()) {
            log.warn("No hay conciliaciones para procesar en la fecha: {}", fecha);
            return 0;
        }

        // Obtener el estado "Descuadrada"
        EstadoConciliacion estadoDescuadrada = estadoConciliacionService.getEstadoDescuadrada()
                .orElseThrow(() -> new IllegalStateException("No se encontró el estado 'Descuadrada'"));

        // Criterios para marcar una conciliación como descuadrada
        // Basado en la descripción del problema, consideramos que:
        // 1. Una diferencia física significativa (> 10.0) indica descuadre
        // 2. Una diferencia de valor grande (> 1,000,000.00) indica descuadre
        int procesadas = 0;

        for (ConciliacionEntity conciliacion : conciliaciones) {
            boolean estabaMarcadaDescuadrada = "D".equals(conciliacion.getEstadoConciliacion().getCodigoEstado());
            boolean debeSerMarcadaDescuadrada = evaluarConciliacionDescuadrada(conciliacion);

            if (debeSerMarcadaDescuadrada && !estabaMarcadaDescuadrada) {
                // Cambiar a estado "Descuadrada"
                conciliacion.setEstadoConciliacion(
                        conciliacionMapper.toConciliacionEntity(
                                Conciliacion.builder().estadoConciliacion(estadoDescuadrada).build()
                        ).getEstadoConciliacion()
                );
                conciliacionRepository.save(conciliacion);
                procesadas++;

                log.info("Conciliación marcada como descuadrada: {}", conciliacion.getIdConciliacion());
            } else if (!debeSerMarcadaDescuadrada && estabaMarcadaDescuadrada) {
                // La conciliación estaba marcada como descuadrada, pero ya no cumple los criterios
                // Por ahora, dejamos este caso para un análisis manual
                log.warn("Conciliación {} estaba marcada como descuadrada pero ya no cumple los criterios",
                        conciliacion.getIdConciliacion());
            }
        }

        log.info("Proceso completado. {} conciliaciones procesadas de un total de {}",
                procesadas, conciliaciones.size());

        return procesadas;
    }

    /**
     * Método auxiliar para evaluar si una conciliación debe ser marcada como descuadrada
     * @param conciliacion La conciliación a evaluar
     * @return true si debe ser marcada como descuadrada, false en caso contrario
     */
    private boolean evaluarConciliacionDescuadrada(ConciliacionEntity conciliacion) {
        // Criterios de descuadre (pueden ajustarse según los requerimientos específicos)

        // 1. Diferencia física significativa
        boolean diferenciaFisicaSignificativa = conciliacion.getDiferenciaFisica().compareTo(new BigDecimal("10.0")) > 0;

        // 2. Diferencia de valor significativa
        boolean diferenciaValorSignificativa = conciliacion.getDiferenciaValor().compareTo(new BigDecimal("1000000.00")) > 0;

        // 3. Si tiene ambas diferencias no nulas, aunque sean pequeñas
        boolean ambasDiferenciasNoNulas =
                conciliacion.getDiferenciaFisica().compareTo(BigDecimal.ZERO) != 0 &&
                        conciliacion.getDiferenciaValor().compareTo(BigDecimal.ZERO) != 0;

        // Una conciliación se considera descuadrada si cumple al menos uno de los criterios
        return diferenciaFisicaSignificativa || diferenciaValorSignificativa || ambasDiferenciasNoNulas;
    }
}