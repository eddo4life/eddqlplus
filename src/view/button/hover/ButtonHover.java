package view.button.hover;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonHover {

	private static final Cursor HAND_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	private static final Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	public static void apply(JButton button, Color defaultColor, Color hoverColor) {
		button.setBackground(defaultColor);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setCursor(HAND_CURSOR);
				button.setBackground(hoverColor);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setCursor(DEFAULT_CURSOR);
				button.setBackground(defaultColor);
			}
		});
	}
}
