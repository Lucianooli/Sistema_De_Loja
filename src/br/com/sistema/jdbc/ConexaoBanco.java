package br.com.sistema.jdbc;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexaoBanco {
        final private String url = "jdbc:mysql://localhost/sistema_loja";
        final private  String usuario = "root";
        final private String senha = "";
    public Connection pegarConexao() {

        try {
            return DriverManager.getConnection(url,usuario,senha);
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "erro ao se conectar ao banco de dados");
        }
        return  null;
}
}

