package com.prototipo.platmod.repository;

import com.prototipo.platmod.entity.Leccion;
import com.prototipo.platmod.entity.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, Long> {

    List<Modulo> findByLeccionOrderByOrdenAsc(Leccion leccion);

    List<Modulo> findByLeccion_IdLeccionOrderByOrdenAsc(Long idLeccion);

    @Transactional
    void deleteByLeccion_IdLeccion(Long idLeccion);
}
