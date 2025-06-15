package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class DisplayConsole {

    public DisplayConsole(ArrayList<String> header, ArrayList<String> content) {
        // System.out.println("called " + header + " \n\n" + content);

        ArrayList<String> tempContent = new ArrayList<>(content);

        int line = content.size() / header.size();

        HashMap<String, ArrayList<String>> map = new HashMap<>();

        for (int x = 0; x < header.size(); x++) {
            map.put(header.get(x), getChunkData(tempContent, line, header.size(), x));

            refill(tempContent, content);

        }

        code = showHeader(header, map);
        code += showContent(header, content, map);

        // new ConsoleApp(code);

    }

    private String showHeader(ArrayList<String> header, HashMap<String, ArrayList<String>> map) {
        StringBuilder line = new StringBuilder();
        line.append(getLine(header, map));
        for (String ss : header) {
            StringBuilder s = new StringBuilder("|" + ss);

            int l = s.length() - 1;
            int maxL = maxLength(ss, map);
            if (l < maxL) {
                s.append(" ".repeat(Math.max(0, maxL - l)));
            }

            line.append(s);
        }
        line.append("|");

        line.append(getLine(header, map));
        return line.toString();

    }

    private String showContent(ArrayList<String> header, ArrayList<String> content, HashMap<String, ArrayList<String>> map) {
        StringBuilder line = new StringBuilder();

        int i = 0, k = 0, c = 0;
        for (String ss : content) {
            StringBuilder s = new StringBuilder("|");

            if (i < content.size() / header.size()) {
                s.append(Objects.requireNonNullElse(ss, "null"));
            }

            int l = s.length() - 1;
            if (c >= header.size()) {
                c = 0;
            }
            int maxL = maxLength(header.get(c), map);
            if (l < maxL) {
                s.append(" ".repeat(Math.max(0, maxL - l)));
            }
            if (k == header.size() - 1) {
                s.append("|");
            }
            if (k % header.size() == 0 && k != 0) {
                k = 0;
                line.append(getLine(header, map));
            }
            k++;
            c++;
            line.append(s);

        }
        line.append(getLine(header, map));

        return line.toString();
    }

    private String getLine(ArrayList<String> header, HashMap<String, ArrayList<String>> map) {
        StringBuilder line = new StringBuilder("+");
        for (String data : header) {

            line.append("-".repeat(Math.max(0, maxLength(data, map))));

            line.append("+");
        }

        return "\n" + line + "\n";

    }

    private void refill(ArrayList<String> tempContent, ArrayList<String> content) {
        tempContent.clear();
        tempContent.addAll(content);

    }

    private ArrayList<String> getChunkData(ArrayList<String> data, int line, int headerSize, int pos) {

        ArrayList<String> dataDivided = new ArrayList<>();

        if (!data.isEmpty()) {
            for (int x = 0; x < line; x++) {
                // adding the column
                dataDivided.add(data.get(pos));
                // removing the line
                if (headerSize > 0) {
                    data.subList(0, headerSize).clear();
                }
            }
        }
        return dataDivided;
    }

    private int maxLength(String key, HashMap<String, ArrayList<String>> map) {
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

    private String code = "";

    public String getCode() {
        return code;
    }

}
