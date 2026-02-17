package com.prototipo.platmod.repository;
import com.prototipo.platmod.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findByRol(Usuario.Rol rol);
    List<Usuario> findByEstado(Boolean estado);
    boolean existsByCorreo(String correo);
}
