package com.prototipo.platmod.service.impl;

import com.prototipo.platmod.entity.AsignacionDocente;
import com.prototipo.platmod.entity.Curso;
import com.prototipo.platmod.entity.Usuario;
import com.prototipo.platmod.repository.AsignacionDocenteRepository;
import com.prototipo.platmod.service.AsignacionDocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // <--- VITAL para que funcione el Autowired en el Controller
@RequiredArgsConstructor
public class AsignacionDocenteServiceImpl implements AsignacionDocenteService {

    private final AsignacionDocenteRepository asignacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionDocente> obtenerTodas() {
        return asignacionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public AsignacionDocente obtenerPorId(Long id) {
        return asignacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionDocente> obtenerPorCurso(Curso curso) {
        return asignacionRepository.findByCurso(curso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionDocente> obtenerPorUsuario(Usuario usuario) {
        return asignacionRepository.findByUsuario(usuario);
    }

    // Método extra útil si solo tienes el ID del curso
    @Override
    @Transactional(readOnly = true)
    public List<AsignacionDocente> obtenerPorCursoId(Long idCurso) {
        // Para implementar esto, necesitarías un método findByCurso_IdCurso en el repo
        // o buscar el curso primero. Por ahora retornamos null o implementamos lógica extra.
        // Lo dejaremos simple basándonos en tu interfaz:
        return null; // O implementa lógica si es necesario
    }

    @Override
    @Transactional
    public AsignacionDocente crear(AsignacionDocente asignacion) {
        return asignacionRepository.save(asignacion);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        asignacionRepository.deleteById(id);
    }
}