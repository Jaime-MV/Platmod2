package com.prototipo.platmod.dto;

public record CursoDTO(
        Long idCurso,
        String titulo,
        String descripcion,
        String portadaUrl,
        Boolean estado,
        Long numDocentes
) {}
