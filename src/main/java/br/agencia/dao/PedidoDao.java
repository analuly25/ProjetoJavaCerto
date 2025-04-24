package br.agencia.dao;

// Importa a classe Pedido do modelo
import br.agencia.model.Pedido;
// Importa a fábrica de conexões com o banco de dados
import br.agencia.util.ConnectionFactory;

//Importações para manipular o banco de dados
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PedidoDao {

    // Método para cadastrar um pedido no banco de dados
    public void cadastrar(Pedido pedido) {
        // Comando SQL de inserção com placeholders para os valores
        String sql = "INSERT INTO pedido (idClientes, idPacoteViagem, data_pedido, valor_total) VALUES (?, ?, ?, ?)";

        // Bloco try-with-resources: garante que a conexão e o statement sejam fechados automaticamente
        try (
                // Obtém conexão com o banco
                Connection conn = ConnectionFactory.getConnection();
                // Prepara o comando SQL para execução
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os valores nos placeholders
            stmt.setInt(1, pedido.getIdCliente());
            stmt.setInt(2, pedido.getIdPacote());
            stmt.setDate(3, java.sql.Date.valueOf(pedido.getDataPedido()));
            stmt.setDouble(4, pedido.getValorTotal());  // Inserindo o valor total no banco

            // Executa o comando de inserção
            stmt.executeUpdate();
            System.out.println("Pedido cadastrado com sucesso!");

        }
            catch (SQLException e) {
            // Em caso de erro, mostra uma mensagem com a descrição do erro
            System.out.println("Erro ao cadastrar pedido: " + e.getMessage());
        }
    }
}

