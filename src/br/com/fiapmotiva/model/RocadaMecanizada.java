package br.com.fiapmotiva.model;

public class RocadaMecanizada extends IntervencaoOperacional {
    
    @Override
    public void executarServico(TrechoRodovia trecho) {
        System.out.println("Executando Roçada Mecanizada no trecho de " + trecho.getQuilometroInicial() + "km a " + trecho.getQuilometroFinal() + "km.");
        // Reseta o nível de vegetação para um nível pós-serviço (ex: 5.0 cm)
        trecho.setNivelVegetacaoCm(5.0);
    }
}
