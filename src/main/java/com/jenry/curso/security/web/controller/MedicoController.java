package com.jenry.curso.security.web.controller;

import com.jenry.curso.security.domain.Medico;
import com.jenry.curso.security.domain.Usuario;
import com.jenry.curso.security.service.MedicoService;
import com.jenry.curso.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoService service;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/dados")
    public String abrirPorMedico(Medico medico, ModelMap model,@AuthenticationPrincipal User user){
        if (medico.hasNotId()){
            medico = service.buscarPorEmail(user.getUsername());
            model.addAttribute("medico", medico);
        }
        return "medico/cadastro";
    }

    //salvar medico
    @PostMapping({"/salvar"})
    public String abrirPorMedico(Medico medico, RedirectAttributes attr, @AuthenticationPrincipal User user){
        if(medico.hasNotId() && medico.getUsuario().hasNotId()){
            Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
            medico.setUsuario(usuario);
        }

        service.salvar(medico);
        attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
        attr.addFlashAttribute("medico", medico);
        return "redirect:/medicos/dados";
    }

    //editar medico
    @PostMapping({"/editar"})
    public String editar(Medico medico, RedirectAttributes attr){
        service.editar(medico);
        attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
        attr.addFlashAttribute("medico", medico);
        return "redirect:/medicos/dados";
    }

    //excluir especialidade
    @GetMapping({"/id/{idMed}/excluir/especializacao/{idEsp}"})
    public String excluirEspecialidadePorMedico(@PathVariable("idMed") Long idMed,
                          @PathVariable("idEsp") Long idEsp, RedirectAttributes attr){

        if (service.existeEspecialidadeAgendada(idMed, idEsp)){
            attr.addFlashAttribute(("falha"), "Existem consultas agendadas com esta especialidade, exlusão negada.");
        } else {
            service.excluirEspecialidadePorMedico(idMed,idEsp);
            attr.addFlashAttribute("sucesso", "Especialidade removida com sucesso.");
        }

        return "redirect:/medicos/dados";
    }

    @GetMapping("/especialidade/titulo/{titulo}")
    public ResponseEntity<?> getMedicoPorEspecialidade(@PathVariable("titulo") String titulo){
        return ResponseEntity.ok(service.buscarMedicoPorEspecialidade(titulo));
    }
}
