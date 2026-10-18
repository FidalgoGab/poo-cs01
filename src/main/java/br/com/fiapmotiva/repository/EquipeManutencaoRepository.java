package br.com.fiapmotiva.repository;

import br.com.fiapmotiva.model.EquipeManutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipeManutencaoRepository extends JpaRepository<EquipeManutencao, Long> {

    List<EquipeManutencao> findByNomeContainingIgnoreCase(String nome);
}
