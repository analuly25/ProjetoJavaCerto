package br.agencia.dao;

// Importa a classe PacoteViagem do modelo
import br.agencia.model.PacoteViagem;
// Importa a fábrica de conexões com o banco de dados
import br.agencia.util.ConnectionFactory;

//Importações para manipular o banco de dados
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


public class PacoteViagemDao {

    // Método para inserir um novo pacote de viagem no banco
    public void inserir(PacoteViagem pacote) {
        // Comando SQL de inserção com placeholders para os valores
        String sql = "INSERT INTO PacoteViagem (nome, destino, descricao, duracao_dias, preco, tipo) VALUES (?, ?, ?, ?, ?, ?)";

        // Bloco try-with-resources: garante o fechamento automático da conexão e do statement
        try (
                // Obtém conexão com o banco
                Connection conn = ConnectionFactory.getConnection();
                // Prepara o comando SQL para execução
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os valores nos placeholders
            stmt.setString(1, pacote.getNome());
            stmt.setString(2, pacote.getDestino());
            stmt.setString(3, pacote.getDescricao());
            stmt.setInt(4, pacote.getDuracaoDias());
            stmt.setDouble(5, pacote.getPreco());
            stmt.setString(6, pacote.getTipo());

            // Executa o comando de inserção
            stmt.executeUpdate();
            System.out.println("Pacote cadastrado com sucesso!");
        }
            catch (SQLException e) {
            // Em caso de erro, mostra uma mensagem com a descrição do erro
            System.out.println("Erro ao inserir pacote: " + e.getMessage());
        }
    }

    // Método para buscar um pacote pelo seu ID
    public PacoteViagem buscarPorId(int id) {
        PacoteViagem pacote = null;
        String sql = "SELECT * FROM PacoteViagem WHERE idPacoteViagem = ?";

        // Bloco try-with-resources para garantir o fechamento das conexões
        try (
                Connection conn = ConnectionFactory.getConnection(); // Obtém conexão com o banco
                PreparedStatement stmt = conn.prepareStatement(sql) // Define o ID como parâmetro no SQL
        ) {
            stmt.setInt(1, id); // Define o ID como parâmetro no SQL
            ResultSet rs = stmt.executeQuery();// Executa a consulta

            // Se encontrar algum registro
            if (rs.next()) {
                pacote = new PacoteViagem();
                pacote.setIdPacoteViagem(rs.getInt("idPacoteViagem"));// Define ID
                pacote.setNome(rs.getString("nome"));// Define nome
                pacote.setPreco(rs.getDouble("preco"));// Define preço
            }
        }
        // Em caso de erro na busca
        catch (Exception e) {
            System.out.println("Erro ao buscar pacote: " + e.getMessage());
        }
        return pacote;// Retorna o pacote encontrado ou null se não achou
    }
}