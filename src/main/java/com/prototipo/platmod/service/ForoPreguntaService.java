package com.prototipo.platmod.service;

import com.prototipo.platmod.entity.ForoPregunta;
import com.prototipo.platmod.entity.Usuario;

import java.util.List;

public interface ForoPreguntaService {
    List<ForoPregunta> obtenerTodas();

    ForoPregunta obtenerPorId(Long id);

    List<ForoPregunta> obtenerPorCategoria(String categoria);

    List<ForoPregunta> obtenerPorUsuario(Usuario usuario);

    List<ForoPregunta> buscarPorTitulo(String titulo);

    ForoPregunta crear(ForoPregunta pregunta);

    ForoPregunta actualizar(Long id, ForoPregunta pregunta);

    void eliminar(Long id);
}
