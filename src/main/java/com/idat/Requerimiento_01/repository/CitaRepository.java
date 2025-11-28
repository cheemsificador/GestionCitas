package com.idat.Requerimiento_01.repository;

import com.idat.Requerimiento_01.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByIdUsuario(Long idUsuario);
}
