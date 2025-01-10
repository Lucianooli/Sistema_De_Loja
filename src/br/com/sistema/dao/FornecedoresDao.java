package br.com.sistema.dao;

import br.com.sistema.jdbc.ConexaoBanco;
import br.com.sistema.model.Fornecedores;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class FornecedoresDao {

    private Connection conn;

    public FornecedoresDao() {
        this.conn = new ConexaoBanco().pegarConexao();
    }

    // Método para salvar um cliente no banco de dados
    public void salvarFornecedor(Fornecedores obj) {
        try {
            String sql = "INSERT INTO tb_fornecedores (nome, email, celular) "
                    + "VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getEmail());
            stmt.setString(3, obj.getCelular());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Fornecedor salvo com sucesso!");
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar Funcionário: " + erro.getMessage());
        }
    }

    // Método para buscar um cliente pelo nome
    public Fornecedores BuscarFornecedor(String nome) {
        String sql = "SELECT * FROM tb_fornecedores WHERE nome = ?";
        Fornecedores obj = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    obj = new Fornecedores();
                    obj.setId(rs.getInt("id"));
                    obj.setNome(rs.getString("nome"));
                    obj.setEmail(rs.getString("email"));
                    obj.setCelular(rs.getString("celular"));
                } else {
                    JOptionPane.showMessageDialog(null, "Fornecedor não encontrado.");
                }
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar Fornecedor: " + erro.getMessage());
        }

        return obj;
    }

    // Método para listar todos os clientes
    public List<Fornecedores> lista() {
        List<Fornecedores> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tb_fornecedores";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Fornecedores c = new Fornecedores();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setEmail(rs.getString("email"));
                c.setCelular(rs.getString("celular"));
                lista.add(c);
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao criar a lista: " + erro.getMessage());
        }

        return lista;
    }

    // Método para filtrar clientes pelo nome
    public List<Fornecedores> filtrar(String nome) {
        List<Fornecedores> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tb_fornecedores WHERE nome LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Fornecedores obj = new Fornecedores();
                obj.setId(rs.getInt("id"));
                obj.setNome(rs.getString("nome"));
                obj.setEmail(rs.getString("email"));
                obj.setCelular(rs.getString("celular"));


                lista.add(obj);
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao filtrar Fornecedor: " + erro.getMessage());
        }

        return lista;
    }

    // Método para editar um cliente
public void Editar(Fornecedores obj) {
    try {
        String sql = "UPDATE tb_fornecedores SET nome=?, email=?, celular=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, obj.getNome());
        stmt.setString(2, obj.getEmail());
        stmt.setString(3, obj.getCelular());
        stmt.setInt(4, obj.getId());

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Fornecedor editado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum Fornecedor foi encontrado com o ID especificado.");
        }

        stmt.close();
    } catch (SQLException erro) {
        erro.printStackTrace(); // Adicionado para debug
        JOptionPane.showMessageDialog(null, "Erro ao editar o Fornecedor: " + erro.getMessage());
    }
}


    // Método para excluir um cliente
    public void Excluir(Fornecedores obj) {
        try {
            String sql = "DELETE FROM tb_fornecedores WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getId());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Fornecedor excluído com sucesso!");
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir o Fornecedor: " + erro.getMessage());
        }
    }
}
