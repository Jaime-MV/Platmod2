package com.prototipo.platmod.repository;

import com.prototipo.platmod.entity.Estudiante;
import com.prototipo.platmod.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Estudiante Repository
@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByUsuario(Usuario usuario);
}
