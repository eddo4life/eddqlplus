package controller.tables;

import model.CreateTableModel;
import view.CreateTable;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CreateTableManager extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final List<CreateTableModel> tableDataList;

    private final String[] columnNames = new String[]{"Field", "Type ", "Limit", "Null", "Constraints", "Key",
            "Default", "Extra"};
    @SuppressWarnings("rawtypes")
    private final Class[] columnClass = new Class[]{String.class, String.class, String.class, String.class,
            String.class, String.class, String.class, String.class};

    public CreateTableManager(List<CreateTableModel> articleList) {
        this.tableDataList = articleList;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return tableDataList.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CreateTableModel row = tableDataList.get(rowIndex);
        if (0 == columnIndex) {
            return row.getName();
        } else if (1 == columnIndex) {
            return row.getDatatype();
        } else if (2 == columnIndex) {
            return row.getLimit();
        } else if (3 == columnIndex) {
            if (row.getConstraint().toLowerCase().contains("null"))
                return "No";
            else
                return "Yes";
        } else if (4 == columnIndex) {
            return row.getConstraintAff();
        } else if (5 == columnIndex) {
            return row.getKey();
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        CreateTableModel row = tableDataList.get(rowIndex);
        if (0 == columnIndex) {
            row.setName((String) aValue);
        } else if (1 == columnIndex) {
            row.setDatatype((String) aValue);
        } else if (2 == columnIndex) {
            if (new CreateTable().hasLimit((String) aValue))
                row.setLimit((String) aValue);
        } else if (4 == columnIndex) {
            row.setConstraint((String) aValue);
        }
    }

}
