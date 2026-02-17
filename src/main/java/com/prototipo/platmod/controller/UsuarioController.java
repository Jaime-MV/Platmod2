package com.prototipo.platmod.controller;
import com.prototipo.platmod.entity.Usuario;
import com.prototipo.platmod.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Usuario> obtenerPorCorreo(@PathVariable String correo) {
        return ResponseEntity.ok(usuarioService.obtenerPorCorreo(correo));
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> obtenerPorRol(@PathVariable Usuario.Rol rol) {
        return ResponseEntity.ok(usuarioService.obtenerPorRol(rol));
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.actualizar(id, usuario));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam Boolean estado) {
        usuarioService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
