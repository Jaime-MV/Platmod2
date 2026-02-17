package com.prototipo.platmod.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser valido")
    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @NotBlank(message = "La contrasena es obligatoria")
    @Column(nullable = false)
    private String contrasena;

    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private Boolean estado = true;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }

    public enum Rol {
        ESTUDIANTE,
        DOCENTE,
        ADMINISTRADOR
    }
}
