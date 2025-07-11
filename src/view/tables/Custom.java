package view.tables;

import javax.swing.*;
import java.awt.*;

public class Custom {
    private final JScrollPane scrollPane;

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public Custom(JTable table, boolean showHline, boolean showVline, int rowHeight, Color bgColor, Color fgColor) {
        // default_required
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        new Sort().tableSortFilter(table);
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportBorder(null);
        scrollPane.setBorder(null);
        this.scrollPane = scrollPane;
        /*
         *
         */
        //table.setBorder(new LineBorder(null));
        table.setShowHorizontalLines(showHline);
        table.setShowVerticalLines(showVline);
        table.setRowHeight(rowHeight);
    }
}
