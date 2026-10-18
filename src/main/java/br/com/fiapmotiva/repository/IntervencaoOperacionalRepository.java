package br.com.fiapmotiva.repository;

import br.com.fiapmotiva.model.IntervencaoOperacional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IntervencaoOperacionalRepository extends JpaRepository<IntervencaoOperacional, Long> {

    Optional<IntervencaoOperacional> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);
}
