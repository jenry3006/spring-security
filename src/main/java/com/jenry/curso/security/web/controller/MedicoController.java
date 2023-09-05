package com.jenry.curso.security.web.controller;

import com.jenry.curso.security.domain.Medico;
import com.jenry.curso.security.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoService service;

    @GetMapping("/dados")
    public String abrirPorMedico(Medico medico, ModelMap model){
        return "medico/cadastro";
    }

    //salvar medico
    @GetMapping("/salvar")
    public String abrirPorMedico(Medico medico, RedirectAttributes attr){
        service.salvar(medico);
        attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
        attr.addFlashAttribute("medico", medico);
        return "redirect:/medicos/dados";
    }

    //editar medico
    @GetMapping("/editar")
    public String abrirPorMedico(Medico medico, ModelMap model){
        service.editar(medico);
        return "medico/cadastro";
    }
}
