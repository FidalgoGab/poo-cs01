package br.com.fiapmotiva.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    private static ConexaoBD instancia;
    private Connection conexao;

    private static final String HOST = getEnv("DB_HOST", "oracle.fiap.com.br");
    private static final String PORT = getEnv("DB_PORT", "1521");
    private static final String SID = getEnv("DB_SID", "ORCL");
    private static final String USER = getEnv("DB_USER", "");
    private static final String PASSWORD = getEnv("DB_PASSWORD", "");

    private static String getEnv(String key, String defaultValue) {
        String valor = System.getenv(key);
        if (valor != null && !valor.trim().isEmpty()) {
            return removerAspas(valor.trim());
        }

        Path arquivoEnv = Paths.get(System.getProperty("user.dir"), ".env");
        if (Files.exists(arquivoEnv)) {
            try {
                for (String linha : Files.readAllLines(arquivoEnv, StandardCharsets.UTF_8)) {
                    String linhaTrim = linha.trim();
                    if (linhaTrim.isEmpty() || linhaTrim.startsWith("#") || !linhaTrim.contains("=")) {
                        continue;
                    }

                    int indice = linhaTrim.indexOf('=');
                    String chave = linhaTrim.substring(0, indice).trim();
                    String valorLinha = linhaTrim.substring(indice + 1).trim();

                    if (key.equals(chave)) {
                        return removerAspas(valorLinha);
                    }
                }
            } catch (IOException ignored) {
                // ignora e usa o valor padrão
            }
        }

        return defaultValue;
    }

    private static String removerAspas(String valor) {
        if (valor.length() >= 2 && valor.startsWith("\"") && valor.endsWith("\"")) {
            return valor.substring(1, valor.length() - 1);
        }
        return valor;
    }

    private ConexaoBD() {
    }

    public static ConexaoBD getInstancia() {
        if (instancia == null) {
            synchronized (ConexaoBD.class) {
                if (instancia == null) {
                    instancia = new ConexaoBD();
                }
            }
        }
        return instancia;
    }

    public Connection conectar() {
        try {
            if (this.conexao != null && !this.conexao.isClosed()) {
                return this.conexao;
            }

            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@" + HOST + ":" + PORT + ":" + SID;
            this.conexao = DriverManager.getConnection(url, USER, PASSWORD);
            System.out.println("✅ Conexão criada com sucesso!");
            return this.conexao;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver Oracle não encontrado: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao Oracle: " + e.getMessage(), e);
        }
    }

    public void desconectar() {
        try {
            if (this.conexao != null && !this.conexao.isClosed()) {
                this.conexao.close();
                System.out.println("🔌 Conexão fechada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (this.conexao == null || this.conexao.isClosed()) {
                return conectar();
            }
            return this.conexao;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar conexão: " + e.getMessage(), e);
        }
    }

    public static Connection getConexao() {
        return getInstancia().getConnection();
    }
}
