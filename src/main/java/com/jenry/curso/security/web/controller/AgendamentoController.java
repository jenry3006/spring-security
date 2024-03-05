package com.jenry.curso.security.web.controller;

import com.jenry.curso.security.domain.Agendamento;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("agendamentos")
public class AgendamentoController {

    @GetMapping("/agendar")
    public String agendarConsulta(Agendamento agendamento){
        return "agendamento/cadastro";
    }

}
