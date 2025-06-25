package view.tables;


import view.Home;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class Sort {

    public Sort() {
    }

    public void tableSortFilter(JTable table) {
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(rowSorter);
        Home.searchField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {

                try {
                    String text = Home.searchField.getText();
                    if (text.isBlank()) {
                        rowSorter.setRowFilter(null);

                    } else {
                        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    }
                } catch (Exception ignored) {
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                String text = Home.searchField.getText();
                try {
                    if (text.isBlank()) {
                        rowSorter.setRowFilter(null);
                    } else {
                        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));

                    }
                } catch (Exception ignored) {
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        });
    }
}
