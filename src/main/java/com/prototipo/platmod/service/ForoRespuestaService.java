package com.prototipo.platmod.service;

import com.prototipo.platmod.entity.ForoPregunta;
import com.prototipo.platmod.entity.ForoRespuesta;

import java.util.List;

public interface ForoRespuestaService {
    List<ForoRespuesta> obtenerTodas();

    ForoRespuesta obtenerPorId(Long id);

    List<ForoRespuesta> obtenerPorPregunta(ForoPregunta pregunta);

    List<ForoRespuesta> obtenerPorPreguntaId(Long idPregunta);

    List<ForoRespuesta> obtenerVerificadas(Long idPregunta);

    ForoRespuesta crear(ForoRespuesta respuesta);

    ForoRespuesta actualizar(Long id, ForoRespuesta respuesta);

    void eliminar(Long id);

    void verificarRespuesta(Long id);
}
