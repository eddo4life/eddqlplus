package controller.library;

import java.util.*;

public class EddoLibrary {

    private static final Set<String> PROHIBITED_COLUMN_NAMES = new HashSet<>(Arrays.asList(
            "drop", "alter", "database", "column", "row", "table",
            "primary key", "foreign key", "check", "varchar", "int",
            "decimal", "blob", "index"
    ));

    private static final Set<String> PROHIBITED_TABLE_NAMES = new HashSet<>(Arrays.asList(
            "select", "from", "where", "insert", "update", "delete", "drop", "alter",
            "create", "table", "column", "database", "join", "group", "order", "index"
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
        Set<String> merged = new HashSet<>(PROHIBITED_COLUMN_NAMES);
        merged.addAll(PROHIBITED_TABLE_NAMES);
        return merged;
    }


    private static boolean isValidSqlName(String name, Set<String> prohibitedSet) {

        if (name == null || name.isBlank()) {
            return false;
        }

        String trimmed = name.trim();
        String lower = trimmed.toLowerCase();

        if (isNumber(trimmed)) {
            return false;
        }

        if (prohibitedSet.contains(lower)) {
            return false;
        }

        return isValidIdentifier(trimmed);
    }

    private static boolean isValidIdentifier(String name) {
        return name.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }

    public static boolean isValidSqlColumnName(String input) {
        return isValidSqlName(input, PROHIBITED_COLUMN_NAMES);
    }

    public static boolean isValidSqlTableName(String input) {
        return isValidSqlName(input, PROHIBITED_TABLE_NAMES);
    }


}
