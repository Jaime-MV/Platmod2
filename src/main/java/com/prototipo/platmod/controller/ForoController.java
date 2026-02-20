package com.prototipo.platmod.controller;

import com.prototipo.platmod.dto.ForoPreguntaDTO;
import com.prototipo.platmod.dto.ForoRespuestaDTO;
import com.prototipo.platmod.entity.ForoFavorito;
import com.prototipo.platmod.entity.ForoPregunta;
import com.prototipo.platmod.entity.ForoRespuesta;
import com.prototipo.platmod.entity.Usuario;
import com.prototipo.platmod.repository.ForoFavoritoRepository;
import com.prototipo.platmod.repository.ForoRespuestaRepository;
import com.prototipo.platmod.repository.UsuarioRepository;
import com.prototipo.platmod.service.ForoPreguntaService;
import com.prototipo.platmod.service.ForoRespuestaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/foro")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ForoController {

    private final ForoPreguntaService foroPreguntaService;
    private final ForoRespuestaService foroRespuestaService;
    private final ForoRespuestaRepository foroRespuestaRepository;
    private final ForoFavoritoRepository foroFavoritoRepository;
    private final UsuarioRepository usuarioRepository;

    // ============ PREGUNTAS ============

    @GetMapping("/preguntas")
    public ResponseEntity<List<ForoPreguntaDTO>> listarPreguntas(Authentication authentication) {
        Long idUsuario = getUsuarioId(authentication);
        List<ForoPregunta> preguntas = foroPreguntaService.obtenerTodas();
        List<ForoPreguntaDTO> dtos = preguntas.stream()
                .map(p -> convertirPreguntaADTO(p, idUsuario))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/preguntas/{id}")
    public ResponseEntity<ForoPreguntaDTO> obtenerPregunta(@PathVariable Long id, Authentication authentication) {
        Long idUsuario = getUsuarioId(authentication);
        ForoPregunta pregunta = foroPreguntaService.obtenerPorId(id);
        return ResponseEntity.ok(convertirPreguntaADTO(pregunta, idUsuario));
    }

    @PostMapping("/preguntas")
    public ResponseEntity<ForoPreguntaDTO> crearPregunta(
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        Usuario usuario = getUsuario(authentication);

        ForoPregunta pregunta = new ForoPregunta();
        pregunta.setUsuario(usuario);
        pregunta.setTitulo(body.get("titulo"));
        pregunta.setContenido(body.get("contenido"));
        pregunta.setCategoria(body.getOrDefault("categoria", "General"));
        pregunta.setArchivoUrl(body.getOrDefault("archivoUrl", null));
        pregunta.setArchivoNombre(body.getOrDefault("archivoNombre", null));

        ForoPregunta creada = foroPreguntaService.crear(pregunta);
        return ResponseEntity.ok(convertirPreguntaADTO(creada, usuario.getIdUsuario()));
    }

    @DeleteMapping("/preguntas/{id}")
    public ResponseEntity<?> eliminarPregunta(@PathVariable Long id, Authentication authentication) {
        Usuario usuario = getUsuario(authentication);
        ForoPregunta pregunta = foroPreguntaService.obtenerPorId(id);

        // Only the owner can delete their question
        if (!pregunta.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "No puedes eliminar una pregunta que no es tuya"));
        }

        foroPreguntaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ============ MIS PREGUNTAS ============

    @GetMapping("/mis-preguntas")
    public ResponseEntity<List<ForoPreguntaDTO>> misPreguntas(Authentication authentication) {
        Usuario usuario = getUsuario(authentication);
        List<ForoPregunta> preguntas = foroPreguntaService.obtenerPorUsuario(usuario);
        List<ForoPreguntaDTO> dtos = preguntas.stream()
                .map(p -> convertirPreguntaADTO(p, usuario.getIdUsuario()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ============ FAVORITOS ============

    @GetMapping("/favoritos")
    public ResponseEntity<List<ForoPreguntaDTO>> listarFavoritos(Authentication authentication) {
        Usuario usuario = getUsuario(authentication);
        List<ForoFavorito> favoritos = foroFavoritoRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        List<ForoPreguntaDTO> dtos = favoritos.stream()
                .map(f -> convertirPreguntaADTO(f.getPregunta(), usuario.getIdUsuario()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/favoritos/{idPregunta}")
    @Transactional
    public ResponseEntity<Map<String, Object>> toggleFavorito(
            @PathVariable Long idPregunta,
            Authentication authentication) {

        Usuario usuario = getUsuario(authentication);
        ForoPregunta pregunta = foroPreguntaService.obtenerPorId(idPregunta);

        Optional<ForoFavorito> existente = foroFavoritoRepository.findByUsuarioAndPregunta(usuario, pregunta);

        if (existente.isPresent()) {
            foroFavoritoRepository.delete(existente.get());
            return ResponseEntity.ok(Map.of("favorito", false));
        } else {
            ForoFavorito favorito = new ForoFavorito();
            favorito.setUsuario(usuario);
            favorito.setPregunta(pregunta);
            foroFavoritoRepository.save(favorito);
            return ResponseEntity.ok(Map.of("favorito", true));
        }
    }

    // ============ RESPUESTAS ============

    @GetMapping("/preguntas/{id}/respuestas")
    public ResponseEntity<List<ForoRespuestaDTO>> listarRespuestas(@PathVariable Long id) {
        List<ForoRespuesta> respuestas = foroRespuestaService.obtenerPorPreguntaId(id);
        List<ForoRespuestaDTO> dtos = respuestas.stream()
                .map(this::convertirRespuestaADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/preguntas/{id}/respuestas")
    public ResponseEntity<ForoRespuestaDTO> crearRespuesta(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        Usuario usuario = getUsuario(authentication);
        ForoPregunta pregunta = foroPreguntaService.obtenerPorId(id);

        ForoRespuesta respuesta = new ForoRespuesta();
        respuesta.setPregunta(pregunta);
        respuesta.setUsuario(usuario);
        respuesta.setContenido(body.get("contenido"));
        respuesta.setArchivoUrl(body.getOrDefault("archivoUrl", null));
        respuesta.setArchivoNombre(body.getOrDefault("archivoNombre", null));

        ForoRespuesta creada = foroRespuestaService.crear(respuesta);
        return ResponseEntity.ok(convertirRespuestaADTO(creada));
    }

    @PutMapping("/respuestas/{id}/verificar")
    public ResponseEntity<Void> verificarRespuesta(@PathVariable Long id) {
        foroRespuestaService.verificarRespuesta(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/respuestas/{id}")
    public ResponseEntity<?> eliminarRespuesta(@PathVariable Long id, Authentication authentication) {
        Usuario usuario = getUsuario(authentication);
        ForoRespuesta respuesta = foroRespuestaService.obtenerPorId(id);

        if (!respuesta.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return ResponseEntity.status(403)
                    .body(java.util.Map.of("error", "No puedes eliminar una respuesta que no es tuya"));
        }

        foroRespuestaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ============ HELPERS ============

    private Usuario getUsuario(Authentication authentication) {
        String correo = authentication.getName();
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private Long getUsuarioId(Authentication authentication) {
        return getUsuario(authentication).getIdUsuario();
    }

    // ============ CONVERSIONES ============

    private ForoPreguntaDTO convertirPreguntaADTO(ForoPregunta pregunta, Long idUsuarioActual) {
        long totalRespuestas = foroRespuestaRepository.countByPregunta_IdPregunta(pregunta.getIdPregunta());
        boolean esFavorito = foroFavoritoRepository
                .existsByUsuario_IdUsuarioAndPregunta_IdPregunta(idUsuarioActual, pregunta.getIdPregunta());

        ForoPreguntaDTO dto = new ForoPreguntaDTO();
        dto.setIdPregunta(pregunta.getIdPregunta());
        dto.setIdUsuario(pregunta.getUsuario().getIdUsuario());
        dto.setNombreUsuario(pregunta.getUsuario().getNombre());
        dto.setTitulo(pregunta.getTitulo());
        dto.setContenido(pregunta.getContenido());
        dto.setCategoria(pregunta.getCategoria());
        dto.setFechaCreacion(pregunta.getFechaCreacion());
        dto.setTotalRespuestas(totalRespuestas);
        dto.setEsFavorito(esFavorito);
        dto.setArchivoUrl(pregunta.getArchivoUrl());
        dto.setArchivoNombre(pregunta.getArchivoNombre());
        return dto;
    }

    private ForoRespuestaDTO convertirRespuestaADTO(ForoRespuesta respuesta) {
        ForoRespuestaDTO dto = new ForoRespuestaDTO();
        dto.setIdRespuesta(respuesta.getIdRespuesta());
        dto.setIdPregunta(respuesta.getPregunta().getIdPregunta());
        dto.setIdUsuario(respuesta.getUsuario().getIdUsuario());
        dto.setNombreUsuario(respuesta.getUsuario().getNombre());
        dto.setContenido(respuesta.getContenido());
        dto.setEsVerificada(respuesta.getEsVerificada());
        dto.setFechaCreacion(respuesta.getFechaCreacion());
        dto.setArchivoUrl(respuesta.getArchivoUrl());
        dto.setArchivoNombre(respuesta.getArchivoNombre());
        return dto;
    }
}
