package br.com.fiapmotiva.repository;

import br.com.fiapmotiva.model.TrechoRodovia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrechoRodoviaRepository extends JpaRepository<TrechoRodovia, Long> {

    List<TrechoRodovia> findByNivelVegetacaoCmGreaterThanEqual(Double nivelMinimo);

    List<TrechoRodovia> findByTipoTrecho(String tipoTrecho);

    List<TrechoRodovia> findByMonitoravelIot(Integer monitoravelIot);

    List<TrechoRodovia> findByEquipeIdEquipe(Long idEquipe);
}
