package com.prototipo.platmod.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asignacion_docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsignacion;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}