package view;

import controller.ide.queryfilter.QueryFilter;
import icon.BufferedImageIO;
import model.FontEditor;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import view.console.ConsoleApp;
import view.ide.event.FileEvent;
import view.scrollpane.Scroll_pane;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;

public class Editor_section implements KeyListener {
    // JDesktopPane desktop;

    private final FontEditor editorPaneFontMemory = new FontEditor();

    private RTextScrollPane sp;
    public static RSyntaxTextArea textArea;

    /*
     *
     * ========================================================
     *
     */
    public Editor_section() {

    }

    public Editor_section(Object obj) {

        JPanel closePanel = new JPanel();
        closePanel.setLayout(new FlowLayout(2, 30, 0));
        JLabel closeLabel = new JLabel("X");
        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // new Tools().menu();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                closeLabel.setForeground(Color.red);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeLabel.setForeground(Color.black);
            }
        });

        // editor_pane = new JTextPane(new KeywordStyledDocument());

        textArea = new RSyntaxTextArea();
        textArea.setCurrentLineHighlightColor(new Color(30, 30, 30));
        textArea.setBackground(new Color(25, 25, 25));
        textArea.setForeground(new Color(245, 245, 245));
        textArea.setFont(new JLabel().getFont());
        textArea.setFont(new Font("", Font.PLAIN, 17));

        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);


        textArea.setCodeFoldingEnabled(true);
        sp = new RTextScrollPane(textArea);
        sp.setBorder(null);

        // editor_pane.add(sp);

        // Example C code
        JPopupMenu popup = textArea.getPopupMenu();
        new Popup(popup);

        textArea.requestFocus();
        textArea.addKeyListener(this);
        textArea.setBorder(null);

        textArea.setFont(editorPaneFontMemory.getFont());
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        sp.addMouseListener(new MouseAdapter() {
            @SuppressWarnings("deprecation")
            @Override
            public void mouseClicked(MouseEvent e) {
                if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
                    // new Popup().showPopupMenu(e.getPoint(), isClose,
                    // sp.getTextArea().getSelectedText() != null);
                }
            }
        });

        createInternalFrame();
        terminPanel.removeAll();
        terminPanel.revalidate();
        terminPanel.repaint();
        Home.content.removeAll();
        Home.content.add(closePanel, BorderLayout.NORTH);
        Home.content.add(main_editor_panel, BorderLayout.CENTER);

        Home.content.revalidate();
        Home.content.repaint();
    }

    /*
     *
     * ========================================================
     *
     */

    protected JMenuBar createMenuBar() {
        FileEvent actionEvent = new FileEvent();
        JMenuBar menu_bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem new_file = new JMenuItem("New");
        JMenuItem open_file = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem print_file = new JMenuItem("Print");
        new_file.addActionListener(actionEvent);
        new_file.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        open_file.addActionListener(actionEvent);
        open_file.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        saveFile.addActionListener(actionEvent);
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        print_file.addActionListener(actionEvent);
        print_file.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
        file.add(new_file);
        file.addSeparator();
        file.add(open_file);
        file.addSeparator();
        file.add(saveFile);
        file.addSeparator();
        file.add(print_file);
        JMenu edit = new JMenu("Edit");
        JMenuItem selectAll = new JMenuItem("select All");
        selectAll.addActionListener(actionEvent);
        JMenuItem cut = new JMenuItem("cut");
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        JMenuItem copy = new JMenuItem("copy");
        edit.setMargin(new Insets(0, 10, 0, 10));
        JMenuItem paste = new JMenuItem("paste");
        JMenuItem clear = new JMenuItem("clear");
        clear.addActionListener(actionEvent);
        cut.addActionListener(actionEvent);
        copy.addActionListener(actionEvent);
        paste.addActionListener(actionEvent);
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        edit.add(selectAll);
        edit.addSeparator();
        edit.add(cut);
        edit.addSeparator();
        edit.add(copy);
        edit.addSeparator();
        edit.add(paste);
        edit.addSeparator();
        edit.add(clear);
        JMenu font = new JMenu("Font");
        font.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isClose) {
                    westPanel_fontChooser_parent.setVisible(true);
                    isClose = false;
                }
            }
        });

        JMenu run = new JMenu("Run");
        run.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                execute();

            }
        });

        // setting the icons

        file.setIcon(new BufferedImageIO().getIcon(20, 20, "folder.png"));
        edit.setIcon(new BufferedImageIO().getIcon(20, 20, "edit.png"));
        font.setIcon(new BufferedImageIO().getIcon(20, 20, "font.png"));
        run.setIcon(new BufferedImageIO().getIcon(18, 20, "energy.png"));

        menu_bar.add(file);
        menu_bar.add(edit);
        menu_bar.add(font);
        menu_bar.add(run);

        return menu_bar;
    }

    /*
     *
     * ========================================================
     *
     */

    public static void close_font() {
        westPanel_fontChooser_parent.setVisible(false);
    }

    public static JPanel westPanel_fontChooser_parent = new JPanel();

    private final JPanel centerPanel = new JPanel();

    private final JTabbedPane tabbedPane = new JTabbedPane();

    private JTabbedPane tabbedPane() {
        tabbedPane.setBorder(new TitledBorder("mode"));
        tabbedPane.add("classic", centerPanel);
        tabbedPane.add("console", new ConsoleApp());
        tabbedPane.setFocusable(false);
        return tabbedPane;
    }

    public static JPanel terminPanel = new JPanel(new BorderLayout());

    protected void createInternalFrame() {

        JPanel pane = new JPanel();
        // pane.setPreferredSize(new Dimension(600, 100));
        pane.setLayout(new BorderLayout());

        westPanel_fontChooser_parent = new JPanel();
        westPanel_fontChooser_parent.setSize(new Dimension(220, 50));
        westPanel_fontChooser_parent.setLayout(new BorderLayout());


        westPanel_fontChooser_parent.add(fontChooser());

        centerPanel.setLayout(new BorderLayout());

        centerPanel.add(sp);

        // fontSection is closed by default
        close_font();

        centerPanel.add(terminPanel, BorderLayout.SOUTH);
        // sizeAdapter();
        pane.add(tabbedPane());

        pane.add(westPanel_fontChooser_parent, BorderLayout.WEST);

        JDesktopPane desktopPane = new JDesktopPane();
        main_editor_panel.add(pane);
        desktopPane.setLayout(new BorderLayout());

        main_editor_panel.add(desktopPane, BorderLayout.NORTH);

        desktopPane.add(createMenuBar());

    }

    private final JPanel main_editor_panel = new JPanel(new BorderLayout());

    private String font = "utf-8", style = "Normal";
    private static int size = 20;

    private void customScroll(JScrollPane scrollPane) {
        scrollPane.setViewportBorder(null);
        scrollPane.setBorder(null);
    }

    private JScrollPane fontChooser() {

        JPanel main_style_font_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        main_style_font_panel.setPreferredSize(new Dimension(225, 560));
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        DefaultListModel<Object> dlm = new DefaultListModel<>();
        dlm.addElement("utf-8");
        for (String s : fonts) {
            dlm.addElement(s);
        }

        JList<Object> fonts_list = new JList<>(fonts);
        fonts_list.setPreferredSize(new Dimension(200, 200));
        fonts_list.setPreferredSize(null);
        JLabel label1 = new JLabel();

        fonts_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                font = fonts_list.getSelectedValue() + "";
                label1.setText(font);
                label1.setFont(getStyle(font, style, size));

            }
        });

        /*
         *
         *
         *
         *
         */

        JPanel stylePanel = new JPanel();
        stylePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JRadioButton normalRadioButton = new JRadioButton("Normal");
        JRadioButton italicRadioButton = new JRadioButton("Italic");
        JRadioButton boldRadioButton = new JRadioButton("Bold");
        normalRadioButton.addActionListener((e) -> {
            style = normalRadioButton.getText();
            label1.setText(font);
            label1.setFont(getStyle(font, style, size));
        });
        italicRadioButton.addActionListener((e) -> {
            style = italicRadioButton.getText();
            label1.setText(font);
            label1.setFont(getStyle(font, style, size));
        });
        boldRadioButton.addActionListener((e) -> {
            style = boldRadioButton.getText();
            label1.setText(font);
            label1.setFont(getStyle(font, style, size));
        });

        ButtonGroup btg = new ButtonGroup();
        normalRadioButton.setSelected(true);
        btg.add(normalRadioButton);
        btg.add(italicRadioButton);
        btg.add(boldRadioButton);
        stylePanel.add(normalRadioButton);
        stylePanel.add(italicRadioButton);
        stylePanel.add(boldRadioButton);

        dlm = new DefaultListModel<>();
        for (int i = 13; i <= 150; i += 1) {
            dlm.addElement(i);
        }

        JList<Object> size_list = new JList<>(dlm);
        size_list.setPreferredSize(new Dimension(100, 100));
        size_list.setPreferredSize(null);
        size_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (size_list.getSelectedValue() != null)
                    size = (Integer) size_list.getSelectedValue();
                label1.setText(font);
                label1.setFont(getStyle(font, style, size));
            }
        });

        main_style_font_panel.add(new JLabel("Font "));
        main_style_font_panel.add(new Scroll_pane(fonts_list).getScrollPane());
        JLabel sze = new JLabel("Size");

        main_style_font_panel.add(sze);
        main_style_font_panel.add(new JScrollPane(size_list));

        JLabel styleLabel = new JLabel("Style");
        JButton colorButton = new JButton("Color");
        colorButton.setPreferredSize(new Dimension(140, 25));
        colorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Color color = JColorChooser.showDialog(Home.frame, "Choose a color", Color.black);
                textArea.setForeground(color);
                editorPaneCurrentFgColor = color;
            }
        });
        JPanel style_color_panel = new JPanel(new FlowLayout(1, 150, 15));
        style_color_panel.setPreferredSize(new Dimension(225, 120));
        style_color_panel.add(styleLabel);

        /*
         *
         *
         *
         */

        style_color_panel.add(stylePanel);
        style_color_panel.add(colorButton);
        main_style_font_panel.add(style_color_panel);

        JButton applyAndCloseBtn = new JButton("Apply&close");
        // applyAndCloseBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI());

        // set hover color property for apply&closebtn

        // applyAndCloseBtn.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        applyAndCloseBtn.addActionListener((e) -> {

            // set the new chosen font
            editorPaneFontMemory.setFont(textArea.getFont());

            westPanel_fontChooser_parent.setVisible(false);
            isClose = true;
        });
        JButton closeBtn = new JButton("Close");
        // closeBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        closeBtn.setForeground(Color.red);
        closeBtn.addActionListener((e) -> {
            // keep the previous font, cancel all changes...
            textArea.setFont(editorPaneFontMemory.getFont());

            close_font();

            isClose = true;
        });
        JPanel closeApplyPanel = new JPanel();
        closeApplyPanel.setLayout(new GridLayout(1, 1, 5, 5));
        closeApplyPanel.setPreferredSize(new Dimension(200, 30));
        closeApplyPanel.add(applyAndCloseBtn);
        closeApplyPanel.add(closeBtn);

        // westPanel_fontChooser_parent.add(closeApplyPanel, BorderLayout.SOUTH);

        main_style_font_panel.add(closeApplyPanel);
        JScrollPane pane = new JScrollPane(main_style_font_panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        customScroll(pane);
        return pane;
    }

    private boolean isClose = true;

    private Font getStyle(String font, String style, int size) {
        Font font2 = new JLabel().getFont();
        switch (style) {
            case "Normal" -> font2 = new Font(font, Font.PLAIN, size);
            case "Bold" -> font2 = new Font(font, Font.BOLD, size);
            case "Italic" -> font2 = new Font(font, Font.ITALIC, size);
        }

        if (font2 == null)
            font2 = new JLabel("").getFont();
        textArea.setFont(font2);

        return font2;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        save += e.getKeyCode();
    }

    private String save = "";

    @SuppressWarnings("deprecation")
    @Override
    public void keyPressed(KeyEvent e) {
        {
            AWTKeyStroke ak = AWTKeyStroke.getAWTKeyStrokeForEvent(e);
            if (ak.equals(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_R, InputEvent.ALT_MASK))) {

                execute();

            } else if (ak.equals(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_MASK))) {
                if (size <= 150) {
                    getStyle(font, style, ++size);
                }
            } else if (ak.equals(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_MASK))) {
                if (size >= 13) {
                    getStyle(font, style, --size);
                }
            }
        }
    }

    public static int maxLength = 0;
    public static int acs = -1;

    @Override
    public void keyReleased(KeyEvent e) {
        maxLength = textArea.getText().length();

        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        save = "";

        if (e.getKeyCode() == 10 && textArea.getText().trim().endsWith(";")) {
            terminPanel.setVisible(true);
            execute();
        }
    }

    void execute() {
        new QueryFilter(textArea.getText().toLowerCase());
    }

    public static void reset() {
        textArea.setText("");
        terminPanel.setVisible(false);
    }

    public static Color editorPaneCurrentFgColor = Color.white;
}
