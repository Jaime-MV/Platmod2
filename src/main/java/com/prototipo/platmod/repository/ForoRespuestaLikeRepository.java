package com.prototipo.platmod.repository;

import com.prototipo.platmod.entity.ForoRespuesta;
import com.prototipo.platmod.entity.ForoRespuestaLike;
import com.prototipo.platmod.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForoRespuestaLikeRepository extends JpaRepository<ForoRespuestaLike, Long> {

    long countByRespuesta_IdRespuesta(Long idRespuesta);

    boolean existsByUsuario_IdUsuarioAndRespuesta_IdRespuesta(Long idUsuario, Long idRespuesta);

    Optional<ForoRespuestaLike> findByUsuarioAndRespuesta(Usuario usuario, ForoRespuesta respuesta);
}
