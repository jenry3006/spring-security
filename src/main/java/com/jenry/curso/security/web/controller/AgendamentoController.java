package com.jenry.curso.security.web.controller;

import com.jenry.curso.security.domain.Agendamento;
import com.jenry.curso.security.domain.Especialidade;
import com.jenry.curso.security.domain.Paciente;
import com.jenry.curso.security.service.AgendamentoService;
import com.jenry.curso.security.service.EspecialidadeService;
import com.jenry.curso.security.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private EspecialidadeService especialidadeService;

    @GetMapping("/agendar")
    public String agendarConsulta(Agendamento agendamento){
        return "agendamento/cadastro";
    }

    @GetMapping("/horario/medico/{id}/data/{data}")
    public ResponseEntity<?> getHorario(@PathVariable("id") Long id,
                                        @PathVariable("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data){
        return ResponseEntity.ok(service.buscarHorariosNaoAgendadosPosMedicoIdEData(id, data));
    }

    @PostMapping("/salvar")
    public String salvar(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user){
        Paciente paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
        String titulo = agendamento.getEspecialidade().getTitulo();
        Especialidade especialidade = especialidadeService
                .buscarPorTitulos(new String[]{titulo}).stream().findFirst().get();
        agendamento.setEspecialidade(especialidade);
        agendamento.setPaciente(paciente);
        service.salvar(agendamento);
        attr.addFlashAttribute("sucesso", "Sua consulta foi agendada com sucesso.");
        return "redirect:/agendamentos/agendar";
    }
}


