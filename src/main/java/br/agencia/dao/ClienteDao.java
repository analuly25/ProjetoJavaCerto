package br.agencia.dao;

import br.agencia.model.*;
import br.agencia.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {

    public void inserir(Cliente cliente) {
        String sql = "INSERT INTO Clientes (nome, cpf, passaporte, telefone, email, nacionalidade) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

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

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setIdCliente(rs.getInt(1));
                }
            }

            System.out.println("Cliente cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir cliente: " + e.getMessage());
        }
    }

    public Cliente buscarPorId(int idCliente) {
        String sql = "SELECT * FROM Clientes WHERE idClientes = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construirCliente(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar cliente: " + e.getMessage());
        }
        return null;
    }

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Clientes";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(construirCliente(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }

        return lista;
    }

    public void excluir(int idCliente) {
        String sql = "DELETE FROM Clientes WHERE idClientes = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            stmt.executeUpdate();

            System.out.println("Cliente excluído com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao excluir cliente: " + e.getMessage());
        }
    }

    private Cliente construirCliente(ResultSet rs) throws SQLException {
        int idCliente = rs.getInt("idClientes");
        String nome = rs.getString("nome");
        String telefone = rs.getString("telefone");
        String email = rs.getString("email");
        String cpf = rs.getString("cpf");
        String passaporte = rs.getString("passaporte");

        Cliente cliente;

        if (cpf != null) {
            cliente = new ClienteNacional(nome, telefone, email, cpf);
        } else {
            cliente = new ClienteEstrangeiro(nome, telefone, email, passaporte);
        }

        cliente.setIdCliente(idCliente);
        return cliente;
    }
}

