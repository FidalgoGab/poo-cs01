package br.com.fiapmotiva.main;

import br.com.fiapmotiva.dao.EquipeManutencaoDAO;
import br.com.fiapmotiva.dao.IntervencaoOperacionalDAO;
import br.com.fiapmotiva.dao.RelatorioPrioridadeDAO;
import br.com.fiapmotiva.dao.TrechoRodoviaDAO;
import br.com.fiapmotiva.db.ConexaoBD;
import br.com.fiapmotiva.model.TrechoRodovia;
import br.com.fiapmotiva.model.TrechoSeco;
import br.com.fiapmotiva.model.TrechoUmido;
import br.com.fiapmotiva.service.GeradorRelatorio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Connection conn = null;

        try {
            // 1. Testar conexão
            ConexaoBD conexao = ConexaoBD.getInstancia();
            conn = conexao.conectar();

            // 2. Executar scripts SQL do projeto
            executarScriptSql(conn, "script-criacao.sql");
            executarScriptSql(conn, "script-dados.sql");

            // 3. Testar CRUD de Equipe
            EquipeManutencaoDAO daoEquipe = new EquipeManutencaoDAO();
            EquipeManutencaoDAO.EquipeManutencaoRecord equipe = new EquipeManutencaoDAO.EquipeManutencaoRecord(null,
                    "Equipe Motiva", "Equipe de manutenção de rodovias");
            daoEquipe.inserir(equipe);

            List<EquipeManutencaoDAO.EquipeManutencaoRecord> equipes = daoEquipe.listarTodas();
            if (!equipes.isEmpty()) {
                EquipeManutencaoDAO.EquipeManutencaoRecord equipeSalva = equipes.get(0);
                System.out.println("Equipe encontrada: " + equipeSalva);

                EquipeManutencaoDAO.EquipeManutencaoRecord equipeAtualizada = new EquipeManutencaoDAO.EquipeManutencaoRecord(
                        equipeSalva.idEquipe(),
                        "Equipe Motiva Atualizada",
                        "Equipe atualizada para manutenção crítica");
                daoEquipe.atualizar(equipeAtualizada);

                System.out.println("Equipe por ID: " + daoEquipe.buscarPorId(equipeSalva.idEquipe()));
            }

            // 3. Testar Trechos
            TrechoRodoviaDAO daoTrecho = new TrechoRodoviaDAO();
            TrechoRodoviaDAO.TrechoRodoviaRecord trechoSeco = new TrechoRodoviaDAO.TrechoRodoviaRecord(
                    null, "BR-116 KM 10 a 20", 10, 20, 22.5, "SECO", false, null);
            daoTrecho.inserir(trechoSeco);

            TrechoRodoviaDAO.TrechoRodoviaRecord trechoUmido = new TrechoRodoviaDAO.TrechoRodoviaRecord(
                    null, "BR-116 KM 21 a 35", 21, 35, 35.0, "UMIDO", true, null);
            daoTrecho.inserir(trechoUmido);

            List<TrechoRodoviaDAO.TrechoRodoviaRecord> trechosCadastrados = daoTrecho.listarTodas();
            TrechoRodoviaDAO.TrechoRodoviaRecord trechoPersistido = trechosCadastrados.get(0);
            System.out.println("Trecho cadastrado: " + trechoPersistido);

            TrechoRodoviaDAO.TrechoRodoviaRecord trechoAtualizado = new TrechoRodoviaDAO.TrechoRodoviaRecord(
                    trechoPersistido.idTrecho(),
                    trechoPersistido.nomeTrecho(),
                    trechoPersistido.kmInicial(),
                    trechoPersistido.kmFinal(),
                    48.5,
                    "UMIDO",
                    true,
                    null);
            daoTrecho.atualizar(trechoAtualizado);

            // 4. Testar Intervenções
            IntervencaoOperacionalDAO daoIntervencao = new IntervencaoOperacionalDAO();
            IntervencaoOperacionalDAO.IntervencaoOperacionalRecord intervencao = new IntervencaoOperacionalDAO.IntervencaoOperacionalRecord(
                    null, "ROCADA_MECANIZADA", "Intervenção para remoção de vegetação crítica");
            daoIntervencao.inserir(intervencao);
            List<IntervencaoOperacionalDAO.IntervencaoOperacionalRecord> intervencoes = daoIntervencao.listarTodas();
            System.out.println("Intervenções cadastradas: " + intervencoes.size());

            // 5. Gerar relatório com persistência
            GeradorRelatorio gerador = new GeradorRelatorio();
            TrechoRodovia[] trechos = {
                    new TrechoSeco(1, 10, 20),
                    new TrechoUmido(11, 20, 35),
                    new TrechoUmido(21, 30, 55)
            };
            gerador.gerarRelatorio(trechos);

            // 6. Consultar histórico de relatórios
            RelatorioPrioridadeDAO daoRelatorio = new RelatorioPrioridadeDAO();
            daoRelatorio.listarTodas().forEach(r -> System.out.println("Histórico: " + r));

        } catch (SQLException | IOException e) {
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("🔌 Conexão fechada no finally.");
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    private static void executarScriptSql(Connection conn, String nomeArquivo) throws IOException, SQLException {
        Path caminho = Paths.get(System.getProperty("user.dir"), nomeArquivo);

        if (!Files.exists(caminho)) {
            throw new IOException("Arquivo SQL não encontrado: " + caminho.toAbsolutePath());
        }

        String sqlCompleto = Files.readString(caminho);
        String[] instrucoes = sqlCompleto.split(";");

        for (String instrucao : instrucoes) {
            String comando = instrucao
                    .replaceAll("(?m)^\\s*PROMPT.*$", "")
                    .replaceAll("(?m)^\\s*--.*$", "")
                    .trim();

            if (comando.isEmpty()) {
                continue;
            }

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(comando);
            }
        }
    }
}
