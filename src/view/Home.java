
package view;

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;

import dao.DBMS;
import dao.mysql.MySQLConnection;
import eddql.launch.LoadData;
import icon.BufferedImageIO;
import jiconfont.icons.font_awesome.FontAwesome;
import model.ConnectingToolsModel;
import test.com.eddo.main.MainTools;
import view.iconmaker.IconFontGenerator;
import view.iconmaker._Icon;
import view.pupupsmessage.PupupMessages;
import view.resize.Resize;
import view.tools.Tools;

public class Home implements MouseListener, KeyListener, FocusListener {
	public static JPanel content = new JPanel();
	public static JToolBar toolBar;
	Back back = new Back();
	public static JFrame frame;
	JPanel smenuPanel;
	GridBagConstraints smenuconstraint;
	Cursor defaultCursor;
	JLabel home;
	public static JLabel db;

	public static JLabel tools;

	public Home() {
		launch();
		getName();
		timer();
	}

	/*
	 * 
	 * ========================================================
	 * 
	 */

	public void launch() {
		frame = new JFrame();
		frame.addKeyListener(this);
		content = new JPanel();
		content.setLayout(new BorderLayout());
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		// frame.setSize(new Dimension(width/2, height/2));
		frame.setMinimumSize(new Dimension(width / 2, height / 2));
		//frame.setUndecorated(true);
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(null);
		// frame.setLocation(117, 100);;

		// we'll get the location from the database

		frame.setIconImage(new BufferedImageIO().getIcon(500, 500, "logo.jpg").getImage());
		passwordField = new JPasswordField(15);
		passwordField.addKeyListener(this);

		toolBar();
		editor();

		Locale locale = new Locale("fr", "FR");
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
		String date = dateFormat.format(new Date());
		JLabel copyright = new JLabel("EddQL 1.4.0 " + date + "--20"
				+ (1 + Integer.parseInt(date.substring(date.length() - 2, date.length()))));

		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(2));
		p.setPreferredSize(new Dimension(10, 35));
		// GridBagConstraints c = new GridBagConstraints();
//		c.gridx = 0;
//		c.gridy = 0;
		p.add(copyright);
		frame.add(p, BorderLayout.SOUTH);
		frame.add(content, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.addKeyListener(new KeyAdapter() {
			@SuppressWarnings("deprecation")
			public void keyPressed(KeyEvent e) {
				AWTKeyStroke ak = AWTKeyStroke.getAWTKeyStrokeForEvent(e);
				if (ak.equals(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK))) {
					System.exit(0);
				}
			}
		});

	}

	JLabel terminal() {
		home = new JLabel("");
		home.setIcon(new BufferedImageIO().getIcon(20, 20, "console.png"));
		setComponent(home);
		home.setBounds(150, 0, 70, 30);
		getToolLabel(home);
		// home.setIcon(new IconFontGenerator(FontAwesome.DESKTOP, 15,
		// color).getIcon());
		home.addMouseListener(this);
		return home;
	}

	/*
	 * 
	 * ========================================================
	 * 
	 */

	int heightbar = 30;
	JButton logout, refresh, restart, _lock;
	JLabel tablesSections = new JLabel("Tables");

	void setDim(JButton button) {
		button.setPreferredSize(new Dimension(35, 30));
		button.setFocusable(false);
	}

	public static JLabel oracleUsers;

	public void toolBar() {

		oracleUsers = new JLabel("Users");
		if (DBMS.dbms != 2) {
			oracleUsers.setVisible(false);
		}

		getToolLabel(oracleUsers);
		oracleUsers.setIcon(new IconFontGenerator(FontAwesome.USER, 17, null).getIcon());

		toolBar = new JToolBar();
		new Resize(toolBar, "toolbar");
		toolBar.setLayout(new FlowLayout(0, 20, 5));
		toolBar.setRollover(false);
		toolBar.setFloatable(false);
		toolBar.setOpaque(true);
		JPanel sysOpt = new JPanel();
		sysOpt.setLayout(new FlowLayout(0, 0, -1));
		logout = new JButton(new IconFontGenerator(FontAwesome.POWER_OFF, 17, null).getIcon());
		logout.addMouseListener(this);
		refresh = new JButton(new IconFontGenerator(FontAwesome.REPEAT, 17, null).getIcon());
		refresh.addMouseListener(this);
		restart = new JButton(new IconFontGenerator(FontAwesome.REFRESH, 17, null).getIcon());
		restart.addMouseListener(this);
		_lock = new JButton(new IconFontGenerator(FontAwesome.LOCK, 17, null).getIcon());
		_lock.addMouseListener(this);
		setDim(_lock);
		setDim(logout);
		setDim(restart);
		setDim(refresh);
		sysOpt.add(logout);
		sysOpt.add(refresh);
		sysOpt.add(restart);
		sysOpt.add(_lock);
		toolBar.add(sysOpt);
		db = new JLabel("Databases");
		db.setIcon(new IconFontGenerator(FontAwesome.DATABASE, 15, null).getIcon());
		if (DBMS.dbms != 1) {
			db.setVisible(false);
		}
		getToolLabel(db);
		tablesSections.setIcon(new IconFontGenerator(FontAwesome.TABLE, 15, null).getIcon());
		getToolLabel(tablesSections);
		tools = new JLabel("Tools");
		tools.setIcon(new IconFontGenerator(FontAwesome.WRENCH, 15, null).getIcon());
		getToolLabel(tools);

		toolBar.add(terminal());
		toolBar.add(oracleUsers);
		toolBar.add(db);
		toolBar.add(tablesSections);
		toolBar.add(tools);

		// searchField.setBorder(new BevelBorder(1, Color.black, Color.black));
		// searchField.setForeground(Color.black);
		searchField.setMargin(new Insets(0, 10, 0, 0));
		searchField.setFocusable(false);
		searchField.setBorder(null);
		// searchField.setBackground(Color.lightGray);
		searchLabel = new JLabel("Search");

		searchLabel.setIcon(new IconFontGenerator(FontAwesome.SEARCH, 15, null).getIcon());

		getToolLabel(searchLabel);
		JPanel searchPanel = new JPanel();
		searchPanel.setBackground(null);
		searchPanel.setLayout(new FlowLayout(0, 10, 0));
		searchPanel.add(searchLabel);
		searchLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				searchField.setFocusable(true);
			}
		});
		searchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				searchField.setFocusable(false);
			}
		});

		searchField.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				searchField.setFocusable(true);
			}
		});

		searchPanel.add(searchField);
		toolBar.add(searchPanel);
		JPanel bckNextPanel = new JPanel();
		bckNextPanel.setBackground(null);
		bckNextPanel.setLayout(new FlowLayout(0, 12, 0));
		bckNextPanel.add(backLabel);
		bckNextPanel.add(nextLabel);
		toolBar.add(bckNextPanel);
		backLabel.setEnabled(false);
		nextLabel.setEnabled(false);
		frame.add(toolBar, BorderLayout.NORTH);
	}

	public JLabel getToolLabel(JLabel label) {
		if (label != searchLabel)
			label.addMouseListener(this);
		label.setFont(new Font("sanserif", Font.PLAIN, 10));
		return label;
	}

	public static JLabel backLabel = new JLabel(new IconFontGenerator(FontAwesome.LONG_ARROW_LEFT, 13, null).getIcon());
	public static JLabel nextLabel = new JLabel(
			new IconFontGenerator(FontAwesome.LONG_ARROW_RIGHT, 13, null).getIcon());
	JLabel searchLabel;
	public static JTextField searchField = new JTextField(15);

	/*
	 * 
	 * ========================================================
	 * 
	 */

//	public void menu() {
//
//		smenuPanel = new JPanel();
//		smenuPanel.setLayout(new GridBagLayout());
//		smenuconstraint = new GridBagConstraints();
//		smenuconstraint.gridx = 0;
//		smenuconstraint.gridy = 0;
//
//		smenuconstraint.gridx = 0;
//		smenuconstraint.gridy = 8;
//	}

	/*
	 * 
	 * ========================================================
	 * 
	 */

	public static void editor() {
		new Editor_section(null);
	}

	static JPasswordField passwordField;

	/*
	 * 
	 * ========================================================
	 * 
	 */

	ConnectingToolsModel toolsModel = new ConnectingToolsModel();
	String password = toolsModel.getPassword();
	String online = "eddo";
	static int lock = 0;

	JDialog log = new JDialog();

	private void lock() {
		log = new JDialog();
		log.setLayout(new GridBagLayout());
		log.setSize(new Dimension(220, 35));
		log.setResizable(false);
		JLabel lockLabel = new JLabel("🔐");
		// lockLabel.setIcon(getIcon(20, 20, "lock.png"));
		log.add(lockLabel);
		log.add(passwordField);
		log.setLocationRelativeTo(frame);
		log.setUndecorated(true);
		log.setModal(true);
		passwordField.setBorder(new BevelBorder(1, null, null));
		log.setVisible(true);
	}

	@SuppressWarnings("deprecation")
	boolean acces() {
		if (passwordField.getText().equals(password) || passwordField.getText().equals(online)) {
			online = password;
			lock = 1;
			return true;
		}
		return false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (getComponent() != null) {
		}
		setComponent(e.getComponent());

		if (Tools.ct != null) {
			if (e.getSource() != backLabel) {
				backLabel.setEnabled(true);
			} else {
				if (Back.homevar) {
					editor();
				}
				if (back.getHistory().size() == 1) {
					editor();
					backLabel.setEnabled(false);
				}
			}
			// unlock
			if (true) {
				if (e.getSource() == tablesSections) {
					try {
						Back.currentVal += 1;
						back.setHistory(2);
						new TablesSections().saisie();
					} catch (NullPointerException e1) {

					}
				} else if (e.getSource() == db) {
					try {
						Back.currentVal += 1;
						new DataBase_section().dataBases();
						back.setHistory(1);
					} catch (SQLException e1) {
						new MySQLConnection().setDbName("");
					}
				} else if (e.getSource() == oracleUsers) {
					new OracleUsersSection();
				}

				else if (e.getSource() == home) {
					Back.currentVal += 1;

					back.setHistory(0);
					editor();
				} else if (e.getSource() == tools) {
					Back.currentVal += 1;

					back.setHistory(6);
				//	new Tools().menu();
					
					content.removeAll();
					content.add(new MainTools());
					content.revalidate();
					content.repaint();

				} else if (e.getSource() == logout) {
					act(0);
				} else if (e.getSource() == refresh) {
					act(1);
				} else if (e.getSource() == restart) {
					act(2);
				} else if (e.getSource() == _lock) {
					act(3);
				}

			}
		}
	}

	/*
	 * 
	 * ========================================================
	 * 
	 */

	Component components;

	public Component getComponent() {
		return components;
	}

	public void setComponent(Component component) {
		this.components = component;
	}

	JLabel anonymousLabel = new JLabel("");

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		defaultCursor = frame.getCursor();
		Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		frame.setCursor(cursor);
		selection(e, true);
		component = e.getComponent();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		selection(e, false);
		frame.setCursor(defaultCursor);
		if (e.getComponent() != getComponent()) {
		}
	}

	/*
	 * 
	 * ========================================================
	 * 
	 */

	Component component = null;

	void selection(MouseEvent e, boolean select) {

	}

	/*
	 * 
	 * ========================================================
	 * 
	 */

	public void timer() {
		Timer chrono = new Timer();
		chrono.schedule(new TimerTask() {
			public void run() {

				String alt = "", dbms = "";
				if (DBMS.dbms == 1) {
					dbms = "mysql-";
				} else if (DBMS.dbms == 2) {
					dbms = "oracle-";
				}

				if (!dbn.isBlank()) {
					alt = "EddQL 1.4.0 -" + dbms + "'" + dbn + "'";
				} else {
					alt = "EddQL 1.4.0 - No database selected";
				}
				frame.setTitle(alt);

			}
		}, 200, 200);
	}

	/*
	 * 
	 * ========================================================
	 * 
	 */

	void act(int action) {
		switch (action) {
		case 0 -> {
			logOut();

		}
		case 1 -> {
			Home.frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			new LoadData().getAll();
			try {
				new Back(null)._switch(Back.history.get(Back.history.size() - 1));
			} catch (Exception e) {
			}

			Home.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			frame.revalidate();
			frame.repaint();

		}
		case 2 -> {
			restart();
		}

		case 3 -> {
			new PupupMessages().message("The default password is \"eddo\", until this functionality is completed!",
					new _Icon().messageIcon());
			unlock = false;
			lock = 0;
			passwordField.setText("");
			lock();
		}

		}

	}

	/*
	 * 
	 * ========================================================
	 * 
	 */
	void restart() {
		frame.dispose();
		frame.removeAll();
		MainEddQL.main(null);
	}

	/*
	 * This method is responsible for the logout dialog
	 * ========================================================
	 * 
	 */

	public void logOut() {

		JDialog logOutDialog = new JDialog();
		logOutDialog.setLayout(new BorderLayout());
		JPanel northPanel, southPanel;
		JButton cancel = (designButton("Cancel"));
		cancel.addActionListener((ActionEvent e) -> {
			logOutDialog.dispose();
		});

		cancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				// cancel.setBorder(new BevelBorder(1, color, Color.decode("#0A9DC8")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				cancel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				// cancel.setBorder(new BevelBorder(1, Color.decode("#0A9DC8"), color));
			}
		});

		JButton yes = designButton("Logout");
		yes.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});

		yes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				yes.setCursor(new Cursor(Cursor.HAND_CURSOR));
				// yes.setBorder(new BevelBorder(1, color, Color.decode("#0A9DC8")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				yes.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				// yes.setBorder(new BevelBorder(1, Color.decode("#0A9DC8"), color));
			}
		});
		southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(1, 10, 5));
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		southPanel.add(yes);
		southPanel.add(cancel);
		logOutDialog.add(southPanel, BorderLayout.SOUTH);
		JLabel imgLabel = new JLabel();
		imgLabel.setIcon(new BufferedImageIO().getIcon(400, 150, "eddql.jpg"));
		northPanel = new JPanel();
		northPanel.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		northPanel.add(imgLabel, c);
		logOutDialog.add(northPanel, BorderLayout.CENTER);
		logOutDialog.setSize(new Dimension(400, 200));
		// logOutDialog.setIconImage(new ImageIcon("./logo.jpg").getImage());
		logOutDialog.setLocationRelativeTo(null);
		logOutDialog.setTitle("Power off");
		logOutDialog.setResizable(false);
		// logOutDialog.setModal(true);
		logOutDialog.setVisible(true);
		logOutDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	/*
	 * ===============cancel and logout button for the log out dialog are designed
	 * bellow
	 */

	public JButton designButton(String name) {
		JButton button = new JButton(name);
		button.setPreferredSize(new Dimension(90, 26));
		button.setFocusable(false);
		return button;
	}

	/*
	 * return the name of the current database while a new connection is established
	 */

	public static void getName() {
		dbn = new MySQLConnection().getDbName();
	}

	public static String dbn = new MySQLConnection().getDbName();
	int inc = 1;

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	boolean unlock = false;

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 10) {
			if (acces()) {
				unlock = true;
				log.dispose();
			}
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub

	}

}
