package com.prototipo.platmod.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lecciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLeccion;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "id_docente", nullable = false)
    private Docente docente;

    @NotBlank(message = "El título es obligatorio")
    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(name = "portada_url", length = 255)
    private String portadaUrl;
}
