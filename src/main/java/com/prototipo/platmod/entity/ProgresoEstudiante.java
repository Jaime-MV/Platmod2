package com.prototipo.platmod.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "progreso_estudiante")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgresoEstudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProgreso;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "id_modulo", nullable = false)
    private Modulo modulo;

    @Column(nullable = false)
    private Boolean completado = false;

    @Column(name = "fecha_completado")
    private LocalDateTime fechaCompletado;
}
