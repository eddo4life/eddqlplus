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

    public static JPanel content = new JPanel(new BorderLayout());
    public static JToolBar toolBar;
    public static JFrame frame;

    private Cursor defaultCursor;
    private final JLabel home = new JLabel();
    private final JLabel tablesSections = new JLabel("Tables");
    public static JLabel oracleUsers, db, tools;
    public static JTextField searchField = new JTextField(15);

    private JButton logout, refresh, restart, lockButton;
    private JLabel searchLabel;
    private Component hoveredComponent;

    private final ConnectingToolsModel toolsModel = new ConnectingToolsModel();
    private final String password = toolsModel.getPassword();
    private String online = "eddo";
    private int lock = 0;

    private JDialog log = new JDialog();
    private final static JPasswordField passwordField = new JPasswordField(15);
    private boolean unlock = false;

    public static String dbn = new MySQLConnection().getDbName();

    /*
     * return the name of the current database while a new connection is established
     */
    public static void getName() {
        dbn = new MySQLConnection().getDbName();
    }

    public Home() {
        launchUI();
        getDbName();
        startTimer();
    }

    private void launchUI() {
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(new BufferedImageIO().getIcon(500, 500, "logo.jpg").getImage());

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        frame.setMinimumSize(new Dimension(gd.getDisplayMode().getWidth() / 2, gd.getDisplayMode().getHeight() / 2));

        passwordField.addKeyListener(this);
        frame.addKeyListener(globalF4Exit());

        setupToolBar();
        Home.content.setLayout(new BorderLayout());
        frame.add(content, BorderLayout.CENTER);
        frame.add(createFooter(), BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        editor();
    }

    private JPanel createFooter() {
        Locale locale = new Locale("fr", "FR");
        String date = DateFormat.getDateInstance(DateFormat.DEFAULT, locale).format(new Date());
        JLabel copyright = new JLabel(
                "EddQL 1.4.0 " + date + "--20" + (1 + Integer.parseInt(date.substring(date.length() - 2)))
        );

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setPreferredSize(new Dimension(10, 35));
        footer.add(copyright);
        return footer;
    }

    private KeyListener globalF4Exit() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (AWTKeyStroke.getAWTKeyStrokeForEvent(e).equals(
                        AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK))) {
                    System.exit(0);
                }
            }
        };
    }

    private void setupToolBar() {
        toolBar = new JToolBar();
        new Resize(toolBar, "toolbar");
        toolBar.setFloatable(false);
        toolBar.setRollover(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));

        toolBar.add(buildSystemOptions());
        toolBar.add(terminal());
        toolBar.add(addLabel("Users", FontAwesome.USER, DBMS.dbms == 2));
        db = addLabel("Databases", FontAwesome.DATABASE, DBMS.dbms == 1);
        tablesSections.setIcon(new IconFontGenerator(FontAwesome.TABLE, 15, null).getIcon());
        addMouseListenerOn(tablesSections);

        tools = addLabel("Tools", FontAwesome.WRENCH, true);
        toolBar.add(tablesSections);
        toolBar.add(tools);
        toolBar.add(buildSearchBar());

        frame.add(toolBar, BorderLayout.NORTH);
    }

    private JPanel buildSystemOptions() {
        JPanel sysOpt = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, -1));
        logout = createButton(FontAwesome.POWER_OFF, "Log out");
        refresh = createButton(FontAwesome.REPEAT, "Refresh");
        restart = createButton(FontAwesome.REFRESH, "Restart");
        lockButton = createButton(FontAwesome.LOCK, "Lock");

        sysOpt.add(logout);
        sysOpt.add(refresh);
        sysOpt.add(restart);
        sysOpt.add(lockButton);
        return sysOpt;
    }

    private JPanel buildSearchBar() {
        searchField.setFocusable(false);
        searchField.setBorder(null);
        searchLabel = new JLabel("Search", new IconFontGenerator(FontAwesome.SEARCH, 15, null).getIcon(), JLabel.LEFT);
        addMouseListenerOn(searchLabel);

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

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(null);
        panel.add(searchLabel);
        panel.add(searchField);
        return panel;
    }

    private JButton createButton(FontAwesome icon, String tooltip) {
        JButton button = new JButton(new IconFontGenerator(icon, 17, null).getIcon());
        button.setPreferredSize(new Dimension(35, 30));
        button.setFocusable(false);
        button.setToolTipText(tooltip);
        button.addMouseListener(this);
        ButtonHover.apply(button, button.getBackground(), Color.LIGHT_GRAY);
        return button;
    }

    private JLabel addLabel(String text, FontAwesome icon, boolean visible) {
        JLabel label = new JLabel(text, new IconFontGenerator(icon, 15, null).getIcon(), JLabel.LEFT);
        label.setFont(new Font("sanserif", Font.PLAIN, 10));
        label.setVisible(visible);
        label.addMouseListener(this);
        toolBar.add(label);
        return label;
    }

    private JLabel terminal() {
        home.setIcon(new BufferedImageIO().getIcon(20, 20, "console.png"));
        addMouseListenerOn(home);
        home.setBounds(150, 0, 70, 30);
        return home;
    }

    public static void editor() {
        new EditorSection(null);
    }

    public static void getDbName() {
        dbn = new MySQLConnection().getDbName();
    }

    public void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                String dbms = switch (DBMS.dbms) {
                    case 1 -> "mysql-";
                    case 2 -> "oracle-";
                    default -> "";
                };
                String title = dbn.isBlank()
                        ? "EddQL 1.4.0 - No database selected"
                        : "EddQL 1.4.0 -" + dbms + "'" + dbn + "'";
                frame.setTitle(title);
            }
        }, 200, 200);
    }

    // ========== MOUSE / KEY EVENTS ==========

    @Override
    public void mouseClicked(MouseEvent e) {
        Component src = e.getComponent();
        setComponent(src);

        if (Tools.connectionModel != null) {
            if (src == tablesSections) new TablesSections().options();
            else if (src == db) openDbSection();
            else if (src == oracleUsers) new OracleUsersSection();
            else if (src == home) editor();
            else if (src == tools) new Tools().showMenu();
            else if (src == logout) act(0);
            else if (src == refresh) act(1);
            else if (src == restart) act(2);
            else if (src == lockButton) act(3);
        }
    }

    private void openDbSection() {
        try {
            new DatabaseSection().dataBases();
        } catch (SQLException e) {
            new MySQLConnection().setDbName("");
        }
    }

    private void act(int action) {
        switch (action) {
            case 0 -> logOut();
            case 1 -> {
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                new LoadData().getAll();
                frame.setCursor(Cursor.getDefaultCursor());
                frame.revalidate();
                frame.repaint();
            }
            case 2 -> restart();
            case 3 -> {
                new PopupMessages().message("The default password is \"eddo\", until this functionality is completed!", new IconGenerator().messageIcon());
                unlock = false;
                lock = 0;
                passwordField.setText("");
                lock();
            }
        }
    }

    private void restart() {
        frame.dispose();
        frame.removeAll();
        MainEddQL.main(null);
    }

    private void lock() {
        log = new JDialog(frame, "Unlock", true);
        log.setLayout(new GridBagLayout());
        log.setSize(220, 35);
        passwordField.setBorder(new BevelBorder(1));
        log.add(new JLabel("🔐"));
        log.add(passwordField);
        log.setLocationRelativeTo(frame);
        log.setUndecorated(true);
        log.setVisible(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && access()) {
            unlock = true;
            log.dispose();
        }
    }

    private boolean access() {
        String input = passwordField.getText();
        if (input.equals(password) || input.equals(online)) {
            online = password;
            lock = 1;
            return true;
        }
        return false;
    }

    private void logOut() {
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setTitle("Power off");
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton logoutBtn = designButton("Logout");
        logoutBtn.addActionListener(e -> System.exit(0));
        JButton cancelBtn = designButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());
        south.add(logoutBtn);
        south.add(cancelBtn);

        JLabel img = new JLabel(new BufferedImageIO().getIcon(400, 150, "eddql.jpg"));
        JPanel center = new JPanel(new GridBagLayout());
        center.add(img);

        dialog.add(center, BorderLayout.CENTER);
        dialog.add(south, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JButton designButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(90, 26));
        button.setFocusable(false);
        return button;
    }

    private void addMouseListenerOn(JLabel label) {
        label.setFont(new Font("sanserif", Font.PLAIN, 10));
        label.addMouseListener(this);
    }

    private void setComponent(Component c) {
        this.hoveredComponent = c;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        defaultCursor = frame.getCursor();
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hoveredComponent = e.getComponent();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        frame.setCursor(defaultCursor);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
    }
}
