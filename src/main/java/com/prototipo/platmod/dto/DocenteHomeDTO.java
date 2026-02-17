package com.prototipo.platmod.dto;

public record DocenteHomeDTO(
        Long idDocente,
        String nombre,
        String especialidad,
        String fotoUrl,     // Viene de perfil_detalle
        String cursoTitulo  // Viene de cursos (tomaremos el primero que encuentre)
) {}
