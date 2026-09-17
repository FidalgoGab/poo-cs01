package br.com.fiapmotiva.service;

import br.com.fiapmotiva.dao.RelatorioPrioridadeDAO;
import br.com.fiapmotiva.db.ConexaoBanco;
import br.com.fiapmotiva.model.MonitoravelViaIoT;
import br.com.fiapmotiva.model.TrechoRodovia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeradorRelatorio {

    public void gerarRelatorioCritico() throws SQLException {
        String sql = "SELECT nome_trecho, tipo_trecho, nivel_vegetacao_cm " +
                "FROM trechos_rodovia WHERE nivel_vegetacao_cm >= 30 ORDER BY nivel_vegetacao_cm DESC";

        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            System.out.println("\nRelatório de trechos críticos:");
            while (rs.next()) {
                System.out.printf("- %s | %s | %.2f cm%n",
                        rs.getString("nome_trecho"),
                        rs.getString("tipo_trecho"),
                        rs.getDouble("nivel_vegetacao_cm"));
            }
        }
    }

    public void gerarRelatorio(TrechoRodovia[] trechos) throws SQLException {
        System.out.println("\n-----------------------------------------------------");
        System.out.println("               RELATÓRIO DE PRIORIDADE DE ROÇADA          ");
        System.out.println("-----------------------------------------------------");

        int qtUrgente = 0;
        int qtCritico = 0;
        int qtAtencao = 0;
        int qtNormal = 0;
        StringBuilder resumo = new StringBuilder();

        for (TrechoRodovia trecho : trechos) {
            double nivelVegetacao = trecho.getNivelVegetacaoCm();

            if (trecho instanceof MonitoravelViaIoT) {
                nivelVegetacao = ((MonitoravelViaIoT) trecho).transmitirDadosSensor();
            }

            System.out.println("\n(Analise) KM " + trecho.getQuilometroInicial() + " ao " + trecho.getQuilometroFinal()
                    + " | Vegetação: " + nivelVegetacao + "cm");

            if (nivelVegetacao >= 50.0) {
                System.out.println(" PRIORIDADE ALTA: Necessita de Roçada Mecanizada urgente!");
                qtUrgente++;
            } else if (nivelVegetacao >= 30.0) {
                System.out.println(" PRIORIDADE MÉDIA: Necessita de intervenção.");
                qtCritico++;
            } else if (nivelVegetacao >= 15.0) {
                System.out.println(" PRIORIDADE ATENÇÃO: Monitorar e acompanhar o crescimento.");
                qtAtencao++;
            } else {
                System.out.println(" PRIORIDADE BAIXA: Nível sob controle. Nenhuma ação necessária.");
                qtNormal++;
            }

            // Salva o resumo textual do trecho para o banco
            resumo.append("KM ")
                    .append(trecho.getQuilometroInicial())
                    .append("-")
                    .append(trecho.getQuilometroFinal())
                    .append(" : ")
                    .append(nivelVegetacao)
                    .append("cm; ");
        }

        RelatorioPrioridadeDAO dao = new RelatorioPrioridadeDAO();
        dao.salvarRelatorio(qtUrgente, qtCritico, qtAtencao, qtNormal, resumo.toString());
    }
}
