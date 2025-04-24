package br.agencia.model;

import java.time.LocalDate;

public class Pedido {
    private int idPedido;
    private int idCliente;
    private int idPacote;
    private LocalDate dataPedido;
    private double valorTotal;

    public Pedido(int idCliente, int idPacote, LocalDate dataPedido, double valorTotal) {
        this.idCliente = idCliente;
        this.idPacote = idPacote;
        this.dataPedido = dataPedido;
        this.valorTotal = valorTotal;
    }

    // Getters e setters
    public int getIdCliente() { return idCliente; }
    public int getIdPacote() { return idPacote; }
    public LocalDate getDataPedido() { return dataPedido; }
    public double getValorTotal() { return valorTotal; }

    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
}



