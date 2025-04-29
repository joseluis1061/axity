package com.josedev.axity_consolidation_back.web.controller;

import com.josedev.axity_consolidation_back.domain.model.Producto;
import com.josedev.axity_consolidation_back.domain.service.ProductoService;
import com.josedev.axity_consolidation_back.web.dto.ProductoDTO;
import com.josedev.axity_consolidation_back.web.mapper.ProductoDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de productos.
 */
@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "API para la gestión de productos")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoDtoMapper productoDtoMapper;

    /**
     * Obtiene todos los productos disponibles.
     *
     * @return Lista de productos
     */
    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ProductoDTO>> getAllProductos() {
        List<Producto> productos = productoService.getAllProductos();
        List<ProductoDTO> productoDTOs = productoDtoMapper.toProductoDtoList(productos);
        return ResponseEntity.ok(productoDTOs);
    }

    /**
     * Obtiene un producto por su código.
     *
     * @param codigoProducto Código del producto
     * @return Producto encontrado o 404 si no existe
     */
    @GetMapping("/{codigoProducto}")
    @Operation(summary = "Obtener un producto por código", description = "Retorna un producto específico según su código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable String codigoProducto) {
        return productoService.getProductoById(codigoProducto)
                .map(producto -> ResponseEntity.ok(productoDtoMapper.toProductoDto(producto)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo producto.
     *
     * @param productoDTO Datos del nuevo producto
     * @return Producto creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del producto inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ProductoDTO> createProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        // Verificar si ya existe un producto con el mismo código
        if (productoService.existsProducto(productoDTO.getCodigoProducto())) {
            return ResponseEntity.badRequest().build();
        }

        Producto producto = productoDtoMapper.toProducto(productoDTO);
        Producto savedProducto = productoService.saveProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoDtoMapper.toProductoDto(savedProducto));
    }

    /**
     * Actualiza un producto existente.
     *
     * @param codigoProducto Código del producto a actualizar
     * @param productoDTO Nuevos datos del producto
     * @return Producto actualizado o 404 si no existe
     */
    @PutMapping("/{codigoProducto}")
    @Operation(summary = "Actualizar un producto", description = "Actualiza un producto existente con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del producto inválidos"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ProductoDTO> updateProducto(
            @PathVariable String codigoProducto,
            @Valid @RequestBody ProductoDTO productoDTO) {

        if (!codigoProducto.equals(productoDTO.getCodigoProducto())) {
            return ResponseEntity.badRequest().build();
        }

        return productoService.updateProducto(codigoProducto, productoDtoMapper.toProducto(productoDTO))
                .map(producto -> ResponseEntity.ok(productoDtoMapper.toProductoDto(producto)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un producto por su código.
     *
     * @param codigoProducto Código del producto a eliminar
     * @return 204 No Content si se eliminó correctamente, 404 si no existe
     */
    @DeleteMapping("/{codigoProducto}")
    @Operation(summary = "Eliminar un producto", description = "Elimina un producto según su código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteProducto(@PathVariable String codigoProducto) {
        if (productoService.deleteProducto(codigoProducto)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}