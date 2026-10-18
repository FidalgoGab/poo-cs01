package br.com.fiapmotiva.service;

import br.com.fiapmotiva.dto.RelatorioResponseDTO;
import br.com.fiapmotiva.exception.BusinessException;
import br.com.fiapmotiva.model.RelatorioPrioridade;
import br.com.fiapmotiva.model.TrechoRodovia;
import br.com.fiapmotiva.repository.RelatorioPrioridadeRepository;
import br.com.fiapmotiva.repository.TrechoRodoviaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RelatorioPrioridadeService {

    private final RelatorioPrioridadeRepository relatorioRepository;
    private final TrechoRodoviaRepository trechoRepository;

    public RelatorioPrioridadeService(RelatorioPrioridadeRepository relatorioRepository,
                                      TrechoRodoviaRepository trechoRepository) {
        this.relatorioRepository = relatorioRepository;
        this.trechoRepository = trechoRepository;
    }

    @Transactional
    public RelatorioResponseDTO gerarRelatorio() {
        List<TrechoRodovia> trechos = trechoRepository.findAll();

        int qtUrgente = 0;
        int qtCritico = 0;
        int qtAtencao = 0;
        int qtNormal = 0;

        StringBuilder resumo = new StringBuilder();

        for (TrechoRodovia trecho : trechos) {
            double nivel = trecho.getNivelVegetacaoCm() != null ? trecho.getNivelVegetacaoCm() : 0.0;
            String tipo = trecho.getTipoTrecho() != null ? trecho.getTipoTrecho().toUpperCase() : "SECO";

            resumo.append("Trecho [").append(trecho.getNomeTrecho()).append("] ");
            resumo.append("KM ").append(trecho.getKmInicial()).append("-").append(trecho.getKmFinal()).append(" ");
            resumo.append("(").append(nivel).append(" cm): ");

            if (nivel >= 50.0) {
                qtUrgente++;
                resumo.append("URGENTE (Rocada Mecanizada); ");
            } else if (nivel >= 30.0) {
                qtCritico++;
                if ("UMIDO".equals(tipo)) {
                    resumo.append("CRITICO (Pulverizacao Quimica); ");
                } else {
                    resumo.append("CRITICO (Rocada Manual); ");
                }
            } else if (nivel >= 15.0) {
                qtAtencao++;
                resumo.append("ATENCAO (Monitoramento); ");
            } else {
                qtNormal++;
                resumo.append("NORMAL (Sob controle); ");
            }
        }

        if (trechos.isEmpty()) {
            resumo.append("Nenhum trecho rodoviario cadastrado para analise.");
        }

        String resumoFinal = resumo.length() > 500 ? resumo.substring(0, 497) + "..." : resumo.toString();

        RelatorioPrioridade relatorio = new RelatorioPrioridade();
        relatorio.setQtUrgente(qtUrgente);
        relatorio.setQtCritico(qtCritico);
        relatorio.setQtAtencao(qtAtencao);
        relatorio.setQtNormal(qtNormal);
        relatorio.setResumo(resumoFinal);
        relatorio.setDataGeracao(LocalDate.now());

        RelatorioPrioridade salvo = relatorioRepository.save(relatorio);
        return toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<RelatorioResponseDTO> listarHistorico() {
        return relatorioRepository.findAllByOrderByDataGeracaoDesc().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RelatorioResponseDTO> consultarPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new BusinessException("As datas de inicio e fim sao obrigatorias.");
        }

        if (inicio.isAfter(fim)) {
            throw new BusinessException("A data de inicio nao pode ser posterior a data de fim.");
        }

        return relatorioRepository.findByDataGeracaoBetween(inicio, fim).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private RelatorioResponseDTO toResponseDTO(RelatorioPrioridade entity) {
        int total = entity.getQtUrgente() + entity.getQtCritico() + entity.getQtAtencao() + entity.getQtNormal();
        return new RelatorioResponseDTO(
                entity.getIdRelatorio(),
                entity.getQtUrgente(),
                entity.getQtCritico(),
                entity.getQtAtencao(),
                entity.getQtNormal(),
                total,
                entity.getResumo(),
                entity.getDataGeracao()
        );
    }
}
