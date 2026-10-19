package br.com.fiapmotiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "relatorio_prioridade")
public class RelatorioPrioridade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_relatorio")
    private Long idRelatorio;

    @Column(name = "qt_urgente", nullable = false)
    private Integer qtUrgente;

    @Column(name = "qt_critico", nullable = false)
    private Integer qtCritico;

    @Column(name = "qt_atencao", nullable = false)
    private Integer qtAtencao;

    @Column(name = "qt_normal", nullable = false)
    private Integer qtNormal;

    @Column(name = "resumo", length = 500)
    private String resumo;

    @Column(name = "data_geracao", nullable = false)
    private LocalDate dataGeracao;

    public RelatorioPrioridade() {
    }

    public RelatorioPrioridade(Long idRelatorio, Integer qtUrgente, Integer qtCritico,
                               Integer qtAtencao, Integer qtNormal, String resumo,
                               LocalDate dataGeracao) {
        this.idRelatorio = idRelatorio;
        this.qtUrgente = qtUrgente;
        this.qtCritico = qtCritico;
        this.qtAtencao = qtAtencao;
        this.qtNormal = qtNormal;
        this.resumo = resumo;
        this.dataGeracao = dataGeracao;
    }

    public Long getIdRelatorio() {
        return idRelatorio;
    }

    public void setIdRelatorio(Long idRelatorio) {
        this.idRelatorio = idRelatorio;
    }

    public Integer getQtUrgente() {
        return qtUrgente;
    }

    public void setQtUrgente(Integer qtUrgente) {
        this.qtUrgente = qtUrgente;
    }

    public Integer getQtCritico() {
        return qtCritico;
    }

    public void setQtCritico(Integer qtCritico) {
        this.qtCritico = qtCritico;
    }

    public Integer getQtAtencao() {
        return qtAtencao;
    }

    public void setQtAtencao(Integer qtAtencao) {
        this.qtAtencao = qtAtencao;
    }

    public Integer getQtNormal() {
        return qtNormal;
    }

    public void setQtNormal(Integer qtNormal) {
        this.qtNormal = qtNormal;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public LocalDate getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDate dataGeracao) {
        this.dataGeracao = dataGeracao;
    }
}

// refactor: added spacing
