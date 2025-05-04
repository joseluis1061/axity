package com.josedev.axity_consolidation_back.domain.service.impl;

import com.josedev.axity_consolidation_back.domain.model.Sucursal;
import com.josedev.axity_consolidation_back.domain.repository.SucursalService;
import com.josedev.axity_consolidation_back.persistence.entity.SucursalEntity;
import com.josedev.axity_consolidation_back.persistence.mapper.SucursalMapper;
import com.josedev.axity_consolidation_back.persistence.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SucursalServiceImpl implements SucursalService {

    private final SucursalRepository sucursalRepository;
    private final SucursalMapper sucursalMapper;

    @Autowired
    public SucursalServiceImpl(SucursalRepository sucursalRepository, SucursalMapper sucursalMapper) {
        this.sucursalRepository = sucursalRepository;
        this.sucursalMapper = sucursalMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sucursal> getAllSucursales() {
        List<SucursalEntity> entities = sucursalRepository.findAll();
        return sucursalMapper.toSucursales(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sucursal> getSucursalById(String codigoSucursal) {
        return sucursalRepository.findById(codigoSucursal)
                .map(sucursalMapper::toSucursal);
    }

    @Override
    @Transactional
    public Sucursal saveSucursal(Sucursal sucursal) {
        SucursalEntity entity = sucursalMapper.toSucursalEntity(sucursal);
        SucursalEntity savedEntity = sucursalRepository.save(entity);
        return sucursalMapper.toSucursal(savedEntity);
    }

    @Override
    @Transactional
    public void deleteSucursal(String codigoSucursal) {
        sucursalRepository.deleteById(codigoSucursal);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String codigoSucursal) {
        return sucursalRepository.existsById(codigoSucursal);
    }
}