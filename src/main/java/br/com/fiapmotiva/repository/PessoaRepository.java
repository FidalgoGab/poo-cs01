package br.com.fiapmotiva.repository;

import br.com.fiapmotiva.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, String> {

    List<Pessoa> findByNomeContainingIgnoreCase(String nome);
}
