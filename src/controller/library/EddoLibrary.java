package controller.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EddoLibrary {

    private static final Set<String> PROHIBITED_NAMES = new HashSet<>(Arrays.asList(
            "drop", "alter", "database", "column", "row", "table",
            "primary key", "foreign key", "check", "varchar", "int",
            "decimal", "blob", "index"
    ));

    public EddoLibrary() {
    }

    static public boolean isNumber(String txt) {
        try {
            Integer.parseInt(txt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static ArrayList<String> getDigitStrings() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            list.add("" + i);
        }
        return list;
    }

    public static Set<String> getProhibitedNames() {
        return PROHIBITED_NAMES;
    }


}
