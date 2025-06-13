package view;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayConsole {

    public DisplayConsole(ArrayList<String> header, ArrayList<String> content) {
        // System.out.println("called " + header + " \n\n" + content);

        ArrayList<String> tempContent = new ArrayList<>(content);

        int line = content.size() / header.size();

        HashMap<String, ArrayList<String>> map = new HashMap<>();

        for (int x = 0; x < header.size(); x++) {
            map.put(header.get(x), getChunckData(tempContent, line, header.size(), x));

            refill(tempContent, content);

        }

        code = showHeader(header, map);
        code += showContent(header, content, map);

        // new ConsoleApp(code);

    }

    String showHeader(ArrayList<String> header, HashMap<String, ArrayList<String>> map) {
        String line = "";
        line += getLine(header, map);
        for (String ss : header) {
            String s = "|" + ss;

            int l = s.length() - 1;
            int maxL = maxLength(ss, map);
            if (l < maxL) {
                for (int i = 0; i < maxL - l; i++) {
                    s += " ";
                }
            }

            line += s;
        }
        line += "|";

        line += getLine(header, map);
        return line;

    }

    String showContent(ArrayList<String> header, ArrayList<String> content, HashMap<String, ArrayList<String>> map) {
        String line = "";

        int i = 0, k = 0, c = 0;
        for (String ss : content) {
            String s = "|";

            if (i < content.size() / header.size()) {
                if (ss == null) {
                    s += "null";
                } else {
                    s += ss;
                }
            }

            int l = s.length() - 1;
            if (c >= header.size()) {
                c = 0;
            }
            int maxL = maxLength(header.get(c), map);
            if (l < maxL) {
                for (int j = 0; j < maxL - l; j++) {
                    s += " ";
                }
            }
            if (k == header.size() - 1) {
                s += "|";
            }
            if (k % header.size() == 0 && k != 0) {
                k = 0;
                line += getLine(header, map);
            }
            k++;
            c++;
            line += s;

        }
        line += getLine(header, map);

        return line;
    }

    String getLine(ArrayList<String> header, HashMap<String, ArrayList<String>> map) {
        String line = "+";
        for (String data : header) {

            for (int i = 0; i < maxLength(data, map); i++) {
                line += "-";
            }

            line += "+";
        }

        return "\n" + line + "\n";

    }

    void refill(ArrayList<String> tempContent, ArrayList<String> content) {
        tempContent.clear();
        for (String data : content) {
            tempContent.add(data);
        }

    }

    ArrayList<String> getChunckData(ArrayList<String> data, int line, int headerSize, int pos) {

        ArrayList<String> dataDivided = new ArrayList<>();

        if (!data.isEmpty()) {
            for (int x = 0; x < line; x++) {
                // adding the column
                dataDivided.add(data.get(pos));
                // removing the line
                for (int i = 0; i < headerSize; i++) {
                    data.remove(0);
                }
            }
        }

        return dataDivided;

    }

    int maxLength(String key, HashMap<String, ArrayList<String>> map) {
        int length = key.length();

        for (String k : map.keySet()) {
            if (key.equals(k)) {
                for (String s : map.get(key)) {
                    if (s != null) {
                        if (length < s.length()) {
                            length = s.length();
                        }
                    }
                }
            }
        }

        return length;
    }

    String code = "";

    public DisplayConsole() {
        // TODO Auto-generated constructor stub
    }

    public String getCode() {
        return code;
    }

}
