package com.prototipo.platmod.controller;

import com.prototipo.platmod.entity.Leccion;
import com.prototipo.platmod.entity.Modulo;
import com.prototipo.platmod.repository.LeccionRepository;
import com.prototipo.platmod.repository.ModuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/estudiantes/contenido")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EstudianteContenidoController {

    private final LeccionRepository leccionRepository;
    private final ModuloRepository moduloRepository;

    // Obtener las lecciones de un curso
    @GetMapping("/cursos/{idCurso}/lecciones")
    public ResponseEntity<?> listarLeccionesPorCurso(@PathVariable Long idCurso) {
        List<Leccion> lecciones = leccionRepository.findByCurso_IdCurso(idCurso);

        List<Map<String, Object>> result = lecciones.stream().map(l -> {
            long totalModulos = moduloRepository.findByLeccion_IdLeccionOrderByOrdenAsc(l.getIdLeccion()).size();
            return Map.<String, Object>of(
                    "idLeccion", l.getIdLeccion(),
                    "idCurso", l.getCurso().getIdCurso(),
                    "titulo", l.getTitulo(),
                    "portadaUrl", l.getPortadaUrl() != null ? l.getPortadaUrl() : "",
                    "totalModulos", totalModulos,
                    "nombreDocente",
                    l.getDocente() != null && l.getDocente().getUsuario() != null
                            ? l.getDocente().getUsuario().getNombre()
                            : "");
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // Obtener los módulos de una lección específica
    @GetMapping("/lecciones/{idLeccion}/modulos")
    public ResponseEntity<?> listarModulosPorLeccion(@PathVariable Long idLeccion) {
        List<Modulo> modulos = moduloRepository.findByLeccion_IdLeccionOrderByOrdenAsc(idLeccion);

        List<Map<String, Object>> result = modulos.stream().map(m -> Map.<String, Object>of(
                "idModulo", m.getIdModulo(),
                "idLeccion", m.getLeccion().getIdLeccion(),
                "tituloSeccion", m.getTituloSeccion(),
                "descripcion", m.getDescripcion() != null ? m.getDescripcion() : "",
                "portadaUrl", m.getPortadaUrl() != null ? m.getPortadaUrl() : "",
                "recursos", m.getRecursos() != null ? m.getRecursos() : "[]",
                "orden", m.getOrden())).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // Obtener un módulo específico por su ID
    @GetMapping("/modulos/{idModulo}")
    public ResponseEntity<?> obtenerModulo(@PathVariable Long idModulo) {
        return moduloRepository.findById(idModulo)
                .map(m -> ResponseEntity.ok(Map.<String, Object>of(
                        "idModulo", m.getIdModulo(),
                        "idLeccion", m.getLeccion().getIdLeccion(),
                        "tituloSeccion", m.getTituloSeccion(),
                        "descripcion", m.getDescripcion() != null ? m.getDescripcion() : "",
                        "portadaUrl", m.getPortadaUrl() != null ? m.getPortadaUrl() : "",
                        "recursos", m.getRecursos() != null ? m.getRecursos() : "[]",
                        "orden", m.getOrden())))
                .orElse(ResponseEntity.notFound().build());
    }
}
