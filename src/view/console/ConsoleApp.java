package view.console;

import view.MySQLTextAreaExample;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.util.Stack;

public class ConsoleApp extends JPanel implements ActionListener, DocumentListener, KeyListener {
    @Serial
    private static final long serialVersionUID = 1L;

    private final JTextField inputField;
    private final JTextArea outputArea;
    private final Stack<String> undoStack;
    private final Stack<String> redoStack;
    private String currentCommand;

    public ConsoleApp() {
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setBorder(null);
        outputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                inputField.requestFocus();
            }
        });

        Font font = new Font("Monospaced", Font.PLAIN, 15);
        outputArea.setFont(font);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.setBackground(null);
        inputField.setBorder(null);
        inputField.addActionListener(this);
        inputField.addKeyListener(this);
        inputField.getDocument().addDocumentListener(this);
        add(inputField, BorderLayout.SOUTH);

        inputField.requestFocus();

        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String input = inputField.getText();
        outputArea.append("> " + input + "\n");
        inputField.setText("");
        undoStack.push(input);
        redoStack.clear();
        outputArea.append(new MySQLTextAreaExample(input).getText());
        //outputArea.append(input);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        currentCommand = inputField.getText();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        currentCommand = inputField.getText();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown()) {
            if (e.getKeyCode() == KeyEvent.VK_Z) {
                if (!undoStack.isEmpty()) {
                    String input = undoStack.pop();
                    redoStack.push(currentCommand);
                    inputField.setText(input);
                    currentCommand = input;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_Y) {
                if (!redoStack.isEmpty()) {
                    String input = redoStack.pop();
                    undoStack.push(currentCommand);
                    inputField.setText(input);
                    currentCommand = input;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
