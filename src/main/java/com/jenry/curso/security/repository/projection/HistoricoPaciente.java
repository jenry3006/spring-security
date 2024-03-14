package com.jenry.curso.security.repository.projection;

import com.jenry.curso.security.domain.Especialidade;
import com.jenry.curso.security.domain.Medico;
import com.jenry.curso.security.domain.Paciente;

public interface HistoricoPaciente {

    Long getId();

    Paciente getPaciente();

    String getDataConsulta();

    Medico getMedico();

    Especialidade getEspecialidade();
}
