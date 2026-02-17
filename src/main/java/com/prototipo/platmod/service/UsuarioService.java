package com.prototipo.platmod.service;
import com.prototipo.platmod.entity.Usuario;
import java.util.List;

public interface UsuarioService {
    List<Usuario> obtenerTodos();
    Usuario obtenerPorId(Long id);
    Usuario obtenerPorCorreo(String correo);
    List<Usuario> obtenerPorRol(Usuario.Rol rol);
    List<Usuario> obtenerPorEstado(Boolean estado);
    Usuario crear(Usuario usuario);
    Usuario actualizar(Long id, Usuario usuario);
    void eliminar(Long id);
    void cambiarEstado(Long id, Boolean estado);
    boolean existePorCorreo(String correo);
}
