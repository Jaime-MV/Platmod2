package com.prototipo.platmod.controller;

import com.prototipo.platmod.entity.*;
import com.prototipo.platmod.repository.*;
import com.prototipo.platmod.service.LeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/docentes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DocenteController {

    private final DocenteRepository docenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final AsignacionDocenteRepository asignacionRepository;
    private final CursoRepository cursoRepository;
    private final LeccionRepository leccionRepository;
    private final LeccionService leccionService;
    private final ModuloRepository moduloRepository;

    // ============ HOME (existing) ============

    @GetMapping("/home")
    public ResponseEntity<List<com.prototipo.platmod.dto.DocenteHomeDTO>> obtenerDocentesParaHome() {
        List<com.prototipo.platmod.dto.DocenteHomeDTO> docentes = docenteRepository.obtenerDocentesHome();
        return ResponseEntity.ok(docentes.stream().limit(8).toList());
    }

    // ============ MIS CURSOS ASIGNADOS ============

    @GetMapping("/mis-cursos")
    public ResponseEntity<List<Map<String, Object>>> misCursos(Authentication authentication) {
        Docente docente = getDocente(authentication);

        List<AsignacionDocente> asignaciones = asignacionRepository.findByUsuario(docente.getUsuario());

        List<Map<String, Object>> cursos = asignaciones.stream().map(a -> {
            Curso curso = a.getCurso();
            long totalLecciones = leccionRepository.findByCurso_IdCurso(curso.getIdCurso()).stream()
                    .filter(l -> l.getDocente().getIdDocente().equals(docente.getIdDocente()))
                    .count();

            return Map.<String, Object>of(
                    "idCurso", curso.getIdCurso(),
                    "titulo", curso.getTitulo(),
                    "descripcion", curso.getDescripcion() != null ? curso.getDescripcion() : "",
                    "portadaUrl", curso.getPortadaUrl() != null ? curso.getPortadaUrl() : "",
                    "totalLecciones", totalLecciones);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(cursos);
    }

    // ============ LECCIONES CRUD ============

    @GetMapping("/cursos/{idCurso}/lecciones")
    public ResponseEntity<?> listarLecciones(@PathVariable Long idCurso, Authentication authentication) {
        Docente docente = getDocente(authentication);

        // Verify the docente is assigned to this course
        if (!isAssignedToCourse(docente, idCurso)) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "No estás asignado a este curso"));
        }

        List<Leccion> lecciones = leccionRepository.findByCurso_IdCurso(idCurso).stream()
                .filter(l -> l.getDocente().getIdDocente().equals(docente.getIdDocente()))
                .collect(Collectors.toList());

        List<Map<String, Object>> result = lecciones.stream().map(l -> {
            long totalModulos = moduloRepository.findByLeccion_IdLeccionOrderByOrdenAsc(l.getIdLeccion()).size();
            return Map.<String, Object>of(
                    "idLeccion", l.getIdLeccion(),
                    "idCurso", l.getCurso().getIdCurso(),
                    "titulo", l.getTitulo(),
                    "portadaUrl", l.getPortadaUrl() != null ? l.getPortadaUrl() : "",
                    "totalModulos", totalModulos);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/cursos/{idCurso}/lecciones")
    public ResponseEntity<?> crearLeccion(
            @PathVariable Long idCurso,
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        Docente docente = getDocente(authentication);

        if (!isAssignedToCourse(docente, idCurso)) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "No estás asignado a este curso"));
        }

        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Leccion leccion = new Leccion();
        leccion.setCurso(curso);
        leccion.setDocente(docente);
        leccion.setTitulo(body.get("titulo"));
        leccion.setPortadaUrl(body.getOrDefault("portadaUrl", null));

        Leccion creada = leccionService.crear(leccion);

        return ResponseEntity.ok(Map.of(
                "idLeccion", creada.getIdLeccion(),
                "idCurso", curso.getIdCurso(),
                "titulo", creada.getTitulo(),
                "portadaUrl", creada.getPortadaUrl() != null ? creada.getPortadaUrl() : ""));
    }

    @PutMapping("/lecciones/{idLeccion}")
    public ResponseEntity<?> editarLeccion(
            @PathVariable Long idLeccion,
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        Docente docente = getDocente(authentication);
        Leccion leccion = leccionService.obtenerPorId(idLeccion);

        // Only the owning docente can edit
        if (!leccion.getDocente().getIdDocente().equals(docente.getIdDocente())) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "No puedes editar una lección que no es tuya"));
        }

        if (body.containsKey("titulo")) {
            leccion.setTitulo(body.get("titulo"));
        }
        if (body.containsKey("portadaUrl")) {
            leccion.setPortadaUrl(body.get("portadaUrl"));
        }

        Leccion actualizada = leccionService.crear(leccion);

        return ResponseEntity.ok(Map.of(
                "idLeccion", actualizada.getIdLeccion(),
                "idCurso", actualizada.getCurso().getIdCurso(),
                "titulo", actualizada.getTitulo(),
                "portadaUrl", actualizada.getPortadaUrl() != null ? actualizada.getPortadaUrl() : ""));
    }

    @DeleteMapping("/lecciones/{idLeccion}")
    @Transactional
    public ResponseEntity<?> eliminarLeccion(@PathVariable Long idLeccion, Authentication authentication) {
        Docente docente = getDocente(authentication);
        Leccion leccion = leccionService.obtenerPorId(idLeccion);

        if (!leccion.getDocente().getIdDocente().equals(docente.getIdDocente())) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "No puedes eliminar una lección que no es tuya"));
        }

        // Delete modules first
        moduloRepository.deleteByLeccion_IdLeccion(idLeccion);
        leccionService.eliminar(idLeccion);

        return ResponseEntity.ok().build();
    }

    // ============ MODULOS CRUD ============

    @GetMapping("/lecciones/{idLeccion}/modulos")
    public ResponseEntity<?> listarModulos(@PathVariable Long idLeccion, Authentication authentication) {
        Docente docente = getDocente(authentication);
        Leccion leccion = leccionService.obtenerPorId(idLeccion);

        if (!leccion.getDocente().getIdDocente().equals(docente.getIdDocente())) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "No tienes acceso a esta lección"));
        }

        List<Modulo> modulos = moduloRepository.findByLeccion_IdLeccionOrderByOrdenAsc(idLeccion);

        List<Map<String, Object>> result = modulos.stream().map(m -> Map.<String, Object>of(
                "idModulo", m.getIdModulo(),
                "idLeccion", m.getLeccion().getIdLeccion(),
                "tituloSeccion", m.getTituloSeccion(),
                "tipoContenido", m.getTipoContenido() != null ? m.getTipoContenido() : "",
                "urlRecurso", m.getUrlRecurso() != null ? m.getUrlRecurso() : "",
                "orden", m.getOrden())).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/lecciones/{idLeccion}/modulos")
    public ResponseEntity<?> crearModulo(
            @PathVariable Long idLeccion,
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        Docente docente = getDocente(authentication);
        Leccion leccion = leccionService.obtenerPorId(idLeccion);

        if (!leccion.getDocente().getIdDocente().equals(docente.getIdDocente())) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "No tienes acceso a esta lección"));
        }

        Modulo modulo = new Modulo();
        modulo.setLeccion(leccion);
        modulo.setTituloSeccion(body.get("tituloSeccion"));
        modulo.setTipoContenido(body.getOrDefault("tipoContenido", null));
        modulo.setUrlRecurso(body.getOrDefault("urlRecurso", null));

        // Auto-calculate order if not provided
        int orden;
        try {
            orden = Integer.parseInt(body.getOrDefault("orden", "0"));
        } catch (NumberFormatException e) {
            // If no order, put at the end
            List<Modulo> existentes = moduloRepository.findByLeccion_IdLeccionOrderByOrdenAsc(idLeccion);
            orden = existentes.isEmpty() ? 1 : existentes.get(existentes.size() - 1).getOrden() + 1;
        }
        modulo.setOrden(orden);

        Modulo creado = moduloRepository.save(modulo);

        return ResponseEntity.ok(Map.of(
                "idModulo", creado.getIdModulo(),
                "idLeccion", leccion.getIdLeccion(),
                "tituloSeccion", creado.getTituloSeccion(),
                "tipoContenido", creado.getTipoContenido() != null ? creado.getTipoContenido() : "",
                "urlRecurso", creado.getUrlRecurso() != null ? creado.getUrlRecurso() : "",
                "orden", creado.getOrden()));
    }

    @PutMapping("/modulos/{idModulo}")
    public ResponseEntity<?> editarModulo(
            @PathVariable Long idModulo,
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        Docente docente = getDocente(authentication);
        Modulo modulo = moduloRepository.findById(idModulo)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        if (!modulo.getLeccion().getDocente().getIdDocente().equals(docente.getIdDocente())) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "No puedes editar un módulo que no es tuyo"));
        }

        if (body.containsKey("tituloSeccion")) {
            modulo.setTituloSeccion(body.get("tituloSeccion"));
        }
        if (body.containsKey("tipoContenido")) {
            modulo.setTipoContenido(body.get("tipoContenido"));
        }
        if (body.containsKey("urlRecurso")) {
            modulo.setUrlRecurso(body.get("urlRecurso"));
        }
        if (body.containsKey("orden")) {
            modulo.setOrden(Integer.parseInt(body.get("orden")));
        }

        Modulo actualizado = moduloRepository.save(modulo);

        return ResponseEntity.ok(Map.of(
                "idModulo", actualizado.getIdModulo(),
                "idLeccion", actualizado.getLeccion().getIdLeccion(),
                "tituloSeccion", actualizado.getTituloSeccion(),
                "tipoContenido", actualizado.getTipoContenido() != null ? actualizado.getTipoContenido() : "",
                "urlRecurso", actualizado.getUrlRecurso() != null ? actualizado.getUrlRecurso() : "",
                "orden", actualizado.getOrden()));
    }

    @DeleteMapping("/modulos/{idModulo}")
    public ResponseEntity<?> eliminarModulo(@PathVariable Long idModulo, Authentication authentication) {
        Docente docente = getDocente(authentication);
        Modulo modulo = moduloRepository.findById(idModulo)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        if (!modulo.getLeccion().getDocente().getIdDocente().equals(docente.getIdDocente())) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "No puedes eliminar un módulo que no es tuyo"));
        }

        moduloRepository.delete(modulo);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/lecciones/{idLeccion}/modulos/orden")
    @Transactional
    public ResponseEntity<?> reordenarModulos(
            @PathVariable Long idLeccion,
            @RequestBody List<Map<String, Object>> ordenList,
            Authentication authentication) {

        Docente docente = getDocente(authentication);
        Leccion leccion = leccionService.obtenerPorId(idLeccion);

        if (!leccion.getDocente().getIdDocente().equals(docente.getIdDocente())) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "No tienes acceso a esta lección"));
        }

        for (Map<String, Object> item : ordenList) {
            Long idModulo = Long.valueOf(item.get("idModulo").toString());
            Integer nuevoOrden = Integer.valueOf(item.get("orden").toString());

            moduloRepository.findById(idModulo).ifPresent(modulo -> {
                modulo.setOrden(nuevoOrden);
                moduloRepository.save(modulo);
            });
        }

        return ResponseEntity.ok().build();
    }

    // ============ HELPERS ============

    private Usuario getUsuario(Authentication authentication) {
        String correo = authentication.getName();
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private Docente getDocente(Authentication authentication) {
        Usuario usuario = getUsuario(authentication);
        return docenteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado para este usuario"));
    }

    private boolean isAssignedToCourse(Docente docente, Long idCurso) {
        Curso curso = cursoRepository.findById(idCurso).orElse(null);
        if (curso == null)
            return false;
        return asignacionRepository.existsByCursoAndUsuario(curso, docente.getUsuario());
    }
}
