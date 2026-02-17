package com.prototipo.platmod.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes_privados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajePrivado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMensaje;

    @ManyToOne
    @JoinColumn(name = "id_emisor", nullable = false)
    private Usuario emisor;

    @ManyToOne
    @JoinColumn(name = "id_receptor", nullable = false)
    private Usuario receptor;

    @NotBlank(message = "El contenido es obligatorio")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @PrePersist
    protected void onCreate() {
        fechaEnvio = LocalDateTime.now();
    }
}
