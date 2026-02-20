package com.prototipo.platmod.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "archivo_url", length = 500)
    private String archivoUrl;

    @Column(name = "archivo_nombre", length = 255)
    private String archivoNombre;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}