package br.com.fiapmotiva.dao;

import br.com.fiapmotiva.db.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IntervencaoOperacionalDAO {

    public record IntervencaoOperacionalRecord(Long idIntervencao, String nome, String descricao) {
    }

    public static final String SQL_INSERIR = "INSERT INTO intervencao_operacional (nome, descricao) " +
            "SELECT ?, ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM intervencao_operacional WHERE nome = ?)";
    public static final String SQL_BUSCAR_POR_ID = "SELECT id_intervencao, nome, descricao FROM intervencao_operacional WHERE id_intervencao = ?";
    public static final String SQL_LISTAR_TODAS = "SELECT id_intervencao, nome, descricao FROM intervencao_operacional ORDER BY id_intervencao";
    public static final String SQL_ATUALIZAR = "UPDATE intervencao_operacional SET nome = ?, descricao = ? WHERE id_intervencao = ?";
    public static final String SQL_DELETAR = "DELETE FROM intervencao_operacional WHERE id_intervencao = ?";

    public IntervencaoOperacionalDAO() {
    }

    public void inserir(IntervencaoOperacionalRecord intervencao) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_INSERIR)) {
            stmt.setString(1, intervencao.nome());
            stmt.setString(2, intervencao.descricao());
            stmt.setString(3, intervencao.nome());
            int linhas = stmt.executeUpdate();
            if (linhas > 0) {
                System.out.println("Intervenção inserida com sucesso.");
            } else {
                System.out.println("Intervenção já existente: " + intervencao.nome());
            }
        }
    }

    public IntervencaoOperacionalRecord buscarPorId(Long idIntervencao) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_BUSCAR_POR_ID)) {
            stmt.setLong(1, idIntervencao);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new IntervencaoOperacionalRecord(
                            rs.getLong("id_intervencao"),
                            rs.getString("nome"),
                            rs.getString("descricao"));
                }
            }
        }
        return null;
    }

    public List<IntervencaoOperacionalRecord> listarTodas() throws SQLException {
        List<IntervencaoOperacionalRecord> intervencoes = new ArrayList<>();
        Connection con = ConexaoBanco.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(SQL_LISTAR_TODAS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                intervencoes.add(new IntervencaoOperacionalRecord(
                        rs.getLong("id_intervencao"),
                        rs.getString("nome"),
                        rs.getString("descricao")));
            }
        }

        return intervencoes;
    }

    public void atualizar(IntervencaoOperacionalRecord intervencao) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_ATUALIZAR)) {
            stmt.setString(1, intervencao.nome());
            stmt.setString(2, intervencao.descricao());
            stmt.setLong(3, intervencao.idIntervencao());
            stmt.executeUpdate();
        }
    }

    public void deletar(Long idIntervencao) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_DELETAR)) {
            stmt.setLong(1, idIntervencao);
            stmt.executeUpdate();
        }
    }
}
