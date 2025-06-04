package view;

//and menu items to it
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import controller.ide.filemanager.FileManager;
import icon.BufferedImageIO;


class Popup extends JFrame implements ActionListener {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	static JPopupMenu pm;
	JMenuItem cut, copy, paste, _new, save, open, print, delete, selectAll, clear, run,debug;

	Popup(JPopupMenu pm) {
		this.pm=pm;
		
        int x=13,y=15;

		cut = new JMenuItem("Cut");
		cut.setMargin(new Insets(3, 10, 0, 10));
		cut.setIcon(new BufferedImageIO().getIcon(x, y, "cut.png"));
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,KeyEvent.CTRL_DOWN_MASK));
		cut.addActionListener(this);
		copy = new JMenuItem("Copy");
		copy.setMargin(new Insets(3, 10, 0, 10));
		copy.setIcon(new BufferedImageIO().getIcon(x, y, "document.png"));
		copy.addActionListener(this);
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_DOWN_MASK));
		paste = new JMenuItem("Paste");
		paste.setIcon(new BufferedImageIO().getIcon(x, y, "clipboard.png"));
		paste.setMargin(new Insets(3, 10, 0, 10));
		paste.addActionListener(this);
		_new = new JMenuItem("New");
		_new.setMargin(new Insets(3, 10, 0, 10));
		_new.addActionListener(this);
		_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,KeyEvent.CTRL_DOWN_MASK));
		open = new JMenuItem("Open");
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,KeyEvent.CTRL_DOWN_MASK));
		open.setMargin(new Insets(3, 10, 0, 10));
		open.addActionListener(this);
		save = new JMenuItem("Save");
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_DOWN_MASK));
		save.setIcon(new BufferedImageIO().getIcon(x, y, "save.png"));
		save.setMargin(new Insets(3, 10, 0, 10));
		save.addActionListener(this);
		print = new JMenuItem("Print");
		print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_DOWN_MASK));
		print.setMargin(new Insets(3, 10, 0, 10));
		print.setIcon(new BufferedImageIO().getIcon(x, y, "print.png"));
		print.addActionListener(this);
		delete = new JMenuItem("Delete");
		delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,KeyEvent.CTRL_DOWN_MASK));
		delete.setMargin(new Insets(3, 10, 0, 10));
		delete.setIcon(new BufferedImageIO().getIcon(x, y, "delete.jpg"));
		delete.addActionListener(this);
		selectAll = new JMenuItem("Select All       ");
		selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,KeyEvent.CTRL_DOWN_MASK));
		selectAll.setMargin(new Insets(3, 10, 0, 10));
		selectAll.setIcon(new BufferedImageIO().getIcon(x, y, "ark.png"));
		selectAll.addActionListener(this);
		clear = new JMenuItem("Clear");
		clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.CTRL_DOWN_MASK));
		clear.setMargin(new Insets(3, 10, 0, 10));
		// clear.setIcon(new Tools().getIcon(18, 20, "ark.png"));
		clear.addActionListener(this);
		run = new JMenuItem("Run");
		run.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,KeyEvent.ALT_DOWN_MASK));
		run.setMargin(new Insets(3, 10, 0, 10));
		run.setIcon(new BufferedImageIO().getIcon(x, y, "energy.png"));
		run.addActionListener(this);
		run.setEnabled(false);
		// run.setIconTextGap(10);
		cut.setEnabled(false);
		copy.setEnabled(false);

		pm.addSeparator();
		pm.add(cut);
		pm.add(copy);
		pm.add(paste);
		pm.add(selectAll);
		pm.addSeparator();
		pm.add(_new);
		pm.add(save);
		pm.add(open);
		pm.add(selectAll);
		pm.addSeparator();
		pm.add(delete);
		pm.add(clear);
		pm.addSeparator();
		pm.add(print);
		pm.addSeparator();
		pm.add(run);
		debug= new JMenuItem("Debug");
		debug.setMargin(new Insets(3, 10, 0, 10));
		debug.addActionListener(this);
		pm.add(debug);
		
		
	}
	

	//ArrayList<String> arr;
	int ams=0;//the maximum size of the arrayList
	int acs=Editor_section.acs;//the current size of the arrayList
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		RSyntaxTextArea t = Editor_section.textArea;
		if(t.isEditable()&& t.isEnabled()) {
			doAction(e, t);
		}
		
	}
	
	void doAction(ActionEvent e,RSyntaxTextArea t) {
		if (e.getSource() == cut) {
			t.cut();
		} else if (e.getSource() == copy) {
			t.copy();
		} else if (e.getSource() == paste) {
			t.paste();
		} else if (e.getSource() == _new) {
			// try to save result and
			// do action which is
			t.setText("");
		} else if (e.getSource() == open) {
			new FileManager().openFile();
		} else if (e.getSource() == save) {
			new FileManager().saveFile();
		} else if (e.getSource() == print) {
			try {
				if (Editor_section.editorpane_current_fgColor == Color.white)
					t.setForeground(Color.black);
				t.print();
			} catch (PrinterException e1) {
				e1.printStackTrace();
			}
			t.setForeground(Editor_section.editorpane_current_fgColor);
		} else if (e.getSource() == delete) {
			// put it delete row and select the row current row to make the action
		} else if (e.getSource() == selectAll) {
			t.selectAll();
		} else if (e.getSource() == clear) {
			t.setText("");
		} else if (e.getSource() == run) {
			
			new Editor_section().execute();
		}
		 else if (e.getSource()==debug){
				t.setEditable(true);
				t.setEnabled(true);
				//to be continued...
			}
	}
	
	

}
