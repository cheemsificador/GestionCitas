package com.idat.Requerimiento_01.service;

import com.idat.Requerimiento_01.model.Usuario;

public interface UsuarioService {
	
    Usuario validarUsuario(String username, String password);
    
    Usuario registrarUsuario(String username, String password);
    
    Usuario buscarPorUsername(String username);
}
