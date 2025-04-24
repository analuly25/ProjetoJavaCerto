package br.agencia.dao;

// Importa a classe PacoteViagem do modelo
import br.agencia.model.PacoteViagem;
// Importa a fábrica de conexões com o banco de dados
import br.agencia.util.ConnectionFactory;

//Importações para manipular o banco de dados
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Define os valores nos placeholders
            stmt.setString(1, pacote.getNome());
            stmt.setString(2, pacote.getDestino());
            stmt.setString(3, pacote.getDescricao());
            stmt.setInt(4, pacote.getDuracaoDias());
            stmt.setDouble(5, pacote.getPreco());
            stmt.setString(6, pacote.getTipo());

            // Executa o comando de inserção
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pacote.setIdPacoteViagem(rs.getInt(1)); // <-- isso atualiza o ID no objeto
                }
            }

            System.out.println("Pacote cadastrado com sucesso!");
        } catch (SQLException e) {
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

    public List<PacoteViagem> listarTodos() {
        List<PacoteViagem> lista = new ArrayList<>();
        String sql = "SELECT * FROM PacoteViagem";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(construirPacote(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar pacotes: " + e.getMessage());
        }

        return lista;
    }

    public void excluir(int idPacoteViagem) {
        String sql = "DELETE FROM PacoteViagem WHERE idPacoteViagem = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPacoteViagem);
            stmt.executeUpdate();

            System.out.println("Pacote excluído com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao excluir pacote: " + e.getMessage());
        }
    }

    private PacoteViagem construirPacote(ResultSet rs) throws SQLException {
        PacoteViagem pacote = new PacoteViagem();
        pacote.setIdPacoteViagem(rs.getInt("idPacoteViagem"));
        pacote.setNome(rs.getString("nome"));
        pacote.setDestino(rs.getString("destino"));
        pacote.setDescricao(rs.getString("descricao"));
        pacote.setDuracaoDias(rs.getInt("duracao_dias"));
        pacote.setPreco(rs.getDouble("preco"));
        pacote.setTipo(rs.getString("tipo"));
        return pacote;
    }
}