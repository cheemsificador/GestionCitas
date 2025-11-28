package com.idat.Requerimiento_01.service.impl;

import com.idat.Requerimiento_01.dto.CitaDTO;
import com.idat.Requerimiento_01.dto.CitaRequest;
import com.idat.Requerimiento_01.model.Cita;
import com.idat.Requerimiento_01.repository.CitaRepository;
import com.idat.Requerimiento_01.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Override
    public CitaDTO registrarCita(Long idUsuario, CitaRequest request) {
        Cita c = new Cita();
        c.setIdUsuario(idUsuario);
        c.setEspecialidad(request.getEspecialidad());
        c.setFecha(request.getFecha());
        c.setMotivo(request.getMotivo());
        c.setEstado("PROGRAMADA");

        Cita guardada = citaRepository.save(c);
        return toDTO(guardada);
    }

    @Override
    public CitaDTO actualizarCita(Long idCita, Long idUsuario, CitaRequest request) {

        Cita c = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!c.getIdUsuario().equals(idUsuario)) {
            throw new RuntimeException("No tiene permiso para modificar esta cita");
        }

        if (!c.getEstado().equals("PROGRAMADA")) {
            throw new RuntimeException("Solo se pueden modificar citas PROGRAMADAS");
        }

        c.setEspecialidad(request.getEspecialidad());
        c.setFecha(request.getFecha());
        c.setMotivo(request.getMotivo());

        return toDTO(citaRepository.save(c));
    }

    @Override
    public void cancelarCita(Long idCita, Long idUsuario) {

        Cita c = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!c.getIdUsuario().equals(idUsuario)) {
            throw new RuntimeException("No tiene permiso para cancelar esta cita");
        }

        c.setEstado("CANCELADA");
        citaRepository.save(c);
    }

    @Override
    public List<CitaDTO> listarCitasPorUsuario(Long idUsuario) {
        return citaRepository.findByIdUsuario(idUsuario)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private CitaDTO toDTO(Cita c) {
        CitaDTO dto = new CitaDTO();
        dto.setId(c.getId());
        dto.setIdUsuario(c.getIdUsuario());
        dto.setEspecialidad(c.getEspecialidad());
        dto.setFecha(c.getFecha());
        dto.setMotivo(c.getMotivo());
        dto.setEstado(c.getEstado());
        return dto;
    }
}

