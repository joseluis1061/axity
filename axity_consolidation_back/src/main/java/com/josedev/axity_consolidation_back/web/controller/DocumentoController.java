package com.josedev.axity_consolidation_back.web.controller;

import com.josedev.axity_consolidation_back.domain.model.Documento;
import com.josedev.axity_consolidation_back.domain.repository.DocumentoService;
import com.josedev.axity_consolidation_back.web.dto.DocumentoDTO;
import com.josedev.axity_consolidation_back.web.mapper.DocumentoDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de documentos.
 */
@RestController
@RequestMapping("/documentos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Documentos", description = "API para la gestión de documentos")
public class DocumentoController {

    private final DocumentoService documentoService;
    private final DocumentoDTOMapper documentoDTOMapper;

    /**
     * Obtiene todos los documentos
     * @return Lista de documentos
     */
    @GetMapping
    @Operation(summary = "Obtener todos los documentos", description = "Retorna una lista con todos los documentos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(schema = @Schema(implementation = DocumentoDTO.class)))
    })
    public ResponseEntity<List<DocumentoDTO>> getAllDocumentos() {
        log.info("REST request para obtener todos los documentos");
        List<Documento> documentos = documentoService.getAllDocumentos();
        return ResponseEntity.ok(documentoDTOMapper.toDocumentoDTOList(documentos));
    }

    /**
     * Obtiene un documento por su código
     * @param codigoDocumento Código del documento
     * @return El documento si existe
     */
    @GetMapping("/{codigoDocumento}")
    @Operation(summary = "Obtener documento por código", description = "Retorna un documento por su código único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documento encontrado",
                    content = @Content(schema = @Schema(implementation = DocumentoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Documento no encontrado", content = @Content)
    })
    public ResponseEntity<DocumentoDTO> getDocumentoById(
            @Parameter(description = "Código del documento", required = true)
            @PathVariable String codigoDocumento) {
        log.info("REST request para obtener documento: {}", codigoDocumento);
        return documentoService.getDocumentoById(codigoDocumento)
                .map(documentoDTOMapper::toDocumentoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo documento
     * @param documentoDTO Datos del documento
     * @return El documento creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear nuevo documento", description = "Crea un nuevo documento con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Documento creado exitosamente",
                    content = @Content(schema = @Schema(implementation = DocumentoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    public ResponseEntity<DocumentoDTO> createDocumento(
            @Parameter(description = "Datos del documento", required = true)
            @Valid @RequestBody DocumentoDTO documentoDTO) {
        log.info("REST request para crear documento: {}", documentoDTO);

        if (documentoService.existsDocumento(documentoDTO.getCodigoDocumento())) {
            log.warn("Ya existe un documento con código: {}", documentoDTO.getCodigoDocumento());
            return ResponseEntity.badRequest().build();
        }

        Documento documento = documentoDTOMapper.toDocumento(documentoDTO);
        Documento documentoCreado = documentoService.saveDocumento(documento);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentoDTOMapper.toDocumentoDTO(documentoCreado));
    }

    /**
     * Actualiza un documento existente
     * @param codigoDocumento Código del documento a actualizar
     * @param documentoDTO Nuevos datos del documento
     * @return El documento actualizado
     */
    @PutMapping("/{codigoDocumento}")
    @Operation(summary = "Actualizar documento", description = "Actualiza un documento existente por su código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documento actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = DocumentoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Documento no encontrado", content = @Content)
    })
    public ResponseEntity<DocumentoDTO> updateDocumento(
            @Parameter(description = "Código del documento", required = true)
            @PathVariable String codigoDocumento,
            @Parameter(description = "Datos actualizados del documento", required = true)
            @Valid @RequestBody DocumentoDTO documentoDTO) {
        log.info("REST request para actualizar documento: {}", codigoDocumento);

        // Verificar que el código en el path coincide con el del body
        if (!codigoDocumento.equals(documentoDTO.getCodigoDocumento())) {
            log.warn("El código del documento en el path ({}) no coincide con el del body ({})",
                    codigoDocumento, documentoDTO.getCodigoDocumento());
            return ResponseEntity.badRequest().build();
        }

        Documento documento = documentoDTOMapper.toDocumento(documentoDTO);
        return documentoService.updateDocumento(codigoDocumento, documento)
                .map(documentoDTOMapper::toDocumentoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un documento
     * @param codigoDocumento Código del documento a eliminar
     * @return Respuesta sin contenido si se eliminó correctamente
     */
    @DeleteMapping("/{codigoDocumento}")
    @Operation(summary = "Eliminar documento", description = "Elimina un documento por su código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Documento eliminado exitosamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Documento no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar el documento porque está siendo utilizado", content = @Content)
    })
    public ResponseEntity<Void> deleteDocumento(
            @Parameter(description = "Código del documento", required = true)
            @PathVariable String codigoDocumento) {
        log.info("REST request para eliminar documento: {}", codigoDocumento);

        try {
            boolean eliminado = documentoService.deleteDocumento(codigoDocumento);
            if (eliminado) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalStateException e) {
            log.warn("No se puede eliminar el documento porque está siendo utilizado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}