package br.com.sistema.dao;

import br.com.sistema.jdbc.ConexaoBanco;
import br.com.sistema.model.Produtos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ProdutosDAO {

    private Connection conn;

    public ProdutosDAO() {
        this.conn = new ConexaoBanco().pegarConexao();
    }

    // Método para salvar um cliente no banco de dados
    public void salvar(Produtos obj) {
        try {
            String sql = "INSERT INTO tb_produtos (nome, preco,estoque) "
                    + "VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getPreco());
            stmt.setString(3, obj.getEstoque());
            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Produto salvo com sucesso!");
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar Produto: " + erro.getMessage());
        }
    }

    // Método para buscar um cliente pelo nome
    public Produtos BuscarProduto(String nome) {
        String sql = "SELECT * FROM tb_produtos WHERE nome = ?";
        Produtos obj = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    obj = new Produtos();
                    obj.setId(rs.getInt("id"));
                    obj.setNome(rs.getString("nome"));
                    obj.setPreco(rs.getString("preco"));
                    obj.setEstoque(rs.getString("estoque"));
                } else {
                    JOptionPane.showMessageDialog(null, "Produto não encontrado.");
                }
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar Produto: " + erro.getMessage());
        }

        return obj;
    }

    // Método para listar todos os clientes
    public List<Produtos> lista() {
        List<Produtos> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tb_produtos";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produtos c = new Produtos();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setPreco(rs.getString("preco"));
                c.setEstoque(rs.getString("estoque"));
                lista.add(c);
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao criar a lista: " + erro.getMessage());
        }

        return lista;
    }

    // Método para filtrar clientes pelo nome
    public List<Produtos> filtrar(String nome) {
        List<Produtos> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tb_produtos WHERE nome LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produtos obj = new Produtos();
                obj.setId(rs.getInt("id"));
                obj.setNome(rs.getString("nome"));
                obj.setPreco(rs.getString("preco"));
                obj.setEstoque(rs.getString("estoque"));


                lista.add(obj);
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao filtrar Produtos: " + erro.getMessage());
        }

        return lista;
    }

    // Método para editar um cliente
public void Editar(Produtos obj) {
    try {
        String sql = "UPDATE tb_produtos SET nome=?, preco=?, estoque=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, obj.getNome());
        stmt.setString(2, obj.getPreco());
        stmt.setString(3, obj.getEstoque());
        stmt.setInt(4, obj.getId());

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Produto editado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum Produto foi encontrado com o ID especificado.");
        }

        stmt.close();
    } catch (SQLException erro) {
        erro.printStackTrace(); // Adicionado para debug
        JOptionPane.showMessageDialog(null, "Erro ao editar o Produto: " + erro.getMessage());
    }
}


    // Método para excluir um cliente
    public void Excluir(Produtos obj) {
        try {
            String sql = "DELETE FROM tb_produtos WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getId());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Produto excluído com sucesso!");
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir o Produto: " + erro.getMessage());
        }
    }
    public void AdicionarEstoque(int id, int qtdAtualizada) {
        try {
            String sql = "UPDATE tb_produtos SET estoque=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, qtdAtualizada); // Define o estoque atualizado no primeiro parâmetro
            stmt.setInt(2, id); // Define o ID do produto no segundo parâmetro
            stmt.executeUpdate(); // Use executeUpdate para comandos SQL do tipo UPDATE
            stmt.close();
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar ao estoque! " + erro);
        }
    }

        public void BaixaEstoque(int id, int qtdAtualizada){
        
        try {
            String sql = "UPDATE tb_produtos SET estoque=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, qtdAtualizada); // Define o estoque atualizado no primeiro parâmetro
            stmt.setInt(2, id);
            stmt.execute();
            stmt.close();
        } catch (SQLException  erro) {
            JOptionPane.showMessageDialog(null, "erro ao dar baixa ao estoque!"+ erro);

        }
    
    }
    
        
        
        
        
    
public int retornaQTDatualEstoque(int id) {
    int qtdAtual_estoque = 0; // Inicializa a variável

    try {
        String sql = "SELECT estoque FROM tb_produtos WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id); // Define o parâmetro id
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            qtdAtual_estoque = rs.getInt("estoque"); // Obtém o valor do estoque
        }

        // Fecha os recursos após o uso
        rs.close();
        stmt.close();
    } catch (SQLException e) {
        throw new RuntimeException("Erro ao obter quantidade atual do estoque: " + e);
    }

    return qtdAtual_estoque; // Retorna a quantidade atual
}

}