package com.josedev.axity_consolidation_back.web.controller;

import com.josedev.axity_consolidation_back.domain.model.Sucursal;
import com.josedev.axity_consolidation_back.domain.repository.SucursalService;
import com.josedev.axity_consolidation_back.web.dto.SucursalDTO;
import com.josedev.axity_consolidation_back.web.mapper.SucursalDTOMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operaciones relacionadas con sucursales.
 */
@RestController
@RequestMapping("/sucursales")
@CrossOrigin(origins = "*")
public class SucursalController {

    private final SucursalService sucursalService;
    private final SucursalDTOMapper sucursalDTOMapper;

    @Autowired
    public SucursalController(SucursalService sucursalService, SucursalDTOMapper sucursalDTOMapper) {
        this.sucursalService = sucursalService;
        this.sucursalDTOMapper = sucursalDTOMapper;
    }

    /**
     * Obtiene todas las sucursales.
     * @return Lista de sucursales.
     */
    @GetMapping
    public ResponseEntity<List<SucursalDTO>> getAllSucursales() {
        List<Sucursal> sucursales = sucursalService.getAllSucursales();
        List<SucursalDTO> sucursalDTOs = sucursalDTOMapper.toSucursalDTOs(sucursales);
        return ResponseEntity.ok(sucursalDTOs);
    }

    /**
     * Obtiene una sucursal por su código.
     * @param codigoSucursal Código de la sucursal a buscar.
     * @return La sucursal si existe.
     */
    @GetMapping("/{codigoSucursal}")
    public ResponseEntity<SucursalDTO> getSucursalById(@PathVariable String codigoSucursal) {
        return sucursalService.getSucursalById(codigoSucursal)
                .map(sucursal -> {
                    SucursalDTO sucursalDTO = sucursalDTOMapper.toSucursalDTO(sucursal);
                    return ResponseEntity.ok(sucursalDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva sucursal.
     * @param sucursalDTO Datos de la sucursal a crear.
     * @return La sucursal creada.
     */
    @PostMapping
    public ResponseEntity<SucursalDTO> createSucursal(@Valid @RequestBody SucursalDTO sucursalDTO) {
        // Verificar si ya existe una sucursal con ese código
        if (sucursalService.existsById(sucursalDTO.getCodigoSucursal())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Sucursal sucursal = sucursalDTOMapper.toSucursal(sucursalDTO);
        Sucursal createdSucursal = sucursalService.saveSucursal(sucursal);
        SucursalDTO createdSucursalDTO = sucursalDTOMapper.toSucursalDTO(createdSucursal);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSucursalDTO);
    }

    /**
     * Actualiza una sucursal existente.
     * @param codigoSucursal Código de la sucursal a actualizar.
     * @param sucursalDTO Nuevos datos de la sucursal.
     * @return La sucursal actualizada.
     */
    @PutMapping("/{codigoSucursal}")
    public ResponseEntity<SucursalDTO> updateSucursal(
            @PathVariable String codigoSucursal,
            @Valid @RequestBody SucursalDTO sucursalDTO) {

        if (!sucursalService.existsById(codigoSucursal)) {
            return ResponseEntity.notFound().build();
        }

        // Asegurar que el código en el path y en el body coincidan
        sucursalDTO.setCodigoSucursal(codigoSucursal);

        Sucursal sucursal = sucursalDTOMapper.toSucursal(sucursalDTO);
        Sucursal updatedSucursal = sucursalService.saveSucursal(sucursal);
        SucursalDTO updatedSucursalDTO = sucursalDTOMapper.toSucursalDTO(updatedSucursal);

        return ResponseEntity.ok(updatedSucursalDTO);
    }

    /**
     * Elimina una sucursal.
     * @param codigoSucursal Código de la sucursal a eliminar.
     * @return Respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{codigoSucursal}")
    public ResponseEntity<Void> deleteSucursal(@PathVariable String codigoSucursal) {
        if (!sucursalService.existsById(codigoSucursal)) {
            return ResponseEntity.notFound().build();
        }

        sucursalService.deleteSucursal(codigoSucursal);
        return ResponseEntity.noContent().build();
    }
}