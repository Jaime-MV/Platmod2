package com.prototipo.platmod.repository;

import com.prototipo.platmod.entity.ForoFavorito;
import com.prototipo.platmod.entity.ForoPregunta;
import com.prototipo.platmod.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForoFavoritoRepository extends JpaRepository<ForoFavorito, Long> {
    List<ForoFavorito> findByUsuario_IdUsuario(Long idUsuario);

    Optional<ForoFavorito> findByUsuarioAndPregunta(Usuario usuario, ForoPregunta pregunta);

    boolean existsByUsuario_IdUsuarioAndPregunta_IdPregunta(Long idUsuario, Long idPregunta);

    void deleteByUsuarioAndPregunta(Usuario usuario, ForoPregunta pregunta);
}
