package br.com.fiapmotiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trechos_rodovia")
public class TrechoRodovia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trecho")
    private Long idTrecho;

    @Column(name = "nome_trecho", nullable = false, length = 120)
    private String nomeTrecho;

    @Column(name = "km_inicial", nullable = false)
    private Double kmInicial;

    @Column(name = "km_final", nullable = false)
    private Double kmFinal;

    @Column(name = "nivel_vegetacao_cm", nullable = false)
    private Double nivelVegetacaoCm;

    @Column(name = "tipo_trecho", nullable = false, length = 20)
    private String tipoTrecho;

    @Column(name = "monitoravel_iot", nullable = false)
    private Integer monitoravelIot = 0;

    @ManyToOne
    @JoinColumn(name = "id_equipe")
    private EquipeManutencao equipe;

    public TrechoRodovia() {
    }

    public TrechoRodovia(Long idTrecho, String nomeTrecho, Double kmInicial, Double kmFinal,
                         Double nivelVegetacaoCm, String tipoTrecho, Integer monitoravelIot,
                         EquipeManutencao equipe) {
        this.idTrecho = idTrecho;
        this.nomeTrecho = nomeTrecho;
        this.kmInicial = kmInicial;
        this.kmFinal = kmFinal;
        this.nivelVegetacaoCm = nivelVegetacaoCm;
        this.tipoTrecho = tipoTrecho;
        this.monitoravelIot = monitoravelIot;
        this.equipe = equipe;
    }

    public Long getIdTrecho() {
        return idTrecho;
    }

    public void setIdTrecho(Long idTrecho) {
        this.idTrecho = idTrecho;
    }

    public String getNomeTrecho() {
        return nomeTrecho;
    }

    public void setNomeTrecho(String nomeTrecho) {
        this.nomeTrecho = nomeTrecho;
    }

    public Double getKmInicial() {
        return kmInicial;
    }

    public void setKmInicial(Double kmInicial) {
        this.kmInicial = kmInicial;
    }

    public Double getKmFinal() {
        return kmFinal;
    }

    public void setKmFinal(Double kmFinal) {
        this.kmFinal = kmFinal;
    }

    public Double getNivelVegetacaoCm() {
        return nivelVegetacaoCm;
    }

    public void setNivelVegetacaoCm(Double nivelVegetacaoCm) {
        this.nivelVegetacaoCm = nivelVegetacaoCm;
    }

    public String getTipoTrecho() {
        return tipoTrecho;
    }

    public void setTipoTrecho(String tipoTrecho) {
        this.tipoTrecho = tipoTrecho;
    }

    public Integer getMonitoravelIot() {
        return monitoravelIot;
    }

    public void setMonitoravelIot(Integer monitoravelIot) {
        this.monitoravelIot = monitoravelIot;
    }

    public EquipeManutencao getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeManutencao equipe) {
        this.equipe = equipe;
    }
}
