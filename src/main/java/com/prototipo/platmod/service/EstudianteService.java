package com.prototipo.platmod.service;
import com.prototipo.platmod.entity.Estudiante;
import com.prototipo.platmod.entity.Usuario;
import com.prototipo.platmod.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;

    public List<Estudiante> obtenerTodos() {
        return estudianteRepository.findAll();
    }

    public Estudiante obtenerPorId(Long id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con id: " + id));
    }

    public Estudiante obtenerPorUsuario(Usuario usuario) {
        return estudianteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado para el usuario"));
    }

    public Estudiante crear(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    public Estudiante actualizar(Long id, Estudiante estudianteActualizado) {
        Estudiante estudiante = obtenerPorId(id);
        estudiante.setNivel(estudianteActualizado.getNivel());
        estudiante.setFechaSuscripcion(estudianteActualizado.getFechaSuscripcion());
        return estudianteRepository.save(estudiante);
    }

    public void eliminar(Long id) {
        Estudiante estudiante = obtenerPorId(id);
        estudianteRepository.delete(estudiante);
    }
}
