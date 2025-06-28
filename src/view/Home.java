package view;

import dao.DBMS;
import dao.mysql.MySQLConnection;
import eddql.launch.LoadData;
import icon.BufferedImageIO;
import jiconfont.icons.font_awesome.FontAwesome;
import model.ConnectingToolsModel;
import view.button.hover.ButtonHover;
import view.iconmaker.IconFontGenerator;
import view.iconmaker.IconGenerator;
import view.pupupsmessage.PopupMessages;
import view.resize.Resize;
import view.tools.Tools;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class Home implements MouseListener, KeyListener, FocusListener {
    public static JPanel content = new JPanel();
    public static JToolBar toolBar;
    public static JFrame frame;

    private Cursor defaultCursor;
    public static JLabel db;

    public static JLabel tools;
    private final JLabel home = new JLabel("");

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

    private void launch() {
        frame = new JFrame();
        frame.addKeyListener(this);
        content = new JPanel();
        content.setLayout(new BorderLayout());
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        frame.setMinimumSize(new Dimension(width / 2, height / 2));

        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

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

    private JLabel terminal() {
        home.setIcon(new BufferedImageIO().getIcon(20, 20, "console.png"));
        setComponent(home);
        home.setBounds(150, 0, 70, 30);
        addMouseListenerOn(home);

        home.addMouseListener(this);
        return home;
    }

    /*
     *
     * ========================================================
     *
     */

    private JButton logout, refresh, restart, _lock;
    private final JLabel tablesSections = new JLabel("Tables");

    private void setDim(JButton button) {
        button.setPreferredSize(new Dimension(35, 30));
        button.setFocusable(false);
        ButtonHover.apply(button, button.getBackground(), Color.lightGray);
    }

    public static JLabel oracleUsers;

    private void toolBar() {

        oracleUsers = new JLabel("Users");
        if (DBMS.dbms != 2) {
            oracleUsers.setVisible(false);
        }

        addMouseListenerOn(oracleUsers);
        oracleUsers.setIcon(new IconFontGenerator(FontAwesome.USER, 17, null).getIcon());

        toolBar = new JToolBar();
        new Resize(toolBar, "toolbar");
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
        toolBar.setRollover(false);
        toolBar.setFloatable(false);
        toolBar.setOpaque(true);
        JPanel sysOpt = new JPanel();
        sysOpt.setLayout(new FlowLayout(FlowLayout.LEFT, 0, -1));
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

        logout.setToolTipText("Log out");
        restart.setToolTipText("Restart");
        refresh.setToolTipText("Refresh");
        _lock.setToolTipText("Lock");

        db = new JLabel("Databases");
        db.setIcon(new IconFontGenerator(FontAwesome.DATABASE, 15, null).getIcon());
        if (DBMS.dbms != 1) {
            db.setVisible(false);
        }
        addMouseListenerOn(db);
        tablesSections.setIcon(new IconFontGenerator(FontAwesome.TABLE, 15, null).getIcon());
        addMouseListenerOn(tablesSections);
        tools = new JLabel("Tools");
        tools.setIcon(new IconFontGenerator(FontAwesome.WRENCH, 15, null).getIcon());
        addMouseListenerOn(tools);

        toolBar.add(terminal());
        toolBar.add(oracleUsers);
        toolBar.add(db);
        toolBar.add(tablesSections);
        toolBar.add(tools);

        searchField.setMargin(new Insets(0, 10, 0, 0));
        searchField.setFocusable(false);
        searchField.setBorder(null);

        searchLabel = new JLabel("Search");

        searchLabel.setIcon(new IconFontGenerator(FontAwesome.SEARCH, 15, null).getIcon());

        addMouseListenerOn(searchLabel);

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(null);
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
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
        bckNextPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));
        toolBar.add(bckNextPanel);
        frame.add(toolBar, BorderLayout.NORTH);
    }

    private void addMouseListenerOn(JLabel label) {
        if (label != searchLabel)
            label.addMouseListener(this);
        label.setFont(new Font("sanserif", Font.PLAIN, 10));
    }


    /*
     *
     * ========================================================
     *
     */

    private JLabel searchLabel;
    public static JTextField searchField = new JTextField(15);

    public static void editor() {
        new EditorSection(null);
    }

    private static JPasswordField passwordField;

    private final ConnectingToolsModel toolsModel = new ConnectingToolsModel();
    private final String password = toolsModel.getPassword();
    private String online = "eddo";
    private int lock = 0;

    private JDialog log = new JDialog();

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
    private boolean access() {
        if (passwordField.getText().equals(password) || passwordField.getText().equals(online)) {
            online = password;
            lock = 1;
            return true;
        }
        return false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        setComponent(e.getComponent());
        if (Tools.ct != null) {
            if (true) {
                if (e.getSource() == tablesSections) {
                    try {
                        new TablesSections().options();
                    } catch (NullPointerException ignored) {
                    }
                } else if (e.getSource() == db) {
                    try {
                        new DatabaseSection().dataBases();
                    } catch (SQLException e1) {
                        new MySQLConnection().setDbName("");
                    }
                } else if (e.getSource() == oracleUsers) {
                    new OracleUsersSection();
                } else if (e.getSource() == home) {
                    editor();
                } else if (e.getSource() == tools) {
                    new Tools().menu();
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

    private Component components;

    public Component getComponent() {
        return components;
    }

    public void setComponent(Component component) {
        this.components = component;
    }

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
    }


    /*
     *
     * ========================================================
     *
     */

    private Component component = null;

    void selection(MouseEvent e, boolean select) {

    }

    /*
     *
     * ========================================================
     *
     */

    public void timer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
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

    private void act(int action) {
        switch (action) {
            case 0 -> {
                logOut();

            }
            case 1 -> {
                Home.frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                new LoadData().getAll();
                Home.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                frame.revalidate();
                frame.repaint();
            }
            case 2 -> {
                restart();
            }
            case 3 -> {
                new PopupMessages().message("The default password is \"eddo\", until this functionality is completed!",
                        new IconGenerator().messageIcon());
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
    private void restart() {
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

    private JButton designButton(String name) {
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
            if (access()) {
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
