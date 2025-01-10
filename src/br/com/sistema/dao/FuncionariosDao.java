package br.com.sistema.dao;

import br.com.sistema.jdbc.ConexaoBanco;
import br.com.sistema.model.Funcionarios;
import br.com.sistema.view.AreaTrabalho;
import br.com.sistema.view.FormularioLogin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class FuncionariosDao {

    private Connection conn;

    public FuncionariosDao() {
        this.conn = new ConexaoBanco().pegarConexao();
    }

    // Método para salvar um cliente no banco de dados
    public void salvarFuncionario(Funcionarios obj) {
        try {
            String sql = "INSERT INTO tb_funcionarios (nome, email, senha, celular, cargo) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getEmail());
            stmt.setString(3, obj.getSenha());
            stmt.setString(4, obj.getCelular());
            stmt.setString(5, obj.getCargo());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Funcionário salvo com sucesso!");
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar Funcionário: " + erro.getMessage());
        }
    }

    // Método para buscar um cliente pelo nome
    public Funcionarios BuscarFuncionario(String nome) {
        String sql = "SELECT * FROM tb_funcionarios WHERE nome = ?";
        Funcionarios obj = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    obj = new Funcionarios();
                    obj.setId(rs.getInt("id"));
                    obj.setNome(rs.getString("nome"));
                    obj.setEmail(rs.getString("email"));
                    obj.setSenha(rs.getString("senha"));
                    obj.setCelular(rs.getString("celular"));
                    obj.setCargo(rs.getString("cargo"));
                } else {
                    JOptionPane.showMessageDialog(null, "Funcionário não encontrado.");
                }
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar Funcionário: " + erro.getMessage());
        }

        return obj;
    }

    // Método para listar todos os clientes
    public List<Funcionarios> lista() {
        List<Funcionarios> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tb_funcionarios";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Funcionarios c = new Funcionarios();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setEmail(rs.getString("email"));
                c.setSenha(rs.getString("senha"));
                c.setCelular(rs.getString("celular"));
                c.setCargo(rs.getString("cargo"));
                lista.add(c);
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao criar a lista: " + erro.getMessage());
        }

        return lista;
    }

    // Método para filtrar clientes pelo nome
    public List<Funcionarios> filtrar(String nome) {
        List<Funcionarios> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tb_funcionarios WHERE nome LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Funcionarios obj = new Funcionarios();
                obj.setId(rs.getInt("id"));
                obj.setNome(rs.getString("nome"));
                obj.setEmail(rs.getString("email"));
                obj.setSenha(rs.getString("senha"));
                obj.setCelular(rs.getString("celular"));
                obj.setCargo(rs.getString("cargo"));


                lista.add(obj);
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao filtrar Funcionário: " + erro.getMessage());
        }

        return lista;
    }

    // Método para editar um cliente
public void Editar(Funcionarios obj) {
    try {
        String sql = "UPDATE tb_funcionarios SET nome=?, email=?, celular=?, cargo=?, senha=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, obj.getNome());
        stmt.setString(2, obj.getEmail());
        stmt.setString(3, obj.getCelular());
        stmt.setString(4,obj.getCargo());
        stmt.setString(5, obj.getSenha());
        stmt.setInt(6, obj.getId());

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Funcionário editado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum Funcionário foi encontrado com o ID especificado.");
        }

        stmt.close();
    } catch (SQLException erro) {
        erro.printStackTrace(); // Adicionado para debug
        JOptionPane.showMessageDialog(null, "Erro ao editar o Funcionário: " + erro.getMessage());
    }
}


    // Método para excluir um cliente
    public void Excluir(Funcionarios obj) {
        try {
            String sql = "DELETE FROM tb_funcionarios WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getId());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Funcionário excluído com sucesso!");
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir o Fucionário: " + erro.getMessage());
        }
    }
    public void EfetuarLogin(String email, String senha) {
    try {
        // Corrigindo o SELECT para incluir as colunas ou usar *
        String sql = "SELECT * FROM tb_funcionarios WHERE email=? AND senha=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, senha);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
          if(rs.getString("cargo").equals("Administrado")){
            AreaTrabalho at = new AreaTrabalho();
            at.usuarioLogado = rs.getString("nome");
            JOptionPane.showMessageDialog(null, "Seja bem-vindo ao sistema!\n"+at.usuarioLogado);
            at.setVisible(true);
          }else if(rs.getString("cargo").equals("usuario")){
            AreaTrabalho at = new AreaTrabalho();
            at.usuarioLogado = rs.getString("nome");
            at.abaFuncionarios.setVisible(false);
            at.AbaControleDeEstoque.setVisible(false);
            JOptionPane.showMessageDialog(null, "Seja bem-vindo ao sistema!\n"+at.usuarioLogado);
            at.setVisible(true);
          }
        } else {
            // Login falhou
            JOptionPane.showMessageDialog(null, "Dados inválidos");
        }
    } catch (SQLException erro) {
        // Tratamento de erro
        JOptionPane.showMessageDialog(null, "Erro ao efetuar login: " + erro.getMessage());
    }
}   
}
