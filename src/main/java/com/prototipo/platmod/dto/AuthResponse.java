package com.prototipo.platmod.dto;

import com.prototipo.platmod.entity.Usuario;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private Long idUsuario;
    private String nombre;
    private String correo;
    private Usuario.Rol rol;
    private String token; // Aqui iria el JWT en el futuro, por ahora enviaremos un placeholder o mensaje
    private String mensaje;
}
