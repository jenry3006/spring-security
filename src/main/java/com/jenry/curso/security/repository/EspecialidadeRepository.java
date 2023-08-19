package com.jenry.curso.security.repository;

import com.jenry.curso.security.domain.Especialidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {
    //query para pesquisa dinamica no datatable
    @Query("select e from Especialidade e where e.titulo like :search%")
    Page<?> findAllByTitulo(String search, Pageable pageable);
}
