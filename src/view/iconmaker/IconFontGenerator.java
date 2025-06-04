package view.iconmaker;

import java.awt.Color;

import javax.swing.Icon;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

public class IconFontGenerator {
	private Icon icon;

	public Icon getIcon() {
		return icon;
	}

	public IconFontGenerator(FontAwesome font, int size, Color color) {
		IconFontSwing.register(FontAwesome.getIconFont());
		icon = IconFontSwing.buildIcon(font, size);
	}
}
