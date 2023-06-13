package com.jenry.curso.security.repository;

import com.jenry.curso.security.domain.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {
}
