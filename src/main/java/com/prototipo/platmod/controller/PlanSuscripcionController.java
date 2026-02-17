package com.prototipo.platmod.controller;

import com.prototipo.platmod.entity.PlanSuscripcion;
import com.prototipo.platmod.service.PlanSuscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
@CrossOrigin(origins = "*")
public class PlanSuscripcionController {

    @Autowired
    private PlanSuscripcionService planService;

    @GetMapping
    public ResponseEntity<List<PlanSuscripcion>> listarPlanes() {
        // CORRECCION: Usamos obtenerTodos()
        //return ResponseEntity.ok(planService.obtenerTodos());
        // AHORA: Usamos el metodo de ordenamiento para que la UI se vea mejor
        return ResponseEntity.ok(planService.obtenerOrdenadosPorPrecio());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanSuscripcion> obtenerPlan(@PathVariable Long id) {
        return ResponseEntity.ok(planService.obtenerPorId(id));
    }
}