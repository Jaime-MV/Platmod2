package com.prototipo.platmod.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForoPreguntaDTO {
    private Long idPregunta;
    private Long idUsuario;
    private String nombreUsuario;
    private String titulo;
    private String contenido;
    private String categoria;
    private LocalDateTime fechaCreacion;
    private long totalRespuestas;
    private boolean esFavorito;
    private String archivoUrl;
    private String archivoNombre;
    private String rolUsuario;
    private long totalLikes;
    private boolean meGusta;
}
