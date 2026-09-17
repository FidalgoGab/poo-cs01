package br.com.fiapmotiva.db;

import java.sql.Connection;
import java.sql.SQLException;

public class ConexaoBanco {

    public static Connection getConexao() {
        return ConexaoBD.getInstancia().getConnection();
    }

    public static void fechar(Connection conexao) {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("🔌 Conexão fechada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
}