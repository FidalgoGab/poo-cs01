package br.com.fiapmotiva.model;

public class TrechoRodovia {
    private int quilometroInicial;
    private int quilometroFinal;
    private double nivelVegetacaoCm;
    private EquipeManutencao equipeAtribuida;

    public TrechoRodovia(int quilometroInicial, int quilometroFinal, double nivelVegetacaoCm) {
        if (quilometroFinal <= quilometroInicial) {
            throw new IllegalArgumentException("O quilômetro final deve ser maior que o quilômetro inicial.");
        }
        if (nivelVegetacaoCm < 0) {
            throw new IllegalArgumentException("Nível de vegetação não pode ser negativo.");
        }
        this.quilometroInicial = quilometroInicial;
        this.quilometroFinal = quilometroFinal;
        this.nivelVegetacaoCm = nivelVegetacaoCm;
    }

    public void registrarCrescimento(double taxaCrescimentoPorcentagem) {
        //Como um metodo difere de uma função solta em programação estruturada?
        //Métodos como esse são definidos somente dentro de classes, e atuam como comportamentos de
        // objetos dessa Classe.
        //
        // Funções genéricas podem ser utilizadas em qualquer parte do código, sem representar algum
        // comportamento de algum objeto/entidade necessáriamente

        // Decisão: O cálculo de crescimento está implementado utilizando taxa percentual (porcentagem),
        // permitindo que o crescimento seja proporcional ao nível atual da vegetação (um modelo dinâmico onde
        // trechos com mais vegetação crescem mais rápido em volume absoluto).
        if(taxaCrescimentoPorcentagem < 0){
            throw new IllegalArgumentException("Taxa de crescimento não pode ser negativa.");
        }
        setNivelVegetacaoCm(nivelVegetacaoCm + nivelVegetacaoCm * taxaCrescimentoPorcentagem );
    }

    public int getQuilometroInicial() {
        return quilometroInicial;
    }

    public int getQuilometroFinal() {
        return quilometroFinal;
    }

    public double getNivelVegetacaoCm() {
        return nivelVegetacaoCm;
    }

    public void setNivelVegetacaoCm(double nivelVegetacaoCm) {
        //Se o nivelVegetacao fosse público, que tipo de "quebra" no sistema de previsão
        // da Motiva um programador descuidado poderia causar?

        //Ao deixar o atributo público corremos o risco dele poder ser atualizado/modificado sem qualquer
        // tipo de validação/restrição, assim recebendo qualquer valor (negativos, positivos, zeros).
        if(nivelVegetacaoCm < 0){
            throw new IllegalArgumentException("Nível de vegetação não pode ser negativo.");
        }
        this.nivelVegetacaoCm = nivelVegetacaoCm;
    }

    public EquipeManutencao getEquipeAtribuida() {
        return equipeAtribuida;
    }

    public void atribuirEquipe(EquipeManutencao equipe) {
        // Validação se o trecho é crítico (vegetação >= 30cm) antes de atribuir a equipe
        if (this.nivelVegetacaoCm < 30.0) {
            throw new IllegalStateException("O trecho de KM " + this.quilometroInicial + " a " + this.quilometroFinal + 
                " não está em estado crítico (vegetação < 30cm) para receber uma equipe de manutenção.");
        }
        this.equipeAtribuida = equipe;
    }

    @Override
    public String toString() {
        return this.getQuilometroInicial() + "km - " + this.getQuilometroFinal() + "km Tem " + this.getNivelVegetacaoCm() + "cm de vegetação" + 
            (equipeAtribuida != null ? " (Equipe Atribuída)" : " (Nenhuma Equipe atribuída)");
    }
}

