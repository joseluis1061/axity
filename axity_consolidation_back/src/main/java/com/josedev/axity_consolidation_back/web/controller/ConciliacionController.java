package com.josedev.axity_consolidation_back.web.controller;

import com.josedev.axity_consolidation_back.domain.model.Conciliacion;
import com.josedev.axity_consolidation_back.domain.model.SucursalProductoId;
import com.josedev.axity_consolidation_back.domain.service.ConciliacionService;
import com.josedev.axity_consolidation_back.web.dto.ConciliacionDTO;
import com.josedev.axity_consolidation_back.web.mapper.ConciliacionDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para la gestión de conciliaciones.
 */
@RestController
@RequestMapping("/conciliaciones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Conciliaciones", description = "API para gestionar conciliaciones bancarias")
public class ConciliacionController {

    private final ConciliacionService conciliacionService;
    private final ConciliacionDtoMapper conciliacionDtoMapper;

    /**
     * Obtiene todas las conciliaciones
     * @return Lista de conciliaciones
     */
    @GetMapping
    @Operation(
            summary = "Obtiene todas las conciliaciones",
            description = "Devuelve una lista con todas las conciliaciones registradas",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Conciliaciones obtenidas correctamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConciliacionDTO.class))
                    )
            }
    )
    public ResponseEntity<List<ConciliacionDTO>> getAllConciliaciones() {
        log.info("Obteniendo todas las conciliaciones");
        List<Conciliacion> conciliaciones = conciliacionService.getAllConciliaciones();
        List<ConciliacionDTO> result = conciliacionDtoMapper.toConciliacionDtoList(conciliaciones);
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene una conciliación por su ID
     * @param id ID de la conciliación
     * @return La conciliación si existe
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene una conciliación por su ID",
            description = "Busca y devuelve una conciliación específica por su identificador",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conciliación encontrada"),
                    @ApiResponse(responseCode = "404", description = "Conciliación no encontrada")
            }
    )
    public ResponseEntity<ConciliacionDTO> getConciliacionById(
            @Parameter(description = "ID de la conciliación") @PathVariable Long id) {

        log.info("Buscando conciliación con ID: {}", id);

        return conciliacionService.getConciliacionById(id)
                .map(conciliacionDtoMapper::toConciliacionDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene conciliaciones por fecha
     * @param fecha Fecha de conciliación
     * @return Lista de conciliaciones para la fecha
     */
    @GetMapping("/fecha/{fecha}")
    @Operation(
            summary = "Obtiene conciliaciones por fecha",
            description = "Devuelve una lista con todas las conciliaciones para una fecha específica",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conciliaciones obtenidas correctamente")
            }
    )
    public ResponseEntity<List<ConciliacionDTO>> getConciliacionesByFecha(
            @Parameter(description = "Fecha de conciliación (formato: yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        log.info("Obteniendo conciliaciones para la fecha: {}", fecha);
        List<Conciliacion> conciliaciones = conciliacionService.getConciliacionesByFecha(fecha);
        List<ConciliacionDTO> result = conciliacionDtoMapper.toConciliacionDtoList(conciliaciones);
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene conciliaciones por sucursal
     * @param codigoSucursal Código de la sucursal
     * @return Lista de conciliaciones para la sucursal
     */
    @GetMapping("/sucursal/{codigoSucursal}")
    @Operation(
            summary = "Obtiene conciliaciones por sucursal",
            description = "Devuelve una lista con todas las conciliaciones para una sucursal específica",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conciliaciones obtenidas correctamente")
            }
    )
    public ResponseEntity<List<ConciliacionDTO>> getConciliacionesBySucursal(
            @Parameter(description = "Código de la sucursal") @PathVariable String codigoSucursal) {

        log.info("Obteniendo conciliaciones para la sucursal: {}", codigoSucursal);
        List<Conciliacion> conciliaciones = conciliacionService.getConciliacionesBySucursal(codigoSucursal);
        List<ConciliacionDTO> result = conciliacionDtoMapper.toConciliacionDtoList(conciliaciones);
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene conciliaciones por producto
     * @param codigoProducto Código del producto
     * @return Lista de conciliaciones para el producto
     */
    @GetMapping("/producto/{codigoProducto}")
    @Operation(
            summary = "Obtiene conciliaciones por producto",
            description = "Devuelve una lista con todas las conciliaciones para un producto específico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conciliaciones obtenidas correctamente")
            }
    )
    public ResponseEntity<List<ConciliacionDTO>> getConciliacionesByProducto(
            @Parameter(description = "Código del producto") @PathVariable String codigoProducto) {

        log.info("Obteniendo conciliaciones para el producto: {}", codigoProducto);
        List<Conciliacion> conciliaciones = conciliacionService.getConciliacionesByProducto(codigoProducto);
        List<ConciliacionDTO> result = conciliacionDtoMapper.toConciliacionDtoList(conciliaciones);
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene conciliaciones por estado
     * @param codigoEstado Código del estado
     * @return Lista de conciliaciones con el estado
     */
    @GetMapping("/estado/{codigoEstado}")
    @Operation(
            summary = "Obtiene conciliaciones por estado",
            description = "Devuelve una lista con todas las conciliaciones que tienen un estado específico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conciliaciones obtenidas correctamente")
            }
    )
    public ResponseEntity<List<ConciliacionDTO>> getConciliacionesByEstado(
            @Parameter(description = "Código del estado (A, B, C, D)") @PathVariable String codigoEstado) {

        log.info("Obteniendo conciliaciones con estado: {}", codigoEstado);
        List<Conciliacion> conciliaciones = conciliacionService.getConciliacionesByEstado(codigoEstado);
        List<ConciliacionDTO> result = conciliacionDtoMapper.toConciliacionDtoList(conciliaciones);
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene conciliaciones por fecha y estado
     * @param fecha Fecha de conciliación
     * @param codigoEstado Código del estado
     * @return Lista de conciliaciones para la fecha y estado
     */
    @GetMapping("/fecha/{fecha}/estado/{codigoEstado}")
    @Operation(
            summary = "Obtiene conciliaciones por fecha y estado",
            description = "Devuelve una lista con todas las conciliaciones para una fecha y estado específicos",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conciliaciones obtenidas correctamente")
            }
    )
    public ResponseEntity<List<ConciliacionDTO>> getConciliacionesByFechaAndEstado(
            @Parameter(description = "Fecha de conciliación (formato: yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @Parameter(description = "Código del estado (A, B, C, D)") @PathVariable String codigoEstado) {

        log.info("Obteniendo conciliaciones para fecha: {} y estado: {}", fecha, codigoEstado);
        List<Conciliacion> conciliaciones = conciliacionService.getConciliacionesByFechaAndEstado(fecha, codigoEstado);
        List<ConciliacionDTO> result = conciliacionDtoMapper.toConciliacionDtoList(conciliaciones);
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene conciliaciones descuadradas por fecha
     * @param fecha Fecha de conciliación
     * @return Lista de conciliaciones descuadradas para la fecha
     */
    @GetMapping("/descuadradas/fecha/{fecha}")
    @Operation(
            summary = "Obtiene conciliaciones descuadradas por fecha",
            description = "Devuelve una lista con todas las conciliaciones descuadradas para una fecha específica",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conciliaciones obtenidas correctamente")
            }
    )
    public ResponseEntity<List<ConciliacionDTO>> getConciliacionesDescuadradasByFecha(
            @Parameter(description = "Fecha de conciliación (formato: yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        log.info("Obteniendo conciliaciones descuadradas para la fecha: {}", fecha);
        List<Conciliacion> conciliaciones = conciliacionService.getConciliacionesDescuadradasByFecha(fecha);
        List<ConciliacionDTO> result = conciliacionDtoMapper.toConciliacionDtoList(conciliaciones);
        return ResponseEntity.ok(result);
    }

    /**
     * Crea una nueva conciliación
     * @param conciliacionDTO Datos de la nueva conciliación
     * @return La conciliación creada
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crea una nueva conciliación",
            description = "Registra una nueva conciliación en el sistema",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Conciliación creada correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    public ResponseEntity<ConciliacionDTO> createConciliacion(
            @Parameter(description = "Datos de la conciliación") @Valid @RequestBody ConciliacionDTO conciliacionDTO) {

        log.info("Creando nueva conciliación: {}", conciliacionDTO);
        Conciliacion conciliacion = conciliacionDtoMapper.toConciliacion(conciliacionDTO);
        Conciliacion created = conciliacionService.saveConciliacion(conciliacion);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(conciliacionDtoMapper.toConciliacionDto(created));
    }

    /**
     * Actualiza una conciliación existente
     * @param id ID de la conciliación
     * @param conciliacionDTO Datos actualizados de la conciliación
     * @return La conciliación actualizada
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza una conciliación",
            description = "Actualiza una conciliación existente en el sistema",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conciliación actualizada correctamente"),
                    @ApiResponse(responseCode = "404", description = "Conciliación no encontrada"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    public ResponseEntity<ConciliacionDTO> updateConciliacion(
            @Parameter(description = "ID de la conciliación") @PathVariable Long id,
            @Parameter(description = "Datos actualizados de la conciliación")
            @Valid @RequestBody ConciliacionDTO conciliacionDTO) {

        log.info("Actualizando conciliación con ID: {}", id);

        // Verificar consistencia entre path variable y body
        if (conciliacionDTO.getIdConciliacion() != null && !id.equals(conciliacionDTO.getIdConciliacion())) {
            return ResponseEntity.badRequest().build();
        }

        Conciliacion conciliacion = conciliacionDtoMapper.toConciliacion(conciliacionDTO);

        return conciliacionService.updateConciliacion(id, conciliacion)
                .map(conciliacionDtoMapper::toConciliacionDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una conciliación
     * @param id ID de la conciliación
     * @return 204 No Content si se eliminó correctamente, 404 Not Found si no existe
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina una conciliación",
            description = "Elimina una conciliación existente del sistema",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Conciliación eliminada correctamente"),
                    @ApiResponse(responseCode = "404", description = "Conciliación no encontrada")
            }
    )
    public ResponseEntity<Void> deleteConciliacion(
            @Parameter(description = "ID de la conciliación") @PathVariable Long id) {

        log.info("Eliminando conciliación con ID: {}", id);

        boolean deleted = conciliacionService.deleteConciliacion(id);

        return deleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Procesa las conciliaciones para una fecha específica
     * @param fecha Fecha para procesar
     * @return Información sobre el procesamiento
     */
    @PostMapping("/procesar/{fecha}")
    @Operation(
            summary = "Procesa las conciliaciones",
            description = "Ejecuta el proceso batch para identificar conciliaciones descuadradas para una fecha específica",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Proceso ejecutado correctamente")
            }
    )
    public ResponseEntity<String> procesarConciliaciones(
            @Parameter(description = "Fecha de conciliación (formato: yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        log.info("Procesando conciliaciones para la fecha: {}", fecha);

        int procesadas = conciliacionService.procesarConciliaciones(fecha);

        return ResponseEntity.ok(String.format(
                "Proceso completado. Se procesaron %d conciliaciones para la fecha %s", procesadas, fecha));
    }

    /**
     * Busca una conciliación específica por fecha y relación sucursal-producto
     * @param fecha Fecha de conciliación
     * @param codigoSucursal Código de sucursal
     * @param codigoProducto Código de producto
     * @param codigoDocumento Código de documento
     * @return La conciliación encontrada o 404 si no existe
     */
    @GetMapping("/fecha/{fecha}/sucursal/{codigoSucursal}/producto/{codigoProducto}/documento/{codigoDocumento}")
    @Operation(
            summary = "Busca una conciliación específica",
            description = "Busca una conciliación por fecha y relación sucursal-producto-documento",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conciliación encontrada"),
                    @ApiResponse(responseCode = "404", description = "Conciliación no encontrada")
            }
    )
    public ResponseEntity<ConciliacionDTO> getConciliacionByFechaAndSucursalProducto(
            @Parameter(description = "Fecha de conciliación (formato: yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @Parameter(description = "Código de sucursal") @PathVariable String codigoSucursal,
            @Parameter(description = "Código de producto") @PathVariable String codigoProducto,
            @Parameter(description = "Código de documento") @PathVariable String codigoDocumento) {

        log.info("Buscando conciliación para fecha: {} y sucursal: {}, producto: {}, documento: {}",
                fecha, codigoSucursal, codigoProducto, codigoDocumento);

        SucursalProductoId sucursalProductoId = new SucursalProductoId(
                codigoSucursal, codigoProducto, codigoDocumento);

        return conciliacionService.getConciliacionByFechaAndSucursalProducto(fecha, sucursalProductoId)
                .map(conciliacionDtoMapper::toConciliacionDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}