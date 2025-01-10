/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.sistema.dao;
import br.com.sistema.model.ItensVendas;
import br.com.sistema.jdbc.ConexaoBanco;
import br.com.sistema.model.Produtos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rustz
 */
public class ItensVendasDao {
    private Connection conn;

    public ItensVendasDao() {
        this.conn = new ConexaoBanco().pegarConexao();
    }
    
public void salvar(ItensVendas obj){
    
    try {
        String sql = "INSERT into ProdutosVendas(id_venda,id_produto,qtd,subtotal)"
                + "VALUES(?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, obj.getVendas().getId());
        stmt.setInt(2, obj.getProdutos().getId());
        stmt.setInt(3, obj.getQtd());
        stmt.setDouble(4, obj.getSubtotal());
        
        stmt.execute();
        stmt.close();
        
    } catch (Exception e) {
        
      throw new RuntimeException("erro ao salvar itens da minha venda");
    }
    
    }


public List<ItensVendas> ListaItens(int venda_id) {
    try {
        List<ItensVendas> lista = new ArrayList<>();
        String sql = "SELECT p.id AS produto_id, p.nome AS produto_nome, p.preco AS produto_preco, " +
                     "i.qtd AS item_qtd, i.subtotal AS item_subtotal " +
                     "FROM ProdutosVendas AS i " +
                     "INNER JOIN tb_produtos AS p ON (i.id_produto = p.id) " +
                     "WHERE i.id_venda = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, venda_id);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            // Criar novo objeto ItensVendas
            ItensVendas item = new ItensVendas();

            // Criar e configurar o objeto Produtos
            Produtos p = new Produtos();
            p.setId(rs.getInt("produto_id"));
            p.setNome(rs.getString("produto_nome"));
            p.setPreco(rs.getString("produto_preco"));

            // Configurar o objeto ItensVendas
            item.setProdutos(p);
            item.setQtd(rs.getInt("item_qtd"));
            item.setSubtotal(rs.getDouble("item_subtotal"));

            // Adicionar o item à lista
            lista.add(item);
        }
        return lista;
    } catch (Exception e) {
        throw new RuntimeException("Erro ao criar a lista de itens: " + e.getMessage(), e);
    }
}


}


