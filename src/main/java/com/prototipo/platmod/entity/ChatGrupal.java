package com.prototipo.platmod.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_grupal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGrupal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChatMsg;

    @ManyToOne
    @JoinColumn(name = "id_leccion", nullable = false)
    private Leccion leccion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @NotBlank(message = "El mensaje es obligatorio")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;
}
