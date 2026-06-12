package com.bff.bff_service.dto;

import lombok.Data;

@Data
public class RankingEstudianteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private Double promedioNotas;
    private Double porcentajeAsistencia;
    private Integer posicion;
}