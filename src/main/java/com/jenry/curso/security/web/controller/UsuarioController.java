package com.jenry.curso.security.web.controller;

import com.jenry.curso.security.domain.Medico;
import com.jenry.curso.security.domain.Perfil;
import com.jenry.curso.security.domain.PerfilTipo;
import com.jenry.curso.security.domain.Usuario;
import com.jenry.curso.security.service.MedicoService;
import com.jenry.curso.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
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

    @Autowired
    private MedicoService medicoService;

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

    //carregar pagina para editar pelo id e editar
    @GetMapping("/editar/credenciais/usuario/{id}")
    public ModelAndView preEditar(@PathVariable ("id") Long id){

        return new ModelAndView("usuario/cadastro", "usuario", service.buscarPorId(id));
    }

    //carregar pagina para editar pelo id e editar
    @GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
    public ModelAndView preEditarCadastroDadosPessoais(@PathVariable ("id") Long usuarioId,
                                                       @PathVariable ("perfis") Long[] perfisId){
        Usuario us = service.buscarPorIdEPerfis(usuarioId, perfisId);

        if (us.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
            !us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod())) ){

            return new ModelAndView("usuario/cadastro", "usuario", us);
        } else if (us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))){

            Medico medico = medicoService.buscarUsuarioPorId(usuarioId);

            return medico.hasNotId()
                    ? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))
                    : new ModelAndView("medico/cadastro", "medico", medico);

        } else if (us.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))){

            ModelAndView model = new ModelAndView("error");
            model.addObject("status", 403);
            model.addObject("error","Área restrita");
            model.addObject("message","Os dados do paciente são restritos.");
            return model;
        }

        return new ModelAndView("redirect:/u/lista");
    }

    @GetMapping("/editar/senha")
    public String abrirEditarSenha(){

        return "usuario/editar-senha";
    }

    @PostMapping("/confirmar/senha")
    public String editarSenha(@RequestParam("senha1") String s1, @RequestParam("senha2") String s2,
                              @RequestParam("senha3") String s3, @AuthenticationPrincipal User user,
                              RedirectAttributes attr){
            if (!s1.equals(s2)){
                attr.addFlashAttribute("falha", "Senhas não coincidem.");
                return "redirect:/u/editar/senha";
            }

            Usuario u = service.buscarPorEmail(user.getUsername());
            if(!UsuarioService.isSenhaCorreta(s3, u.getSenha())){
                attr.addFlashAttribute("falha", "Senha atual não confere.");
                return "redirect:/u/editar/senha";
            }

            service.alterarSenha(u, s1);
            attr.addFlashAttribute("sucesso", "Senha alternada com sucesso.");
            return "redirect:/u/editar/senha";

    }
}
