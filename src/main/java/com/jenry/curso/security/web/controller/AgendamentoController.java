package com.jenry.curso.security.web.controller;

import com.jenry.curso.security.domain.Agendamento;
import com.jenry.curso.security.domain.Especialidade;
import com.jenry.curso.security.domain.Paciente;
import com.jenry.curso.security.domain.PerfilTipo;
import com.jenry.curso.security.service.AgendamentoService;
import com.jenry.curso.security.service.EspecialidadeService;
import com.jenry.curso.security.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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

    @PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
    @GetMapping("/agendar")
    public String agendarConsulta(Agendamento agendamento){
        return "agendamento/cadastro";
    }

    @PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
    @GetMapping("/horario/medico/{id}/data/{data}")
    public ResponseEntity<?> getHorario(@PathVariable("id") Long id,
                                        @PathVariable("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data){
        return ResponseEntity.ok(service.buscarHorariosNaoAgendadosPosMedicoIdEData(id, data));
    }

    @PreAuthorize("hasAuthority('PACIENTE')")
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

    //abrir pagina de historico de agendamento do paciente
    @PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
    @GetMapping({"/historico/paciente","/historico/consultas"})
    public String historico(){
        return "agendamento/historico-paciente";
    }

    //localizar o historico de agendamentos por usuario logado
    @PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
    @GetMapping("/datatables/server/historico")
    public ResponseEntity<?> historicoAgendamentoPorPaciente(HttpServletRequest request,@AuthenticationPrincipal User user){
        if(user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.PACIENTE.getDesc()))){ //SimpleGrantedAuthority: passa o tipo de perfil que esta procurando.
            return ResponseEntity.ok(service.buscarHistoricoPorPacienteEmail(user.getUsername(),request));
        }

        if(user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.MEDICO.getDesc()))){

            return ResponseEntity.ok(service.buscarHistoricoPorMedicoEmail(user.getUsername(),request));
        }

        return ResponseEntity.notFound().build();
    }

    //localizar agendamento atraves do id e enviar para pagina cadastro
    @PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
    @GetMapping("/editar/consulta/{id}")
    public String preEditarConsultaPaciente(@PathVariable("id") Long id,
                                            ModelMap model, @AuthenticationPrincipal User user){
        Agendamento agendamento = service.buscarPorIdEUsuario(id, user.getUsername());

        model.addAttribute("agendamento", agendamento);

        return "agendamento/cadastro";
    }

    @PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
    @PostMapping("/editar")
    public String editarConsulta(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user){
        String titulo = agendamento.getEspecialidade().getTitulo();
        Especialidade especialidade = especialidadeService
                .buscarPorTitulos(new String[]{titulo}).stream().findFirst().get();
        agendamento.setEspecialidade(especialidade);

        service.editar(agendamento,user.getUsername());
        attr.addFlashAttribute("sucesso","Consulta editada com sucesso.");
        return "redirect:/agendamentos/agendar";
    }

    @PreAuthorize("hasAuthority('PACIENTE')")
    @GetMapping("/excluir/consulta/{id}")
    public String excluirConsulta(@PathVariable("id") Long id, RedirectAttributes attr){
        service.remover(id);
        attr.addFlashAttribute("sucesso","Consulta excluida com sucesso!");
        return "redirect:/agendamentos/historico/paciente";
    }

}