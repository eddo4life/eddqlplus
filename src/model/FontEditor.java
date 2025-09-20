package model;

import java.awt.*;

public class FontEditor {

    private Font font = new Font("utf-8", Font.PLAIN, 17);

    public FontEditor() {
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
