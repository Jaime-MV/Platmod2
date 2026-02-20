package com.prototipo.platmod.repository;

import com.prototipo.platmod.entity.ForoPregunta;
import com.prototipo.platmod.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForoPreguntaRepository extends JpaRepository<ForoPregunta, Long> {
    List<ForoPregunta> findByCategoria(String categoria);

    List<ForoPregunta> findByUsuario(Usuario usuario);

    List<ForoPregunta> findAllByOrderByIdPreguntaDesc();

    List<ForoPregunta> findByTituloContainingIgnoreCase(String titulo);
}
