package com.jenry.curso.security.web.controller;

import com.jenry.curso.security.domain.Perfil;
import com.jenry.curso.security.domain.Usuario;
import com.jenry.curso.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
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

    //salvar cadastro do usuarios por administrador
    @PostMapping("/cadastro/salvar")
    public String salvarUsuario(Usuario usuario, RedirectAttributes attr){

        List<Perfil> perfis = usuario.getPerfis();
        if(perfis.size() > 2 ||
           perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L))) || //lista perfis possui perfil de id 1 (admin) e id 3(paciente)
           perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L)))) { //lista perfis possui perfil de id 2 (medico) e id 3(paciente)
            attr.addFlashAttribute("falha", "Paciente não pode ser um Admin e/ou Médico.");
            attr.addFlashAttribute("usuario", usuario);
        } else{
            try {
                service.salvarUsuario(usuario);
                attr.addFlashAttribute("sucesso","operação realizada com sucesso!");
            } catch (DataIntegrityViolationException ex){
                attr.addFlashAttribute("falha", "Cadastro não realizado, o email informado já existe.");
            }

        }
        return "redirect:/u/novo/cadastro/usuario";
    }

}
