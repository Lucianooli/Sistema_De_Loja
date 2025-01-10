package br.com.sistema.dao;

import br.com.sistema.jdbc.ConexaoBanco;
import br.com.sistema.model.clientes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ClientesDao {

    private Connection conn;

    public ClientesDao() {
        this.conn = new ConexaoBanco().pegarConexao();
    }

    // Método para salvar um cliente no banco de dados
    public void salvar(clientes obj) {
        try {
            String sql = "INSERT INTO tb_clientes (nome, email, celular, telefone, cep, endereco, bairro, cidade, complemento, situacao, modelo, descricao) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getEmail());
            stmt.setString(3, obj.getCelular());
            stmt.setString(4, obj.getTelefone());
            stmt.setString(5, obj.getCep());
            stmt.setString(6, obj.getEndereço());
            stmt.setString(7, obj.getBairro());
            stmt.setString(8, obj.getCidade());
            stmt.setString(9, obj.getComplemento());
            stmt.setString(10, obj.getSituacao());
            stmt.setString(11, obj.getModelo());
            stmt.setString(12, obj.getDescricao());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Cliente salvo com sucesso!");
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar cliente: " + erro.getMessage());
        }
    }

    // Método para buscar um cliente pelo nome
    public clientes BuscarCliente(String nome) {
        String sql = "SELECT * FROM tb_clientes WHERE nome = ?";
        clientes obj = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    obj = new clientes();
                    obj.setId(rs.getInt("id"));
                    obj.setNome(rs.getString("nome"));
                    obj.setEmail(rs.getString("email"));
                    obj.setCelular(rs.getString("celular"));
                    obj.setTelefone(rs.getString("telefone"));
                    obj.setCep(rs.getString("cep"));
                    obj.setEndereço(rs.getString("endereco"));
                    obj.setBairro(rs.getString("bairro"));
                    obj.setCidade(rs.getString("cidade"));
                    obj.setComplemento(rs.getString("complemento"));
                    obj.setSituacao(rs.getString("situacao"));
                    obj.setModelo(rs.getString("modelo"));
                    obj.setDescricao(rs.getString("descricao"));
                } else {
                    JOptionPane.showMessageDialog(null, "Cliente não encontrado.");
                }
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar cliente: " + erro.getMessage());
        }

        return obj;
    }

    // Método para listar todos os clientes
    public List<clientes> lista() {
        List<clientes> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tb_clientes";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                clientes c = new clientes();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setEmail(rs.getString("email"));
                c.setTelefone(rs.getString("telefone"));
                c.setCelular(rs.getString("celular"));
                c.setCep(rs.getString("cep"));
                c.setEndereço(rs.getString("endereco"));
                c.setBairro(rs.getString("bairro"));
                c.setCidade(rs.getString("cidade"));
                c.setComplemento(rs.getString("complemento"));
                c.setSituacao(rs.getString("situacao"));
                c.setModelo(rs.getString("modelo"));
                c.setDescricao(rs.getString("descricao"));
                lista.add(c);
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao criar a lista: " + erro.getMessage());
        }

        return lista;
    }

    // Método para filtrar clientes pelo nome
    public List<clientes> filtrar(String nome) {
        List<clientes> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tb_clientes WHERE nome LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                clientes obj = new clientes();
                obj.setId(rs.getInt("id"));
                obj.setNome(rs.getString("nome"));
                obj.setEmail(rs.getString("email"));
                obj.setCelular(rs.getString("celular"));
                obj.setTelefone(rs.getString("telefone"));
                obj.setCep(rs.getString("cep"));
                obj.setEndereço(rs.getString("endereco"));
                obj.setBairro(rs.getString("bairro"));
                obj.setCidade(rs.getString("cidade"));
                obj.setComplemento(rs.getString("complemento"));
                obj.setSituacao(rs.getString("situacao"));
                obj.setModelo(rs.getString("modelo"));
                obj.setDescricao(rs.getString("descricao"));

                lista.add(obj);
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao filtrar clientes: " + erro.getMessage());
        }

        return lista;
    }

    // Método para editar um cliente
public void Editar(clientes obj) {
    try {
        String sql = "UPDATE tb_clientes SET nome=?, email=?, celular=?, telefone=?, cep=?, endereco=?, bairro=?, cidade=?, complemento=?, situacao=?, modelo=?, descricao=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, obj.getNome());
        stmt.setString(2, obj.getEmail());
        stmt.setString(3, obj.getCelular());
        stmt.setString(4, obj.getTelefone());
        stmt.setString(5, obj.getCep());
        stmt.setString(6, obj.getEndereço()); // Removido acento
        stmt.setString(7, obj.getBairro());
        stmt.setString(8, obj.getCidade());
        stmt.setString(9, obj.getComplemento());
        stmt.setString(10, obj.getSituacao());
        stmt.setString(11, obj.getModelo());
        stmt.setString(12, obj.getDescricao());
        stmt.setInt(13, obj.getId());

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Cliente editado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum cliente foi encontrado com o ID especificado.");
        }

        stmt.close();
    } catch (SQLException erro) {
        erro.printStackTrace(); // Adicionado para debug
        JOptionPane.showMessageDialog(null, "Erro ao editar o cliente: " + erro.getMessage());
    }
}


    // Método para excluir um cliente
    public void Excluir(clientes obj) {
        try {
            String sql = "DELETE FROM tb_clientes WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getId());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso!");
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir o cliente: " + erro.getMessage());
        }
    }
    
    


}
