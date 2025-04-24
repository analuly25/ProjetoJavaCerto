// Pacote principal da interface gráfica da agência de viagens
package br.agencia.view;

// Importações dos pacotes DAO (acesso a dados) e model (entidades)
import br.agencia.dao.*;
import br.agencia.model.*;

// Importações da biblioteca Swing e utilitários
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Classe principal da interface gráfica, herdando de JFrame
public class MainGUI extends JFrame {
    // Objetos DAO para manipulação de dados
    ClienteDao clienteDao = new ClienteDao();
    PacoteViagemDao pacoteDao = new PacoteViagemDao();
    PedidoDao pedidoDao = new PedidoDao();

    public MainGUI() {
        // Configurações da janela principal
        setTitle("Sistema de Agência de Viagens");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Criação de abas para cliente, pacote e pedido
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Cliente", criarPainelCliente());
        tabbedPane.add("Pacote", criarPainelPacote());
        tabbedPane.add("Pedido", criarPainelPedido());

        // Adiciona o painel de abas à janela principal
        add(tabbedPane);
        setVisible(true);
    }

    private JPanel criarPainelCliente() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 2));
        JTextField nomeField = new JTextField();
        JTextField telefoneField = new JTextField();
        JTextField emailField = new JTextField();
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"NACIONAL", "ESTRANGEIRO"});
        JTextField docField = new JTextField();

        form.add(new JLabel("Nome:")); form.add(nomeField);
        form.add(new JLabel("Telefone:")); form.add(telefoneField);
        form.add(new JLabel("Email:")); form.add(emailField);
        form.add(new JLabel("Tipo:")); form.add(tipoCombo);
        form.add(new JLabel("CPF/Passaporte:")); form.add(docField);

        JButton salvarBtn = new JButton("Salvar Cliente");
        salvarBtn.addActionListener(e -> {
            String nome = nomeField.getText();
            String telefone = telefoneField.getText();
            String email = emailField.getText();
            String tipo = (String) tipoCombo.getSelectedItem();
            String doc = docField.getText();

            Cliente cliente;
            if ("NACIONAL".equals(tipo)) {
                cliente = new ClienteNacional(nome, telefone, email, doc);
            } else {
                cliente = new ClienteEstrangeiro(nome, telefone, email, doc);
            }

            clienteDao.inserir(cliente);
            JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
            atualizarTabelaClientes();
        });

        JButton buscarBtn = new JButton("Buscar por ID");
        buscarBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Digite o ID do cliente:");
            try {
                int id = Integer.parseInt(input);
                Cliente cliente = clienteDao.buscarPorId(id);
                if (cliente != null) {
                    JOptionPane.showMessageDialog(this, cliente.toString());
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        });

        JButton excluirBtn = new JButton("Excluir Selecionado");
        excluirBtn.addActionListener(e -> {
            int linhaSelecionada = tabelaClientes.getSelectedRow();
            if (linhaSelecionada != -1) {
                int id = (int) tabelaClientes.getValueAt(linhaSelecionada, 0);
                clienteDao.excluir(id);
                atualizarTabelaClientes();
                JOptionPane.showMessageDialog(this, "Cliente excluído.");
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela.");
            }
        });

        JPanel topoPanel = new JPanel(new BorderLayout());
        topoPanel.add(form, BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel();
        botoesPanel.add(salvarBtn);
        botoesPanel.add(buscarBtn);
        botoesPanel.add(excluirBtn);
        topoPanel.add(botoesPanel, BorderLayout.SOUTH);

        JTable tabela = new JTable(new DefaultTableModel(new Object[]{"ID", "Nome", "Tipo"}, 0));
        JScrollPane scrollPane = new JScrollPane(tabela);

        panel.add(topoPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        tabelaClientes = tabela;
        atualizarTabelaClientes();
        return panel;
    }

    private void atualizarTabelaClientes() {
        DefaultTableModel model = (DefaultTableModel) tabelaClientes.getModel();
        model.setRowCount(0);

        List<Cliente> clientes = clienteDao.listarTodos();
        for (Cliente c : clientes) {
            String tipo = (c instanceof ClienteNacional) ? "NACIONAL" : "ESTRANGEIRO";
            model.addRow(new Object[]{c.getIdCliente(), c.getNome(), tipo});
        }
    }


    // Painel da aba Pacote de Viagem
    private JTable tabelaPacotes; // nova referência para tabela de pacotes

    private JPanel criarPainelPacote() {
        JPanel formPanel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2));
        JTextField nomeField = new JTextField();
        JTextField destinoField = new JTextField();
        JTextField descField = new JTextField();
        JTextField duracaoField = new JTextField();
        JTextField precoField = new JTextField();
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"NACIONAL", "ESTRANGEIRO"});

        form.add(new JLabel("Nome:")); form.add(nomeField);
        form.add(new JLabel("Destino:")); form.add(destinoField);
        form.add(new JLabel("Descrição:")); form.add(descField);
        form.add(new JLabel("Duração:")); form.add(duracaoField);
        form.add(new JLabel("Preço:")); form.add(precoField);
        form.add(new JLabel("Tipo:")); form.add(tipoCombo);

        JButton salvarBtn = new JButton("Salvar Pacote");
        JButton buscarBtn = new JButton("Buscar por ID");


        JPanel botoesPanel = new JPanel();
        botoesPanel.add(salvarBtn);
        botoesPanel.add(buscarBtn);



        salvarBtn.addActionListener(e -> {
            try {
                String nome = nomeField.getText();
                String destino = destinoField.getText();
                String desc = descField.getText();
                int duracao = Integer.parseInt(duracaoField.getText());
                double preco = Double.parseDouble(precoField.getText());
                String tipo = (String) tipoCombo.getSelectedItem();

                PacoteViagem pacote = new PacoteViagem(nome, destino, desc, duracao, preco, tipo);
                pacoteDao.inserir(pacote);
                JOptionPane.showMessageDialog(this, "Pacote salvo com sucesso!");
                atualizarTabelaPacotes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar pacote: " + ex.getMessage());
            }
        });

        buscarBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Digite o ID do pacote:");
            try {
                int id = Integer.parseInt(input);
                PacoteViagem pacote = pacoteDao.buscarPorId(id);
                if (pacote != null) {
                    JOptionPane.showMessageDialog(this, "ID: " + pacote.getIdPacoteViagem() + "\nNome: " + pacote.getNome() + "\nPreço: R$ " + pacote.getPreco());
                } else {
                    JOptionPane.showMessageDialog(this, "Pacote não encontrado.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        });

        JButton excluirBtn= new JButton("Excluir Selecionado");
        excluirBtn.addActionListener(e -> {
            int linhaSelecionada = tabelaPacotes.getSelectedRow();
            if (linhaSelecionada != -1) {
                int id = (int) tabelaPacotes.getValueAt(linhaSelecionada, 0);
                pacoteDao.excluir(id);
                atualizarTabelaPacotes();
                JOptionPane.showMessageDialog(this, "Pacote excluído.");
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um pacote na tabela.");
            }
        });

        botoesPanel.add(excluirBtn);


        JPanel topoPanel = new JPanel(new BorderLayout());
        topoPanel.add(form, BorderLayout.CENTER);
        topoPanel.add(botoesPanel, BorderLayout.SOUTH);

        tabelaPacotes = new JTable(new DefaultTableModel(new Object[]{"ID", "Nome", "Destino", "Preço"}, 0));
        JScrollPane scrollPane = new JScrollPane(tabelaPacotes);


        formPanel.add(topoPanel, BorderLayout.NORTH);
        formPanel.add(scrollPane, BorderLayout.CENTER);

        atualizarTabelaPacotes();

        return formPanel;
    }
    private void atualizarTabelaPacotes() {
        DefaultTableModel model = (DefaultTableModel) tabelaPacotes.getModel();
        model.setRowCount(0);
        for (PacoteViagem p : pacoteDao.listarTodos()) {
            model.addRow(new Object[]{
                    p.getIdPacoteViagem(), p.getNome(), p.getDestino(), p.getPreco()
            });
        }
    }


    // Painel da aba Pedido
    private JPanel criarPainelPedido() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(0, 2));

        JTextField clienteIdField = new JTextField();
        JTextField pacoteIdField = new JTextField();

        Dimension fieldSize = new Dimension(10, 2); // você pode ajustar os valores

        clienteIdField.setPreferredSize(fieldSize);
        pacoteIdField.setPreferredSize(fieldSize);

        DefaultListModel<ServicoAdicional> servicoListModel = new DefaultListModel<>();
        JList<ServicoAdicional> servicoList = new JList<>(servicoListModel);

        JButton addServicoBtn = new JButton("Adicionar Serviço Adicional");

        addServicoBtn.addActionListener(e -> {
            JTextField nomeField = new JTextField();
            JTextField descField = new JTextField();
            JTextField precoField = new JTextField();

            JPanel inputPanel = new JPanel(new GridLayout(0, 1));
            inputPanel.add(new JLabel("Nome do Serviço:"));
            inputPanel.add(nomeField);
            inputPanel.add(new JLabel("Descrição:"));
            inputPanel.add(descField);
            inputPanel.add(new JLabel("Preço:"));
            inputPanel.add(precoField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel,
                    "Adicionar Serviço Adicional", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String nome = nomeField.getText();
                    String descricao = descField.getText();
                    double preco = Double.parseDouble(precoField.getText());
                    ServicoAdicional servico = new ServicoAdicional(nome, descricao, preco);
                    servicoListModel.addElement(servico);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Preço inválido.");
                }
            }
        });

        JButton salvarBtn = new JButton("Salvar Pedido");
        salvarBtn.addActionListener(e -> {
            try {
                int idCliente = Integer.parseInt(clienteIdField.getText());
                int idPacote = Integer.parseInt(pacoteIdField.getText());

                JOptionPane.showMessageDialog(this, "Pedido salvo com sucesso!");
                atualizarTabelaPedidos();

                PacoteViagem pacote = pacoteDao.buscarPorId(idPacote);
                if (pacote == null) {
                    JOptionPane.showMessageDialog(this, "Pacote não encontrado.");
                    return;
                }

                double valorTotal = pacote.getPreco();

                ServicoAdicionalDAO servicoDao = new ServicoAdicionalDAO();
                List<ServicoAdicional> servicosAdicionados = new ArrayList<>();
                for (int i = 0; i < servicoListModel.size(); i++) {
                    ServicoAdicional servico = servicoListModel.getElementAt(i);
                    servicoDao.inserir(servico); // salva no banco
                    servicosAdicionados.add(servico);
                    valorTotal += servico.getPreco();
                }

                Pedido pedido = new Pedido(idCliente, idPacote, LocalDate.now(), valorTotal);
                JOptionPane.showMessageDialog(this, "Pedido salvo com sucesso! Valor total: R$ " + valorTotal);

                servicoListModel.clear();
                atualizarTabelaPedidos();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "IDs inválidos.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar pedido: " + ex.getMessage());
            }
        });

        JButton excluirBtn = new JButton("Excluir Pedido Selecionado");
        excluirBtn.addActionListener(e -> {
            int linhaSelecionada = tabelaPedidos.getSelectedRow();
            if (linhaSelecionada != -1) {
                int idPedido = (int) tabelaPedidos.getValueAt(linhaSelecionada, 0);
                try {
                    pedidoDao.excluir(idPedido);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(this, "Pedido excluído.");
                atualizarTabelaPedidos();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um pedido na tabela.");
            }
        });

        formPanel.add(new JLabel("ID Cliente:")); formPanel.add(clienteIdField);
        formPanel.add(new JLabel("ID Pacote:")); formPanel.add(pacoteIdField);
        formPanel.add(addServicoBtn); formPanel.add(new JScrollPane(servicoList));

        JPanel botoesPanel = new JPanel();
        botoesPanel.add(salvarBtn);
        botoesPanel.add(excluirBtn);

        tabelaPedidos = new JTable(new DefaultTableModel(new Object[]{"ID", "Cliente", "Pacote", "Data", "Valor Total"}, 0));
        JScrollPane scrollPanel = new JScrollPane(tabelaPedidos);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPanel, BorderLayout.CENTER);
        panel.add(botoesPanel, BorderLayout.SOUTH);

        atualizarTabelaPedidos();
        return panel;
    }

    private void atualizarTabelaPedidos() {
        DefaultTableModel model = (DefaultTableModel) tabelaPedidos.getModel();
        model.setRowCount(0);
        for (Pedido p : pedidoDao.listarTodos()) {
            model.addRow(new Object[]{
                    p.getIdPedido(),
                    p.getIdCliente(),
                    p.getIdPacote(),
                    p.getDataPedido(),
                    p.getValorTotal()
            });
        }
    }


    // Referência para a tabela de clientes
    private JTable tabelaClientes;
    private JTable tabelaPedidos;



    // Método main para iniciar a aplicação
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}
