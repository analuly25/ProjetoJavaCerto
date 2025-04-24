package br.agencia.util;

//Importações para manipular o banco de dados
import java.sql.Connection;
import java.sql.SQLException;

// Classe que testa se a conexão com o banco de dados está funcionando corretamente
public class testeconexao {
    public static void main(String[] args) {
        try {
            // Tenta estabelecer uma conexão com o banco usando a ConnectionFactory
            Connection conn = ConnectionFactory.getConnection();
            System.out.println(" Conexão com MySQL estabelecida com sucesso!");
            conn.close();
        }
            catch (SQLException e) {
            // Caso ocorra algum erro ao tentar conectar, exibe a mensagem de erro no console
            System.out.println(" Erro na conexão: " + e.getMessage());
        }
    }
}