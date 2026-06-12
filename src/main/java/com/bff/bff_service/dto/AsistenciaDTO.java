package com.bff.bff_service.dto;

import lombok.Data;

@Data
public class AsistenciaDTO {
    private Long id;
    private Long estudianteId;
    private String fechaHora;
    private String estado;
}