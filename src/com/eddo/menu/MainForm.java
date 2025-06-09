package com.eddo.menu;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JPanel {

    public static MainForm main;

    public MainForm() {
        setLayout(new BorderLayout());
        main = this;
    }

    public void displayForm(Component component) {
        if (main != null) {
            main.removeAll();
            main.add(component);
            main.revalidate();
            main.repaint();
        }
    }

    public static MainForm getInstance() {
        return main;
    }
}
