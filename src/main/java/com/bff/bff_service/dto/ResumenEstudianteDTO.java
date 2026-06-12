package com.bff.bff_service.dto;

import java.util.List;
import lombok.Data;

@Data
public class ResumenEstudianteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private List<AsistenciaDTO> asistencias;
    private List<NotaDTO> notas;
    private List<AnotacionDTO> anotaciones;
    private Double porcentajeAsistencia;
    private Double promedioNotas;
}