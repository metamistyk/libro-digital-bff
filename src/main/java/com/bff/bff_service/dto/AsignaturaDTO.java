package com.bff.bff_service.dto;

import lombok.Data;

@Data
public class AsignaturaDTO {
    private Long id;
    private String nombre;
    private String codigo;
    private Long cursoId;
    private String nombreCurso;
}