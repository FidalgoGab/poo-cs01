package br.com.fiapmotiva.dao;

import br.com.fiapmotiva.db.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TrechoRodoviaDAO {

    public record TrechoRodoviaRecord(Long idTrecho, String nomeTrecho, int kmInicial, int kmFinal,
            double nivelVegetacaoCm, String tipoTrecho, boolean monitoravelIot,
            Long idEquipe) {
    }

    public static final String SQL_INSERIR = "INSERT INTO trechos_rodovia (nome_trecho, km_inicial, km_final, nivel_vegetacao_cm, tipo_trecho, monitoravel_iot, id_equipe) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_BUSCAR_POR_ID = "SELECT id_trecho, nome_trecho, km_inicial, km_final, nivel_vegetacao_cm, tipo_trecho, monitoravel_iot, id_equipe FROM trechos_rodovia WHERE id_trecho = ?";
    public static final String SQL_LISTAR_TODAS = "SELECT id_trecho, nome_trecho, km_inicial, km_final, nivel_vegetacao_cm, tipo_trecho, monitoravel_iot, id_equipe FROM trechos_rodovia ORDER BY id_trecho";
    public static final String SQL_ATUALIZAR = "UPDATE trechos_rodovia SET nome_trecho = ?, km_inicial = ?, km_final = ?, nivel_vegetacao_cm = ?, tipo_trecho = ?, monitoravel_iot = ?, id_equipe = ? WHERE id_trecho = ?";
    public static final String SQL_DELETAR = "DELETE FROM trechos_rodovia WHERE id_trecho = ?";

    public TrechoRodoviaDAO() {
    }

    public void inserir(TrechoRodoviaRecord trecho) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_INSERIR, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, trecho.nomeTrecho());
            stmt.setInt(2, trecho.kmInicial());
            stmt.setInt(3, trecho.kmFinal());
            stmt.setDouble(4, trecho.nivelVegetacaoCm());
            stmt.setString(5, trecho.tipoTrecho());
            stmt.setInt(6, trecho.monitoravelIot() ? 1 : 0);
            if (trecho.idEquipe() != null) {
                stmt.setLong(7, trecho.idEquipe());
            } else {
                stmt.setNull(7, java.sql.Types.BIGINT);
            }
            stmt.executeUpdate();
        }
    }

    public TrechoRodoviaRecord buscarPorId(Long idTrecho) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_BUSCAR_POR_ID)) {
            stmt.setLong(1, idTrecho);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new TrechoRodoviaRecord(
                            rs.getLong("id_trecho"),
                            rs.getString("nome_trecho"),
                            rs.getInt("km_inicial"),
                            rs.getInt("km_final"),
                            rs.getDouble("nivel_vegetacao_cm"),
                            rs.getString("tipo_trecho"),
                            rs.getInt("monitoravel_iot") == 1,
                            rs.getObject("id_equipe") != null ? rs.getLong("id_equipe") : null);
                }
            }
        }
        return null;
    }

    public List<TrechoRodoviaRecord> listarTodas() throws SQLException {
        List<TrechoRodoviaRecord> trechos = new ArrayList<>();
        Connection con = ConexaoBanco.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(SQL_LISTAR_TODAS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                trechos.add(new TrechoRodoviaRecord(
                        rs.getLong("id_trecho"),
                        rs.getString("nome_trecho"),
                        rs.getInt("km_inicial"),
                        rs.getInt("km_final"),
                        rs.getDouble("nivel_vegetacao_cm"),
                        rs.getString("tipo_trecho"),
                        rs.getInt("monitoravel_iot") == 1,
                        rs.getObject("id_equipe") != null ? rs.getLong("id_equipe") : null));
            }
        }

        return trechos;
    }

    public void atualizar(TrechoRodoviaRecord trecho) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_ATUALIZAR)) {
            stmt.setString(1, trecho.nomeTrecho());
            stmt.setInt(2, trecho.kmInicial());
            stmt.setInt(3, trecho.kmFinal());
            stmt.setDouble(4, trecho.nivelVegetacaoCm());
            stmt.setString(5, trecho.tipoTrecho());
            stmt.setInt(6, trecho.monitoravelIot() ? 1 : 0);
            if (trecho.idEquipe() != null) {
                stmt.setLong(7, trecho.idEquipe());
            } else {
                stmt.setNull(7, java.sql.Types.BIGINT);
            }
            stmt.setLong(8, trecho.idTrecho());
            stmt.executeUpdate();
        }
    }

    public void deletar(Long idTrecho) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_DELETAR)) {
            stmt.setLong(1, idTrecho);
            stmt.executeUpdate();
        }
    }
}
