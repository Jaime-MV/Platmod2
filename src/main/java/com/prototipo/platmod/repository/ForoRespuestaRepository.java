package com.prototipo.platmod.repository;

import com.prototipo.platmod.entity.ForoPregunta;
import com.prototipo.platmod.entity.ForoRespuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForoRespuestaRepository extends JpaRepository<ForoRespuesta, Long> {
    List<ForoRespuesta> findByPregunta(ForoPregunta pregunta);

    List<ForoRespuesta> findByPreguntaAndEsVerificada(ForoPregunta pregunta, Boolean esVerificada);

    List<ForoRespuesta> findByPregunta_IdPregunta(Long idPregunta);

    long countByPregunta_IdPregunta(Long idPregunta);
}
