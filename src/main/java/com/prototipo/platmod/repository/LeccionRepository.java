package com.prototipo.platmod.repository;

import com.prototipo.platmod.entity.Curso;
import com.prototipo.platmod.entity.Docente;
import com.prototipo.platmod.entity.Leccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Leccion Repository
@Repository
public interface LeccionRepository extends JpaRepository<Leccion, Long> {
    List<Leccion> findByCurso(Curso curso);

    List<Leccion> findByDocente(Docente docente);
}
