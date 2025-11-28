package com.idat.Requerimiento_01.controller;

import com.idat.Requerimiento_01.dto.CitaDTO;
import com.idat.Requerimiento_01.dto.CitaRequest;
import com.idat.Requerimiento_01.security.jwt.JwtUtil;
import com.idat.Requerimiento_01.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private JwtUtil jwtUtil;

    // 🔒 Obtener ID usuario desde token
    private Long getUserIdFromToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.replace("Bearer ", "");
        return jwtUtil.getIdUsuario(token);
    }

    // ========== REGISTRAR CITA ==========
    @PostMapping
    public ResponseEntity<CitaDTO> registrarCita(
            @RequestBody CitaRequest request,
            HttpServletRequest httpRequest) {

        Long idUsuario = getUserIdFromToken(httpRequest);

        CitaDTO dto = citaService.registrarCita(idUsuario, request);
        return ResponseEntity.ok(dto);
    }

    // ========== ACTUALIZAR CITA ==========
    @PutMapping("/{id}")
    public ResponseEntity<CitaDTO> actualizarCita(
            @PathVariable Long id,
            @RequestBody CitaRequest request,
            HttpServletRequest httpRequest) {

        Long idUsuario = getUserIdFromToken(httpRequest);

        CitaDTO dto = citaService.actualizarCita(id, idUsuario, request);
        return ResponseEntity.ok(dto);
    }

    // ========== CANCELAR CITA ==========
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarCita(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {

        Long idUsuario = getUserIdFromToken(httpRequest);

        citaService.cancelarCita(id, idUsuario);
        return ResponseEntity.ok("Cita cancelada");
    }

    // ========== LISTAR MIS CITAS ==========
    @GetMapping("/mis")
    public ResponseEntity<List<CitaDTO>> listarMisCitas(HttpServletRequest httpRequest) {

        Long idUsuario = getUserIdFromToken(httpRequest);

        List<CitaDTO> citas = citaService.listarCitasPorUsuario(idUsuario);
        return ResponseEntity.ok(citas);
    }
}
