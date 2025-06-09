package view.tools;

import dao.DBMS;
import dao.mysql.MySQLConnection;
import dao.sqlite.SystemDatabaseTreatment;
import eddql.launch.LoadData;
import jiconfont.icons.font_awesome.FontAwesome;
import model.ConnectingToolsModel;
import view.Editor_section;
import view.Home;
import view.iconmaker.IconFontGenerator;
import view.iconmaker._Icon;
import view.pupupsmessage.PupupMessages;
import view.scrollpane.Scroll_pane;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class Tools implements FocusListener, MouseListener, KeyListener {

	public Tools() {
	}

	JButton getButton(String text) {
		JButton button = new JButton(text);
		button.setFocusable(false);
		button.setPreferredSize(new Dimension(140, 25));

		button.addMouseListener(this);
		// button.setBorder(new BevelBorder(3, Color.lightGray, Color.darkGray));
		return button;
	}

	public void menu() {
		connectionLabel = getButton("Connection");
		displayLabel = getButton("Display");
		securityLabel = getButton("Security");
		helpLabel = getButton("Help");
		rulesLabel = getButton("Rules");
		aboutLabel = getButton("About us");
		editorLabel = getButton("Editor");
		if (Home.content != null) {
			Home.content.removeAll();
		}
		home();
//		Home.content.add(panelsBuilder.getScrollPane(), BorderLayout.WEST);
		// Creating a new panel to set the preferring size will avoid an unnecessarily
		// scrolling
		JPanel menuPanel = new JPanel(new BorderLayout());
		menuPanel.setPreferredSize(new Dimension(195, 0));
		menuPanel.add(menuPanel());
		Home.content.add(menuPanel, BorderLayout.WEST);
		Home.content.revalidate();
		Home.content.repaint();
	}

	JPanel headerPanel() {
		JPanel headerPanel = new JPanel(new GridBagLayout());

		headerPanel.setPreferredSize(new Dimension(100, 70));
		JLabel headerLabel = new JLabel();
		headerLabel.setIcon(new IconFontGenerator(FontAwesome.WRENCH, 30, null).getIcon());
		headerPanel.add(headerLabel);
		return headerPanel;
	}

	JScrollPane menuPanel() {
		JPanel operationsPanel = new JPanel(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(20, 5, 15, 5);
		int x = 0;
		constraints.gridx = 0;
		constraints.gridy = x++;
		operationsPanel.add(headerPanel(), constraints);
		constraints.gridx = 0;
		constraints.gridy = x++;
		operationsPanel.add(connectionLabel, constraints);
		constraints.gridx = 0;
		constraints.gridy = x++;
		operationsPanel.add(displayLabel, constraints);
		constraints.gridx = 0;
		constraints.gridy = x++;
		operationsPanel.add(securityLabel, constraints);
		constraints.gridx = 0;
		constraints.gridy = x++;
		operationsPanel.add(helpLabel, constraints);
		constraints.gridx = 0;
		constraints.gridy = x++;
		operationsPanel.add(rulesLabel, constraints);
		constraints.gridx = 0;
		constraints.gridy = x++;
		operationsPanel.add(aboutLabel, constraints);
		constraints.gridx = 0;
		constraints.gridy = x++;
		operationsPanel.add(editorLabel, constraints);
		constraints.gridx = 0;
		constraints.gridy = x++;
		operationsPanel.add(editorLabel, constraints);
		constraints.gridx = 0;
		constraints.gridy = x++;
		operationsPanel.add(getButton("Exit"), constraints);

		JScrollPane scrollPane = new JScrollPane(operationsPanel);

		scrollPane.setViewportBorder(null);
		scrollPane.setBorder(null);
		tools_menu_panel = scrollPane;
		return scrollPane;
	}

	public static JScrollPane tools_menu_panel;

	public JPanel home() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("Tools"));
		return panel;
	}

	JPanel aboutUsPanel = new JPanel();

	public void aboutUs() {
		aboutUsPanel = new JPanel();
		aboutUsPanel.setLayout(new BorderLayout());
		JPanel centerPanel = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel eastPanel = new JPanel();
		JPanel westPanel = new JPanel();
		JPanel southPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		JLabel closeJLabel = new JLabel();
		closeJLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				menu();
			}
		});
		northPanel.add(closeJLabel, BorderLayout.EAST);
		eastPanel.setPreferredSize(new Dimension(50, 0));
		westPanel.setPreferredSize(new Dimension(50, 0));
		centerPanel.setLayout(new GridLayout());
		JLabel txt = new JLabel();
		txt.setText("<html><h2 style=' background-color: blue;'>Hello my name is Boaz and im the founder of eddosql"
				+ "my goal in this app is to help engineer symplify they task using mysql"
				+ "THis software has alot to show p so stay tuned"
				+ "<br>my goal in this app is to help engineer symplify they task using mysql"
				+ "THis software has alot to show p so stay tuned"
				+ "my goal in this app is to help engineer symplify they task using mysql"
				+ "THis software has alot to show p so stay tuned"
				+ "my goal in this app is to help engineer symplify they task using mysql"
				+ "THis software has alot to show p so stay tuned"
				+ "<br>my goal in this app is to help engineer symplify they task using mysql"
				+ "THis software has alot to show p so stay tuned"
				+ "my goal in this app is to help engineer symplify they task using mysql"
				+ "THis software has alot to show p so stay tuned" + "</html></h2>");
		centerPanel.add(txt);
		aboutUsPanel.add(centerPanel, BorderLayout.CENTER);
		aboutUsPanel.add(eastPanel, BorderLayout.EAST);
		aboutUsPanel.add(westPanel, BorderLayout.WEST);
		aboutUsPanel.add(southPanel, BorderLayout.SOUTH);
		aboutUsPanel.add(northPanel, BorderLayout.NORTH);
		Home.content.removeAll();
		Home.content.add(aboutUsPanel, BorderLayout.CENTER);
		Home.content.revalidate();
		Home.content.repaint();

	}

	public void help() {
		JPanel helPanel = new JPanel();
		helPanel.setBackground(Color.black);
		JLabel helpLabel = new JLabel("Help");
		Home.content.removeAll();
		Home.content.add(helpLabel);
		revalidate();
	}

	public void security() {
		JPanel helPanel = new JPanel();
		helPanel.setBackground(Color.black);
		JLabel helpLabel = new JLabel("Security");
		Home.content.removeAll();
		Home.content.add(helpLabel);
		revalidate();
	}

	public void revalidate() {
		mainPanel.revalidate();
		mainPanel.repaint();
		Home.content.revalidate();
		Home.content.repaint();
	}

	public void rules() {
		JPanel helPanel = new JPanel();
		helPanel.setBackground(Color.black);
		JLabel helpLabel = new JLabel("Rules");
		Home.content.removeAll();
		Home.content.add(helpLabel);
		revalidate();
	}

	public void recovery() {
		JPanel helPanel = new JPanel();
		helPanel.setBackground(Color.black);
		JLabel helpLabel = new JLabel("recovery");
		Home.content.removeAll();
		Home.content.add(helpLabel);
		revalidate();
	}

	JComboBox<String> host;

	public JPanel sqlConnector() {
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridBagLayout());
		northPanel.setPreferredSize(new Dimension(0, 100));

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(1));
		southPanel.setPreferredSize(new Dimension(0, 100));
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(500, 300));
		connectionPanel = new JPanel();
		connectionPanel.setLayout(new GridBagLayout());
		JLabel port = new JLabel(new IconFontGenerator(FontAwesome.USB, 15, null).getIcon());
		field(portField);
		portField.setBackground(null);
		JLabel user = new JLabel(new IconFontGenerator(FontAwesome.USER, 15, null).getIcon());
		field(userField);
		userField.setBackground(null);
		JLabel passWord = new JLabel(new IconFontGenerator(FontAwesome.KEY, 15, null).getIcon());
		passwordField = new JPasswordField("", 20);
		passwordField.setBackground(null);
		passwordField.addKeyListener(this);
		passwordField.setForeground(null);
		passwordField.setBorder(new BevelBorder(1, Color.darkGray, Color.darkGray));
		gb = new GridBagConstraints();
		gb.insets = new Insets(5, 5, 5, 5);

		JPanel switchPanel = new JPanel();
		switchPanel.setLayout(new FlowLayout());

		JRadioButton mySql = new JRadioButton("mySQL");
		JRadioButton oracle = new JRadioButton("Oracle");
		oracle.setEnabled(false);
		mySql.addActionListener((e) -> {
			DBMS.dbms = 1;
			new MySQLConnection().setDbName("");
			Home.db.setVisible(true);
			Home.oracleUsers.setVisible(false);
			Home.getName();// Reinitialize the title
		});
		oracle.addActionListener((e) -> {
			DBMS.dbms = 2;
			Home.db.setVisible(false);
			Home.oracleUsers.setVisible(true);
			new MySQLConnection().setDbName("scott");
			new LoadData().tablesSectionLoader();
			// will make this dynamic
//			new PupupMessages().message("Oracle system is currently unavailable", new _Icon().failedIcon());
//			mySql.setSelected(true);
			Home.getName();// Reinitialize the title
		});
		if (DBMS.dbms == 1) {
			mySql.setSelected(true);
		} else {
			oracle.setSelected(true);
		}
		mySql.setFocusable(false);
		oracle.setFocusable(false);
		;
		ButtonGroup group = new ButtonGroup();
		group.add(oracle);
		group.add(mySql);

		switchPanel.add(oracle);
		switchPanel.add(mySql);

		gb.gridx = 1;
		gb.gridy = 0;
		connectionPanel.add(switchPanel, gb);
		gb.gridx = 0;
		gb.gridy = 1;
		connectionPanel.add(port, gb);
		gb.gridx = 1;
		gb.gridy = 1;
		connectionPanel.add(portField, gb);
		gb.gridx = 2;
		gb.gridy = 2;
		String arr[] = { "localhost" };
		host = new JComboBox<String>(arr);
		host.setBackground(null);
		host.setPreferredSize(new Dimension(77, 20));
		host.addActionListener((ActionEvent e) -> {
			host.setEditable(true);
		});
		connectionPanel.add(host, gb);
		gb.gridx = 0;
		gb.gridy = 2;
		connectionPanel.add(user, gb);
		gb.gridx = 1;
		gb.gridy = 2;
		connectionPanel.add(userField, gb);
		gb.gridx = 0;
		gb.gridy = 3;
		connectionPanel.add(passWord, gb);
		gb.gridx = 1;
		gb.gridy = 3;
		connectionPanel.add(passwordField, gb);
		gb.gridx = 2;
		gb.gridy = 3;

		mainPanel.removeAll();
		mainPanel.add(new Scroll_pane(connectionPanel).getScrollPane(), BorderLayout.CENTER);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		mainPanel.add(northPanel, BorderLayout.NORTH);

		return mainPanel;
	}

	void field(JTextField field) {
		field.requestFocus();
		field.addFocusListener(this);
		field.addKeyListener(this);
		field.setBorder(new BevelBorder(1, Color.darkGray, Color.darkGray));

	}

	@SuppressWarnings("deprecation")
	public void save() {

		String pass = "";
		pass = passwordField.getText();
		ConnectingToolsModel m = new ConnectingToolsModel((String) host.getSelectedItem(), userField.getText(),
				portField.getText(), pass, "");

		try {
			LoadData.wait = true;
			// test if datas are correct
			if (new MySQLConnection().getCon(m) != null) {
				new PupupMessages().message("Data verified successfully", new _Icon().succesIcon());
				// test if a connection already existed
				if (new SystemDatabaseTreatment().getDataConnection() != null) {
					// message choice
					new PupupMessages().confirm("A connection is currently saved, do you really wanna override it?");
					if (PupupMessages.getAction == 1) {
						if (new SystemDatabaseTreatment().updateConnection(m)) {
							new PupupMessages().message("Successfully switched", new _Icon().succesIcon());
						} else {
							new PupupMessages().message("Switch failed, try again", new _Icon().failedIcon());
						}
					} else {
						new PupupMessages().message("Operation canceled!", new _Icon().messageIcon());
					}
				} else {
					// insert
					if (new SystemDatabaseTreatment().newMySQLConnection(m)) {
						new PupupMessages().message("Successfully connected", new _Icon().succesIcon());
					} else {
						new PupupMessages().message("Connection failed, try again", new _Icon().failedIcon());
					}
				}
				new LoadData().databaseSectionLoader();
				Home.editor();
			} else {
				new PupupMessages().message("Incorrect datas, try again", new _Icon().failedIcon());
			}

		} catch (SQLException e) {
			new PupupMessages().message(e.getMessage(), new _Icon().failedIcon());
		}
	}

	@Override
	public void focusGained(FocusEvent e) {

	}

	@Override
	public void focusLost(FocusEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getSource() == aboutLabel) {
			// aboutUs();
		} else if (e.getSource() == editorLabel) {
			// editor();
		} else if (e.getSource() == securityLabel) {
			// security();
		} else if (e.getSource() == rulesLabel) {
			// rules();
		} else if (e.getSource() == displayLabel) {

		} else if (e.getSource() == connectionLabel) {
			sqlConnector();
		}

	}

	public void editor() {
		new Editor_section(menuPanel);

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Home.frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
		e.getComponent().setFont(new Font("sansserif", Font.BOLD, 12));

	}

	@Override
	public void mouseExited(MouseEvent e) {
		Home.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		if (e.getComponent() != getComponent()) {
			e.getComponent().setFont(new FontUIResource("sansserif", Font.PLAIN, 12));

		}
	}

	Component component;

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 10 && e.getSource() == portField) {
			userField.requestFocus();
		} else if (e.getKeyCode() == 10 && e.getSource() == userField) {
			passwordField.requestFocus();
		} else if (e.getSource() == passwordField && e.getKeyCode() == 10) {
			save();
		}
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 */

	public static ConnectingToolsModel ct = new ConnectingToolsModel();

	JPanel menuPanel = new JPanel();
	JPanel connectionPanel = new JPanel();
	JButton arrowsLabel, connectionLabel, displayLabel, securityLabel, helpLabel, rulesLabel, aboutLabel, editorLabel;
	JTextField portField = new JTextField(20);
	JTextField userField = new JTextField(20);
	JPasswordField passwordField;
	GridBagConstraints gb;
	JPanel mainPanel = new JPanel();;
	int choice = -1, save = 0, save1 = 0;
	boolean isTried = false;

}
