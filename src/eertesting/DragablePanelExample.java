package eertesting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DragablePanelExample {
 public DragablePanelExample() {
	 
	   JFrame frame = new JFrame("Dragable Panel Example");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JPanel panel = new JPanel();
	    panel.setBackground(Color.BLUE);

	    // Add a mouse listener to the panel to detect when the user
	    // clicks and drags the mouse
	    panel.addMouseListener(new MouseAdapter() {
	      Point dragStart;

	      @Override
	      public void mousePressed(MouseEvent e) {
	        // Save the mouse cursor position when the user clicks on the panel
	        dragStart = new Point(e.getX(), e.getY());
	      }

	      @Override
	      public void mouseDragged(MouseEvent e) {
	        // Get the current position of the mouse cursor
	        Point dragEnd = new Point(e.getX(), e.getY());

	        // Calculate the difference between the start and end position
	        int xDiff = dragEnd.x - dragStart.x;
	        int yDiff = dragEnd.y - dragStart.y;

	        // Get the current location of the panel
	        int x = panel.getX();
	        int y = panel.getY();

	        // Set the new location of the panel based on the difference between
	        // the start and end position of the mouse cursor
	        panel.setLocation(x + xDiff, y + yDiff);
	      }
	    });

	    frame.add(panel);
	    frame.pack();
	    frame.setVisible(true);
}
 
public static void main(String[] args) {
	new DragablePanelExample();
}
}