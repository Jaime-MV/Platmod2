package com.prototipo.platmod.service;

import com.prototipo.platmod.entity.Estudiante;
import com.prototipo.platmod.entity.Suscripcion;

import java.util.List;

// ============================================
// SUSCRIPCION SERVICE
// ============================================
public interface SuscripcionService {
    List<Suscripcion> obtenerTodas();

    Suscripcion obtenerPorId(Long id);

    List<Suscripcion> obtenerPorEstudiante(Estudiante estudiante);

    List<Suscripcion> obtenerPorEstado(Boolean estado);

    List<Suscripcion> obtenerActivas();

    Suscripcion crear(Suscripcion suscripcion);

    Suscripcion actualizar(Long id, Suscripcion suscripcion);

    void eliminar(Long id);

    void cancelarSuscripcion(Long id);
}
