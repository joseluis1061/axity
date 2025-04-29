package com.josedev.axity_consolidation_back.domain.service.impl;

import com.josedev.axity_consolidation_back.domain.model.Producto;
import com.josedev.axity_consolidation_back.domain.service.ProductoService;
import com.josedev.axity_consolidation_back.persistence.entity.ProductoEntity;
import com.josedev.axity_consolidation_back.persistence.mapper.ProductoMapper;
import com.josedev.axity_consolidation_back.persistence.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz ProductoService que proporciona
 * la lógica de negocio para las operaciones con productos.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Producto> getAllProductos() {
        List<ProductoEntity> productoEntities = productoRepository.findAll();
        return productoMapper.toProductoList(productoEntities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> getProductoById(String codigoProducto) {
        return productoRepository.findById(codigoProducto)
                .map(productoMapper::toProducto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Producto saveProducto(Producto producto) {
        ProductoEntity productoEntity = productoMapper.toProductoEntity(producto);
        ProductoEntity savedEntity = productoRepository.save(productoEntity);
        return productoMapper.toProducto(savedEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Producto> updateProducto(String codigoProducto, Producto producto) {
        if (productoRepository.existsById(codigoProducto)) {
            // Asegurarse de que el código del producto sea el correcto
            producto.setCodigoProducto(codigoProducto);
            ProductoEntity productoEntity = productoMapper.toProductoEntity(producto);
            ProductoEntity updatedEntity = productoRepository.save(productoEntity);
            return Optional.of(productoMapper.toProducto(updatedEntity));
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteProducto(String codigoProducto) {
        if (productoRepository.existsById(codigoProducto)) {
            productoRepository.deleteById(codigoProducto);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsProducto(String codigoProducto) {
        return productoRepository.existsById(codigoProducto);
    }
}