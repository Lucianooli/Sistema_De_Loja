package br.com.sistema.dao;

import br.com.sistema.jdbc.ConexaoBanco;
import br.com.sistema.model.Produtos;
import br.com.sistema.model.Vendas;
import br.com.sistema.model.ItensVendas;
import br.com.sistema.model.clientes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author rustz
 */
public class VendasDao {
        private Connection conn;

    public VendasDao() {
        this.conn = new ConexaoBanco().pegarConexao();
    }
    
    
public void salvar(Vendas obj) {
    String sqlVenda = "INSERT INTO tb_vendas (cliente_id, data_venda, total_venda, observacoes) VALUES (?, ?, ?, ?)";
    String sqlVerificaCliente = "SELECT id FROM tb_clientes WHERE nome = ?";
    String sqlInsereClienteTemp = "INSERT INTO tb_clientes (nome, email, celular, telefone, cep, endereco, descricao, bairro, cidade, complemento, situacao, modelo) "
            + "VALUES (?, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Temporário', NULL)";
    
    try {
        int clienteId;

        // Verifica se o cliente existe
        try (PreparedStatement stmtVerifica = conn.prepareStatement(sqlVerificaCliente)) {
            stmtVerifica.setString(1, obj.getClientes().getNome());
            ResultSet rs = stmtVerifica.executeQuery();

            if (rs.next()) {
                // Cliente encontrado
                clienteId = rs.getInt("id");
            } else {
                // Cliente não encontrado, criar um temporário
                try (PreparedStatement stmtInsere = conn.prepareStatement(sqlInsereClienteTemp, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmtInsere.setString(1, obj.getClientes().getNome());
                    stmtInsere.executeUpdate();

                    // Obter o ID gerado para o cliente temporário
                    ResultSet rsGeneratedKeys = stmtInsere.getGeneratedKeys();
                    if (rsGeneratedKeys.next()) {
                        clienteId = rsGeneratedKeys.getInt(1);
                    } else {
                        throw new RuntimeException("Erro ao criar cliente temporário.");
                    }
                }
            }
        }

        // Salva a venda
        try (PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda)) {
            stmtVenda.setInt(1, clienteId);
            stmtVenda.setString(2, obj.getData_venda());
            stmtVenda.setDouble(3, obj.getTotal_venda());
            stmtVenda.setString(4, obj.getObservacoes());

            stmtVenda.executeUpdate();
            System.out.println("Venda salva com sucesso!");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erro ao realizar a venda: " + e.getMessage(), e);
    }
}


    public int retornaUltimoIdVenda(){
    
        try {
            int ultimoId = 0;
            String sql = "SELECT MAX(id) id FROM tb_vendas";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
            
                Vendas v = new Vendas();
                v.setId(rs.getInt("id"));
                ultimoId = v.getId();
            }
            return ultimoId;
        } catch (Exception e) {
            throw new RuntimeException("erro ao retornar o ultimo id da venda");
        }
    
    }
    
    
public List<Vendas> historicoVendas(String dataInicio, String dataFim) {
    List<Vendas> lista = new ArrayList<>();
    String sql = """
        SELECT v.id, c.nome, v.data_venda, v.total_venda, v.observacoes
        FROM tb_vendas v
        LEFT JOIN tb_clientes c ON v.cliente_id = c.id
        WHERE v.data_venda BETWEEN ? AND ?
    """;

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, dataInicio);
        stmt.setString(2, dataFim);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Vendas venda = new Vendas();
            venda.setId(rs.getInt("id"));
            venda.setData_venda(rs.getString("data_venda"));
            venda.setTotal_venda(rs.getDouble("total_venda"));
            venda.setObservacoes(rs.getString("observacoes"));

            clientes cliente = new clientes();
            cliente.setNome(rs.getString("nome"));
            venda.setClientes(cliente);

            lista.add(venda);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Erro ao buscar histórico de vendas: " + e.getMessage(), e);
    }

    return lista;
}
public double posicaoDoDia(String dataVenda) {
    String sql = "SELECT SUM(total_venda) AS total FROM tb_vendas WHERE DATE(data_venda) = ?";
    double totalVenda = 0.0;

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, dataVenda); // Passa a data como String
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            totalVenda = rs.getDouble("total");
        }

    } catch (SQLException e) {
        System.err.println("Erro ao executar consulta: " + e.getMessage());
    }

    return totalVenda;
}







}