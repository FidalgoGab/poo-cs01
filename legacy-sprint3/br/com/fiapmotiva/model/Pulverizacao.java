package br.com.fiapmotiva.model;

public class Pulverizacao extends IntervencaoOperacional {
    
    @Override
    public void executarServico(TrechoRodovia trecho) {
        System.out.println("Executando Pulverização química no trecho de " + trecho.getQuilometroInicial() + "km a " + trecho.getQuilometroFinal() + "km.");
        // Reseta o nível de vegetação para um nível pós-serviço (ex: 8.0 cm)
        trecho.setNivelVegetacaoCm(8.0);
    }
}
