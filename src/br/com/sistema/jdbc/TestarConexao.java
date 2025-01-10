package br.com.sistema.jdbc;

import java.sql.Connection;
import javax.swing.JOptionPane;

public class TestarConexao {

    public static void main(String[] args) {
        
        try {
            new ConexaoBanco().pegarConexao();
            JOptionPane.showMessageDialog(null,"conectado ao banco de dados com sucesso!");
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null,"erro ao conectar no banco de dados"+e);
        }
    
}}
