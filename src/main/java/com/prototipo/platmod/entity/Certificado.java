package com.prototipo.platmod.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "certificados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCertificado;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "id_leccion", nullable = false)
    private Leccion leccion;

    @NotBlank(message = "El codigo de verificacion es obligatorio")
    @Column(name = "codigo_verificacion", nullable = false, unique = true, length = 100)
    private String codigoVerificacion;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @PrePersist
    protected void onCreate() {
        fechaEmision = LocalDate.now();
    }
}
