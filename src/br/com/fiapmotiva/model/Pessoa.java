package br.com.fiapmotiva.model;

import java.text.DateFormat;
import java.util.Date;

public class Pessoa {
    private final String documento;
    private String nome;
    private Date dataNascimento;

    public Pessoa(String documento, String nome, Date dataNascimento) {
        this.documento = documento;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    @Override
    public String toString() {
        return "Documento: " +
                this.getDocumento() +
                " Nome: " + this.getNome() + " Data Nascimento: " + DateFormat.getDateInstance().format(this.getDataNascimento());
    }
}
