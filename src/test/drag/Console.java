package test.drag;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Console extends JFrame {
  private JTextField commandField;
  private JTextArea consoleArea;

  public Console() {
    // Initialisation de la fenêtre
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Console");

    // Initialisation des composants
    commandField = new JTextField();
    commandField.setBorder(null);
    consoleArea = new JTextArea();
    consoleArea.setEditable(false);
    consoleArea.setFocusable(false);

    // Ajout des composants à la fenêtre
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(commandField, BorderLayout.SOUTH);
    mainPanel.add(consoleArea, BorderLayout.CENTER);
    add(mainPanel);

    // Écouter les événements d'action sur le champ de commande
    commandField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String command = commandField.getText();
        consoleArea.append("> " + command + "\n");
        commandField.setText("");
        // Insérez ici le code pour exécuter la commande et afficher les résultats
      }
    });
  }

  public static void main(String[] args) {
    Console console = new Console();
    console.setVisible(true);
  }
}
