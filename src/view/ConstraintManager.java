package view;

import model.CreateTableModel;
import view.pupupsmessage.PopupMessages;

import java.util.*;

public class ConstraintManager {
    private final List<String> constraints = new ArrayList<>();

    public void add(String constraint) {
        if (constraint.equalsIgnoreCase("Primary key") && constraints.contains("Foreign key")) return;
        if (constraint.equalsIgnoreCase("Foreign key") && constraints.contains("Primary key")) return;
        if (!constraints.contains(constraint)) constraints.add(constraint);
    }

    public void remove(String constraint) {
        constraints.remove(constraint);
    }

    public String[] getArray() {
        return constraints.toArray(new String[0]);
    }

    public String analyze(CreateTableModel ctm, List<CreateTableModel> existingColumns) {
        StringBuilder line = new StringBuilder();

        for (String c : constraints) {
            line.append(" ").append(c);
        }

        if (line.toString().contains("Primary key")) {
            ctm.setKey("Primary key");

            for (CreateTableModel col : existingColumns) {
                if ("Primary key".equals(col.getKey())) {
                    new PopupMessages().confirm("SQL table can't have two primary key, wanna update it?");
                    if (PopupMessages.getAction == 1) {
                        col.setConstraint(col.getConstraint().replace("Primary key", ""));
                        col.setKey(null);
                        break;
                    } else {
                        ctm.setKey(null);
                        line = new StringBuilder(line.toString().replace("Primary key", ""));
                    }
                }
            }

            ctm.setConstraintAff(line.toString().replace("Primary key", ""));
        } else if (line.toString().contains("Foreign key")) {
            ctm.setConstraintAff(line.toString().replace("Foreign key", ""));
            ctm.setKey("Foreign key");
            line = new StringBuilder(line.toString().replace("Foreign key",
                    "Foreign key (" + ctm.getName() + ") REFERENCES (" + ctm.getReferences() + ")"));
        } else {
            ctm.setConstraintAff(line.toString());
        }

        return line.toString();
    }

    public void clear() {
        constraints.clear();
    }

    public boolean contains(String element){
        return constraints.contains(element);
    }

    public void removeConstraint(String constraint) {
        constraints.removeIf(constraint::equals);
    }

}
