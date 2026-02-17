package com.prototipo.platmod.service;

import com.prototipo.platmod.entity.AsignacionDocente;
import com.prototipo.platmod.entity.Curso;
import com.prototipo.platmod.entity.Usuario;

import java.util.List;

// ============================================
// ASIGNACION DOCENTE SERVICE
// ============================================
public interface AsignacionDocenteService {
    List<AsignacionDocente> obtenerTodas();

    AsignacionDocente obtenerPorId(Long id);

    List<AsignacionDocente> obtenerPorCurso(Curso curso);

    List<AsignacionDocente> obtenerPorUsuario(Usuario usuario);

    List<AsignacionDocente> obtenerPorCursoId(Long idCurso);

    AsignacionDocente crear(AsignacionDocente asignacion);

    void eliminar(Long id);
}
