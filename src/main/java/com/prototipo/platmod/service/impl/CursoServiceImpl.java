package com.prototipo.platmod.service.impl;
import com.prototipo.platmod.entity.Curso;
import com.prototipo.platmod.entity.Usuario;
import com.prototipo.platmod.repository.CursoRepository;
import com.prototipo.platmod.service.CursoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> obtenerTodos() {
        return cursoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Curso obtenerPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> obtenerPorAdministrador(Usuario administrador) {
        return cursoRepository.findByAdministrador(administrador);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> buscarPorTitulo(String titulo) {
        return cursoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    @Override
    public Curso crear(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    public Curso actualizar(Long id, Curso cursoActualizado) {
        Curso curso = obtenerPorId(id);
        curso.setTitulo(cursoActualizado.getTitulo());
        curso.setDescripcion(cursoActualizado.getDescripcion());
        curso.setPortadaUrl(cursoActualizado.getPortadaUrl());
        if (cursoActualizado.getEstado() != null) {
            curso.setEstado(cursoActualizado.getEstado());
        }

        return cursoRepository.save(curso);
    }

    @Override
    public void eliminar(Long id) {
        Curso curso = obtenerPorId(id);
        cursoRepository.delete(curso);
    }
}
