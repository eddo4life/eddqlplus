package view;

import javax.swing.*;
import java.awt.*;

public class ScreenSelector extends Home {
    private static final JPanel mainContent = Home.content;

    public static void showContent(JComponent component) {
        clearContentPanel();
        mainContent.add(component, BorderLayout.CENTER);
        revalidateContent();
    }

    private static void clearContentPanel() {
        if (mainContent != null) {
            mainContent.removeAll();
        }
    }

    private static void revalidateContent() {
        mainContent.revalidate();
        mainContent.repaint();
    }
}
