package com.josedev.axity_consolidation_back.web.controller;

import com.josedev.axity_consolidation_back.domain.model.SucursalProducto;
import com.josedev.axity_consolidation_back.domain.model.SucursalProductoId;
import com.josedev.axity_consolidation_back.domain.repository.SucursalProductoService;
import com.josedev.axity_consolidation_back.web.dto.SucursalProductoDTO;
import com.josedev.axity_consolidation_back.web.mapper.SucursalProductoDtoMapper;
import com.josedev.axity_consolidation_back.web.mapper.SucursalProductoIdDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de relaciones entre Sucursales, Productos y Documentos.
 */
@RestController
@RequestMapping("/sucursal-productos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Sucursal-Producto", description = "API para gestionar relaciones entre sucursales, productos y documentos")
public class SucursalProductoController {

    private final SucursalProductoService sucursalProductoService;
    private final SucursalProductoDtoMapper sucursalProductoDtoMapper;
    private final SucursalProductoIdDtoMapper sucursalProductoIdDtoMapper;

    /**
     * Obtiene todas las relaciones sucursal-producto
     * @return Lista de relaciones sucursal-producto
     */
    @GetMapping
    @Operation(
            summary = "Obtiene todas las relaciones sucursal-producto",
            description = "Devuelve una lista con todas las relaciones entre sucursales, productos y documentos",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Relaciones obtenidas correctamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SucursalProductoDTO.class))
                    )
            }
    )
    public ResponseEntity<List<SucursalProductoDTO>> getAllSucursalProductos() {
        log.info("Obteniendo todas las relaciones sucursal-producto");
        List<SucursalProducto> sucursalProductos = sucursalProductoService.getAllSucursalProductos();
        List<SucursalProductoDTO> result = sucursalProductoDtoMapper.toSucursalProductoDtoList(sucursalProductos);
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene una relación sucursal-producto por su ID compuesto
     * @param codigoSucursal Código de la sucursal
     * @param codigoProducto Código del producto
     * @param codigoDocumento Código del documento
     * @return La relación sucursal-producto si existe
     */
    @GetMapping("/{codigoSucursal}/{codigoProducto}/{codigoDocumento}")
    @Operation(
            summary = "Obtiene una relación sucursal-producto por su ID compuesto",
            description = "Busca y devuelve una relación específica entre sucursal, producto y documento",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relación encontrada"),
                    @ApiResponse(responseCode = "404", description = "Relación no encontrada")
            }
    )
    public ResponseEntity<SucursalProductoDTO> getSucursalProductoById(
            @Parameter(description = "Código de la sucursal") @PathVariable String codigoSucursal,
            @Parameter(description = "Código del producto") @PathVariable String codigoProducto,
            @Parameter(description = "Código del documento") @PathVariable String codigoDocumento) {

        log.info("Buscando relación para sucursal: {}, producto: {}, documento: {}",
                codigoSucursal, codigoProducto, codigoDocumento);

        return sucursalProductoService.getSucursalProductoById(codigoSucursal, codigoProducto, codigoDocumento)
                .map(sucursalProductoDtoMapper::toSucursalProductoDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todas las relaciones para una sucursal específica
     * @param codigoSucursal Código de la sucursal
     * @return Lista de relaciones para la sucursal
     */
    @GetMapping("/sucursal/{codigoSucursal}")
    @Operation(
            summary = "Obtiene todas las relaciones para una sucursal",
            description = "Devuelve una lista con todas las relaciones que involucran a la sucursal especificada",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relaciones obtenidas correctamente")
            }
    )
    public ResponseEntity<List<SucursalProductoDTO>> getSucursalProductosBySucursal(
            @Parameter(description = "Código de la sucursal") @PathVariable String codigoSucursal) {

        log.info("Obteniendo relaciones para la sucursal: {}", codigoSucursal);
        List<SucursalProducto> sucursalProductos = sucursalProductoService.getSucursalProductosBySucursal(codigoSucursal);
        List<SucursalProductoDTO> result = sucursalProductoDtoMapper.toSucursalProductoDtoList(sucursalProductos);
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene todas las relaciones para un producto específico
     * @param codigoProducto Código del producto
     * @return Lista de relaciones para el producto
     */
    @GetMapping("/producto/{codigoProducto}")
    @Operation(
            summary = "Obtiene todas las relaciones para un producto",
            description = "Devuelve una lista con todas las relaciones que involucran al producto especificado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relaciones obtenidas correctamente")
            }
    )
    public ResponseEntity<List<SucursalProductoDTO>> getSucursalProductosByProducto(
            @Parameter(description = "Código del producto") @PathVariable String codigoProducto) {

        log.info("Obteniendo relaciones para el producto: {}", codigoProducto);
        List<SucursalProducto> sucursalProductos = sucursalProductoService.getSucursalProductosByProducto(codigoProducto);
        List<SucursalProductoDTO> result = sucursalProductoDtoMapper.toSucursalProductoDtoList(sucursalProductos);
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene todas las relaciones para un documento específico
     * @param codigoDocumento Código del documento
     * @return Lista de relaciones para el documento
     */
    @GetMapping("/documento/{codigoDocumento}")
    @Operation(
            summary = "Obtiene todas las relaciones para un documento",
            description = "Devuelve una lista con todas las relaciones que involucran al documento especificado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relaciones obtenidas correctamente")
            }
    )
    public ResponseEntity<List<SucursalProductoDTO>> getSucursalProductosByDocumento(
            @Parameter(description = "Código del documento") @PathVariable String codigoDocumento) {

        log.info("Obteniendo relaciones para el documento: {}", codigoDocumento);
        List<SucursalProducto> sucursalProductos = sucursalProductoService.getSucursalProductosByDocumento(codigoDocumento);
        List<SucursalProductoDTO> result = sucursalProductoDtoMapper.toSucursalProductoDtoList(sucursalProductos);
        return ResponseEntity.ok(result);
    }

    /**
     * Crea una nueva relación sucursal-producto
     * @param sucursalProductoDTO Datos de la nueva relación
     * @return La relación creada
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crea una nueva relación sucursal-producto",
            description = "Registra una nueva relación entre una sucursal, un producto y un documento",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Relación creada correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    public ResponseEntity<SucursalProductoDTO> createSucursalProducto(
            @Parameter(description = "Datos de la relación") @Valid @RequestBody SucursalProductoDTO sucursalProductoDTO) {

        log.info("Creando nueva relación sucursal-producto: {}", sucursalProductoDTO);
        SucursalProducto sucursalProducto = sucursalProductoDtoMapper.toSucursalProducto(sucursalProductoDTO);
        SucursalProducto created = sucursalProductoService.saveSucursalProducto(sucursalProducto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sucursalProductoDtoMapper.toSucursalProductoDto(created));
    }

    /**
     * Actualiza una relación sucursal-producto existente
     * @param codigoSucursal Código de la sucursal
     * @param codigoProducto Código del producto
     * @param codigoDocumento Código del documento
     * @param sucursalProductoDTO Datos actualizados de la relación
     * @return La relación actualizada
     */
    @PutMapping("/{codigoSucursal}/{codigoProducto}/{codigoDocumento}")
    @Operation(
            summary = "Actualiza una relación sucursal-producto",
            description = "Actualiza una relación existente entre sucursal, producto y documento",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relación actualizada correctamente"),
                    @ApiResponse(responseCode = "404", description = "Relación no encontrada"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    public ResponseEntity<SucursalProductoDTO> updateSucursalProducto(
            @Parameter(description = "Código de la sucursal") @PathVariable String codigoSucursal,
            @Parameter(description = "Código del producto") @PathVariable String codigoProducto,
            @Parameter(description = "Código del documento") @PathVariable String codigoDocumento,
            @Parameter(description = "Datos actualizados de la relación") @Valid @RequestBody SucursalProductoDTO sucursalProductoDTO) {

        log.info("Actualizando relación para sucursal: {}, producto: {}, documento: {}",
                codigoSucursal, codigoProducto, codigoDocumento);

        // Verificar consistencia entre path variables y body
        if (!codigoSucursal.equals(sucursalProductoDTO.getCodigoSucursal()) ||
                !codigoProducto.equals(sucursalProductoDTO.getCodigoProducto()) ||
                !codigoDocumento.equals(sucursalProductoDTO.getCodigoDocumento())) {
            return ResponseEntity.badRequest().build();
        }

        SucursalProductoId id = new SucursalProductoId(codigoSucursal, codigoProducto, codigoDocumento);
        SucursalProducto sucursalProducto = sucursalProductoDtoMapper.toSucursalProducto(sucursalProductoDTO);

        return sucursalProductoService.updateSucursalProducto(id, sucursalProducto)
                .map(sucursalProductoDtoMapper::toSucursalProductoDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una relación sucursal-producto
     * @param codigoSucursal Código de la sucursal
     * @param codigoProducto Código del producto
     * @param codigoDocumento Código del documento
     * @return 204 No Content si se eliminó correctamente, 404 Not Found si no existe
     */
    @DeleteMapping("/{codigoSucursal}/{codigoProducto}/{codigoDocumento}")
    @Operation(
            summary = "Elimina una relación sucursal-producto",
            description = "Elimina una relación existente entre sucursal, producto y documento",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Relación eliminada correctamente"),
                    @ApiResponse(responseCode = "404", description = "Relación no encontrada")
            }
    )
    public ResponseEntity<Void> deleteSucursalProducto(
            @Parameter(description = "Código de la sucursal") @PathVariable String codigoSucursal,
            @Parameter(description = "Código del producto") @PathVariable String codigoProducto,
            @Parameter(description = "Código del documento") @PathVariable String codigoDocumento) {

        log.info("Eliminando relación para sucursal: {}, producto: {}, documento: {}",
                codigoSucursal, codigoProducto, codigoDocumento);

        SucursalProductoId id = new SucursalProductoId(codigoSucursal, codigoProducto, codigoDocumento);
        boolean deleted = sucursalProductoService.deleteSucursalProducto(id);

        return deleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Verifica si existe una relación sucursal-producto
     * @param codigoSucursal Código de la sucursal
     * @param codigoProducto Código del producto
     * @param codigoDocumento Código del documento
     * @return 200 OK si existe, 404 Not Found si no
     */
    @GetMapping("/exists/{codigoSucursal}/{codigoProducto}/{codigoDocumento}")
    @Operation(
            summary = "Verifica si existe una relación",
            description = "Comprueba si existe una relación con los códigos especificados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "La relación existe"),
                    @ApiResponse(responseCode = "404", description = "La relación no existe")
            }
    )
    public ResponseEntity<Void> existsSucursalProducto(
            @Parameter(description = "Código de la sucursal") @PathVariable String codigoSucursal,
            @Parameter(description = "Código del producto") @PathVariable String codigoProducto,
            @Parameter(description = "Código del documento") @PathVariable String codigoDocumento) {

        log.info("Verificando existencia de relación para sucursal: {}, producto: {}, documento: {}",
                codigoSucursal, codigoProducto, codigoDocumento);

        SucursalProductoId id = new SucursalProductoId(codigoSucursal, codigoProducto, codigoDocumento);
        boolean exists = sucursalProductoService.existsSucursalProducto(id);

        return exists ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
}