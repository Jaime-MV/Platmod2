package com.prototipo.platmod.service.impl;

import com.prototipo.platmod.entity.PlanSuscripcion;
import com.prototipo.platmod.repository.PlanSuscripcionRepository;
import com.prototipo.platmod.service.PlanSuscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // <--- ESTO ES VITAL para que Spring lo encuentre
@RequiredArgsConstructor
public class PlanSuscripcionServiceImpl implements PlanSuscripcionService {

    private final PlanSuscripcionRepository planRepository;
    private final com.prototipo.platmod.repository.PlanBeneficioRepository beneficioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PlanSuscripcion> obtenerTodos() {
        return planRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PlanSuscripcion obtenerPorId(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanSuscripcion> obtenerOrdenadosPorPrecio() {
        return planRepository.findByOrderByPrecioAsc();
    }

    @Override
    @Transactional
    public PlanSuscripcion crear(PlanSuscripcion plan) {
        return planRepository.save(plan);
    }

    @Override
    @Transactional
    public PlanSuscripcion actualizar(Long id, PlanSuscripcion planActualizado) {
        PlanSuscripcion plan = obtenerPorId(id);
        plan.setNombre(planActualizado.getNombre());
        plan.setPrecio(planActualizado.getPrecio());
        plan.setDuracionDias(planActualizado.getDuracionDias());
        plan.setDescuento(planActualizado.getDescuento());
        plan.setOfertaActiva(planActualizado.getOfertaActiva());
        return planRepository.save(plan);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        planRepository.deleteById(id);
    }

    @Override
    @Transactional
    public com.prototipo.platmod.entity.PlanBeneficio agregarBeneficio(Long idPlan, String descripcion) {
        PlanSuscripcion plan = obtenerPorId(idPlan);
        com.prototipo.platmod.entity.PlanBeneficio beneficio = new com.prototipo.platmod.entity.PlanBeneficio();
        beneficio.setDescripcion(descripcion);
        beneficio.setPlan(plan);
        return beneficioRepository.save(beneficio);
    }

    @Override
    @Transactional
    public void eliminarBeneficio(Long idBeneficio) {
        beneficioRepository.deleteById(idBeneficio);
    }
}