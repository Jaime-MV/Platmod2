package com.prototipo.platmod.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plan_beneficio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanBeneficio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBeneficio;

    @Column(nullable = false)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plan", nullable = false)
    @JsonIgnore
    private PlanSuscripcion plan;
}
