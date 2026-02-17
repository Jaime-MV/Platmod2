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

    @Column(name = "tipo_contenido", length = 50)
    private String tipoContenido;

    @Column(name = "url_recurso", length = 255)
    private String urlRecurso;

    @NotNull(message = "El orden es obligatorio")
    @Column(nullable = false)
    private Integer orden;
}
