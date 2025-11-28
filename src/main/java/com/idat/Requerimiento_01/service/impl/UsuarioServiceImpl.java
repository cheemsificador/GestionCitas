package com.idat.Requerimiento_01.service.impl;

import com.idat.Requerimiento_01.model.Usuario;
import com.idat.Requerimiento_01.repository.UsuarioRepository;
import com.idat.Requerimiento_01.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Usuario validarUsuario(String username, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        System.out.println("Intentando login con username: " + username);
        System.out.println("Usuario encontrado en DB: " + usuarioOpt);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            System.out.println("Hash en DB: " + usuario.getPassword());
            System.out.println("Password ingresado: " + password);
            System.out.println("Coincide? " + passwordEncoder.matches(password, usuario.getPassword()));

            if (passwordEncoder.matches(password, usuario.getPassword())) {
                return usuario;
            }
        }

        System.out.println("Credenciales incorrectas para username: " + username);
        return null;
    }



    @Override
    public Usuario registrarUsuario(String username, String password) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }
}
