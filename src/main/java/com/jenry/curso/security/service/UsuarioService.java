package com.jenry.curso.security.service;

import com.jenry.curso.security.domain.Usuario;
import com.jenry.curso.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email);

    }

}
