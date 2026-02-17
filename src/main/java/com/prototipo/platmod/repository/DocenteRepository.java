package com.prototipo.platmod.repository;

import com.prototipo.platmod.dto.DocenteHomeDTO;
import com.prototipo.platmod.entity.Docente;
import com.prototipo.platmod.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

// Docente Repository
@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByUsuario(Usuario usuario);

    List<Docente> findByEstadoDocente(Boolean estadoDocente);

    // --- NUEVO MÉTODO PARA EL HOME (Agrégalo aquí) ---
    // Esta consulta trae: Nombre, Especialidad, Foto (del perfil) y un Curso
    // asignado
    @Query("""
                SELECT new com.prototipo.platmod.dto.DocenteHomeDTO(
                    d.idDocente,
                    u.nombre,
                    d.especialidad,
                    p.foto,
                    (SELECT c.titulo
                     FROM AsignacionDocente ad
                     JOIN ad.curso c
                     WHERE ad.usuario.idUsuario = u.idUsuario
                     ORDER BY ad.idAsignacion ASC LIMIT 1)
                )
                FROM Docente d
                JOIN d.usuario u
                LEFT JOIN PerfilDetalle p ON p.usuario.idUsuario = u.idUsuario
                WHERE d.estadoDocente = true
            """)
    List<DocenteHomeDTO> obtenerDocentesHome();
}
