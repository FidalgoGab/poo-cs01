package br.com.fiapmotiva.model;

public class TrechoUmido extends TrechoRodovia implements MonitoravelViaIoT {
    
    public TrechoUmido(int quilometroInicial, int quilometroFinal, double nivelVegetacaoCm) {
        super(quilometroInicial, quilometroFinal, nivelVegetacaoCm);
    }

    @Override
    public void registrarCrescimento(double taxaCrescimentoPorcentagem) {
        // Trecho úmido cresce 1.5x mais rápido que a taxa básica
        super.registrarCrescimento(taxaCrescimentoPorcentagem * 1.5);
    }

    @Override
    public double transmitirDadosSensor() {
        System.out.println("[IoT Sensor KM " + getQuilometroInicial() + "-" + getQuilometroFinal() + "] Transmitindo nível de vegetação atual.");
        return getNivelVegetacaoCm();
    }

    @Override
    public String toString() {
        return super.toString() + " (Trecho Úmido - IoT)";
    }
}
