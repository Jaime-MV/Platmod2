package com.prototipo.platmod.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "perfil_detalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPerfil;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 255)
    private String foto;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;
}
