package com.prototipo.platmod.controller;

import com.prototipo.platmod.dto.CursoDTO;
import com.prototipo.platmod.entity.Curso;
import com.prototipo.platmod.repository.AsignacionDocenteRepository; // Import correcto
import com.prototipo.platmod.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "*")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    // 1. INYECTAR EL REPOSITORIO AQUÍ 👇
    @Autowired
    private AsignacionDocenteRepository asignacionDocenteRepository;

    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarCursos() {
        List<Curso> cursos = cursoService.obtenerTodos();

        List<CursoDTO> dtos = cursos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> obtenerCurso(@PathVariable Long id) {
        Curso curso = cursoService.obtenerPorId(id);
        return ResponseEntity.ok(convertirADTO(curso));
    }

    private CursoDTO convertirADTO(Curso curso) {
        // 2. USAR LA INSTANCIA (minúscula) EN LUGAR DE LA CLASE (Mayúscula) 👇
        long totalDocentes = asignacionDocenteRepository.countByCurso_IdCurso(curso.getIdCurso());

        return new CursoDTO(
                curso.getIdCurso(),
                curso.getTitulo(),
                curso.getDescripcion(),
                curso.getPortadaUrl(),
                curso.getEstado(),
                totalDocentes
        );
    }
}