package test.drag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Console extends JFrame {
  private JTextField commandField;
  private JTextArea consoleArea;

  public Console() {
    // Initialisation de la fen�tre
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Console");

    // Initialisation des composants
    commandField = new JTextField();
    commandField.setBorder(null);
    consoleArea = new JTextArea();
    consoleArea.setEditable(false);
    consoleArea.setFocusable(false);

    // Ajout des composants � la fen�tre
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(commandField, BorderLayout.SOUTH);
    mainPanel.add(consoleArea, BorderLayout.CENTER);
    add(mainPanel);

    // �couter les �v�nements d'action sur le champ de commande
    commandField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String command = commandField.getText();
        consoleArea.append("> " + command + "\n");
        commandField.setText("");
        // Ins�rez ici le code pour ex�cuter la commande et afficher les r�sultats
      }
    });
  }

  public static void main(String[] args) {
    Console console = new Console();
    console.setVisible(true);
  }
}
