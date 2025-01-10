package br.com.sistema.utilitarios;


import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Utilitarios {
    public void LimpaTela(JPanel container){
        Component conponet[] = container.getComponents();
        for(Component component : conponet){
        
         if(component instanceof JTextField){
         
            ((JTextField)component).setText(null);
         }
        }
    }
}
