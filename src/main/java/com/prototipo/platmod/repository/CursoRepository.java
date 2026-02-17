package com.prototipo.platmod.repository;

import com.prototipo.platmod.entity.Curso;
import com.prototipo.platmod.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Curso Repository
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByAdministrador(Usuario administrador);

    List<Curso> findByTituloContainingIgnoreCase(String titulo);
    List<Curso> findByEstadoTrue();
}
