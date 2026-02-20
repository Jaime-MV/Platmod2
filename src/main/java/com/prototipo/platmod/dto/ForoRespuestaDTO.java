package com.prototipo.platmod.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForoRespuestaDTO {
    private Long idRespuesta;
    private Long idPregunta;
    private Long idUsuario;
    private String nombreUsuario;
    private String contenido;
    private Boolean esVerificada;
    private LocalDateTime fechaCreacion;
}
