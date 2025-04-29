package com.josedev.axity_consolidation_back.domain.service;

import com.josedev.axity_consolidation_back.domain.model.Sucursal;

import java.util.List;
import java.util.Optional;

public interface SucursalService {

    List<Sucursal> getAllSucursales();

    Optional<Sucursal> getSucursalById(String codigoSucursal);

    Sucursal saveSucursal(Sucursal sucursal);

    void deleteSucursal(String codigoSucursal);

    boolean existsById(String codigoSucursal);
}