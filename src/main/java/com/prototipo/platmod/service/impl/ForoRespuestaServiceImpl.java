package com.prototipo.platmod.service.impl;

import com.prototipo.platmod.entity.ForoPregunta;
import com.prototipo.platmod.entity.ForoRespuesta;
import com.prototipo.platmod.repository.ForoRespuestaRepository;
import com.prototipo.platmod.service.ForoRespuestaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ForoRespuestaServiceImpl implements ForoRespuestaService {

    private final ForoRespuestaRepository foroRespuestaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ForoRespuesta> obtenerTodas() {
        return foroRespuestaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ForoRespuesta obtenerPorId(Long id) {
        return foroRespuestaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ForoRespuesta> obtenerPorPregunta(ForoPregunta pregunta) {
        return foroRespuestaRepository.findByPregunta(pregunta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ForoRespuesta> obtenerPorPreguntaId(Long idPregunta) {
        return foroRespuestaRepository.findByPregunta_IdPregunta(idPregunta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ForoRespuesta> obtenerVerificadas(Long idPregunta) {
        ForoPregunta pregunta = new ForoPregunta();
        pregunta.setIdPregunta(idPregunta);
        return foroRespuestaRepository.findByPreguntaAndEsVerificada(pregunta, true);
    }

    @Override
    public ForoRespuesta crear(ForoRespuesta respuesta) {
        return foroRespuestaRepository.save(respuesta);
    }

    @Override
    public ForoRespuesta actualizar(Long id, ForoRespuesta respuestaActualizada) {
        ForoRespuesta respuesta = obtenerPorId(id);
        respuesta.setContenido(respuestaActualizada.getContenido());
        return foroRespuestaRepository.save(respuesta);
    }

    @Override
    public void eliminar(Long id) {
        ForoRespuesta respuesta = obtenerPorId(id);
        foroRespuestaRepository.delete(respuesta);
    }

    @Override
    public void verificarRespuesta(Long id) {
        ForoRespuesta respuesta = obtenerPorId(id);
        respuesta.setEsVerificada(true);
        foroRespuestaRepository.save(respuesta);
    }
}
