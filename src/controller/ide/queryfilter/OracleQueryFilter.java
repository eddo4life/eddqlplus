package controller.ide.queryfilter;

import controller.ide.command.system.CommandSystem;
import dao.mysql.MySQLConnection;
import dao.mysql.MySQLDaoOperation;
import view.Home;
import view.terminal.Terminal;

import java.sql.SQLException;

public class OracleQueryFilter {

    public OracleQueryFilter(String query) {

        //JOptionPane.showMessageDialog(null, query + " -> Oracle needs to be configured first!");
        queryAnalysis(query);

    }


    public void queryAnalysis(String query) {

        if (query.substring(0, 1).isBlank()) {
            query = query.substring(1, query.length());
        }

        query = query.trim();

        if (query.startsWith("select") || query.startsWith("desc") || query.startsWith("describe")
                || query.contains("show")) {
            new Terminal(query, query.contains("table_name"), "");
        } else if (query.toLowerCase().contains("use ") || query.contains("update") || query.contains("delete")
                || query.contains("create") || query.contains("drop")) {

            try {
                // components.add(ex);
                String feedBack;
                if (query.toLowerCase().trim().contains("drop database")) {
                    String name = getName(query);
                    if (new MySQLConnection().getDbName().equalsIgnoreCase(name.substring(0, name.length() - 1))) {
                        String defaultName = new MySQLDaoOperation().showDataBases().get(0);
                        new MySQLDaoOperation().executeUpdate(query);
                        Home.dbn = defaultName;
                        new MySQLConnection().setDbName(defaultName);
                        feedBack = name.replaceAll(";", "") + " successfully deleted, currently using "
                                + defaultName;
                    } else {
                        new MySQLDaoOperation().executeUpdate(query);
                        feedBack = name.replaceAll(";", "") + " successfully deleted";
                    }
                } else if (query.toLowerCase().trim().contains("drop ")) {
                    new MySQLDaoOperation().executeUpdate(query);
                    feedBack = getName(query).replaceAll(";", "") + " successfully deleted";
                } else {
                    int i = new MySQLDaoOperation().executeUpdate(query);
                    if (query.contains("use ")) {
                        String freshName = getName(query);
                        freshName = freshName.substring(0, freshName.length() - 1);
                        new MySQLConnection().setDbName(freshName);
                        Home.dbn = freshName;
                        feedBack = "Successfully connected to " + freshName;
                    } else {
                        feedBack = "Process complete " + i + " row(s) affected.";
                    }
                }

                new Terminal("", false, feedBack);
            } catch (SQLException ignored) {

            }

        } else {
            new CommandSystem(query);
        }

    }

    public String getName(String query) {
        query = query.toLowerCase();
        String[] arr = query.trim().split(" ");
        for (String s : arr) {
            if (!s.equals("drop") && !s.equals("database") && !s.isBlank() && !s.endsWith("use")) {
                query = s;
            }
        }
        return query;
    }

}
