package com.jenry.curso.security.web.controller;

import com.jenry.curso.security.domain.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("u")
public class UsuarioController {

    @GetMapping("/novo/cadastro/usuario")
    public String cadastroPorAdmin(Usuario usuario, ModelMap model){
        return "usuario/cadastro";
    }

}
