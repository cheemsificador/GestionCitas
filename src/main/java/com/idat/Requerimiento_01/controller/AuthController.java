package com.idat.Requerimiento_01.controller;

import com.idat.Requerimiento_01.dto.JwtResponse;
import com.idat.Requerimiento_01.dto.LoginRequest;
import com.idat.Requerimiento_01.dto.RegisterRequest;
import com.idat.Requerimiento_01.model.Usuario;
import com.idat.Requerimiento_01.security.jwt.JwtUtil;
import com.idat.Requerimiento_01.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    // ========== LOGIN ==========
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioService.validarUsuario(request.getUsername(), request.getPassword());

        if (usuario == null) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }

        String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getId());

        return ResponseEntity.ok(new JwtResponse(token));
    }

    // ========== REGISTRO (opcional) ==========
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (usuarioService.buscarPorUsername(request.getUsername()) != null) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }

        Usuario nuevo = usuarioService.registrarUsuario(request.getUsername(), request.getPassword());

        return ResponseEntity.ok(nuevo);
    }
}
