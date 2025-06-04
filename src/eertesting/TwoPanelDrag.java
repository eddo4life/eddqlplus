package eertesting;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
public class TwoPanelDrag {
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Two Panel Drag"); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel1 = new JPanel();
		  panel1.setBackground(Color.CYAN);
		  panel1.setPreferredSize(new Dimension(200, 100));
		  panel1.setBounds(0, 0, 200, 100);

		  JPanel panel2 = new JPanel();
		  panel2.setBackground(Color.ORANGE);
		  panel2.setPreferredSize(new Dimension(200, 100));
		  panel2.setBounds(200, 0, 200, 100);

		  frame.add(panel1);
		  frame.add(panel2);

		  frame.pack();
		  frame.setVisible(true);

		  panel1.addMouseListener(new MouseAdapter() {
		     public void mousePressed(MouseEvent e) {
		        JPanel panel = (JPanel) e.getSource();
		        panel.setLocation(panel.getX() + e.getX(), panel.getY() + e.getY());
		     }
		  });

		
		  
		  panel2.addMouseListener(new MouseAdapter() {
			     public void mousePressed(MouseEvent e) {
			        JPanel panel = (JPanel) e.getSource();
			        panel.setLocation(panel.getX() + e.getX(), panel.getY() + e.getY());
			     }
			  });
 
		  
	}
	
	
}