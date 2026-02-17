package com.prototipo.platmod.controller;

import com.prototipo.platmod.dto.AuthResponse;
import com.prototipo.platmod.dto.LoginRequest;
import com.prototipo.platmod.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // Agrega esto para ver si la peticion logra entrar al metodo
        System.out.println("✅ PETICION ENTRO AL CONTROLADOR: " + request.getCorreo());

        return ResponseEntity.ok(authService.login(request));
    }
}
