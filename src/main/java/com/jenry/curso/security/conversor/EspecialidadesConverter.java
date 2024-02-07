package com.jenry.curso.security.conversor;

import com.jenry.curso.security.domain.Especialidade;
import com.jenry.curso.security.service.EspecialidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


import java.util.HashSet;
import java.util.Set;

@Component
public class EspecialidadesConverter implements Converter<String[], Set<Especialidade>> {

    @Autowired
    private EspecialidadeService service;

    @Override
    public Set<Especialidade> convert (String[] titulos){

        Set<Especialidade> especialidades = new HashSet<>();
        if(titulos != null && titulos.length > 0){
            especialidades.addAll(service.buscarPorTitulos(titulos));
        }
        return especialidades;
    }
}
