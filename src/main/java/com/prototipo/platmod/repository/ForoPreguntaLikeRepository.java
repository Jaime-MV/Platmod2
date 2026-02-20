package com.prototipo.platmod.repository;

import com.prototipo.platmod.entity.ForoPregunta;
import com.prototipo.platmod.entity.ForoPreguntaLike;
import com.prototipo.platmod.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForoPreguntaLikeRepository extends JpaRepository<ForoPreguntaLike, Long> {

    long countByPregunta_IdPregunta(Long idPregunta);

    boolean existsByUsuario_IdUsuarioAndPregunta_IdPregunta(Long idUsuario, Long idPregunta);

    Optional<ForoPreguntaLike> findByUsuarioAndPregunta(Usuario usuario, ForoPregunta pregunta);
}
