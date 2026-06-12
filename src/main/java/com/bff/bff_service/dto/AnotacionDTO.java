package com.bff.bff_service.dto;

import lombok.Data;

@Data
public class AnotacionDTO {
    private Long id;
    private Long estudianteId;
    private String descripcion;
    private String fechaCreacion;
    private String tipo;
}