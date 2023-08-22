package com.jenry.curso.security.web.controller;

import com.jenry.curso.security.domain.Usuario;
import com.jenry.curso.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("u")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    //Abrir cadastro de usuarios
    @GetMapping("/novo/cadastro/usuario")
    public String cadastroPorAdmin(Usuario usuario, ModelMap model){
        return "usuario/cadastro";
    }

    //Abrir lista de usuarios
    @GetMapping("/lista")
    public String listarUsuarios(){
        return "usuario/lista";
    }

    //Abrir lista de usuarios
    @GetMapping("/datatables/server/usuarios")
    public ResponseEntity<?> listarUsuariosDatatables(HttpServletRequest request){

        return ResponseEntity.ok(service.buscarTodos(request));
    }

}
