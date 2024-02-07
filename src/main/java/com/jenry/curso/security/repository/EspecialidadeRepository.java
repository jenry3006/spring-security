package com.jenry.curso.security.repository;

import com.jenry.curso.security.domain.Especialidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {
    //query para pesquisa dinamica no datatable
    @Query("select e from Especialidade e where e.titulo like :search%")
    Page<?> findAllByTitulo(String search, Pageable pageable);

    @Query("select e.titulo from Especialidade e where e.titulo like :termo%")
    List<String> findByEspecialidadesByTermo(String termo);


    @Query("select e from Especialidade e where e.titulo IN :titulos")
    Set<Especialidade> findByTitulos(String[] titulos);

    @Query("select e from Especialidade e "
    + "join e.medicos m "
    + "where m.id = :id")
    Page<Especialidade> findByIdMedico(Long id, Pageable pageable);
}
