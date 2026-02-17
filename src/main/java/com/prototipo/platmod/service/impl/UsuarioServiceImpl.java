package com.prototipo.platmod.service.impl;
import com.prototipo.platmod.entity.Usuario;
import com.prototipo.platmod.repository.UsuarioRepository;
import com.prototipo.platmod.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerPorRol(Usuario.Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerPorEstado(Boolean estado) {
        return usuarioRepository.findByEstado(estado);
    }

    @Override
    public Usuario crear(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya esta registrado: " + usuario.getCorreo());
        }

        // 1. ENCRIPTAR LA CONTRASENA
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        // 2. 🔒 SEGURIDAD: FORZAR SIEMPRE EL ROL DE ESTUDIANTE
        // Esto ignora cualquier dato de "rol" que venga en el JSON
        usuario.setRol(Usuario.Rol.ESTUDIANTE);

        // 3. ASEGURAR QUE EL USUARIO NAZCA ACTIVO (O false si requieres verificación por correo)
        usuario.setEstado(true);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        Usuario usuario = obtenerPorId(id);

        // Verificar si el correo ya existe (excepto el del mismo usuario)
        if (!usuario.getCorreo().equals(usuarioActualizado.getCorreo())
                && usuarioRepository.existsByCorreo(usuarioActualizado.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado: " + usuarioActualizado.getCorreo());
        }

        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setCorreo(usuarioActualizado.getCorreo());

        if (usuarioActualizado.getContrasena() != null && !usuarioActualizado.getContrasena().isEmpty()) {
            usuario.setContrasena(usuarioActualizado.getContrasena());
        }

        usuario.setRol(usuarioActualizado.getRol());
        usuario.setEstado(usuarioActualizado.getEstado());

        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
    }

    @Override
    public void cambiarEstado(Long id, Boolean estado) {
        Usuario usuario = obtenerPorId(id);
        usuario.setEstado(estado);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }
}