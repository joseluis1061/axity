package com.josedev.axity_consolidation_back.web.controller;

import com.josedev.axity_consolidation_back.domain.model.EstadoConciliacion;
import com.josedev.axity_consolidation_back.domain.repository.EstadoConciliacionService;
import com.josedev.axity_consolidation_back.web.dto.EstadoConciliacionDTO;
import com.josedev.axity_consolidation_back.web.mapper.EstadoConciliacionDtoMapper;
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
 * Controlador REST para la gestión de estados de conciliación.
 */
@RestController
@RequestMapping("/estados-conciliacion")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Estados de Conciliación", description = "API para gestionar estados de conciliación bancaria")
public class EstadoConciliacionController {

    private final EstadoConciliacionService estadoConciliacionService;
    private final EstadoConciliacionDtoMapper estadoConciliacionDtoMapper;

    /**
     * Obtiene todos los estados de conciliación
     * @return Lista de estados de conciliación
     */
    @GetMapping
    @Operation(
            summary = "Obtiene todos los estados de conciliación",
            description = "Devuelve una lista con todos los estados de conciliación registrados",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Estados obtenidos correctamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoConciliacionDTO.class))
                    )
            }
    )
    public ResponseEntity<List<EstadoConciliacionDTO>> getAllEstadosConciliacion() {
        log.info("Obteniendo todos los estados de conciliación");
        List<EstadoConciliacion> estados = estadoConciliacionService.getAllEstadosConciliacion();
        List<EstadoConciliacionDTO> result = estadoConciliacionDtoMapper.toEstadoConciliacionDtoList(estados);
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene un estado de conciliación por su código
     * @param codigoEstado Código del estado
     * @return El estado de conciliación si existe
     */
    @GetMapping("/{codigoEstado}")
    @Operation(
            summary = "Obtiene un estado de conciliación por su código",
            description = "Busca y devuelve un estado de conciliación específico por su código",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estado encontrado"),
                    @ApiResponse(responseCode = "404", description = "Estado no encontrado")
            }
    )
    public ResponseEntity<EstadoConciliacionDTO> getEstadoConciliacionById(
            @Parameter(description = "Código del estado") @PathVariable String codigoEstado) {

        log.info("Buscando estado de conciliación con código: {}", codigoEstado);

        return estadoConciliacionService.getEstadoConciliacionById(codigoEstado)
                .map(estadoConciliacionDtoMapper::toEstadoConciliacionDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca un estado de conciliación por su descripción
     * @param descripcion Descripción del estado
     * @return El estado de conciliación si existe
     */
    @GetMapping("/descripcion/{descripcion}")
    @Operation(
            summary = "Busca un estado de conciliación por su descripción",
            description = "Busca y devuelve un estado de conciliación que coincida con la descripción proporcionada",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estado encontrado"),
                    @ApiResponse(responseCode = "404", description = "Estado no encontrado")
            }
    )
    public ResponseEntity<EstadoConciliacionDTO> getEstadoConciliacionByDescripcion(
            @Parameter(description = "Descripción del estado") @PathVariable String descripcion) {

        log.info("Buscando estado de conciliación con descripción: {}", descripcion);

        return estadoConciliacionService.getEstadoConciliacionByDescripcion(descripcion)
                .map(estadoConciliacionDtoMapper::toEstadoConciliacionDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene el estado de conciliación "Descuadrada"
     * @return El estado "Descuadrada" si existe
     */
    @GetMapping("/descuadrada")
    @Operation(
            summary = "Obtiene el estado de conciliación 'Descuadrada'",
            description = "Devuelve el estado de conciliación que corresponde a 'Descuadrada' (código D)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estado encontrado"),
                    @ApiResponse(responseCode = "404", description = "Estado no encontrado")
            }
    )
    public ResponseEntity<EstadoConciliacionDTO> getEstadoDescuadrada() {
        log.info("Obteniendo estado 'Descuadrada'");

        return estadoConciliacionService.getEstadoDescuadrada()
                .map(estadoConciliacionDtoMapper::toEstadoConciliacionDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo estado de conciliación
     * @param estadoConciliacionDTO Datos del nuevo estado
     * @return El estado creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crea un nuevo estado de conciliación",
            description = "Registra un nuevo estado de conciliación en el sistema",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Estado creado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    public ResponseEntity<EstadoConciliacionDTO> createEstadoConciliacion(
            @Parameter(description = "Datos del estado") @Valid @RequestBody EstadoConciliacionDTO estadoConciliacionDTO) {

        log.info("Creando nuevo estado de conciliación: {}", estadoConciliacionDTO);
        EstadoConciliacion estadoConciliacion = estadoConciliacionDtoMapper.toEstadoConciliacion(estadoConciliacionDTO);
        EstadoConciliacion created = estadoConciliacionService.saveEstadoConciliacion(estadoConciliacion);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(estadoConciliacionDtoMapper.toEstadoConciliacionDto(created));
    }

    /**
     * Actualiza un estado de conciliación existente
     * @param codigoEstado Código del estado
     * @param estadoConciliacionDTO Datos actualizados del estado
     * @return El estado actualizado
     */
    @PutMapping("/{codigoEstado}")
    @Operation(
            summary = "Actualiza un estado de conciliación",
            description = "Actualiza un estado de conciliación existente en el sistema",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Estado no encontrado"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    public ResponseEntity<EstadoConciliacionDTO> updateEstadoConciliacion(
            @Parameter(description = "Código del estado") @PathVariable String codigoEstado,
            @Parameter(description = "Datos actualizados del estado")
            @Valid @RequestBody EstadoConciliacionDTO estadoConciliacionDTO) {

        log.info("Actualizando estado de conciliación con código: {}", codigoEstado);

        // Verificar consistencia entre path variable y body
        if (!codigoEstado.equals(estadoConciliacionDTO.getCodigoEstado())) {
            return ResponseEntity.badRequest().build();
        }

        EstadoConciliacion estadoConciliacion = estadoConciliacionDtoMapper.toEstadoConciliacion(estadoConciliacionDTO);

        return estadoConciliacionService.updateEstadoConciliacion(codigoEstado, estadoConciliacion)
                .map(estadoConciliacionDtoMapper::toEstadoConciliacionDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un estado de conciliación
     * @param codigoEstado Código del estado
     * @return 204 No Content si se eliminó correctamente, 404 Not Found si no existe
     */
    @DeleteMapping("/{codigoEstado}")
    @Operation(
            summary = "Elimina un estado de conciliación",
            description = "Elimina un estado de conciliación existente del sistema",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Estado eliminado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Estado no encontrado")
            }
    )
    public ResponseEntity<Void> deleteEstadoConciliacion(
            @Parameter(description = "Código del estado") @PathVariable String codigoEstado) {

        log.info("Eliminando estado de conciliación con código: {}", codigoEstado);

        boolean deleted = estadoConciliacionService.deleteEstadoConciliacion(codigoEstado);

        return deleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Verifica si existe un estado de conciliación
     * @param codigoEstado Código del estado
     * @return 200 OK si existe, 404 Not Found si no
     */
    @GetMapping("/exists/{codigoEstado}")
    @Operation(
            summary = "Verifica si existe un estado de conciliación",
            description = "Comprueba si existe un estado de conciliación con el código especificado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "El estado existe"),
                    @ApiResponse(responseCode = "404", description = "El estado no existe")
            }
    )
    public ResponseEntity<Void> existsEstadoConciliacion(
            @Parameter(description = "Código del estado") @PathVariable String codigoEstado) {

        log.info("Verificando existencia de estado con código: {}", codigoEstado);

        boolean exists = estadoConciliacionService.existsEstadoConciliacion(codigoEstado);

        return exists ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
}