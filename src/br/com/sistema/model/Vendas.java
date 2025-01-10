package br.com.sistema.model;

/**
 *
 * @author rustz
 */
public class Vendas {
    private int id;
    private clientes clientes;
    private String data_venda;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public clientes getClientes() {
        return clientes;
    }

    public void setClientes(clientes clientes) {
        this.clientes = clientes;
    }

    public String getData_venda() {
        return data_venda;
    }

    public void setData_venda(String data_venda) {
        this.data_venda = data_venda;
    }

    public double getTotal_venda() {
        return total_venda;
    }

    public void setTotal_venda(double total_venda) {
        this.total_venda = total_venda;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    private double total_venda;
    private String observacoes;
}
