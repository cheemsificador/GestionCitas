package com.idat.Requerimiento_01.service;

import com.idat.Requerimiento_01.dto.CitaDTO;
import com.idat.Requerimiento_01.dto.CitaRequest;

import java.util.List;

public interface CitaService {

    CitaDTO registrarCita(Long idUsuario, CitaRequest request);

    CitaDTO actualizarCita(Long idCita, Long idUsuario, CitaRequest request);

    void cancelarCita(Long idCita, Long idUsuario);

    List<CitaDTO> listarCitasPorUsuario(Long idUsuario);
}
