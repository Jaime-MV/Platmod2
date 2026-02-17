package com.prototipo.platmod.service;

import com.prototipo.platmod.dto.AuthResponse;
import com.prototipo.platmod.dto.LoginRequest;
import com.prototipo.platmod.entity.Usuario;
import com.prototipo.platmod.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // 1. INYECTAMOS EL SERVICIO DE TOKEN

    public AuthResponse login(LoginRequest request) {
        // 1. Buscar usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o credenciales invalidas"));

        // 2. Verificar contrasena (comparar texto plano vs hash en DB)
        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new RuntimeException("Contrasena incorrecta");
        }

        // 3. Verificar estado
        if (!usuario.getEstado()) {
            throw new RuntimeException("El usuario esta inactivo");
        }

        // 4. Generar el Token REAL usando JwtService
        String jwtToken = jwtService.generateToken(usuario); // 2. GENERAMOS EL TOKEN REAL

        // 5. Devolver la respuesta con el token real
        return AuthResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol()) // Asegurate que tu DTO acepte el Enum, si no usa .toString()
                .token(jwtToken) //3. REEMPLAZAMOS EL TEXTO DE PRUEBA POR EL TOKEN REAL
                .mensaje("Login exitoso")
                .build();
    }
}