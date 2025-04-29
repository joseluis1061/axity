package com.josedev.axity_consolidation_back.web.mapper;

import com.josedev.axity_consolidation_back.domain.model.Documento;
import com.josedev.axity_consolidation_back.web.dto.DocumentoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper para convertir entre DocumentoDTO y el modelo de dominio Documento.
 */
@Mapper(componentModel = "spring")
public interface DocumentoDTOMapper {

    /**
     * Convierte un DTO a modelo de dominio
     * @param documentoDTO El DTO recibido del cliente
     * @return El modelo de dominio correspondiente
     */
    @Mapping(target = "codigoDocumento", source = "codigoDocumento")
    @Mapping(target = "descripcion", source = "descripcion")
    Documento toDocumento(DocumentoDTO documentoDTO);

    /**
     * Convierte un modelo de dominio a DTO
     * @param documento El modelo de dominio
     * @return El DTO para enviar al cliente
     */
    @Mapping(target = "codigoDocumento", source = "codigoDocumento")
    @Mapping(target = "descripcion", source = "descripcion")
    DocumentoDTO toDocumentoDTO(Documento documento);

    /**
     * Convierte una lista de modelos de dominio a una lista de DTOs
     * @param documentos Lista de modelos de dominio
     * @return Lista de DTOs
     */
    List<DocumentoDTO> toDocumentoDTOList(List<Documento> documentos);
}