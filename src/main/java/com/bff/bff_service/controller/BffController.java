package com.bff.bff_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bff.bff_service.dto.EstudianteDTO;
import com.bff.bff_service.dto.RankingEstudianteDTO;
import com.bff.bff_service.dto.ResumenEstudianteDTO;
import com.bff.bff_service.service.BffService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/bff")
@RequiredArgsConstructor
public class BffController {

    private final BffService bffService;

    // Devuelve la lista de todos los estudiantes
    @GetMapping("/estudiantes")
    public ResponseEntity<List<EstudianteDTO>> listarEstudiantes(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(bffService.listarEstudiantes(token));
    }

    // Devuelve el resumen completo de un estudiante:
    // datos personales + asistencias + notas + anotaciones + porcentajes
    @GetMapping("/estudiantes/{id}/resumen")
    public ResponseEntity<ResumenEstudianteDTO> obtenerResumen(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(bffService.obtenerResumenEstudiante(id, token));
    }

    @GetMapping("/ranking/estudiantes")
    public ResponseEntity<List<RankingEstudianteDTO>> obtenerRankingEstudiantes(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(bffService.obtenerRankingEstudiantes(token));
    }
}