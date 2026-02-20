package com.prototipo.platmod.service.impl;

import com.prototipo.platmod.entity.ForoPregunta;
import com.prototipo.platmod.entity.Usuario;
import com.prototipo.platmod.repository.ForoPreguntaRepository;
import com.prototipo.platmod.service.ForoPreguntaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ForoPreguntaServiceImpl implements ForoPreguntaService {

    private final ForoPreguntaRepository foroPreguntaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ForoPregunta> obtenerTodas() {
        return foroPreguntaRepository.findAllByOrderByIdPreguntaDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public ForoPregunta obtenerPorId(Long id) {
        return foroPreguntaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ForoPregunta> obtenerPorCategoria(String categoria) {
        return foroPreguntaRepository.findByCategoria(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ForoPregunta> obtenerPorUsuario(Usuario usuario) {
        return foroPreguntaRepository.findByUsuario(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ForoPregunta> buscarPorTitulo(String titulo) {
        return foroPreguntaRepository.findByTituloContainingIgnoreCase(titulo);
    }

    @Override
    public ForoPregunta crear(ForoPregunta pregunta) {
        return foroPreguntaRepository.save(pregunta);
    }

    @Override
    public ForoPregunta actualizar(Long id, ForoPregunta preguntaActualizada) {
        ForoPregunta pregunta = obtenerPorId(id);
        pregunta.setTitulo(preguntaActualizada.getTitulo());
        pregunta.setContenido(preguntaActualizada.getContenido());
        pregunta.setCategoria(preguntaActualizada.getCategoria());
        return foroPreguntaRepository.save(pregunta);
    }

    @Override
    public void eliminar(Long id) {
        ForoPregunta pregunta = obtenerPorId(id);
        foroPreguntaRepository.delete(pregunta);
    }
}
