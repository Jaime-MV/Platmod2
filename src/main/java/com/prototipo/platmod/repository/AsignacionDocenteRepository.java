package com.prototipo.platmod.repository;

import com.prototipo.platmod.entity.AsignacionDocente;
import com.prototipo.platmod.entity.Curso;
import com.prototipo.platmod.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// AsignacionDocente Repository
@Repository
public interface AsignacionDocenteRepository extends JpaRepository<AsignacionDocente, Long> {
    List<AsignacionDocente> findByCurso(Curso curso);

    List<AsignacionDocente> findByUsuario(Usuario usuario);

    // Para verificar si ya existe (lo tenías de antes seguramente)
    boolean existsByCursoAndUsuario(Curso curso, Usuario usuario);

    // 1. NUEVO: Contar docentes por curso
    long countByCurso_IdCurso(Long idCurso);

    // 2. NUEVO: Borrar todas las asignaciones de un curso (antes de borrar el curso)
    @Transactional
    void deleteByCurso_IdCurso(Long idCurso);
}
