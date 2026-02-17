package com.prototipo.platmod.repository;

import com.prototipo.platmod.entity.PlanSuscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// PlanSuscripcion Repository
@Repository
public interface PlanSuscripcionRepository extends JpaRepository<PlanSuscripcion, Long> {
    List<PlanSuscripcion> findByOrderByPrecioAsc();
}
