package br.com.fiapmotiva.dao;

import br.com.fiapmotiva.db.ConexaoBanco;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RelatorioPrioridadeDAO {

    public record RelatorioPrioridadeRecord(Long idRelatorio, int qtUrgente, int qtCritico,
            int qtAtencao, int qtNormal, String resumo, Date dataGeracao) {
    }

    public static final String SQL_INSERIR = "INSERT INTO relatorio_prioridade (qt_urgente, qt_critico, qt_atencao, qt_normal, resumo, data_geracao) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String SQL_BUSCAR_POR_ID = "SELECT id_relatorio, qt_urgente, qt_critico, qt_atencao, qt_normal, resumo, data_geracao FROM relatorio_prioridade WHERE id_relatorio = ?";
    public static final String SQL_LISTAR_TODAS = "SELECT id_relatorio, qt_urgente, qt_critico, qt_atencao, qt_normal, resumo, data_geracao FROM relatorio_prioridade ORDER BY id_relatorio";
    public static final String SQL_ATUALIZAR = "UPDATE relatorio_prioridade SET qt_urgente = ?, qt_critico = ?, qt_atencao = ?, qt_normal = ?, resumo = ?, data_geracao = ? WHERE id_relatorio = ?";
    public static final String SQL_DELETAR = "DELETE FROM relatorio_prioridade WHERE id_relatorio = ?";

    public RelatorioPrioridadeDAO() {
    }

    public void inserir(RelatorioPrioridadeRecord relatorio) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_INSERIR, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, relatorio.qtUrgente());
            stmt.setInt(2, relatorio.qtCritico());
            stmt.setInt(3, relatorio.qtAtencao());
            stmt.setInt(4, relatorio.qtNormal());
            stmt.setString(5, relatorio.resumo());
            stmt.setDate(6, relatorio.dataGeracao());
            stmt.executeUpdate();
        }
    }

    public void salvarRelatorio(int qtUrgente, int qtCritico, int qtAtencao, int qtNormal, String resumo)
            throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_INSERIR)) {
            stmt.setInt(1, qtUrgente);
            stmt.setInt(2, qtCritico);
            stmt.setInt(3, qtAtencao);
            stmt.setInt(4, qtNormal);
            stmt.setString(5, resumo);
            stmt.setDate(6, new Date(System.currentTimeMillis()));
            stmt.executeUpdate();
        }
    }

    public RelatorioPrioridadeRecord buscarPorId(Long idRelatorio) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_BUSCAR_POR_ID)) {
            stmt.setLong(1, idRelatorio);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new RelatorioPrioridadeRecord(
                            rs.getLong("id_relatorio"),
                            rs.getInt("qt_urgente"),
                            rs.getInt("qt_critico"),
                            rs.getInt("qt_atencao"),
                            rs.getInt("qt_normal"),
                            rs.getString("resumo"),
                            rs.getDate("data_geracao"));
                }
            }
        }
        return null;
    }

    public List<RelatorioPrioridadeRecord> listarTodas() throws SQLException {
        List<RelatorioPrioridadeRecord> relatorios = new ArrayList<>();
        Connection con = ConexaoBanco.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(SQL_LISTAR_TODAS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                relatorios.add(new RelatorioPrioridadeRecord(
                        rs.getLong("id_relatorio"),
                        rs.getInt("qt_urgente"),
                        rs.getInt("qt_critico"),
                        rs.getInt("qt_atencao"),
                        rs.getInt("qt_normal"),
                        rs.getString("resumo"),
                        rs.getDate("data_geracao")));
            }
        }

        return relatorios;
    }

    public void atualizar(RelatorioPrioridadeRecord relatorio) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_ATUALIZAR)) {
            stmt.setInt(1, relatorio.qtUrgente());
            stmt.setInt(2, relatorio.qtCritico());
            stmt.setInt(3, relatorio.qtAtencao());
            stmt.setInt(4, relatorio.qtNormal());
            stmt.setString(5, relatorio.resumo());
            stmt.setDate(6, relatorio.dataGeracao());
            stmt.setLong(7, relatorio.idRelatorio());
            stmt.executeUpdate();
        }
    }

    public void deletar(Long idRelatorio) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_DELETAR)) {
            stmt.setLong(1, idRelatorio);
            stmt.executeUpdate();
        }
    }
}
