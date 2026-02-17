package com.prototipo.platmod.controller;

import com.prototipo.platmod.dto.DocenteHomeDTO;
import com.prototipo.platmod.repository.DocenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/docentes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DocenteController {

    private final DocenteRepository docenteRepository;

    @GetMapping("/home")
    public ResponseEntity<List<DocenteHomeDTO>> obtenerDocentesParaHome() {
        // Limitamos a 4 docentes para que se vea estético como en Platzi
        List<DocenteHomeDTO> docentes = docenteRepository.obtenerDocentesHome();
        return ResponseEntity.ok(docentes.stream().limit(8).toList());
    }
}
