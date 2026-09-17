package br.com.fiapmotiva.dao;

import br.com.fiapmotiva.db.ConexaoBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipeManutencaoDAO {

    public record EquipeManutencaoRecord(Long idEquipe, String nome, String descricao) {
    }

    public static final String SQL_INSERIR = "INSERT INTO equipes_manutencao (nome, descricao) VALUES (?, ?)";
    public static final String SQL_BUSCAR_POR_ID = "SELECT id_equipe, nome, descricao FROM equipes_manutencao WHERE id_equipe = ?";
    public static final String SQL_LISTAR_TODAS = "SELECT id_equipe, nome, descricao FROM equipes_manutencao ORDER BY id_equipe";
    public static final String SQL_ATUALIZAR = "UPDATE equipes_manutencao SET nome = ?, descricao = ? WHERE id_equipe = ?";
    public static final String SQL_DELETAR = "DELETE FROM equipes_manutencao WHERE id_equipe = ?";

    public EquipeManutencaoDAO() {
    }

    public void inserir(EquipeManutencaoRecord equipe) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_INSERIR)) {
            stmt.setString(1, equipe.nome());
            stmt.setString(2, equipe.descricao());
            stmt.executeUpdate();
            System.out.println("Equipe inserida com sucesso.");
        }
    }

    public EquipeManutencaoRecord buscarPorId(Long idEquipe) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_BUSCAR_POR_ID)) {
            stmt.setLong(1, idEquipe);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new EquipeManutencaoRecord(
                            rs.getLong("id_equipe"),
                            rs.getString("nome"),
                            rs.getString("descricao"));
                }
            }
        }
        return null;
    }

    public List<EquipeManutencaoRecord> listarTodas() throws SQLException {
        List<EquipeManutencaoRecord> equipes = new ArrayList<>();
        Connection con = ConexaoBanco.getConexao();

        try (PreparedStatement stmt = con.prepareStatement(SQL_LISTAR_TODAS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                equipes.add(new EquipeManutencaoRecord(
                        rs.getLong("id_equipe"),
                        rs.getString("nome"),
                        rs.getString("descricao")));
            }
        }

        return equipes;
    }

    public void atualizar(EquipeManutencaoRecord equipe) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_ATUALIZAR)) {
            stmt.setString(1, equipe.nome());
            stmt.setString(2, equipe.descricao());
            stmt.setLong(3, equipe.idEquipe());
            stmt.executeUpdate();
        }
    }

    public void deletar(Long idEquipe) throws SQLException {
        Connection con = ConexaoBanco.getConexao();
        try (PreparedStatement stmt = con.prepareStatement(SQL_DELETAR)) {
            stmt.setLong(1, idEquipe);
            stmt.executeUpdate();
        }
    }
}
