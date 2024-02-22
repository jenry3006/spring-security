package com.jenry.curso.security.web.controller;

import com.jenry.curso.security.domain.Paciente;
import com.jenry.curso.security.domain.Usuario;
import com.jenry.curso.security.service.PacienteService;
import com.jenry.curso.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/dados")
    public String cadastrar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user){

        paciente =service.buscarPorUsuarioEmail(user.getUsername());

        if (paciente.hasNotId()){
            paciente.setUsuario(new Usuario(user.getUsername()));
        }
        model.addAttribute("paciente",paciente);
        return "paciente/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user){

        Usuario u = usuarioService.buscarPorEmail(user.getUsername());
        if(UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), u.getSenha())){
            paciente.setUsuario(u);
            service.salvar(paciente);
            model.addAttribute("sucesso", "Seus dados foram cadastrados com sucesso.");
        } else {
            model.addAttribute("falha", "Sua senha não confere.");
        }
        return "paciente/cadastro";
    }

    @PostMapping("/editar")
    public String editar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user){

        Usuario u = usuarioService.buscarPorEmail(user.getUsername());
        if(UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), u.getSenha())){
            service.editar(paciente);
            model.addAttribute("sucesso", "Seus dados foram editados com sucesso.");
        } else {
            model.addAttribute("falha", "Sua senha não confere.");
        }
        return "paciente/cadastro";
    }
}
