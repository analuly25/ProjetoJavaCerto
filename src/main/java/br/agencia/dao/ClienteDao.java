package br.agencia.dao;

// Importa as classes do modelo
import br.agencia.model.*;
// Importa a fábrica de conexões com o banco de dados
import br.agencia.util.ConnectionFactory;

//Importações para manipular o banco de dados
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class ClienteDao {

    // Método para inserir um cliente no banco de dados
    public void inserir(Cliente cliente) {
        // Comando SQL de inserção com placeholders para os valores
        String sql = "INSERT INTO Clientes (nome, cpf, passaporte, telefone, email, nacionalidade) VALUES (?, ?, ?, ?, ?, ?)";

        // Bloco try-with-resources: garante que a conexão e o statement sejam fechados automaticamente
        try (
                // Obtém conexão com o banco
                Connection conn = ConnectionFactory.getConnection();
                // Prepara o comando SQL e solicita o retorno das chaves geradas (como o ID)
                PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            // Define os parâmetros da query
            stmt.setString(1, cliente.getNome());

            if (cliente instanceof ClienteNacional) {
                stmt.setString(2, cliente.getDocumento()); // CPF
                stmt.setString(3, null);                   // Passaporte
            } else {
                stmt.setString(2, null);                   // CPF
                stmt.setString(3, cliente.getDocumento()); // Passaporte
            }

            stmt.setString(4, cliente.getTelefone());
            stmt.setString(5, cliente.getEmail());
            stmt.setString(6, cliente.getNacionalidade());

            // Executa o comando de inserção
            stmt.executeUpdate();

            // Recupera o ID gerado automaticamente pelo banco
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setIdCliente(rs.getInt(1)); // Define o ID no objeto cliente
                }
            }

            System.out.println("Cliente cadastrado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir cliente: " + e.getMessage());
        }
    }
}

