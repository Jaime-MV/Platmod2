package com.prototipo.platmod.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "foro_respuesta_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "id_usuario", "id_respuesta" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForoRespuestaLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLike;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_respuesta", nullable = false)
    private ForoRespuesta respuesta;
}
