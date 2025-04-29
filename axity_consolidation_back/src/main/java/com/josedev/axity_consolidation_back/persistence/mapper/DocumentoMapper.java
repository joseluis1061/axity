package com.josedev.axity_consolidation_back.persistence.mapper;

import com.josedev.axity_consolidation_back.domain.model.Documento;
import com.josedev.axity_consolidation_back.persistence.entity.DocumentoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Interfaz que define los métodos de mapeo entre DocumentoEntity y Documento.
 * Utiliza MapStruct para la implementación automática de estos métodos.
 */
@Mapper(componentModel = "spring")
public interface DocumentoMapper {

    /**
     * Convierte una entidad de documento a un modelo de dominio
     * @param documentoEntity La entidad de documento proveniente de la base de datos
     * @return El modelo de dominio documento
     */
    @Mapping(target = "codigoDocumento", source = "codigoDocumento")
    @Mapping(target = "descripcion", source = "descripcion")
    Documento toDocumento(DocumentoEntity documentoEntity);

    /**
     * Convierte un modelo de dominio documento a una entidad de documento
     * @param documento El modelo de dominio documento
     * @return La entidad de documento para persistir en base de datos
     */
    @Mapping(target = "codigoDocumento", source = "codigoDocumento")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "sucursalProductos", ignore = true)
    DocumentoEntity toDocumentoEntity(Documento documento);

    /**
     * Convierte una lista de entidades documento a una lista de modelos de dominio
     * @param documentoEntities Lista de entidades documento
     * @return Lista de modelos de dominio documento
     */
    List<Documento> toDocumentoList(List<DocumentoEntity> documentoEntities);
}
/*
    DocumentoMapper INSTANCE = Mappers.getMapper(DocumentoMapper.class);

    Documento entityToModel(DocumentoEntity entity);

    DocumentoEntity modelToEntity(Documento model);

    DocumentoDTO modelToDto(Documento model);

    Documento dtoToModel(DocumentoDTO dto);

    List<Documento> entityListToModelList(List<DocumentoEntity> entities);

    List<DocumentoDTO> modelListToDtoList(List<Documento> models);
* */