package com.prototipo.platmod.service;

import com.prototipo.platmod.entity.Curso;
import com.prototipo.platmod.entity.Usuario;
import java.util.List;

// Fijate que dice "interface", no "class"
public interface CursoService {

    List<Curso> obtenerTodos();

    Curso obtenerPorId(Long id);

    List<Curso> obtenerPorAdministrador(Usuario administrador);

    List<Curso> buscarPorTitulo(String titulo);

    Curso crear(Curso curso);

    Curso actualizar(Long id, Curso cursoActualizado);

    void eliminar(Long id);
}