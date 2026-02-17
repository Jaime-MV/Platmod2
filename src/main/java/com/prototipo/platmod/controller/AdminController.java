package com.prototipo.platmod.controller;

import com.prototipo.platmod.dto.CursoDTO;
import com.prototipo.platmod.dto.DocenteAsignacionDTO;
import com.prototipo.platmod.entity.*;
import com.prototipo.platmod.repository.*;
import com.prototipo.platmod.service.AsignacionDocenteService;
import com.prototipo.platmod.service.PlanSuscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private PlanSuscripcionService planService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AsignacionDocenteService asignacionService;
    @Autowired
    private AsignacionDocenteRepository asignacionRepository;
    @Autowired
    private DocenteRepository docenteRepository;

    // --- GESTION DE PLANES ---
    @PutMapping("/planes/{id}")
    public ResponseEntity<PlanSuscripcion> actualizarPlan(@PathVariable Long id,
            @RequestBody PlanSuscripcion planDetalles) {
        try {
            return ResponseEntity.ok(planService.actualizar(id, planDetalles));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/planes/{idPlan}/beneficios")
    public ResponseEntity<PlanSuscripcion> agregarBeneficio(@PathVariable Long idPlan,
            @RequestBody java.util.Map<String, String> payload) {
        String descripcion = payload.get("descripcion");
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        planService.agregarBeneficio(idPlan, descripcion);
        return ResponseEntity.ok(planService.obtenerPorId(idPlan));
    }

    @DeleteMapping("/planes/beneficios/{idBeneficio}")
    public ResponseEntity<?> eliminarBeneficio(@PathVariable Long idBeneficio) {
        planService.eliminarBeneficio(idBeneficio);
        return ResponseEntity.ok().build();
    }

    // --- GESTION DE CURSOS ---

    // 1. LISTAR CURSOS CON CONTEO DE DOCENTES (Actualizado)
    @GetMapping("/cursos")
    public List<CursoDTO> listarCursosAdmin() {
        List<Curso> cursos = cursoRepository.findAll(Sort.by(Sort.Direction.ASC, "idCurso"));

        return cursos.stream().map(curso -> {
            // Contamos cuántos docentes tiene este curso
            long totalDocentes = asignacionRepository.countByCurso_IdCurso(curso.getIdCurso());

            return new CursoDTO(
                    curso.getIdCurso(),
                    curso.getTitulo(),
                    curso.getDescripcion(),
                    curso.getPortadaUrl(),
                    curso.getEstado(),
                    totalDocentes // Pasamos el conteo al DTO
            );
        }).collect(Collectors.toList());
    }

    @PostMapping("/cursos")
    public Curso crearCurso(@RequestBody Curso curso) {
        // 1. Obtener el usuario autenticado del contexto de seguridad
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
                .getName();

        // 2. Buscar el usuario en la BD
        Usuario admin = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));

        // 3. Asignar el admin al curso
        curso.setAdministrador(admin);

        return cursoRepository.save(curso);
    }

    @PutMapping("/cursos/{id}")
    public ResponseEntity<Curso> editarCurso(@PathVariable Long id, @RequestBody Curso cursoDetalles) {
        return cursoRepository.findById(id).map(curso -> {
            // Solo actualizamos si el dato viene en el JSON (evita nulos accidentales)
            if (cursoDetalles.getTitulo() != null)
                curso.setTitulo(cursoDetalles.getTitulo());
            if (cursoDetalles.getDescripcion() != null)
                curso.setDescripcion(cursoDetalles.getDescripcion());
            if (cursoDetalles.getPortadaUrl() != null)
                curso.setPortadaUrl(cursoDetalles.getPortadaUrl());

            // Log para depurar en consola de Render/Local
            if (cursoDetalles.getEstado() != null) {
                System.out.println("Actualizando estado curso ID " + id + " a: " + cursoDetalles.getEstado());
                curso.setEstado(cursoDetalles.getEstado());
            }

            return ResponseEntity.ok(cursoRepository.save(curso));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 2. NUEVO ENDPOINT: ELIMINAR CURSO
    @DeleteMapping("/cursos/{id}")
    public ResponseEntity<?> eliminarCurso(@PathVariable Long id) {
        if (!cursoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Primero borramos las relaciones en la tabla intermedia (limpieza)
        asignacionRepository.deleteByCurso_IdCurso(id);

        // Ahora sí borramos el curso
        cursoRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }

    // --- ASIGNACIÓN DE DOCENTES ---
    @PostMapping("/cursos/{idCurso}/asignar-docente/{idUsuario}")
    public ResponseEntity<?> asignarDocente(@PathVariable Long idCurso, @PathVariable Long idUsuario) {
        // 1. Buscamos las entidades
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        Usuario docente = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        // 2. Crear la relación
        AsignacionDocente nuevaAsignacion = new AsignacionDocente();
        nuevaAsignacion.setCurso(curso);
        nuevaAsignacion.setUsuario(docente);

        // 3. Guardar usando el servicio
        AsignacionDocente guardado = asignacionService.crear(nuevaAsignacion);

        return ResponseEntity.ok(guardado);
    }

    // 4. NUEVO: Listar docentes con estado de asignación para un curso
    @GetMapping("/cursos/{idCurso}/docentes-asignacion")
    public ResponseEntity<List<DocenteAsignacionDTO>> obtenerDocentesParaAsignacion(@PathVariable Long idCurso) {
        // 1. Validar curso
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        // 2. Obtener todos los docentes activos
        List<Docente> docentes = docenteRepository.findByEstadoDocente(true);

        // 3. Mapear a DTO verificando si ya están asignados
        List<DocenteAsignacionDTO> resultado = docentes.stream().map(docente -> {
            boolean asignado = asignacionRepository.existsByCursoAndUsuario(curso, docente.getUsuario());

            // Buscar foto de perfil si existe (opcional, por ahora null o placeholder)
            String fotoUrl = null;
            // Si quisieras la foto, tendrías que inyectar PerfilDetalleRepository o sacarla
            // de alguna relación en Docente

            return new DocenteAsignacionDTO(
                    docente.getUsuario().getIdUsuario(),
                    docente.getUsuario().getNombre(),
                    docente.getEspecialidad(),
                    fotoUrl,
                    asignado);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/docentes-list")
    public List<Usuario> listarDocentes() {
        // Usamos el enum que está dentro de tu clase Usuario
        return usuarioRepository.findByRol(Usuario.Rol.DOCENTE);
    }

    // 5. NUEVO: Actualización masiva de docentes asignados (Drag & Drop)
    @PutMapping("/cursos/{idCurso}/asignaciones")
    public ResponseEntity<?> updateDocentesPorCurso(@PathVariable Long idCurso, @RequestBody List<Long> docentesIds) {
        // 1. Validar curso
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        // 2. Borrar asignaciones existentes
        asignacionRepository.deleteByCurso_IdCurso(idCurso);

        // 3. Crear nuevas asignaciones
        List<Usuario> docentes = usuarioRepository.findAllById(docentesIds);

        for (Usuario docente : docentes) {
            AsignacionDocente asignacion = new AsignacionDocente();
            asignacion.setCurso(curso);
            asignacion.setUsuario(docente);
            asignacionRepository.save(asignacion);
        }

        return ResponseEntity.ok().build();
    }
}