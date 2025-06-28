package view.scrollpane;

import javax.swing.*;
import java.awt.*;


public class CustomScrollPane {

    private final JScrollPane scrollPane;

    public CustomScrollPane(Component component) {
        scrollPane = new JScrollPane(component, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportBorder(null);
        scrollPane.setBorder(null);
    }

    public JScrollPane getScrollPane() {
        scrollPane.setBounds(230, 50, 250, 100);
        return scrollPane;
    }

}
