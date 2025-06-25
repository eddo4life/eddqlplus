package view.tables;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class JTableUtilities {
    public static void setCellsAlignment(JTable table, int alignment, int x) {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(alignment);
        TableModel tableModel = table.getModel();
        for (int ci = 0; ci < tableModel.getColumnCount(); ci++) {
            if (x == 1) {
                if (ci != 0)
                    table.getColumnModel().getColumn(ci).setCellRenderer(rightRenderer);
            } else if (x == 3) {
                if (ci != 1)
                    table.getColumnModel().getColumn(ci).setCellRenderer(rightRenderer);
            } else {
                table.getColumnModel().getColumn(ci).setCellRenderer(rightRenderer);
            }
        }
    }
}
