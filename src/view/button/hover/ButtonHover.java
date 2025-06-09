package view.button.hover;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ButtonHover {
	
	
	public ButtonHover(JButton button,Color color){
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setCursor(new Cursor(Cursor.HAND_CURSOR));
				button.setBackground(Color.darkGray);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				button.setBackground(color);
			}
		});
		
	}

}
