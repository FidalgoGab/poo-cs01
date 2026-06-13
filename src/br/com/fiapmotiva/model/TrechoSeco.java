package br.com.fiapmotiva.model;

public class TrechoSeco extends TrechoRodovia {
    
    public TrechoSeco(int quilometroInicial, int quilometroFinal, double nivelVegetacaoCm) {
        super(quilometroInicial, quilometroFinal, nivelVegetacaoCm);
    }

    @Override
    public void registrarCrescimento(double taxaCrescimentoPorcentagem) {
        // Trecho seco cresce normalmente
        super.registrarCrescimento(taxaCrescimentoPorcentagem);
    }

    @Override
    public String toString() {
        return super.toString() + " (Trecho Seco)";
    }
}
