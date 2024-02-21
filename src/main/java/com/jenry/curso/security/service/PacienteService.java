package com.jenry.curso.security.service;

import com.jenry.curso.security.domain.Paciente;
import com.jenry.curso.security.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repository;

    @Transactional(readOnly = true)
    public Paciente buscarPorUsuarioEmail(String email){
        return repository.findByUsuarioEmail(email).orElse(new Paciente());
    }

    @Transactional(readOnly = false)
    public void salvar(Paciente paciente) {
        repository.save(paciente);
    }
}
