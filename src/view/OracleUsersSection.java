package view;

import dao.oracle.OracleDaoOperation;
import model.oracle.OracleUsers;
import view.scrollpane.Scroll_pane;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class OracleUsersSection {

    public OracleUsersSection() {
        Home.content.removeAll();
        Home.content.add(mainPanel());
        Home.content.revalidate();
        Home.content.repaint();
    }

    private JPanel mainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(headerPanel(), BorderLayout.NORTH);
        panel.add(usersPanel(), BorderLayout.CENTER);
        // testing to add another at the west
        panel.add(detailsPanel(), BorderLayout.EAST);
        panel.add(operationsPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel operationsPanel() {
        JPanel operationsPanel = new JPanel(new BorderLayout());
        //operationsPanel.setBackground(Color.gray);
        operationsPanel.setPreferredSize(new Dimension(0, 200));
        operationsPanel.add(operationHeaderPanel("PL/SQL configuration", 130), BorderLayout.NORTH);
        operationsPanel.add(new JButton("Privileges,overriding connections..goes here"));

        return operationsPanel;
    }
    //East Details

    private JPanel detailsPanel() {
        JPanel operationsPanel = new JPanel(new BorderLayout());
        //operationsPanel.setBackground(Color.gray);
        operationsPanel.setPreferredSize(new Dimension(400, 200));
        operationsPanel.add(operationHeaderPanel("Oracle user operations", 135), BorderLayout.NORTH);
        operationsPanel.add(new JButton("locked, password features..goes here"));

        return operationsPanel;
    }


    private JPanel headerPanel() {
        JPanel panel = new JPanel(null);
        panel.setSize(new Dimension(20, 50));
        JLabel titleLabel = new JLabel("Oracle users");
        titleLabel.setBounds(10, 5, 100, 20);
        panel.setBounds(0, 5, 1000, 20);
        JSeparator separator = new JSeparator();
        separator.setBounds(100, 15, 10000, 2);
        panel.add(titleLabel);
        panel.add(separator);
        panel.setPreferredSize(new Dimension(100, 40));

        return panel;
    }

    private JPanel usersPanel() {
        JPanel usersPanel = new JPanel(new BorderLayout());

        //	usersPanel.setBackground(Color.cyan);

        JPanel gridBagPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        int x = 0;

        gbc.gridx = 0;
        gbc.gridy = x++;
        gridBagPanel.add(getPanels("USER_ID", "USERNAME", "CREATED"), gbc);
        for (OracleUsers user : new OracleDaoOperation().showUsers()) {
            gbc.gridx = 0;
            gbc.gridy = x++;
            gridBagPanel.add(getPanels(user.getId(), user.getName(), user.getDate()), gbc);
        }
        JScrollPane scrollPane = new Scroll_pane(gridBagPanel).getScrollPane();
        usersPanel.add(scrollPane);
        usersPanel.setPreferredSize(new Dimension(800, 300));

        return usersPanel;
    }

    private JPanel getPanels(String id, String name, String date) {

        JLabel idLabel = new JLabel(id);
        JLabel nameLabel = new JLabel(name);
        JLabel dateLabel = new JLabel(date);

        JPanel gridPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        //gridPanel.setBackground(Color.DARK_GRAY);
        gridPanel.setPreferredSize(new Dimension(700, 40));

        JPanel idPanel = new JPanel(new GridBagLayout());
        idPanel.setBorder(new LineBorder(null));
        //idPanel.setBackground(new Color(0, 0, 20));
        idPanel.add(idLabel);
        gridPanel.add(idPanel);

        JPanel namePanel = new JPanel(new GridBagLayout());
        namePanel.setBorder(new LineBorder(null));
        //namePanel.setBackground(new Color(0, 15, 0));
        namePanel.add(nameLabel);
        gridPanel.add(namePanel);

        JPanel datePanel = new JPanel(new GridBagLayout());
        datePanel.setBorder(new LineBorder(null));
        //datePanel.setBackground(new Color(15, 0, 0));
        datePanel.add(dateLabel);
        gridPanel.add(datePanel);

        // gridPanel.setBackground(Color.darkGray);

        return gridPanel;

    }

    private JPanel operationHeaderPanel(String title, int push) {
        JPanel panel = new JPanel(null);
        panel.setSize(new Dimension(20, 50));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setBounds(10, 5, push, 20);
        panel.setBounds(0, 5, 1000, 20);
        JSeparator separator = new JSeparator();
        separator.setBounds(push, 15, 10000, 2);
        panel.add(titleLabel);
        panel.add(separator);
        panel.setPreferredSize(new Dimension(100, 40));

        return panel;
    }
}
