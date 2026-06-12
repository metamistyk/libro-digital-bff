package com.bff.bff_service.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bff.bff_service.dto.AnotacionDTO;
import com.bff.bff_service.dto.AsistenciaDTO;
import com.bff.bff_service.dto.EstudianteDTO;
import com.bff.bff_service.dto.NotaDTO;
import com.bff.bff_service.dto.RankingEstudianteDTO;
import com.bff.bff_service.dto.ResumenEstudianteDTO;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BffService {

    private final RestTemplate restTemplate;

    @Value("${usuarios.service.url}")
    private String usuariosServiceUrl;

    @Value("${asistencia.service.url}")
    private String asistenciaServiceUrl;

    // Construye los headers con el token para llamar a otros servicios
    private HttpHeaders buildHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        return headers;
    }

    // Obtiene la lista de todos los estudiantes
    public List<EstudianteDTO> listarEstudiantes(String token) {

        HttpEntity<Void> entity = new HttpEntity<>(buildHeaders(token));

        ResponseEntity<EstudianteDTO[]> response = restTemplate.exchange(
            usuariosServiceUrl + "/api/v1/estudiantes",
            HttpMethod.GET,
            entity,
            EstudianteDTO[].class);

        EstudianteDTO[] body = response.getBody();
        return body != null ? Arrays.asList(body) : Collections.emptyList();
    }

    // Obtiene el resumen completo de un estudiante agregando datos de 3 servicios
    public ResumenEstudianteDTO obtenerResumenEstudiante(Long estudianteId, String token) {

        HttpEntity<Void> entity = new HttpEntity<>(buildHeaders(token));

        // Llama a usuarios-service
        ResponseEntity<EstudianteDTO> estudianteResponse = restTemplate.exchange(
            usuariosServiceUrl + "/api/v1/estudiantes/" + estudianteId,
            HttpMethod.GET,
            entity,
            EstudianteDTO.class);

        // Llama a asistencia-service para asistencias
        ResponseEntity<AsistenciaDTO[]> asistenciasResponse = restTemplate.exchange(
            asistenciaServiceUrl + "/api/v1/asistencias?estudianteId=" + estudianteId,
            HttpMethod.GET,
            entity,
            AsistenciaDTO[].class);

        // Llama a asistencia-service para notas
        ResponseEntity<NotaDTO[]> notasResponse = restTemplate.exchange(
            asistenciaServiceUrl + "/api/v1/notas?estudianteId=" + estudianteId,
            HttpMethod.GET,
            entity,
            NotaDTO[].class);

        // Llama a asistencia-service para anotaciones
        ResponseEntity<AnotacionDTO[]> anotacionesResponse = restTemplate.exchange(
            asistenciaServiceUrl + "/api/v1/anotaciones?estudianteId=" + estudianteId,
            HttpMethod.GET,
            entity,
            AnotacionDTO[].class);

        EstudianteDTO estudiante = estudianteResponse.getBody();

        List<AsistenciaDTO> asistencias = asistenciasResponse.getBody() != null
            ? Arrays.asList(asistenciasResponse.getBody())
            : Collections.emptyList();

        List<NotaDTO> notas = notasResponse.getBody() != null
            ? Arrays.asList(notasResponse.getBody())
            : Collections.emptyList();

        List<AnotacionDTO> anotaciones = anotacionesResponse.getBody() != null
            ? Arrays.asList(anotacionesResponse.getBody())
            : Collections.emptyList();

        // Calcula porcentaje de asistencia
        double porcentajeAsistencia = 0.0;
        if (!asistencias.isEmpty()) {
            long presentes = asistencias.stream()
                .filter(a -> "PRESENTE".equals(a.getEstado()))
                .count();
            porcentajeAsistencia = (presentes * 100.0) / asistencias.size();
        }

        // Calcula promedio de notas
        double promedioNotas = notas.stream()
            .mapToDouble(NotaDTO::getNota)
            .average()
            .orElse(0.0);

        // Arma el resumen
        ResumenEstudianteDTO resumen = new ResumenEstudianteDTO();
        resumen.setId(estudiante.getId());
        resumen.setNombre(estudiante.getNombre());
        resumen.setApellido(estudiante.getApellido());
        resumen.setEmail(estudiante.getEmail());
        resumen.setAsistencias(asistencias);
        resumen.setNotas(notas);
        resumen.setAnotaciones(anotaciones);
        resumen.setPorcentajeAsistencia(porcentajeAsistencia);
        resumen.setPromedioNotas(promedioNotas);

        return resumen;
    }

public List<RankingEstudianteDTO> obtenerRankingEstudiantes(String token) {

    HttpEntity<Void> entity = new HttpEntity<>(buildHeaders(token));

    // Obtiene todos los estudiantes
    ResponseEntity<EstudianteDTO[]> estudiantesResponse = restTemplate.exchange(
        usuariosServiceUrl + "/api/v1/estudiantes",
        HttpMethod.GET,
        entity,
        EstudianteDTO[].class);

    EstudianteDTO[] estudiantes = estudiantesResponse.getBody();
    if (estudiantes == null) return Collections.emptyList();

    List<RankingEstudianteDTO> ranking = new java.util.ArrayList<>();
    int posicion = 1;

    // Para cada estudiante obtiene sus notas y asistencias y calcula promedios
    for (EstudianteDTO estudiante : estudiantes) {

        ResponseEntity<NotaDTO[]> notasResponse = restTemplate.exchange(
            asistenciaServiceUrl + "/api/v1/notas?estudianteId=" + estudiante.getId(),
            HttpMethod.GET,
            entity,
            NotaDTO[].class);

        ResponseEntity<AsistenciaDTO[]> asistenciasResponse = restTemplate.exchange(
            asistenciaServiceUrl + "/api/v1/asistencias?estudianteId=" + estudiante.getId(),
            HttpMethod.GET,
            entity,
            AsistenciaDTO[].class);

        List<NotaDTO> notas = notasResponse.getBody() != null
            ? Arrays.asList(notasResponse.getBody())
            : Collections.emptyList();

        List<AsistenciaDTO> asistencias = asistenciasResponse.getBody() != null
            ? Arrays.asList(asistenciasResponse.getBody())
            : Collections.emptyList();

        double promedio = notas.stream()
            .mapToDouble(NotaDTO::getNota)
            .average()
            .orElse(0.0);

        double porcentaje = 0.0;
        if (!asistencias.isEmpty()) {
            long presentes = asistencias.stream()
                .filter(a -> "PRESENTE".equals(a.getEstado()))
                .count();
            porcentaje = (presentes * 100.0) / asistencias.size();
        }

        RankingEstudianteDTO item = new RankingEstudianteDTO();
        item.setId(estudiante.getId());
        item.setNombre(estudiante.getNombre());
        item.setApellido(estudiante.getApellido());
        item.setPromedioNotas(promedio);
        item.setPorcentajeAsistencia(porcentaje);
        item.setPosicion(posicion++);

        ranking.add(item);
    }

        // Ordena de mayor a menor promedio
        ranking.sort((a, b) -> Double.compare(b.getPromedioNotas(), a.getPromedioNotas()));

        // Reasigna posiciones después de ordenar
        for (int i = 0; i < ranking.size(); i++) {
            ranking.get(i).setPosicion(i + 1);
        }

        return ranking;
    }

}