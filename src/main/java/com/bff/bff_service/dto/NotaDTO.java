package com.bff.bff_service.dto;

import lombok.Data;

@Data
public class NotaDTO {
    private Long id;
    private Long estudianteId;
    private Long asignaturaId;
    private String nombreAsignatura;
    private Long cursoId;
    private String nombreCurso;
    private Double nota;
    private String descripcion;
    private String fechaCreacion;
}