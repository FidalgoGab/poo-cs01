package br.com.fiapmotiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "intervencao_operacional")
public class IntervencaoOperacional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_intervencao")
    private Long idIntervencao;

    @Column(name = "nome", nullable = false, unique = true, length = 50)
    private String nome;

    @Column(name = "descricao", length = 200)
    private String descricao;

    public IntervencaoOperacional() {
    }

    public IntervencaoOperacional(Long idIntervencao, String nome, String descricao) {
        this.idIntervencao = idIntervencao;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getIdIntervencao() {
        return idIntervencao;
    }

    public void setIdIntervencao(Long idIntervencao) {
        this.idIntervencao = idIntervencao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
