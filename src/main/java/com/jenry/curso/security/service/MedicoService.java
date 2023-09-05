package com.jenry.curso.security.service;

import com.jenry.curso.security.domain.Medico;
import com.jenry.curso.security.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class MedicoService {

    @Autowired
    private MedicoRepository repository;

    @Transactional(readOnly = true)
    public Medico buscarUsuarioPorId(Long id){

        return repository.findByUsuarioId(id).orElse(new Medico());
    }

    @Transactional(readOnly = false)
    public void salvar(Medico medico) {
        repository.save(medico);
    }

    @Transactional(readOnly = false)
    public void editar(Medico medico) {

    }
}
