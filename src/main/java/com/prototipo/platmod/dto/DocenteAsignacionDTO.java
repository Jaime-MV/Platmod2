package com.prototipo.platmod.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocenteAsignacionDTO {
    private Long idUsuario;
    private String nombre;
    private String especialidad;
    private String fotoUrl;
    private boolean asignado; // true si ya dicta este curso
}
