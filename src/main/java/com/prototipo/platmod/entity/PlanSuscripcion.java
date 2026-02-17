package com.prototipo.platmod.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "plan_suscripcion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanSuscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlan;

    @NotBlank(message = "El nombre del plan es obligatorio")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @NotNull(message = "La duracion es obligatoria")
    @Positive(message = "La duracion debe ser positiva")
    @Column(name = "duracion_dias", nullable = false)
    private Integer duracionDias;

    @Column(precision = 5, scale = 2)
    private BigDecimal descuento;

    @Column(name = "oferta_activa")
    private Boolean ofertaActiva;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private java.util.List<PlanBeneficio> beneficios;
}
