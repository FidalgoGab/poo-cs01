package br.com.fiapmotiva.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EquipeManutencao {
    private List<Pessoa> membros;
    private List<TrechoRodovia> trechos;

    public EquipeManutencao() {
        this.membros = new ArrayList<>();
        this.trechos = new ArrayList<>();
    }

    public void listarMembros() {
        for(Pessoa pessoa : membros) {
            System.out.println(pessoa.toString());
        }
    }

    public void listarTrechos() {
        for(TrechoRodovia trechoRodovia : trechos) {
            System.out.println(trechoRodovia.toString());
        }
    }

    public void adicionarMembros(Pessoa pessoa) {
        Set<String> pessoaDocumentoSet = new HashSet<>();

        for(Pessoa pes : this.membros) {
            pessoaDocumentoSet.add(pes.getDocumento());
        }

        if(pessoaDocumentoSet.contains(pessoa.getDocumento())) {
            throw new IllegalArgumentException("Essa pessoa já existe nessa equipe");
        } else {
            pessoaDocumentoSet.add(pessoa.getDocumento());
        }

        this.membros.add(pessoa);
    }

    public void adicionarTrechoRodovia(TrechoRodovia trechoRodovia) {
        Set<String> trechoRodoviaSet = new HashSet<>();

        for(TrechoRodovia trech : this.trechos) {
            trechoRodoviaSet.add(trech.getQuilometroInicial() + " - " + trech.getQuilometroFinal());
        }

        if(trechoRodoviaSet.contains(trechoRodovia.getQuilometroInicial() + " - " + trechoRodovia.getQuilometroFinal())) {
            throw new IllegalArgumentException("Trecho já adicionado para equipe");
        } else {
            trechoRodoviaSet.add(trechoRodovia.getQuilometroInicial() + " - " + trechoRodovia.getQuilometroFinal());
        }

        this.trechos.add(trechoRodovia);
    }
}
