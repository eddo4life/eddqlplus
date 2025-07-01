package controller.library;

import java.util.*;

public class EddoLibrary {

    private static final Set<String> PROHIBITED_NAMES = new HashSet<>(Arrays.asList(
            "drop", "alter", "database", "column", "row", "table",
            "primary key", "foreign key", "check", "varchar", "int",
            "decimal", "blob", "index"
    ));

    public EddoLibrary() {
    }

    public static boolean isNumber(String txt) {
        if (txt == null) return false;
        try {
            Integer.parseInt(txt);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static List<String> getDigitStrings() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    public static Set<String> getProhibitedNames() {
        return PROHIBITED_NAMES;
    }

    /**
     * Checks if a given string is a valid SQL column name.
     * Valid if:
     * - Not null or blank
     * - Not a number
     * - Not in the list of prohibited SQL keywords
     * - Matches SQL identifier naming rules:
     * starts with a letter or underscore,
     * followed by letters, digits, or underscores
     */
    public static boolean isValidSqlColumnName(String input) {

        if (input == null || input.isBlank()) {
            return false;
        }

        String columnName = input.trim();

        if (isNumber(columnName)) {
            return false;
        }

        if (PROHIBITED_NAMES.contains(columnName.toLowerCase())) {
            return false;
        }

        return columnName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }


}
