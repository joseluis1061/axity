package com.josedev.axity_consolidation_back.domain.service.impl;

import com.josedev.axity_consolidation_back.domain.model.SucursalProducto;
import com.josedev.axity_consolidation_back.domain.model.SucursalProductoId;
import com.josedev.axity_consolidation_back.domain.repository.DocumentoService;
import com.josedev.axity_consolidation_back.domain.repository.ProductoService;
import com.josedev.axity_consolidation_back.domain.repository.SucursalProductoService;
import com.josedev.axity_consolidation_back.domain.repository.SucursalService;
import com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoEntity;
import com.josedev.axity_consolidation_back.persistence.mapper.SucursalProductoMapper;
import com.josedev.axity_consolidation_back.persistence.repository.SucursalProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz SucursalProductoService.
 * Proporciona la lógica de negocio para las operaciones relacionadas con SucursalProducto.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SucursalProductoServiceImpl implements SucursalProductoService {

    private final SucursalProductoRepository sucursalProductoRepository;
    private final SucursalProductoMapper sucursalProductoMapper;
    private final SucursalService sucursalService;
    private final ProductoService productoService;
    private final DocumentoService documentoService;

    /**
     * Obtiene todas las relaciones de sucursal-producto-documento
     *
     * @return Lista de relaciones sucursal-producto-documento
     */
    @Override
    @Transactional(readOnly = true)
    public List<SucursalProducto> getAllSucursalProductos() {
        log.info("Obteniendo todas las relaciones sucursal-producto-documento");
        List<SucursalProductoEntity> entities = sucursalProductoRepository.findAll();
        return sucursalProductoMapper.toSucursalProductoList(entities);
    }

    /**
     * Busca una relación por su identificador compuesto
     *
     * @param id Identificador compuesto (codigoSucursal, codigoProducto, codigoDocumento)
     * @return Optional con la relación si existe, empty si no
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SucursalProducto> getSucursalProductoById(SucursalProductoId id) {
        log.info("Buscando relación sucursal-producto con ID: {}", id);

        com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoId entityId =
                new com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoId(
                        id.getCodigoSucursal(),
                        id.getCodigoProducto(),
                        id.getCodigoDocumento()
                );

        return sucursalProductoRepository.findById(entityId)
                .map(sucursalProductoMapper::toSucursalProducto);
    }

    /**
     * Busca una relación por sus códigos componentes
     *
     * @param codigoSucursal Código de la sucursal
     * @param codigoProducto Código del producto
     * @param codigoDocumento Código del documento
     * @return Optional con la relación si existe, empty si no
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SucursalProducto> getSucursalProductoById(String codigoSucursal, String codigoProducto, String codigoDocumento) {
        log.info("Buscando relación para sucursal: {}, producto: {}, documento: {}",
                codigoSucursal, codigoProducto, codigoDocumento);

        SucursalProductoId id = new SucursalProductoId(codigoSucursal, codigoProducto, codigoDocumento);
        return getSucursalProductoById(id);
    }

    /**
     * Guarda una nueva relación sucursal-producto-documento
     *
     * @param sucursalProducto La relación a guardar
     * @return La relación guardada
     */
    @Override
    @Transactional
    public SucursalProducto saveSucursalProducto(SucursalProducto sucursalProducto) {
        log.info("Guardando nueva relación sucursal-producto: {}", sucursalProducto);

        // Verificar si existen las entidades relacionadas
        verificarEntidadesRelacionadas(sucursalProducto);

        // Verificar si ya existe la relación
        SucursalProductoId id = new SucursalProductoId(
                sucursalProducto.getCodigoSucursal(),
                sucursalProducto.getCodigoProducto(),
                sucursalProducto.getCodigoDocumento()
        );

        if (existsSucursalProducto(id)) {
            log.warn("La relación sucursal-producto ya existe: {}", id);
            return getSucursalProductoById(id).orElse(sucursalProducto);
        }

        // Convertir y guardar
        SucursalProductoEntity entity = sucursalProductoMapper.toSucursalProductoEntity(sucursalProducto);
        SucursalProductoEntity savedEntity = sucursalProductoRepository.save(entity);
        return sucursalProductoMapper.toSucursalProducto(savedEntity);
    }

    /**
     * Actualiza una relación existente
     *
     * @param id Identificador compuesto de la relación a actualizar
     * @param sucursalProducto Datos actualizados de la relación
     * @return La relación actualizada o empty si no se encontró
     */
    @Override
    @Transactional
    public Optional<SucursalProducto> updateSucursalProducto(SucursalProductoId id, SucursalProducto sucursalProducto) {
        log.info("Actualizando relación sucursal-producto con ID: {}", id);

        // Verificar si las entidades relacionadas existen
        verificarEntidadesRelacionadas(sucursalProducto);

        // Convertir ID a entidad
        com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoId entityId =
                new com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoId(
                        id.getCodigoSucursal(),
                        id.getCodigoProducto(),
                        id.getCodigoDocumento()
                );

        return sucursalProductoRepository.findById(entityId)
                .map(existingEntity -> {
                    // En este caso no hay muchos campos que actualizar directamente
                    // ya que los IDs no se pueden modificar y las relaciones se manejan de forma especial
                    // Sin embargo, podríamos actualizar los objetos relacionados si fuera necesario

                    // Guardar la entidad actualizada
                    SucursalProductoEntity updatedEntity = sucursalProductoRepository.save(existingEntity);
                    return sucursalProductoMapper.toSucursalProducto(updatedEntity);
                });
    }

    /**
     * Elimina una relación por su identificador compuesto
     *
     * @param id Identificador compuesto de la relación a eliminar
     * @return true si se eliminó correctamente, false si no existe
     */
    @Override
    @Transactional
    public boolean deleteSucursalProducto(SucursalProductoId id) {
        log.info("Eliminando relación sucursal-producto con ID: {}", id);

        com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoId entityId =
                new com.josedev.axity_consolidation_back.persistence.entity.SucursalProductoId(
                        id.getCodigoSucursal(),
                        id.getCodigoProducto(),
                        id.getCodigoDocumento()
                );

        if (sucursalProductoRepository.existsById(entityId)) {
            sucursalProductoRepository.deleteById(entityId);
            return true;
        }

        return false;
    }

    /**
     * Verifica si existe una relación con el identificador especificado
     *
     * @param id Identificador compuesto a verificar
     * @return true si existe, false si no
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsSucursalProducto(SucursalProductoId id) {
        log.info("Verificando existencia de relación sucursal-producto con ID: {}", id);

        return sucursalProductoRepository.existsByIdCodigoSucursalAndIdCodigoProductoAndIdCodigoDocumento(
                id.getCodigoSucursal(),
                id.getCodigoProducto(),
                id.getCodigoDocumento()
        );
    }

    /**
     * Obtiene todas las relaciones para una sucursal específica
     *
     * @param codigoSucursal Código de la sucursal
     * @return Lista de relaciones para la sucursal
     */
    @Override
    @Transactional(readOnly = true)
    public List<SucursalProducto> getSucursalProductosBySucursal(String codigoSucursal) {
        log.info("Obteniendo relaciones para la sucursal: {}", codigoSucursal);

        List<SucursalProductoEntity> entities = sucursalProductoRepository.findByIdCodigoSucursal(codigoSucursal);
        return sucursalProductoMapper.toSucursalProductoList(entities);
    }

    /**
     * Obtiene todas las relaciones para un producto específico
     *
     * @param codigoProducto Código del producto
     * @return Lista de relaciones para el producto
     */
    @Override
    @Transactional(readOnly = true)
    public List<SucursalProducto> getSucursalProductosByProducto(String codigoProducto) {
        log.info("Obteniendo relaciones para el producto: {}", codigoProducto);

        List<SucursalProductoEntity> entities = sucursalProductoRepository.findByIdCodigoProducto(codigoProducto);
        return sucursalProductoMapper.toSucursalProductoList(entities);
    }

    /**
     * Obtiene todas las relaciones para un documento específico
     *
     * @param codigoDocumento Código del documento
     * @return Lista de relaciones para el documento
     */
    @Override
    @Transactional(readOnly = true)
    public List<SucursalProducto> getSucursalProductosByDocumento(String codigoDocumento) {
        log.info("Obteniendo relaciones para el documento: {}", codigoDocumento);

        List<SucursalProductoEntity> entities = sucursalProductoRepository.findByIdCodigoDocumento(codigoDocumento);
        return sucursalProductoMapper.toSucursalProductoList(entities);
    }

    /**
     * Método auxiliar para verificar que las entidades relacionadas existen
     * @param sucursalProducto La relación a verificar
     */
    private void verificarEntidadesRelacionadas(SucursalProducto sucursalProducto) {
        // Verificar si existe la sucursal
        if (sucursalProducto.getCodigoSucursal() != null) {
            sucursalService.getSucursalById(sucursalProducto.getCodigoSucursal())
                    .orElseThrow(() -> new RuntimeException("La sucursal con código "
                            + sucursalProducto.getCodigoSucursal() + " no existe"));
        }

        // Verificar si existe el producto
        if (sucursalProducto.getCodigoProducto() != null) {
            productoService.getProductoById(sucursalProducto.getCodigoProducto())
                    .orElseThrow(() -> new RuntimeException("El producto con código "
                            + sucursalProducto.getCodigoProducto() + " no existe"));
        }

        // Verificar si existe el documento
        if (sucursalProducto.getCodigoDocumento() != null) {
            documentoService.getDocumentoById(sucursalProducto.getCodigoDocumento())
                    .orElseThrow(() -> new RuntimeException("El documento con código "
                            + sucursalProducto.getCodigoDocumento() + " no existe"));
        }
    }
}