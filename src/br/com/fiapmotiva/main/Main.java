package br.com.fiapmotiva.main;

import br.com.fiapmotiva.model.EquipeManutencao;
import br.com.fiapmotiva.model.Pessoa;
import br.com.fiapmotiva.model.TrechoRodovia;

import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        TrechoRodovia primeiroTrecho = new TrechoRodovia(1, 500, 20);
        TrechoRodovia segundoTrecho = new TrechoRodovia(501, 1000, 35);

        //Por que TrechoRodovia é uma classe e "BR-116 KM 10 ao 15" é um objeto?
        //TrechoRodovia é definido como classe pois serve como "molde",
        // e representa de forma comportamental um Trecho de Rodovia.
        //
        // Já "BR-116 KM 10 ao 15" trata-se de um objeto dessa classe,
        // ao qual os comportamentos da classe realmente tem efeito

        System.out.println("Listando primeiro trecho: " + primeiroTrecho.getQuilometroInicial() + "km - " + primeiroTrecho.getQuilometroFinal() + "km Tem " + primeiroTrecho.getNivelVegetacaoCm() + "cm de vegetação");
        System.out.println("Listando segundo trecho: " + segundoTrecho.getQuilometroInicial() + "km - " + segundoTrecho.getQuilometroFinal() + "km Tem " + segundoTrecho.getNivelVegetacaoCm() + "cm de vegetação");

        primeiroTrecho.registrarCrescimento(0.2);

        System.out.println("\n\nPrimeiro trecho após crescimento de 20%: " + primeiroTrecho.getNivelVegetacaoCm() + "cm");

        //LANÇANDO EXCEÇÃO
//        System.out.println("\n\nTentando usar metodo setNivelVegetacaoCm para valor negativo");
//        segundoTrecho.setNivelVegetacaoCm(-50);


        Pessoa joao = new Pessoa("1234567890", "João", new Date(100, Calendar.AUGUST, 6));
        Pessoa pedro = new Pessoa("0987654321", "Pedro", new Date(107, Calendar.OCTOBER, 22));

        EquipeManutencao equipeManutencao = new EquipeManutencao();


        equipeManutencao.adicionarMembros(joao);
        equipeManutencao.adicionarMembros(pedro);
        equipeManutencao.adicionarTrechoRodovia(primeiroTrecho);
        equipeManutencao.adicionarTrechoRodovia(segundoTrecho);

        System.out.println("\nMembros:");
        equipeManutencao.listarMembros();
        System.out.println("\nTrechos:");
        equipeManutencao.listarTrechos();

        //LANÇANDO EXCEÇÃO
        System.out.println("\n\nTentando atribuir trechos e membros duplicados");
//        equipeManutencao.adicionarTrechoRodovia(segundoTrecho);
//        equipeManutencao.adicionarMembros(pedro);

    }
}
