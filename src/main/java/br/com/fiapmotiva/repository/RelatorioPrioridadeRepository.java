package br.com.fiapmotiva.repository;

import br.com.fiapmotiva.model.RelatorioPrioridade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RelatorioPrioridadeRepository extends JpaRepository<RelatorioPrioridade, Long> {

    List<RelatorioPrioridade> findByDataGeracaoBetween(LocalDate inicio, LocalDate fim);

    List<RelatorioPrioridade> findAllByOrderByDataGeracaoDesc();
}
