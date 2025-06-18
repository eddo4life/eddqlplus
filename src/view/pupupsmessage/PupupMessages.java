package view.pupupsmessage;

import icon.BufferedImageIO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;


public class PupupMessages extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel northPanel, southPanel, westPanel, eastPanel, centerPanel;

	public PupupMessages() {
		getAction = -2;
		this.setLayout(new BorderLayout());
		northPanel = new JPanel();
		northPanel.setLayout(new GridBagLayout());
		westPanel = new JPanel();
		westPanel.setLayout(new GridBagLayout());
		westPanel.setPreferredSize(new Dimension(2, 10));
		eastPanel = new JPanel();
		eastPanel.setLayout(new GridBagLayout());
		eastPanel.setPreferredSize(new Dimension(2, 10));
		southPanel = new JPanel();
		southPanel.setLayout(null);
		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		this.setLocationRelativeTo(null);
		this.setSize(new Dimension(350, 135));
		this.setIconImage(new ImageIcon("sql2.jpeg").getImage());
		this.setLocationRelativeTo(null);
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(westPanel, BorderLayout.WEST);
		this.setUndecorated(true);
		this.setResizable(false);
		this.setModal(true);
		this.setDefaultCloseOperation(PupupMessages.DISPOSE_ON_CLOSE);
	}

	// ======================set bgColor

	private void bgColor(JPanel panel) {
	//	panel.setBackground(Color.lightGray);
	}

	// ========================================================---------

	public void confirm(String message) {
		westPanel.setPreferredSize(new Dimension(2, 10));
		eastPanel.setPreferredSize(new Dimension(2, 10));
		westPanel.setBackground(null);
		eastPanel.setBackground(null);
		this.remove(southPanel);
		this.remove(centerPanel);
		mess = new JLabel(message);
		centerPanel.setLayout(new GridBagLayout());
		centerPanel.add(mess);
		bgColor(centerPanel);
		this.setTitle("EddoSQL");
		JButton cancel = createButton("Cancel");
		cancel.addActionListener((ActionEvent e) -> {
			this.dispose();
			getAction = -1;
		});

		JButton yes = createButton("Yes");
		yes.addActionListener((ActionEvent e) -> {
			this.dispose();
			getAction = 1;
		});
		JButton no = createButton("No");
		no.addActionListener((ActionEvent e) -> {
			this.dispose();
			getAction = 0;
		});
		southPanel = new JPanel();
		southPanel.setLayout(null);
		int x = 35;
		yes.setBounds(x, 10, 80, 25);
		no.setBounds(x + 97, 10, 80, 25);
		cancel.setBounds(2 * x + 160, 10, 80, 25);
		southPanel.setPreferredSize(new Dimension(10, 50));
		southPanel.add(yes);
		southPanel.add(no);
		southPanel.add(cancel);
		bgColor(southPanel);
		northPanel = new JPanel();
		northPanel.setLayout(new GridBagLayout());
		JLabel label = new JLabel();
		label.setIcon(new BufferedImageIO().getIcon(25, 25, "question.png"));
		northPanel.add(label);
		bgColor(northPanel);
		JPanel borderPanel = new JPanel();
		borderPanel.setLayout(new BorderLayout());
		borderPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new EtchedBorder()));
		borderPanel.add(northPanel, BorderLayout.NORTH);
		borderPanel.add(centerPanel, BorderLayout.CENTER);
		borderPanel.add(southPanel, BorderLayout.SOUTH);
		this.add(borderPanel);

		this.revalidate();
		this.repaint();
		this.setVisible(true);

	}

	// ========================================================---------

	private JLabel mess;

	public void message(String message, Icon icon) {

		this.remove(centerPanel);
		this.remove(northPanel);
		JLabel imgLabel = new JLabel();
		imgLabel.setIcon(icon);
		northPanel.add(imgLabel);
		this.setTitle("EddQL");
		mess = new JLabel();
		if (message != null) {
			if (message.length() > 55) {
				message = "<html><p>" + message + "</p></html>";
				mess.setText(message);
				centerPanel.add(mess, BorderLayout.CENTER);
				westPanel.setPreferredSize(new Dimension(11, 10));
				eastPanel.setPreferredSize(new Dimension(11, 10));
			//	westPanel.setBackground(Color.white);
				//eastPanel.setBackground(Color.white);
			} else {
				centerPanel.setLayout(new GridBagLayout());
				mess.setText(message);
				centerPanel.add(mess);
				westPanel.setPreferredSize(new Dimension(2, 10));
				eastPanel.setPreferredSize(new Dimension(2, 10));
				westPanel.setBackground(null);
				eastPanel.setBackground(null);
			}
		}

		JButton ok = createButton("Ok");
		ok.addActionListener((ActionEvent e) -> {
			this.dispose();
		});
		ok.setPreferredSize(new Dimension(80, 25));
		ok.setBounds((this.getWidth() / 2) - 55, 3, 80, 25);
		southPanel.setPreferredSize(new Dimension(10, 31));
		southPanel.add(ok);
		bgColor(centerPanel);
		bgColor(southPanel);
		bgColor(northPanel);

		JPanel borderPanel = new JPanel();
		borderPanel.setLayout(new BorderLayout());
		borderPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new EtchedBorder()));
		borderPanel.add(northPanel, BorderLayout.NORTH);
		borderPanel.add(centerPanel, BorderLayout.CENTER);
		borderPanel.add(southPanel, BorderLayout.SOUTH);
		this.add(borderPanel);
		this.revalidate();
		this.repaint();
		this.setVisible(true);
		
	}


	// ========================================================---------

	public JButton createButton(String name) {
		JButton button = new JButton(name);
		button.setBorder(null);
		button.setFocusable(false);
		return button;
	}

	public static int getAction;

}
