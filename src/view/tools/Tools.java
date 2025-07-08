package view.tools;

import dao.DBMS;
import dao.mysql.MySQLConnection;
import dao.sqlite.SystemDatabaseTreatment;
import eddql.launch.LoadData;
import jiconfont.icons.font_awesome.FontAwesome;
import model.ConnectingToolsModel;
import view.Home;
import view.iconmaker.IconFontGenerator;
import view.iconmaker.IconGenerator;
import view.pupupsmessage.PopupMessages;
import view.scrollpane.CustomScrollPane;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class Tools implements FocusListener, MouseListener, KeyListener {

    // Constants
    private static final Dimension BUTTON_SIZE = new Dimension(140, 25);
    private static final Dimension MENU_PANEL_SIZE = new Dimension(195, 0);
    private static final Dimension HEADER_PANEL_SIZE = new Dimension(100, 70);
    private static final Font HOVER_FONT = new Font("sanserif", Font.BOLD, 12);
    private static final Font DEFAULT_FONT = new FontUIResource("sanserif", Font.PLAIN, 12);

    // UI Components
    private JButton connectionLabel, displayLabel, securityLabel, helpLabel, rulesLabel, aboutLabel, editorLabel;
    private final JTextField portField = new JTextField(20);
    private final JTextField userField = new JTextField(20);
    private JPasswordField passwordField;
    private final JPanel mainPanel = new JPanel();
    private JComboBox<String> host;

    // Models
    public static ConnectingToolsModel connectionModel = new ConnectingToolsModel();
    public static JScrollPane tools_menu_panel;

    public Tools() {
        initializeFields();
    }

    private void initializeFields() {
        connectionLabel = createMenuButton("Connection");
        displayLabel = createMenuButton("Display");
        securityLabel = createMenuButton("Security");
        helpLabel = createMenuButton("Help");
        rulesLabel = createMenuButton("Rules");
        aboutLabel = createMenuButton("About us");
        editorLabel = createMenuButton("Editor");
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setPreferredSize(BUTTON_SIZE);
        button.addMouseListener(this);
        return button;
    }

    public void showMenu() {
        clearContentPanel();
        setupMenuLayout();
        revalidateContent();
    }

    private void clearContentPanel() {
        if (Home.content != null) {
            Home.content.removeAll();
        }
    }

    private void setupMenuLayout() {
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setPreferredSize(MENU_PANEL_SIZE);
        menuPanel.add(createMenuScrollPane());
        Home.content.add(menuPanel, BorderLayout.WEST);
    }

    private JScrollPane createMenuScrollPane() {
        JPanel operationsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(20, 5, 15, 5);

        addComponentsToMenu(operationsPanel, constraints);

        JScrollPane scrollPane = new JScrollPane(operationsPanel);
        scrollPane.setViewportBorder(null);
        scrollPane.setBorder(null);
        tools_menu_panel = scrollPane;
        return scrollPane;
    }

    private void addComponentsToMenu(JPanel panel, GridBagConstraints constraints) {
        int yPos = 0;
        panel.add(createHeaderPanel(), setConstraints(constraints, 0, yPos++));

        panel.add(connectionLabel, setConstraints(constraints, 0, yPos++));
        panel.add(displayLabel, setConstraints(constraints, 0, yPos++));
        panel.add(securityLabel, setConstraints(constraints, 0, yPos++));
        panel.add(helpLabel, setConstraints(constraints, 0, yPos++));
        panel.add(rulesLabel, setConstraints(constraints, 0, yPos++));
        panel.add(aboutLabel, setConstraints(constraints, 0, yPos++));
        panel.add(editorLabel, setConstraints(constraints, 0, yPos++));
        panel.add(createMenuButton("Exit"), setConstraints(constraints, 0, yPos++));
    }

    private GridBagConstraints setConstraints(GridBagConstraints constraints, int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
        return constraints;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setPreferredSize(HEADER_PANEL_SIZE);
        JLabel headerLabel = new JLabel();
        headerLabel.setIcon(new IconFontGenerator(FontAwesome.WRENCH, 30, null).getIcon());
        headerPanel.add(headerLabel);
        return headerPanel;
    }

    // Connection-related methods
    private void sqlConnector() {
        setupMainPanelLayout();
        Home.content.add(mainPanel, BorderLayout.CENTER);
        revalidateContent();
    }

    private void setupMainPanelLayout() {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(500, 300));

        JPanel connectionPanel = createConnectionFormPanel();
        mainPanel.add(new CustomScrollPane(connectionPanel).getScrollPane(), BorderLayout.CENTER);
        mainPanel.add(createPanelWithSize(0, 100), BorderLayout.NORTH);
        mainPanel.add(createPanelWithSize(0, 100), BorderLayout.SOUTH);
    }

    private JPanel createConnectionFormPanel() {
        JPanel connectionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gb = new GridBagConstraints();
        gb.insets = new Insets(5, 5, 5, 5);

        addDatabaseTypeSwitcher(connectionPanel, gb);
        addConnectionFields(connectionPanel, gb);

        return connectionPanel;
    }

    private void addDatabaseTypeSwitcher(JPanel panel, GridBagConstraints gb) {
        JPanel switchPanel = new JPanel(new FlowLayout());

        JRadioButton mySql = createDatabaseRadioButton("mySQL", 1, true);
        JRadioButton oracle = createDatabaseRadioButton("Oracle", 2, false);

        ButtonGroup group = new ButtonGroup();
        group.add(oracle);
        group.add(mySql);

        switchPanel.add(oracle);
        switchPanel.add(mySql);

        panel.add(switchPanel, setConstraints(gb, 1, 0));
    }

    private JRadioButton createDatabaseRadioButton(String text, int dbmsType, boolean enabled) {
        JRadioButton button = new JRadioButton(text);
        button.setEnabled(enabled);
        button.setFocusable(false);
        button.setSelected(DBMS.dbms == dbmsType);

        button.addActionListener(e -> {
            DBMS.dbms = dbmsType;
            handleDatabaseSwitch(dbmsType);
            Home.getName(); // Reinitialize the title
        });

        return button;
    }

    private void handleDatabaseSwitch(int dbmsType) {
        if (dbmsType == 1) {
            new MySQLConnection().setDbName("");
            Home.db.setVisible(true);
            Home.oracleUsers.setVisible(false);
        } else {
            Home.db.setVisible(false);
            Home.oracleUsers.setVisible(true);
            new MySQLConnection().setDbName("scott");
            new LoadData().tablesSectionLoader();
        }
    }

    private void addConnectionFields(JPanel panel, GridBagConstraints gb) {
        // Port field
        panel.add(createIconLabel(FontAwesome.USB), setConstraints(gb, 0, 1));
        configureTextField(portField);
        panel.add(portField, setConstraints(gb, 1, 1));

        // Host field
        String[] hosts = {"localhost"};
        host = new JComboBox<>(hosts);
        host.setBackground(null);
        host.setPreferredSize(new Dimension(77, 20));
        host.addActionListener(e -> host.setEditable(true));
        panel.add(host, setConstraints(gb, 2, 2));

        // User field
        panel.add(createIconLabel(FontAwesome.USER), setConstraints(gb, 0, 2));
        configureTextField(userField);
        panel.add(userField, setConstraints(gb, 1, 2));

        // Password field
        panel.add(createIconLabel(FontAwesome.KEY), setConstraints(gb, 0, 3));
        passwordField = new JPasswordField("", 20);
        configurePasswordField(passwordField);
        panel.add(passwordField, setConstraints(gb, 1, 3));
    }

    private JLabel createIconLabel(FontAwesome icon) {
        return new JLabel(new IconFontGenerator(icon, 15, null).getIcon());
    }

    private void configureTextField(JTextField field) {
        field.requestFocus();
        field.addFocusListener(this);
        field.addKeyListener(this);
        field.setBorder(new BevelBorder(1, Color.darkGray, Color.darkGray));
        field.setBackground(null);
    }

    private void configurePasswordField(JPasswordField field) {
        field.addKeyListener(this);
        field.setBackground(null);
        field.setForeground(null);
        field.setBorder(new BevelBorder(1, Color.darkGray, Color.darkGray));
    }

    private JPanel createPanelWithSize(int width, int height) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(width, height));
        return panel;
    }

    private void saveConnection() {
        String password = new String(passwordField.getPassword());
        ConnectingToolsModel model = new ConnectingToolsModel(
                (String) host.getSelectedItem(),
                userField.getText(),
                portField.getText(),
                password,
                ""
        );

        try {
            LoadData.wait = true;
            if (new MySQLConnection().getCon(model) != null) {
                handleSuccessfulConnectionTest(model);
            } else {
                new PopupMessages().message("Incorrect credentials, try again", new IconGenerator().failedIcon());
            }
        } catch (SQLException e) {
            new PopupMessages().message(e.getMessage(), new IconGenerator().failedIcon());
        }
    }

    private void handleSuccessfulConnectionTest(ConnectingToolsModel model) {
        new PopupMessages().message("Data verified successfully", new IconGenerator().successIcon());

        if (new SystemDatabaseTreatment().getDataConnection() != null) {
            handleExistingConnection(model);
        } else {
            handleNewConnection(model);
        }

        new LoadData().databaseSectionLoader();
        Home.editor();
    }

    private void handleExistingConnection(ConnectingToolsModel model) {
        new PopupMessages().confirm("A connection is currently saved, do you really want to override it?");
        if (PopupMessages.getAction == 1) {
            if (new SystemDatabaseTreatment().updateConnection(model)) {
                new PopupMessages().message("Successfully switched", new IconGenerator().successIcon());
            } else {
                new PopupMessages().message("Switch failed, try again", new IconGenerator().failedIcon());
            }
        } else {
            new PopupMessages().message("Operation canceled!", new IconGenerator().messageIcon());
        }
    }

    private void handleNewConnection(ConnectingToolsModel model) {
        if (new SystemDatabaseTreatment().newMySQLConnection(model)) {
            new PopupMessages().message("Successfully connected", new IconGenerator().successIcon());
        } else {
            new PopupMessages().message("Connection failed, try again", new IconGenerator().failedIcon());
        }
    }


    // Event Handlers
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == connectionLabel) {
            sqlConnector();
        }
        // Other button handlers can be added here
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Home.frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
        e.getComponent().setFont(HOVER_FONT);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Home.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        e.getComponent().setFont(DEFAULT_FONT);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            handleEnterKeyPress(e.getSource());
        }
    }

    private void handleEnterKeyPress(Object source) {
        if (source == portField) {
            userField.requestFocus();
        } else if (source == userField) {
            passwordField.requestFocus();
        } else if (source == passwordField) {
            saveConnection();
        }
    }

    // Utility methods
    private void revalidateContent() {
        Home.content.revalidate();
        Home.content.repaint();
    }

    // Unused event handlers
    @Override public void focusGained(FocusEvent e) {}
    @Override public void focusLost(FocusEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
}