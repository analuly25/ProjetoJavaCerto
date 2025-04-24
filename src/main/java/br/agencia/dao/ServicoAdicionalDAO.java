package br.agencia.dao;

// Importa a classe ServicoAdicional do modelo
import br.agencia.model.ServicoAdicional;
// Importa a fábrica de conexões com o banco de dados
import br.agencia.util.ConnectionFactory;

//Importações para manipular o banco de dados
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServicoAdicionalDAO {

    // Método para inserir um serviço adicional na tabela ServicoAdicional
    public void inserir(ServicoAdicional servico) {
        // Comando SQL de inserção com placeholders para os valores
        String sql = "INSERT INTO ServicoAdicional (nome, descricao, preco) VALUES (?, ?, ?)";

        // Bloco try-with-resources: garante o fechamento automático da conexão e do statement
        try (
                // Obtém conexão com o banco
                Connection conn = ConnectionFactory.getConnection();
                // Prepara o comando SQL para execução
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os valores nos placeholders
            stmt.setString(1, servico.getNome());
            stmt.setString(2, servico.getDescricao());
            stmt.setDouble(3, servico.getPreco());

            // Executa o comando de inserção
            stmt.executeUpdate();
            System.out.println("Serviço adicional inserido com sucesso!");
        }
            catch (SQLException e) {
            // Em caso de erro, mostra uma mensagem com a descrição do erro
            System.out.println("Erro ao inserir serviço adicional: " + e.getMessage());
        }
    }
}