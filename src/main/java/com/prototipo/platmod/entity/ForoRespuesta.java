package com.prototipo.platmod.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "foro_respuestas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForoRespuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespuesta;

    @ManyToOne
    @JoinColumn(name = "id_pregunta", nullable = false)
    private ForoPregunta pregunta;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @NotBlank(message = "El contenido es obligatorio")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "es_verificada", nullable = false)
    private Boolean esVerificada = false;
}