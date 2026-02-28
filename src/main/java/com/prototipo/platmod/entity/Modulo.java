package com.prototipo.platmod.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "modulos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idModulo;

    @ManyToOne
    @JoinColumn(name = "id_leccion", nullable = false)
    private Leccion leccion;

    @NotBlank(message = "El título de la seccion es obligatorio")
    @Column(name = "titulo_seccion", nullable = false, length = 200)
    private String tituloSeccion;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "portada_url", length = 1000)
    private String portadaUrl;

    @Column(name = "recursos", columnDefinition = "TEXT")
    private String recursos;

    @NotNull(message = "El orden es obligatorio")
    @Column(nullable = false)
    private Integer orden;
}
