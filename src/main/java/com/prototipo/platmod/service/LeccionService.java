package com.prototipo.platmod.service;
import com.prototipo.platmod.entity.Curso;
import com.prototipo.platmod.entity.Leccion;
import com.prototipo.platmod.repository.LeccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeccionService {

    private final LeccionRepository leccionRepository;

    public List<Leccion> obtenerTodas() {
        return leccionRepository.findAll();
    }

    public Leccion obtenerPorId(Long id) {
        return leccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leccion no encontrada con id: " + id));
    }

    public List<Leccion> obtenerPorCurso(Curso curso) {
        return leccionRepository.findByCurso(curso);
    }

    public Leccion crear(Leccion leccion) {
        return leccionRepository.save(leccion);
    }

    public Leccion actualizar(Long id, Leccion leccionActualizada) {
        Leccion leccion = obtenerPorId(id);
        leccion.setTitulo(leccionActualizada.getTitulo());
        leccion.setPortadaUrl(leccionActualizada.getPortadaUrl());
        return leccionRepository.save(leccion);
    }

    public void eliminar(Long id) {
        Leccion leccion = obtenerPorId(id);
        leccionRepository.delete(leccion);
    }
}
