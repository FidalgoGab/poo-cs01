package br.com.fiapmotiva.main;

import br.com.fiapmotiva.model.*;

import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        //Por que TrechoRodovia é uma classe e "BR-116 KM 10 ao 15" é um objeto?
        //TrechoRodovia é definido como classe pois serve como "molde",
        // e representa de forma comportamental um Trecho de Rodovia.
        //
        // Já "BR-116 KM 10 ao 15" trata-se de um objeto dessa classe,
        // ao qual os comportamentos da classe realmente tem efeito


        // Teste de validação do construtor (quilômetro final <= inicial)
        try {
            System.out.println("Tentando criar trecho com KM inicial maior que o final...");
            new TrechoSeco(500, 200, 10);
        } catch (IllegalArgumentException e) {
            System.out.println("Exceção capturada com sucesso: " + e.getMessage());
        }

        // 1. Garantir a impossibilidade de instanciar a classe base com new (via Reflection)
        // Auxilio de IA 
        try {
            System.out.println("\n[Teste Unitário] Tentando instanciar a classe abstrata IntervencaoOperacional via Reflection...");
            IntervencaoOperacional.class.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            System.out.println("Sucesso! Exceção capturada (Impossível instanciar classe abstrata): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exceção capturada: " + e.getMessage());
        }

        // 2. Simular um objeto "Mock" que implementa MonitoravelViaIoT e testar a captura de dados de crescimento
        System.out.println("\n[Teste Unitário] Simulando objeto Mock que implementa MonitoravelViaIoT...");
        MockSensorIoT mockIoT = new MockSensorIoT(15.0);
        System.out.println("Nível inicial do sensor IoT Mock: " + mockIoT.transmitirDadosSensor() + "cm");
        mockIoT.simularCrescimento(0.5); // Crescimento de 50%
        System.out.println("Nível do sensor IoT Mock pós-crescimento (captura de dados): " + mockIoT.transmitirDadosSensor() + "cm (esperado: 22.5cm)");

        // Criando trechos de teste
        TrechoRodovia trechoSeco1 = new TrechoSeco(1, 100, 20); // Não crítico inicialmente
        TrechoRodovia trechoUmido1 = new TrechoUmido(101, 200, 25); // Não crítico inicialmente
        TrechoRodovia trechoUmido2 = new TrechoUmido(201, 300, 35); // Já inicia crítico (>= 30cm)

        System.out.println("\nTrechos criados:");
        System.out.println(trechoSeco1);
        System.out.println(trechoUmido1);
        System.out.println(trechoUmido2);

        // Criando equipe de manutenção
        Pessoa joao = new Pessoa("1234567890", "João", new Date(100, Calendar.AUGUST, 6));
        EquipeManutencao equipe = new EquipeManutencao();
        equipe.adicionarMembros(joao);

        // Testando a associação e validação de trecho crítico
        System.out.println("\nTentando atribuir equipe ao trecho seco não crítico (20cm)...");
        try {
            // Isso deve lançar uma exceção pois a vegetação é < 30cm
            equipe.adicionarTrechoRodovia(trechoSeco1);
        } catch (IllegalStateException e) {
            System.out.println("Exceção capturada com sucesso: " + e.getMessage());
        }

        System.out.println("\nAtribuindo equipe ao trecho úmido crítico (35cm)...");
        try {
            equipe.adicionarTrechoRodovia(trechoUmido2);
            System.out.println("Equipe atribuída com sucesso! Detalhes do trecho:");
            System.out.println(trechoUmido2);
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }

        // Simula crescimento em todos os trechos
        System.out.println("Aplicando taxa de crescimento básica de 40% (0.4)...");
        trechoSeco1.registrarCrescimento(0.4);   
        trechoUmido1.registrarCrescimento(0.4);  
        trechoUmido2.registrarCrescimento(0.4);  

        System.out.println("Novos níveis de vegetação:");
        System.out.println(trechoSeco1);
        System.out.println(trechoUmido1);
        System.out.println(trechoUmido2);

        // Array de trechos para gerar o relatório
        TrechoRodovia[] todosTrechos = { trechoSeco1, trechoUmido1, trechoUmido2 };

        gerarRelatorioPrioridade(todosTrechos);
    }

    /**
     * Algoritmo que varre um array de trechos e gera um "Relatório de Prioridade" automático
     */
    public static void gerarRelatorioPrioridade(TrechoRodovia[] trechos) {
        System.out.println("\n-----------------------------------------------------");
        System.out.println("               RELATÓRIO DE PRIORIDADE DE ROÇADA          ");
        System.out.println("-----------------------------------------------------");

        IntervencaoOperacional rocadaMecanizada = new RocadaMecanizada();
        IntervencaoOperacional pulverizacao = new Pulverizacao();

        for (TrechoRodovia trecho : trechos) {
            double nivelVegetacao = trecho.getNivelVegetacaoCm();
            
            // Simulação de leitura de dados via IoT se o trecho for monitorável
            if (trecho instanceof MonitoravelViaIoT) {
                nivelVegetacao = ((MonitoravelViaIoT) trecho).transmitirDadosSensor();
            }

            System.out.println("\n(Analise) KM " + trecho.getQuilometroInicial() + " ao " + trecho.getQuilometroFinal() + " | Vegetação: " + nivelVegetacao + "cm");

            if (nivelVegetacao >= 50.0) {
                System.out.println(" PRIORIDADE ALTA: Necessita de Roçada Mecanizada urgente!");
                rocadaMecanizada.executarServico(trecho);
            } else if (nivelVegetacao >= 30.0) {
                System.out.println(" PRIORIDADE MÉDIA: Necessita de intervenção.");
                if (trecho instanceof TrechoUmido) {
                    System.out.println(" Indicado: Pulverização química (Trecho úmido tem crescimento acelerado).");
                    pulverizacao.executarServico(trecho);
                } else {
                    System.out.println(" Indicado: Roçada Manual.");
                    trecho.setNivelVegetacaoCm(10.0); // Simula uma roçada manual voltando para 10cm
                }
            } else {
                System.out.println(" PRIORIDADE BAIXA: Nível sob controle. Nenhuma ação necessária.");
            }
        }
    }
}

class MockSensorIoT implements MonitoravelViaIoT {
    private double nivelVegetacao;

    public MockSensorIoT(double nivelVegetacao) {
        this.nivelVegetacao = nivelVegetacao;
    }

    @Override
    public double transmitirDadosSensor() {
        return this.nivelVegetacao;
    }

    public void simularCrescimento(double taxaCrescimento) {
        this.nivelVegetacao += this.nivelVegetacao * taxaCrescimento;
    }
}
